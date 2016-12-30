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
import dao.PassDAO;
import model.Pass;

public class SyncPassRunnable implements Runnable {
	private PassDAO pDAO;
	private APIController g3API;

	@Override
	public void run() {
		try {
			ConsoleLog.setText("Syncing passes");
			// check if has to update
			HashMap<String, String> params = new HashMap<String, String>();
			g3API = new APIController(APIUrl.G3, "pass/massUpdateStatus", RequestType.GET, params);
			pDAO = new PassDAO();

			JSONObject mainStatus = g3API.getJsonResult().getJSONObject(0);
			TreeMap<String, String> localStatus = pDAO.updateStatus();

			if (localStatus.get("Count").equals(mainStatus.getString("Count"))
					&& localStatus.get("LastUpdated").equals(mainStatus.getString("LastUpdated"))) {

				System.out.println("Pass up to date");
				return;
			}

			// get main table values
			g3API.setUrl("pass");
			JSONArray mainJsonList = g3API.getJsonResult();

			ArrayList<Pass> localList = pDAO.selectAll();
			HashMap<UUID, Pass> mainMap = new HashMap<UUID, Pass>();
			HashMap<UUID, Pass> localMap = new HashMap<UUID, Pass>();

			for (Pass item : localList)
			{
				localMap.put(item.getPassID(), item);
			}

			for (int i = 0; i < mainJsonList.length(); i++) {
				JSONObject obj = mainJsonList.getJSONObject(i);
				Pass p = new Pass();

				p.setPassID(UUID.fromString(obj.getString("PassID")));
				p.setTypePassID(UUID.fromString(obj.getJSONObject("TypePass").getString("TypePassID")));
				p.setDate(obj.getString("Date"));
				p.setStartDate(obj.getString("StartDate"));
				p.setComfortClass(obj.getInt("ComfortClass"));
				p.setLastUpdated(obj.getLong("LastUpdated"));

				mainMap.put(p.getPassID(), p);
			}

			//update tables
			HashMap<UUID, Pass> smallerMap = new HashMap<UUID, Pass>();
			HashMap<UUID, Pass> biggerMap = new HashMap<UUID, Pass>();
			
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
					Pass bItem = biggerMap.get(key);
					Pass sItem = smallerMap.get(key);
					
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
			System.out.println("SyncPassError");
			System.out.println(e);
		}
		finally
		{
			System.out.println("---- Pass ----");
		}
	}
	
	private void updateLocal(HashMap<UUID, Pass> passMap)
	{
		pDAO.setSyncFunction();
		
		for (Pass t : passMap.values())
		{
			pDAO.insertOrUpdate(t);		
		}
	}
	
	private void updateMain(HashMap<UUID, Pass> passMap)
	{
		try {
			if (passMap.isEmpty())
				return;
			
			HashMap<String, String> params = new HashMap<String, String>();
			
			JSONArray passListJSON = new JSONArray(passMap.values());

			params.put("passList", passListJSON.toString());

			g3API.setUrl("pass/massUpdate");
			g3API.setRequestType(RequestType.MASSPUT);
			g3API.setParams(params);
			g3API.getJsonResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
