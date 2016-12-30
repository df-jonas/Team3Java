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
import dao.SubscriptionDAO;
import model.Subscription;

public class SyncSubscriptionRunnable implements Runnable
{

	private SubscriptionDAO aDAO;
	private APIController g3API;

	@Override
	public void run()
	{
		try {
			ConsoleLog.setText("Syncing subscriptions");
			// check if has to update
			HashMap<String, String> params = new HashMap<String, String>();
			g3API = new APIController(APIUrl.G3, "subscription/massUpdateStatus", RequestType.GET, params);
			aDAO = new SubscriptionDAO();

			JSONObject mainStatus = g3API.getJsonResult().getJSONObject(0);
			TreeMap<String, String> localStatus = aDAO.updateStatus();

			if (localStatus.get("Count").equals(mainStatus.getString("Count"))
					&& localStatus.get("LastUpdated").equals(mainStatus.getString("LastUpdated"))) {

				System.out.println("Subscriptions up to date");
				return;
			}

			// get main table values
			g3API.setUrl("subscription");
			JSONArray mainJsonList = g3API.getJsonResult();

			ArrayList<Subscription> localList = aDAO.selectAll();
			HashMap<UUID, Subscription> mainMap = new HashMap<UUID, Subscription>();
			HashMap<UUID, Subscription> localMap = new HashMap<UUID, Subscription>();

			for (Subscription item : localList)
			{
				localMap.put(item.getSubscriptionID(), item);
			}

			for (int i = 0; i < mainJsonList.length(); i++) {
				JSONObject obj = mainJsonList.getJSONObject(i);
				Subscription a = new Subscription();

				a.setSubscriptionID(UUID.fromString(obj.getString("SubscriptionID")));
				a.setRailCardID(UUID.fromString(obj.getString("RailCardID")));
				a.setRouteID(UUID.fromString(obj.getJSONObject("Route").getString("RouteID")));
				a.setDiscountID(UUID.fromString(obj.getJSONObject("Discount").getString("DiscountID")));
				a.setValidFrom(obj.getString("ValidFrom"));
				a.setValidUntil(obj.getString("ValidUntil"));
				a.setLastUpdated(obj.getLong("LastUpdated"));

				mainMap.put(a.getSubscriptionID(), a);
			}

			//update tables
			HashMap<UUID, Subscription> smallerMap = new HashMap<UUID, Subscription>();
			HashMap<UUID, Subscription> biggerMap = new HashMap<UUID, Subscription>();
			
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
					Subscription bItem = biggerMap.get(key);
					Subscription sItem = smallerMap.get(key);
					
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
			System.out.println("SyncSubscriptionError");
			System.out.println(e);
		}
		finally
		{
			System.out.println("---- Subscription ----");
		}
	}
	
	private void updateLocal(HashMap<UUID, Subscription> subscriptionMap)
	{
		aDAO.setSyncFunction();
		
		for (Subscription t : subscriptionMap.values())
		{
			aDAO.insertOrUpdate(t);		
		}
	}
	
	private void updateMain(HashMap<UUID, Subscription> subscriptionMap)
	{
		try {
			if (subscriptionMap.isEmpty())
				return;
			
			HashMap<String, String> params = new HashMap<String, String>();
			
			JSONArray subscriptionListJSON = new JSONArray(subscriptionMap.values());

			params.put("subscriptionList", subscriptionListJSON.toString());

			g3API.setUrl("subscription/massUpdate");
			g3API.setRequestType(RequestType.MASSPUT);
			g3API.setParams(params);
			g3API.getJsonResult();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}