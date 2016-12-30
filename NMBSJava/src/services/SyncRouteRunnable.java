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
import dao.RouteDAO;
import model.Route;

public class SyncRouteRunnable implements Runnable
{

	private RouteDAO rDao;
	private APIController g3API;

	@Override
	public void run()
	{
		try {
			ConsoleLog.setText("Syncing routes");
			// check if has to update
			HashMap<String, String> params = new HashMap<String, String>();
			g3API = new APIController(APIUrl.G3, "route/massUpdateStatus", RequestType.GET, params);
			rDao = new RouteDAO();

			JSONObject mainStatus = g3API.getJsonResult().getJSONObject(0);
			TreeMap<String, String> localStatus = rDao.updateStatus();

			if (localStatus.get("Count").equals(mainStatus.getString("Count"))
					&& localStatus.get("LastUpdated").equals(mainStatus.getString("LastUpdated"))) {

				System.out.println("Routes up to date");
				return;
			}

			// get main table values
			g3API.setUrl("route");
			JSONArray mainJsonList = g3API.getJsonResult();

			ArrayList<Route> localList = rDao.selectAll();
			HashMap<UUID, Route> mainMap = new HashMap<UUID, Route>();
			HashMap<UUID, Route> localMap = new HashMap<UUID, Route>();

			for (Route item : localList)
			{
				localMap.put(item.getRouteID(), item);
			}

			for (int i = 0; i < mainJsonList.length(); i++) {
				JSONObject obj = mainJsonList.getJSONObject(i);
				Route r = new Route();

				r.setRouteID(UUID.fromString(obj.getString("RouteID")));
				r.setDepartureStationID(UUID.fromString(obj.getJSONObject("DepartureStation").getString("StationID")));
				r.setArrivalStationID(UUID.fromString(obj.getJSONObject("ArrivalStation").getString("StationID")));
				r.setLastUpdated(obj.getLong("LastUpdated"));

				mainMap.put(r.getRouteID(), r);
			}

			//update tables
			HashMap<UUID, Route> smallerMap = new HashMap<UUID, Route>();
			HashMap<UUID, Route> biggerMap = new HashMap<UUID, Route>();
			
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
					Route bItem = biggerMap.get(key);
					Route sItem = smallerMap.get(key);
					
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
			System.out.println("SyncRouteError");
			System.out.println(e);
		}
		finally
		{
			System.out.println("---- Route ----");
		}
	}
	
	private void updateLocal(HashMap<UUID, Route> routeMap)
	{
		rDao.setSyncFunction();
		
		for (Route t : routeMap.values())
		{
			rDao.insertOrUpdate(t);		
		}
	}
	
	private void updateMain(HashMap<UUID, Route> routeMap)
	{
		try {
			if (routeMap.isEmpty())
				return;
			
			HashMap<String, String> params = new HashMap<String, String>();
			
			JSONArray routeListJSON = new JSONArray(routeMap.values());

			params.put("routeList", routeListJSON.toString());

			g3API.setUrl("route/massUpdate");
			g3API.setRequestType(RequestType.MASSPUT);
			g3API.setParams(params);
			g3API.getJsonResult();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}