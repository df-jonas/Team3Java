package api;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import controller.APIController.APIUrl;
import controller.APIController.RequestType;
import controller.DateTimeConverter;
import gui.GUIDateFormat;
import services.APIRequest;
import services.APIThread;
import services.ThreadListener;

public class RouteberekeningAPI {

	private String van;
	private String naar;
	private String version;
	private String timestamp;
	private ArrayList<ConnectionAPI> connections;

	public RouteberekeningAPI(String from, String to) 
	{
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("to", to);
		params.put("from", from);
		params.put("date", GUIDateFormat.getRawDate());
		params.put("time", GUIDateFormat.getRawTime());
		params.put("timeSel", "depart");
		params.put("format", "json");
		params.put("lang", "NL");

		UUID requestID = UUID.randomUUID();
		APIRequest request = new APIRequest(requestID, APIUrl.IRAILS, "connections", RequestType.GET, params);
		
		ThreadListener listener = new ThreadListener() {

			@Override
			public void setResult(JSONArray data)
			{
				JSONObject json = data.getJSONObject(0);
				
				van = from;
				naar = to;
				version = json.getString("version");
				timestamp = json.getString("timestamp");
				connections = new ArrayList<ConnectionAPI>();
				for (int i = 0; i < json.getJSONArray("connection").length(); i++) {
					ConnectionAPI e = new ConnectionAPI(json.getJSONArray("connection").getJSONObject(i));
					connections.add(e);
				}
				
			}
			
		};
		
		APIThread apiThread = APIThread.getThread();
		apiThread.addListener(requestID, listener);
		apiThread.addAPIRequest(request);
	}

	public RouteberekeningAPI(String from, String to, String date, String time, TimeSelector timeSel) {
		try {
			String tempTimeSel;
			switch (timeSel) {
			case VERTREK:
				tempTimeSel = "depart";
				break;
			case AANKOMST:
				tempTimeSel = "arrive";
				break;
			default:
				tempTimeSel = "depart";
				break;
			}
			
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("to", to);
			params.put("from", from);
			params.put("date", GUIDateFormat.getRawDate(date));
			params.put("time", GUIDateFormat.getRawTime(time));
			params.put("timeSel", tempTimeSel);
			params.put("format", "json");
			params.put("lang", "NL");

			UUID requestID = UUID.randomUUID();
			APIRequest request = new APIRequest(requestID, APIUrl.IRAILS, "connections", RequestType.GET, params);
			
			ThreadListener listener = new ThreadListener() {

				@Override
				public void setResult(JSONArray data)
				{
					JSONObject json = data.getJSONObject(0);
					
					van = from;
					naar = to;
					version = json.getString("version");
					timestamp = json.getString("timestamp");
					connections = new ArrayList<ConnectionAPI>();
					
					for (int i = 0; i < json.getJSONArray("connection").length(); i++) 
					{
						ConnectionAPI e = new ConnectionAPI(json.getJSONArray("connection").getJSONObject(i));
						connections.add(e);
					}
					
				}
				
			};
			
			APIThread apiThread = APIThread.getThread();
			apiThread.addListener(requestID, listener);
			apiThread.addAPIRequest(request);

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		String temp = "";
		for (int i = 0; i < this.getConnections().size(); i++) {
			// ROUTE ALGEMENE INFO
			// Vertek
			String ss = "Route " + (i + 1) + " -> " + this.getConnections().get(i).getDeparture().getTime();
			if (!this.getConnections().get(i).getDeparture().getDelay().equals("0")) {
				ss += " (+" + this.getConnections().get(i).getDeparture().getDelay() + ")";
			}

			ss += " - ";

			// Aankomst
			ss += this.getConnections().get(i).getArrival().getTime();
			if (!this.getConnections().get(i).getArrival().getDelay().equals("0")) {
				ss += " (+" + this.getConnections().get(i).getArrival().getDelay() + ")";
			}

			ss += "\n";

			// ROUTE EXTRA INFO
			ss += this.getConnections().get(i).getDuration() + " minuten";
			if (!this.getConnections().get(i).getNumberOfVias().equals("0")) {
				ss += " - " + this.getConnections().get(i).getNumberOfVias() + " keer overstappen";
			}
			if (!this.getConnections().get(i).getDeparture().getCancelled().equals("0")) {
				ss += " - AFGELAST";
			}
			ss += "\n\n";

			// ROUTEBESCHRIJVING
			// Vertrek
			ss += "\t" + this.getConnections().get(i).getDeparture().getTime();
			if (!this.getConnections().get(i).getDeparture().getDelay().equals("0")) {
				ss += " (+" + this.getConnections().get(i).getDeparture().getDelay() + ")";
			}
			ss += " - " + this.getConnections().get(i).getDeparture().getStation() + " ("
					+ this.getConnections().get(i).getDeparture().getPlatform() + ")";
			ss += "\n";

			if (!this.getConnections().get(i).getNumberOfVias().equals("0")) {
				// Treininfo
				ss += "\t\t" + this.getConnections().get(i).getDeparture().getDirection() + " - "
						+ this.getConnections().get(i).getDeparture().getVehicle().split("\\.")[2];
				ss += "\n";
			}
			// als er overstappen zijn
			for (int j = 0; j < this.getConnections().get(i).getVias().size(); j++) {
				if (j != 0) {
					// via treininfo
					ss += "\t\t" + this.getConnections().get(i).getVias().get(j).getDirection() + " - "
							+ this.getConnections().get(i).getVias().get(j).getVehicle().split("\\.")[2];
					ss += "\n";
				}

				// via aankomst
				ss += "\t" + this.getConnections().get(i).getVias().get(j).getArrival().getTime();
				if (!this.getConnections().get(i).getVias().get(j).getArrival().getDelay().equals("0")) {
					ss += " (+" + this.getConnections().get(i).getVias().get(j).getArrival().getDelay() + ")";
				}
				ss += " - " + this.getConnections().get(i).getVias().get(j).getStation() + " ("
						+ this.getConnections().get(i).getVias().get(j).getArrival().getPlatform() + ")";
				ss += "\n\n";
				// via vertrek
				ss += "\t" + this.getConnections().get(i).getVias().get(j).getDeparture().getTime();
				if (!this.getConnections().get(i).getVias().get(j).getDeparture().getDelay().equals("0")) {
					ss += " (+" + this.getConnections().get(i).getVias().get(j).getDeparture().getDelay() + ")";
				}
				ss += " - " + this.getConnections().get(i).getVias().get(j).getStation() + " ("
						+ this.getConnections().get(i).getVias().get(j).getDeparture().getPlatform() + ")";
				ss += "\n";
			}

			// Treininfo
			ss += "\t\t" + this.getConnections().get(i).getArrival().getDirection() + " - "
					+ this.getConnections().get(i).getArrival().getVehicle().split("\\.")[2];
			ss += "\n";

			// Aankomst
			ss += "\t" + this.getConnections().get(i).getArrival().getTime();
			if (!this.getConnections().get(i).getArrival().getDelay().equals("0")) {
				ss += " (+" + this.getConnections().get(i).getArrival().getDelay() + ")";
			}
			ss += " - " + this.getConnections().get(i).getArrival().getStation() + " ("
					+ this.getConnections().get(i).getArrival().getPlatform() + ")";

			ss += "\n";

			temp += ss + "\n";
		}
		return temp;
	}
	
	public ArrayList<String> treinID(){
		ArrayList<String> s = new ArrayList<String>();
		for (int i = 0; i < this.getConnections().size(); i++) {
			s.add(this.getConnections().get(i).getDeparture().getVehicle());
		}
		return s;
	}

	public String toStringHTML() {
		return "<html>" + this.toString().replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br/>")
				.replaceAll("\t", "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;") + "</html>";
	}

	public String getVersion() {
		return version;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public String getFormattedTimestamp() {
		return DateTimeConverter.getDateString(this.timestamp);
	}

	public ArrayList<ConnectionAPI> getConnections() {
		return connections;
	}

	public String getVan() {
		return van;
	}

	public String getNaar() {
		return naar;
	}
}