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
import dao.CustomerDAO;
import model.Customer;
public class SyncCustomerRunnable implements Runnable  {

		private CustomerDAO cDAO;
		private APIController g3API;
		
		@Override
		public void run()
		{
			try {
				ConsoleLog.setText("Syncing customers");
				//check if has to update
				HashMap<String, String> params = new HashMap<String, String>();
				g3API = new APIController(APIUrl.G3, "customer/massUpdateStatus", RequestType.GET, params);
				cDAO = new CustomerDAO();

				JSONObject mainStatus = g3API.getJsonResult().getJSONObject(0);
				TreeMap<String, String> localStatus = cDAO.updateStatus();

				if (localStatus.get("Count").equals(mainStatus.getString("Count"))
						&& localStatus.get("LastUpdated").equals(mainStatus.getString("LastUpdated"))) {
					
					System.out.println("Customer up to date");
					return;
				}
				
				//get main table values
				g3API.setUrl("customer");
				JSONArray mainJsonList = g3API.getJsonResult();
				
				ArrayList<Customer> localList = cDAO.selectAllSync();
				HashMap<UUID, Customer> mainMap = new HashMap<UUID, Customer>();
				HashMap<UUID, Customer> localMap = new HashMap<UUID, Customer>();

				for (Customer item : localList)
				{
					localMap.put(item.getCustomerID(), item);
				}
				
				for(int i = 0; i < mainJsonList.length(); i++)
				{
					JSONObject obj = mainJsonList.getJSONObject(i);
					Customer c = new Customer();

					c.setCustomerID(UUID.fromString(obj.getString("CustomerID")));
					c.setAddressID(UUID.fromString(obj.getJSONObject("Address").getString("AddressID")));
					c.setRailCardID(UUID.fromString(obj.getJSONObject("RailCard").getString("CardID")));					
					c.setFirstName(obj.getString("FirstName"));
					c.setLastName(obj.getString("LastName"));
					c.setBirthDate(obj.getString("BirthDate"));
					c.setEmail(obj.getString("Email"));
					c.setLastUpdated(obj.getLong("LastUpdated"));

					mainMap.put(c.getCustomerID(), c);
				}
				
				//update tables
				HashMap<UUID, Customer> smallerMap = new HashMap<UUID, Customer>();
				HashMap<UUID, Customer> biggerMap = new HashMap<UUID, Customer>();
				
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
						Customer bItem = biggerMap.get(key);
						Customer sItem = smallerMap.get(key);
						
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
				System.out.println("SyncCustomerError");
				System.out.println(e);
			}
			finally
			{
				System.out.println("---- Customer ----");
			}
		}
		
		private void updateLocal(HashMap<UUID, Customer> customerMap)
		{
			cDAO.setSyncFunction();
			
			for (Customer t : customerMap.values())
			{
				cDAO.insertOrUpdate(t);		
			}
		}
		
		private void updateMain(HashMap<UUID, Customer> customerMap)
		{
			try {
				if (customerMap.isEmpty())
					return;
				
				HashMap<String, String> params = new HashMap<String, String>();
				
				JSONArray customerListJSON = new JSONArray(customerMap.values());
				
				params.put("customerList", customerListJSON.toString());
				
				
				g3API.setUrl("customer/massUpdate");
				g3API.setRequestType(RequestType.MASSPUT);
				g3API.setParams(params);
				g3API.getJsonResult();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}