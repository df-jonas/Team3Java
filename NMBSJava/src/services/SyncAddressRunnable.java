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
import dao.AddressDAO;
import model.Address;

public class SyncAddressRunnable implements Runnable
{

	private AddressDAO aDAO;
	private APIController g3API;
	
	@Override
	public void run()
	{
		try {
			ConsoleLog.setText("Syncing addresses");
			//check if has to update
			HashMap<String, String> params = new HashMap<String, String>();
			g3API = new APIController(APIUrl.G3, "address/massUpdateStatus", RequestType.GET, params);
			aDAO = new AddressDAO();

			JSONObject mainStatus = g3API.getJsonResult().getJSONObject(0);
			TreeMap<String, String> localStatus = aDAO.updateStatus();

			if (localStatus.get("Count").equals(mainStatus.getString("Count"))
					&& localStatus.get("LastUpdated").equals(mainStatus.getString("LastUpdated"))) {
				
				System.out.println("Address' up to date");
				return;
			}
			
			//get main table values
			g3API.setUrl("address");
			JSONArray mainJsonList = g3API.getJsonResult();
			
			ArrayList<Address> localList = aDAO.selectAll();
			HashMap<UUID, Address> mainMap = new HashMap<UUID, Address>();
			HashMap<UUID, Address> localMap = new HashMap<UUID, Address>();

			for (Address item : localList)
			{
				localMap.put(item.getAddressID(), item);
			}
			
			for(int i = 0; i < mainJsonList.length(); i++)
			{
				JSONObject obj = mainJsonList.getJSONObject(i);
				Address a = new Address();
				
				a.setAddressID(UUID.fromString(obj.getString("AddressID")));
				a.setStreet(obj.getString("Street"));
				a.setNumber(obj.getInt("Number"));
				a.setCity(obj.getString("City"));
				a.setZipCode(obj.getInt("ZipCode"));
				a.setCoordinates(obj.getString("Coordinates"));
				a.setLastUpdated(obj.getLong("LastUpdated"));
				
				mainMap.put(a.getAddressID(), a);
			}
			
			//update tables
			HashMap<UUID, Address> smallerMap = new HashMap<UUID, Address>();
			HashMap<UUID, Address> biggerMap = new HashMap<UUID, Address>();
			
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
					Address bItem = biggerMap.get(key);
					Address sItem = smallerMap.get(key);
					
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
			System.out.println("SyncAddressError");
			System.out.println(e);
		}
		finally
		{
			System.out.println("---- Address ----");
		}
	}
	
	private void updateLocal(HashMap<UUID, Address> addressMap)
	{
		aDAO.setSyncFunction();
		
		for (Address t : addressMap.values())
		{
			aDAO.insertOrUpdate(t);		
		}
	}
	
	private void updateMain(HashMap<UUID, Address> addressMap)
	{
		try {
			if (addressMap.isEmpty())
				return;
			
			HashMap<String, String> params = new HashMap<String, String>();
			
			JSONArray addressListJSON = new JSONArray(addressMap.values());
			
			params.put("addressList", addressListJSON.toString());
			
			
			g3API.setUrl("address/massUpdate");
			g3API.setRequestType(RequestType.MASSPUT);
			g3API.setParams(params);
			g3API.getJsonResult();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
