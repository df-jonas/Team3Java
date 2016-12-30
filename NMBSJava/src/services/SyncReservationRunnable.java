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
import dao.ReservationDAO;
import model.Reservation;

public class SyncReservationRunnable implements Runnable
{
	private ReservationDAO rDAO;
	private APIController g3API;

	@Override
	public void run()
	{
		try {
			ConsoleLog.setText("Syncing reservations");
			// check if has to update
			HashMap<String, String> params = new HashMap<String, String>();
			g3API = new APIController(APIUrl.G3, "reservation/massUpdateStatus", RequestType.GET, params);
			rDAO = new ReservationDAO();

			JSONObject mainStatus = g3API.getJsonResult().getJSONObject(0);
			TreeMap<String, String> localStatus = rDAO.updateStatus();

			if (localStatus.get("Count").equals(mainStatus.getString("Count"))
					&& localStatus.get("LastUpdated").equals(mainStatus.getString("LastUpdated"))) {

				System.out.println("Reservation up to date");
				return;
			}

			// get main table values
			g3API.setUrl("reservation");
			JSONArray mainJsonList = g3API.getJsonResult();

			ArrayList<Reservation> localList = rDAO.selectAll();
			HashMap<UUID, Reservation> mainMap = new HashMap<UUID, Reservation>();
			HashMap<UUID, Reservation> localMap = new HashMap<UUID, Reservation>();

			for (Reservation item : localList)
			{
				localMap.put(item.getReservationID(), item);
			}

			for (int i = 0; i < mainJsonList.length(); i++) {
				JSONObject obj = mainJsonList.getJSONObject(i);
				Reservation r = new Reservation();

				r.setReservationID(UUID.fromString(obj.getString("ReservationID")));
				r.setPassengerCount(obj.getInt("PassengerCount"));
				r.setTrainID(obj.getString("TrainID"));
				r.setPrice(obj.getDouble("Price"));
				r.setReservationDate(obj.getString("ReservationDate"));
				r.setRouteID(UUID.fromString(obj.getJSONObject("Route").getString("RouteID")));
				r.setLastUpdated(obj.getLong("LastUpdated"));

				mainMap.put(r.getReservationID(), r);
			}

			//update tables
			HashMap<UUID, Reservation> smallerMap = new HashMap<UUID, Reservation>();
			HashMap<UUID, Reservation> biggerMap = new HashMap<UUID, Reservation>();
			
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
					Reservation bItem = biggerMap.get(key);
					Reservation sItem = smallerMap.get(key);
					
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
			System.out.println("SyncReservationError");
			System.out.println(e);
		}
		finally
		{
			System.out.println("---- Reservation ----");
		}
	}
	
	private void updateLocal(HashMap<UUID, Reservation> reservationMap)
	{
		rDAO.setSyncFunction();
		
		for (Reservation t : reservationMap.values())
		{
			rDAO.insertOrUpdate(t);		
		}
	}
	
	private void updateMain(HashMap<UUID, Reservation> reservationMap)
	{
		try {
			if (reservationMap.isEmpty())
				return;
			
			HashMap<String, String> params = new HashMap<String, String>();
			
			JSONArray reservationListJSON = new JSONArray(reservationMap.values());

			params.put("reservationList", reservationListJSON.toString());

			g3API.setUrl("reservation/massUpdate");
			g3API.setRequestType(RequestType.MASSPUT);
			g3API.setParams(params);
			g3API.getJsonResult();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}