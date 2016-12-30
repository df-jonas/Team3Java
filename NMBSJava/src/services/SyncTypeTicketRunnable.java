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
import dao.TypeTicketDAO;
import model.TypeTicket;

public class SyncTypeTicketRunnable implements Runnable {

	private TypeTicketDAO ttDAO;
	private APIController g3API;

	@Override
	public void run() {
		try {
			ConsoleLog.setText("Syncing tickettypes");
			// check if has to update
			HashMap<String, String> params = new HashMap<String, String>();
			g3API = new APIController(APIUrl.G3, "typeTicket/massUpdateStatus", RequestType.GET, params);
			ttDAO = new TypeTicketDAO();

			JSONObject mainStatus = g3API.getJsonResult().getJSONObject(0);
			TreeMap<String, String> localStatus = ttDAO.updateStatus();

			if (localStatus.get("Count").equals(mainStatus.getString("Count"))
					&& localStatus.get("LastUpdated").equals(mainStatus.getString("LastUpdated"))) {

				System.out.println("typeTicket up to date");
				return;
			}

			// get main table values
			g3API.setUrl("typeTicket");
			JSONArray mainJsonList = g3API.getJsonResult();

			ArrayList<TypeTicket> localList = ttDAO.selectAll();
			HashMap<UUID, TypeTicket> mainMap = new HashMap<UUID, TypeTicket>();
			HashMap<UUID, TypeTicket> localMap = new HashMap<UUID, TypeTicket>();

			for (TypeTicket item : localList)
			{
				localMap.put(item.getTypeTicketID(), item);
			}
			
			for (int i = 0; i < mainJsonList.length(); i++) {
				JSONObject obj = mainJsonList.getJSONObject(i);
				TypeTicket tt = new TypeTicket();

				tt.setTypeTicketID(UUID.fromString(obj.getString("TypeTicketID")));
				tt.setName(obj.getString("Name"));
				tt.setPrice(obj.getDouble("Price"));
				tt.setComfortClass(obj.getInt("ComfortClass"));
				tt.setLastUpdated(obj.getLong("LastUpdated"));

				mainMap.put(tt.getTypeTicketID(), tt);
			}

			//update tables
			HashMap<UUID, TypeTicket> smallerMap = new HashMap<UUID, TypeTicket>();
			HashMap<UUID, TypeTicket> biggerMap = new HashMap<UUID, TypeTicket>();
			
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
					TypeTicket bItem = biggerMap.get(key);
					TypeTicket sItem = smallerMap.get(key);
					
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
			System.out.println("SyncTypeTicketError");
			System.out.println(e);
		}
		finally
		{
			System.out.println("---- TypeTicket ----");
		}
	}
	
	private void updateLocal(HashMap<UUID, TypeTicket> TypeTicketMap)
	{
		ttDAO.setSyncFunction();
		
		for (TypeTicket t : TypeTicketMap.values())
		{
			ttDAO.insertOrUpdate(t);		
		}
	}
	
	private void updateMain(HashMap<UUID, TypeTicket> TypeTicketMap)
	{
		try {
			if (TypeTicketMap.isEmpty())
				return;
			
			HashMap<String, String> params = new HashMap<String, String>();
			
			JSONArray typeTicketListJSON = new JSONArray(TypeTicketMap.values());

			params.put("typeTicketList", typeTicketListJSON.toString());

			g3API.setUrl("typeTicket/massUpdate");
			g3API.setRequestType(RequestType.MASSPUT);
			g3API.setParams(params);
			g3API.getJsonResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
