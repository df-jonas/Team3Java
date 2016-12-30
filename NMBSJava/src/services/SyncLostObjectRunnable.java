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
import dao.LostObjectDAO;
import model.LostObject;

public class SyncLostObjectRunnable implements Runnable
{
	private LostObjectDAO lDAO;
	private APIController g3API;

	@Override
	public void run()
	{
		try {
			ConsoleLog.setText("Syncing lostobjects");
			// check if has to update
			HashMap<String, String> params = new HashMap<String, String>();
			g3API = new APIController(APIUrl.G3, "lostObject/massUpdateStatus", RequestType.GET, params);
			lDAO = new LostObjectDAO();

			JSONObject mainStatus = g3API.getJsonResult().getJSONObject(0);
			TreeMap<String, String> localStatus = lDAO.updateStatus();

			if (localStatus.get("Count").equals(mainStatus.getString("Count"))
					&& localStatus.get("LastUpdated").equals(mainStatus.getString("LastUpdated"))) {

				System.out.println("Lost Object up to date");
				return;
			}

			// get main table values
			g3API.setUrl("lostObject");
			JSONArray mainJsonList = g3API.getJsonResult();

			ArrayList<LostObject> localList = lDAO.selectAll();
			HashMap<UUID, LostObject> mainMap = new HashMap<UUID, LostObject>();
			HashMap<UUID, LostObject> localMap = new HashMap<UUID, LostObject>();

			for (LostObject item : localList) {
				localMap.put(item.getObjectID(), item);
			}

			for (int i = 0; i < mainJsonList.length(); i++) {
				JSONObject obj = mainJsonList.getJSONObject(i);
				LostObject l = new LostObject();

				l.setObjectID(UUID.fromString(obj.getString("ObjectID")));
				l.setStationID(UUID.fromString(obj.getJSONObject("Station").getString("StationID")));
				l.setDescription(obj.getString("Description"));
				l.setDate(obj.getString("Date"));
				l.setTrainID(obj.getString("TrainID"));
				l.setFound((obj.getString("Found").equals("1")) ? true : false);
				l.setLastUpdated(obj.getLong("LastUpdated"));

				mainMap.put(l.getObjectID(), l);
			}

			//update tables
			HashMap<UUID, LostObject> smallerMap = new HashMap<UUID, LostObject>();
			HashMap<UUID, LostObject> biggerMap = new HashMap<UUID, LostObject>();
			
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
					LostObject bItem = biggerMap.get(key);
					LostObject sItem = smallerMap.get(key);
					
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
			System.out.println("SyncLostObjectError");
			System.out.println(e);
		}
		finally
		{
			System.out.println("---- LostObject ----");
		}
	}
	
	private void updateLocal(HashMap<UUID, LostObject> lostObjectMap)
	{
		lDAO.setSyncFunction();
		
		for (LostObject t : lostObjectMap.values())
		{
			lDAO.insertOrUpdate(t);		
		}
	}
	
	private void updateMain(HashMap<UUID, LostObject> lostObjectMap)
	{
		try {
			if (lostObjectMap.isEmpty())
				return;
			
			HashMap<String, String> params = new HashMap<String, String>();
			
			JSONArray lostObjectListJSON = new JSONArray(lostObjectMap.values());

			params.put("lostObjectList", lostObjectListJSON.toString());

			g3API.setUrl("lostObject/massUpdate");
			g3API.setRequestType(RequestType.MASSPUT);
			g3API.setParams(params);
			g3API.getJsonResult();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
