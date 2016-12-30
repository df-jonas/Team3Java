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
import dao.TicketDAO;
import model.Ticket;

public class SyncTicketRunnable implements Runnable
{

	private TicketDAO aDAO;
	private APIController g3API;

	@Override
	public void run()
	{
		try {
			ConsoleLog.setText("Syncing tickets");
			// check if has to update
			HashMap<String, String> params = new HashMap<String, String>();
			g3API = new APIController(APIUrl.G3, "ticket/massUpdateStatus", RequestType.GET, params);
			aDAO = new TicketDAO();

			JSONObject mainStatus = g3API.getJsonResult().getJSONObject(0);
			TreeMap<String, String> localStatus = aDAO.updateStatus();

			if (localStatus.get("Count").equals(mainStatus.getString("Count"))
					&& localStatus.get("LastUpdated").equals(mainStatus.getString("LastUpdated"))) {

				System.out.println("Ticket up to date");
				return;
			}

			// get main table values
			g3API.setUrl("ticket");
			JSONArray mainJsonList = g3API.getJsonResult();

			ArrayList<Ticket> localList = aDAO.selectAllSync();
			HashMap<UUID, Ticket> mainMap = new HashMap<UUID, Ticket>();
			HashMap<UUID, Ticket> localMap = new HashMap<UUID, Ticket>();

			for (Ticket item : localList)
			{
				localMap.put(item.getTicketID(), item);
			}
			

			for (int i = 0; i < mainJsonList.length(); i++) {
				JSONObject obj = mainJsonList.getJSONObject(i);
				Ticket a = new Ticket();

				a.setTicketID(UUID.fromString(obj.getString("TicketID")));
				a.setRouteID(UUID.fromString(obj.getJSONObject("Route").getString("RouteID")));
				a.setTypeTicketID(UUID.fromString(obj.getJSONObject("TypeTicket").getString("TypeTicketID")));
				a.setDate(obj.getString("Date"));
				a.setValidFrom(obj.getString("ValidFrom"));
				a.setValidUntil(obj.getString("ValidUntil"));
				a.setLastUpdated(obj.getLong("LastUpdated"));

				mainMap.put(a.getTicketID(), a);
			}

			//update tables
			HashMap<UUID, Ticket> smallerMap = new HashMap<UUID, Ticket>();
			HashMap<UUID, Ticket> biggerMap = new HashMap<UUID, Ticket>();
			
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
					Ticket bItem = biggerMap.get(key);
					Ticket sItem = smallerMap.get(key);
					
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
			System.out.println("SyncTicketError");
			System.out.println(e);
		}
		finally
		{
			System.out.println("---- Ticket ----");
		}
	}
	
	private void updateLocal(HashMap<UUID, Ticket> ticketMap)
	{
		aDAO.setSyncFunction();
		
		for (Ticket t : ticketMap.values())
		{
			aDAO.insertOrUpdate(t);		
		}
	}
	
	private void updateMain(HashMap<UUID, Ticket> ticketMap)
	{
		try {
			if (ticketMap.isEmpty())
				return;
			
			HashMap<String, String> params = new HashMap<String, String>();
			
			JSONArray ticketListJSON = new JSONArray(ticketMap.values());

			params.put("ticketList", ticketListJSON.toString());

			g3API.setUrl("ticket/massUpdate");
			g3API.setRequestType(RequestType.MASSPUT);
			g3API.setParams(params);
			g3API.getJsonResult();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}