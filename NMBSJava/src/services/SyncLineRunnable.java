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
import dao.LineDAO;
import model.Line;

public class SyncLineRunnable implements Runnable
{

	private LineDAO aDAO;
	private APIController g3API;
	
	@Override
	public void run()
	{
		try {
			ConsoleLog.setText("Syncing lines");
			//check if has to update
			HashMap<String, String> params = new HashMap<String, String>();
			g3API = new APIController(APIUrl.G3, "line/massUpdateStatus", RequestType.GET, params);
			aDAO = new LineDAO();

			JSONObject mainStatus = g3API.getJsonResult().getJSONObject(0);
			TreeMap<String, String> localStatus = aDAO.updateStatus();

			if (localStatus.get("Count").equals(mainStatus.getString("Count"))
					&& localStatus.get("LastUpdated").equals(mainStatus.getString("LastUpdated"))) {
				
				System.out.println("Lines up to date");
				return;
			}
			
			//get main table values
			g3API.setUrl("line");
			JSONArray mainJsonList = g3API.getJsonResult();
			
			ArrayList<Line> localList = aDAO.selectAll();
			HashMap<UUID, Line> mainMap = new HashMap<UUID, Line>();
			HashMap<UUID, Line> localMap = new HashMap<UUID, Line>();

			for (Line item : localList)
			{
				localMap.put(item.getLineID(), item);
			}
			
			for(int i = 0; i < mainJsonList.length(); i++)
			{
				JSONObject obj = mainJsonList.getJSONObject(i);
				Line l = new Line();
				
				l.setLineID(UUID.fromString(obj.getString("LineID")));
				l.setRouteID(UUID.fromString(obj.getJSONObject("Route").getString("RouteID")));
				l.setTrainType(obj.getString("TrainType"));
				l.setLastUpdated(obj.getLong("LastUpdated"));
				
				mainMap.put(l.getLineID(), l);
			}
			
			//update tables
			HashMap<UUID, Line> smallerMap = new HashMap<UUID, Line>();
			HashMap<UUID, Line> biggerMap = new HashMap<UUID, Line>();
			
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
					Line bItem = biggerMap.get(key);
					Line sItem = smallerMap.get(key);
					
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
			System.out.println("SyncLineError");
			System.out.println(e);
		}
		finally
		{
			System.out.println("---- Line ----");
		}
	}
	
	private void updateLocal(HashMap<UUID, Line> lineMap)
	{
		aDAO.setSyncFunction();
		
		for (Line t : lineMap.values())
		{
			aDAO.insertOrUpdate(t);		
		}
	}
	
	private void updateMain(HashMap<UUID, Line> lineMap)
	{
		try {
			if (lineMap.isEmpty())
				return;
			
			HashMap<String, String> params = new HashMap<String, String>();
			
			JSONArray lineListJSON = new JSONArray(lineMap.values());
			
			params.put("lineList", lineListJSON.toString());
			
			
			g3API.setUrl("line/massUpdate");
			g3API.setRequestType(RequestType.MASSPUT);
			g3API.setParams(params);
			g3API.getJsonResult();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}