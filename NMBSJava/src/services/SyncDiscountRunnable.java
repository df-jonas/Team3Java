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
import dao.DiscountDAO;
import model.Discount;

public class SyncDiscountRunnable implements Runnable
{

	private DiscountDAO dDAO;
	private APIController g3API;

	@Override
	public void run()
	{
		try {
			ConsoleLog.setText("Syncing discounts");
			// check if has to update
			HashMap<String, String> params = new HashMap<String, String>();
			g3API = new APIController(APIUrl.G3, "discount/massUpdateStatus", RequestType.GET, params);
			dDAO = new DiscountDAO();

			JSONObject mainStatus = g3API.getJsonResult().getJSONObject(0);
			TreeMap<String, String> localStatus = dDAO.updateStatus();

			if (localStatus.get("Count").equals(mainStatus.getString("Count"))
					&& localStatus.get("LastUpdated").equals(mainStatus.getString("LastUpdated"))) {

				System.out.println("Discount up to date");
				return;
			}

			// get main table values
			g3API.setUrl("discount");
			JSONArray mainJsonList = g3API.getJsonResult();

			ArrayList<Discount> localList = dDAO.selectAll();
			HashMap<UUID, Discount> mainMap = new HashMap<UUID, Discount>();
			HashMap<UUID, Discount> localMap = new HashMap<UUID, Discount>();

			for (Discount item : localList)
			{
				localMap.put(item.getDiscountID(), item);
			}

			for (int i = 0; i < mainJsonList.length(); i++) {
				JSONObject obj = mainJsonList.getJSONObject(i);
				Discount d = new Discount();

				d.setDiscountID(UUID.fromString(obj.getString("DiscountID")));
				d.setName(obj.getString("Name"));
				d.setAmount(obj.getDouble("Amount"));
				d.setLastUpdated(obj.getLong("LastUpdated"));

				mainMap.put(d.getDiscountID(), d);
			}

			//update tables
			HashMap<UUID, Discount> smallerMap = new HashMap<UUID, Discount>();
			HashMap<UUID, Discount> biggerMap = new HashMap<UUID, Discount>();
			
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
					Discount bItem = biggerMap.get(key);
					Discount sItem = smallerMap.get(key);
					
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
			System.out.println("SyncDiscountError");
			System.out.println(e);
		}
		finally
		{
			System.out.println("---- Discount ----");
		}
	}
	
	private void updateLocal(HashMap<UUID, Discount> discountMap)
	{
		dDAO.setSyncFunction();
		
		for (Discount t : discountMap.values())
		{
			dDAO.insertOrUpdate(t);		
		}
	}
	
	private void updateMain(HashMap<UUID, Discount> discountMap)
	{
		try {
			if (discountMap.isEmpty())
				return;
			
			HashMap<String, String> params = new HashMap<String, String>();
			
			JSONArray discountListJSON = new JSONArray(discountMap.values());

			params.put("discountList", discountListJSON.toString());

			g3API.setUrl("discount/massUpdate");
			g3API.setRequestType(RequestType.MASSPUT);
			g3API.setParams(params);
			g3API.getJsonResult();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
