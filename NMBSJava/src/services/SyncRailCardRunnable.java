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
import dao.RailCardDAO;
import model.RailCard;

public class SyncRailCardRunnable implements Runnable {

	private RailCardDAO rDAO;
	private APIController g3API;

	@Override
	public void run() {
		try {
			ConsoleLog.setText("Syncing railcards");
			// check if has to update
			HashMap<String, String> params = new HashMap<String, String>();
			g3API = new APIController(APIUrl.G3, "railCard/massUpdateStatus", RequestType.GET, params);
			rDAO = new RailCardDAO();

			JSONObject mainStatus = g3API.getJsonResult().getJSONObject(0);
			TreeMap<String, String> localStatus = rDAO.updateStatus();

			if (localStatus.get("Count").equals(mainStatus.getString("Count"))
					&& localStatus.get("LastUpdated").equals(mainStatus.getString("LastUpdated"))) {

				System.out.println("RailCard up to date");
				return;
			}

			// get main table values
			g3API.setUrl("railCard");
			JSONArray mainJsonList = g3API.getJsonResult();

			ArrayList<RailCard> localList = rDAO.selectAll();
			HashMap<UUID, RailCard> mainMap = new HashMap<UUID, RailCard>();
			HashMap<UUID, RailCard> localMap = new HashMap<UUID, RailCard>();

			for (RailCard item : localList)
			{
				localMap.put(item.getRailCardID(), item);
			}

			for (int i = 0; i < mainJsonList.length(); i++) {
				JSONObject obj = mainJsonList.getJSONObject(i);
				RailCard r = new RailCard();

				r.setRailCardID(UUID.fromString(obj.getString("CardID")));
				r.setLastUpdated(obj.getLong("LastUpdated"));

				mainMap.put(r.getRailCardID(), r);
			}

			//update tables
			HashMap<UUID, RailCard> smallerMap = new HashMap<UUID, RailCard>();
			HashMap<UUID, RailCard> biggerMap = new HashMap<UUID, RailCard>();
			
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
					RailCard bItem = biggerMap.get(key);
					RailCard sItem = smallerMap.get(key);
					
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
			System.out.println("SyncRailCardError");
			System.out.println(e);
		}
		finally
		{
			System.out.println("---- RailCard ----");
		}
	}
	
	private void updateLocal(HashMap<UUID, RailCard> railCardMap)
	{
		rDAO.setSyncFunction();
		
		for (RailCard t : railCardMap.values())
		{
			rDAO.insertOrUpdate(t);		
		}
	}
	
	private void updateMain(HashMap<UUID, RailCard> railCardMap)
	{
		try {
			if (railCardMap.isEmpty())
				return;
			
			HashMap<String, String> params = new HashMap<String, String>();
			
			JSONArray railCardListJSON = new JSONArray(railCardMap.values());

			params.put("railCardList", railCardListJSON.toString());

			g3API.setUrl("railCard/massUpdate");
			g3API.setRequestType(RequestType.MASSPUT);
			g3API.setParams(params);
			g3API.getJsonResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
