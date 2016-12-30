package api;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;
import controller.DateTimeConverter;
import controller.URLCon;
import gui.GUIDateFormat;
import gui.LangageHandler;

public class RouteberekeningAPI {

	private JSONObject json;
	private String van;
	private String naar;
	private String version;
	private String timestamp;
	private ArrayList<ConnectionAPI> connections;

	public RouteberekeningAPI(String from, String to) 
	{
		try {
			this.json = new JSONObject(URLCon.readUnsecureUrl("https://api.irail.be/connections/?to=" + to + "&from="
					+ from + "&date=" + GUIDateFormat.getRawDate() + "&time=" + GUIDateFormat.getRawTime()
					+ "&timeSel=depart&format=json&lang=NL"));

			this.van = from;
			this.naar = to;
			this.version = this.json.getString("version");
			this.timestamp = this.json.getString("timestamp");
			this.connections = new ArrayList<ConnectionAPI>();
			for (int i = 0; i < this.json.getJSONArray("connection").length(); i++) {
				ConnectionAPI e = new ConnectionAPI(this.json.getJSONArray("connection").getJSONObject(i));
				connections.add(e);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
			this.json = new JSONObject(URLCon.readUnsecureUrl("https://api.irail.be/connections/?to=" + to + "&from="
					+ from + "&date=" + GUIDateFormat.getRawDate(date) + "&time=" + GUIDateFormat.getRawTime(time)
					+ "&timeSel=" + tempTimeSel + "&format=json&lang=NL"));

			this.van = from;
			this.naar = to;
			this.version = this.json.getString("version");
			this.timestamp = this.json.getString("timestamp");
			this.connections = new ArrayList<ConnectionAPI>();
			for (int i = 0; i < this.json.getJSONArray("connection").length(); i++) {
				ConnectionAPI e = new ConnectionAPI(this.json.getJSONArray("connection").getJSONObject(i));
				connections.add(e);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
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
			ss += this.getConnections().get(i).getDuration() + " " + LangageHandler.chooseLangage("minuten");
			if (!this.getConnections().get(i).getNumberOfVias().equals("0")) {
				ss += " - " + this.getConnections().get(i).getNumberOfVias() + " " + LangageHandler.chooseLangage("overstappen");
			}
			if (!this.getConnections().get(i).getDeparture().getCancelled().equals("0")) {
				ss += " - " + LangageHandler.chooseLangage("afgelast");
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