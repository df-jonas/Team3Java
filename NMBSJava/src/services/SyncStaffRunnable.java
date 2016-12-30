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
import dao.StaffDAO;
import model.Staff;

public class SyncStaffRunnable implements Runnable
{
	private StaffDAO sDAO;
	private APIController g3API;
	
	@Override
	public void run()
	{
		try {
			ConsoleLog.setText("Syncing staff");
			//check if has to update
			HashMap<String, String> params = new HashMap<String, String>();
			g3API = new APIController(APIUrl.G3, "staff/massUpdateStatus", RequestType.GET, params);
			sDAO = new StaffDAO();

			JSONObject mainStatus = g3API.getJsonResult().getJSONObject(0);
			TreeMap<String, String> localStatus = sDAO.updateStatus();

			if (localStatus.get("Count").equals(mainStatus.getString("Count"))
					&& localStatus.get("LastUpdated").equals(mainStatus.getString("LastUpdated"))) {
				
				System.out.println("Staff' up to date");
				return;
			}
			
			//get main table values
			g3API.setUrl("staff");
			JSONArray mainJsonList = g3API.getJsonResult();
			
			ArrayList<Staff> localList = sDAO.selectAll();
			HashMap<UUID, Staff> mainMap = new HashMap<UUID, Staff>();
			HashMap<UUID, Staff> localMap = new HashMap<UUID, Staff>();

			for (Staff item : localList)
			{
				localMap.put(item.getStaffID(), item);
			}
			
			for(int i = 0; i < mainJsonList.length(); i++)
			{
				JSONObject obj = mainJsonList.getJSONObject(i);
				Staff s = new Staff();
				
				s.setStaffID(UUID.fromString(obj.getString("StaffID")));
				
				s.setAddressID(UUID.fromString(obj.getJSONObject("Address").getString("AddressID")));
				s.setStationID(UUID.fromString(obj.getJSONObject("Station").getString("StationID")));
				
				s.setFirstName(obj.getString("FirstName"));
				s.setLastName(obj.getString("LastName"));
				s.setUserName(obj.getString("UserName"));
				s.setPassword(obj.getString("Password"));
				s.setRights(obj.getInt("Rights"));
				s.setBirthDate(obj.getString("BirthDate"));
				s.setEmail(obj.getString("Email"));
				s.setApiToken(obj.getString("Api_token"));
				s.setLastUpdated(obj.getLong("LastUpdated"));

				mainMap.put(s.getStationID(), s);
			}
			
			//update tables
			HashMap<UUID, Staff> smallerMap = new HashMap<UUID, Staff>();
			HashMap<UUID, Staff> biggerMap = new HashMap<UUID, Staff>();
			
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
					Staff bItem = biggerMap.get(key);
					Staff sItem = smallerMap.get(key);
					
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
			System.out.println("SyncStaffError");
			System.out.println(e);
		}
		finally
		{
			System.out.println("---- Staff ----");
		}
	}
	
	private void updateLocal(HashMap<UUID, Staff> staffMap)
	{
		sDAO.setSyncFunction();
		
		for (Staff t : staffMap.values())
		{
			sDAO.insertOrUpdate(t);		
		}
	}
	
	private void updateMain(HashMap<UUID, Staff> staffMap)
	{
		try {
			if (staffMap.isEmpty())
				return;
			
			HashMap<String, String> params = new HashMap<String, String>();
			
			JSONArray staffListJSON = new JSONArray(staffMap.values());
			
			params.put("staffList", staffListJSON.toString());
			
			
			g3API.setUrl("staff/massUpdate");
			g3API.setRequestType(RequestType.MASSPUT);
			g3API.setParams(params);
			g3API.getJsonResult();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}