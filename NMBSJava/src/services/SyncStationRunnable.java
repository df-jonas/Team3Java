package services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

import controller.APIController;
import controller.ConsoleLog;
import controller.APIController.APIUrl;
import controller.APIController.RequestType;
import dao.StationDAO;
import model.Station;

public class SyncStationRunnable implements Runnable
{
	private StationDAO sDAO;
	private APIController g3API;
	
	@Override
	public void run()
	{
		try {
			ConsoleLog.setText("Syncing stations");
			//check if has to update
			HashMap<String, String> params = new HashMap<String, String>();
			g3API = new APIController(APIUrl.G3, "station/massUpdateStatus", RequestType.GET, params);
			sDAO = new StationDAO();

			JSONObject mainStatus = g3API.getJsonResult().getJSONObject(0);
			TreeMap<String, String> localStatus = sDAO.updateStatus();

			if (localStatus.get("Count").equals(mainStatus.getString("Count"))
					&& localStatus.get("LastUpdated").equals(mainStatus.getString("LastUpdated"))) {
				
				System.out.println("Stations up to date");
				return;
			}
			
			//get main table values
			g3API.setUrl("station");
			JSONArray mainJsonList = g3API.getJsonResult();
			
			ArrayList<Station> localList = sDAO.selectAll();
			HashMap<UUID, Station> mainMap = new HashMap<UUID, Station>();
			HashMap<UUID, Station> localMap = new HashMap<UUID, Station>();

			for (Station item : localList)
			{
				localMap.put(item.getStationID(), item);
			}
			
			for(int i = 0; i < mainJsonList.length(); i++)
			{
				JSONObject obj = mainJsonList.getJSONObject(i);
				Station s = new Station();
				
				s.setStationID(UUID.fromString(obj.getString("StationID")));
				s.setStationName(obj.getString("Name"));
				s.setCoX(obj.getString("CoX"));
				s.setCoY(obj.getString("CoY"));
				s.setLastUpdated(obj.getLong("LastUpdated"));
				
				mainMap.put(s.getStationID(), s);
			}
			
			//update tables
			HashMap<UUID, Station> smallerMap = new HashMap<UUID, Station>();
			HashMap<UUID, Station> biggerMap = new HashMap<UUID, Station>();
			
			boolean localIsBigger = false;
			
			if (localMap.size() < mainMap.size())
			{
				smallerMap = localMap;
				biggerMap = mainMap;
			}
			else
			{
				smallerMap = mainMap;
				biggerMap = localMap;
				localIsBigger = true;
			}
			
			TreeSet<UUID> smallerKeys = new TreeSet<UUID>(smallerMap.keySet());
			
			for (UUID key : smallerKeys)
			{
				if (biggerMap.containsKey(key))
				{
					Station bItem = biggerMap.get(key);
					Station sItem = smallerMap.get(key);
					
					if (bItem.equals(sItem))
					{
						if (bItem.getLastUpdated() == sItem.getLastUpdated())
						{
							biggerMap.remove(key);
							smallerMap.remove(key);
						}
					}
					if (bItem.getLastUpdated() > sItem.getLastUpdated())
					{
						smallerMap.replace(key, bItem);
					}
					else
					{
						biggerMap.replace(key, sItem);
					}
				}
			}
			
			
			
			//update local
			if (localIsBigger)
				updateLocal(smallerMap);
			else
				updateLocal(biggerMap);
			
			//update main
			if (localIsBigger)
				updateMain(biggerMap);
			else
				updateMain(smallerMap);

		}
		catch (Exception e) {
			System.out.println("SyncStationError");
			System.out.println(e);
		}
		finally
		{
			System.out.println("---- Station ----");
		}
	}
	
	private void updateLocal(HashMap<UUID, Station> stationMap)
	{
		sDAO.setSyncFunction();
		
		for (Station t : stationMap.values())
		{
			sDAO.insertOrUpdate(t);		
		}
	}
	
	private void updateMain(HashMap<UUID, Station> stationMap)
	{
		try {
			if (stationMap.isEmpty())
				return;
			
			HashMap<String, String> params = new HashMap<String, String>();
			
			JSONArray stationListJSON = new JSONArray(stationMap.values());
			
			params.put("stationList", stationListJSON.toString());
			
			System.out.println(params);
			
			
			g3API.setUrl("station/massUpdate");
			g3API.setRequestType(RequestType.MASSPUT);
			g3API.setParams(params);
			g3API.getJsonResult();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
