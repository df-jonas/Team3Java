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
import dao.TypePassDAO;
import model.TypePass;

public class SyncTypePassRunnable implements Runnable  {

		private TypePassDAO ttDAO;
		private APIController g3API;
		
		@Override
		public void run()
		{
			try {
				ConsoleLog.setText("Syncing passtypes");
				//check if has to update
				HashMap<String, String> params = new HashMap<String, String>();
				g3API = new APIController(APIUrl.G3, "typePass/massUpdateStatus", RequestType.GET, params);
				ttDAO = new TypePassDAO();

				JSONObject mainStatus = g3API.getJsonResult().getJSONObject(0);
				TreeMap<String, String> localStatus = ttDAO.updateStatus();

				if (localStatus.get("Count").equals(mainStatus.getString("Count"))
						&& localStatus.get("LastUpdated").equals(mainStatus.getString("LastUpdated"))) {
					
					System.out.println("typePass up to date");
					return;
				}
				
				//get main table values
				g3API.setUrl("typePass");
				JSONArray mainJsonList = g3API.getJsonResult();
				
				ArrayList<TypePass> localList = ttDAO.selectAll();
				HashMap<UUID, TypePass> mainMap = new HashMap<UUID, TypePass>();
				HashMap<UUID, TypePass> localMap = new HashMap<UUID, TypePass>();

				for (TypePass item : localList)
				{
					localMap.put(item.getTypePassID(), item);
				}
				
				for(int i = 0; i < mainJsonList.length(); i++)
				{
					JSONObject obj = mainJsonList.getJSONObject(i);
					TypePass tt = new TypePass();

					tt.setTypePassID(UUID.fromString(obj.getString("TypePassID")));
					tt.setName(obj.getString("Name"));
					tt.setPrice(obj.getDouble("Price"));
					tt.setLastUpdated(obj.getLong("LastUpdated"));

					mainMap.put(tt.getTypePassID(), tt);
				}
				
				//update tables
				HashMap<UUID, TypePass> smallerMap = new HashMap<UUID, TypePass>();
				HashMap<UUID, TypePass> biggerMap = new HashMap<UUID, TypePass>();
				
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
						TypePass bItem = biggerMap.get(key);
						TypePass sItem = smallerMap.get(key);
						
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
				System.out.println("SyncTypePassError");
				System.out.println(e);
			}
			finally
			{
				System.out.println("---- TypePass ----");
			}
		}
		
		private void updateLocal(HashMap<UUID, TypePass> typePassMap)
		{
			ttDAO.setSyncFunction();
			
			for (TypePass t : typePassMap.values())
			{
				ttDAO.insertOrUpdate(t);		
			}
		}
		
		private void updateMain(HashMap<UUID, TypePass> typePassMap)
		{
			try {
				if (typePassMap.isEmpty())
					return;
				
				HashMap<String, String> params = new HashMap<String, String>();
				
				JSONArray typePassListJSON = new JSONArray(typePassMap.values());
				
				params.put("typePassList", typePassListJSON.toString());
				
				
				g3API.setUrl("typePass/massUpdate");
				g3API.setRequestType(RequestType.MASSPUT);
				g3API.setParams(params);
				g3API.getJsonResult();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
