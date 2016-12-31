package model.api;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import gui.LangageHandler;

public class RouteBerekening
{
	private String van;
	private String naar;
	private String version;
	private String timestamp;
	private ArrayList<Connection> connections;
	
	public RouteBerekening()
	{
		this.connections = new ArrayList<Connection>();
	}

	public RouteBerekening(String van, String naar, String version, String timestamp, ArrayList<Connection> connections)
	{
		this.van = van;
		this.naar = naar;
		this.version = version;
		this.timestamp = timestamp;
		this.connections = connections;
	}
	
	public ArrayList<String> treinID(){
		ArrayList<String> s = new ArrayList<String>();
		for (int i = 0; i < this.getConnections().size(); i++) {
			s.add(this.getConnections().get(i).getDeparture().getVehicle());
		}
		return s;
	}
	
	public RouteBerekening(JSONObject json)
	{
		this.connections = new ArrayList<Connection>();
		this.timestamp = json.getString("timestamp");
		this.version = json.getString("version");
		
		JSONArray connections = json.getJSONArray("connection");
		for (int i = 0; i < connections.length(); i++)
		{
			Connection c = new Connection(connections.getJSONObject(i));
			this.connections.add(c);
		}
	}

	public String toStringHTML() {
		return "<html>" + this.toString().replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br/>")
				.replaceAll("\t", "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;") + "</html>";
	}
	
	@Override
	public String toString() {
		String temp = "";
		for (int i = 0; i < this.connections.size(); i++) {
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
			ss += this.getConnections().get(i).getDuration() + " " +LangageHandler.chooseLangage("minuten");
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

	public String getVan()
	{
		return van;
	}

	public void setVan(String van)
	{
		this.van = van;
	}

	public String getNaar()
	{
		return naar;
	}

	public void setNaar(String naar)
	{
		this.naar = naar;
	}

	public String getVersion()
	{
		return version;
	}

	public void setVersion(String version)
	{
		this.version = version;
	}

	public String getTimestamp()
	{
		return timestamp;
	}

	public void setTimestamp(String timestamp)
	{
		this.timestamp = timestamp;
	}

	public ArrayList<Connection> getConnections()
	{
		return connections;
	}

	public void setConnections(ArrayList<Connection> connections)
	{
		this.connections = connections;
	}
}
