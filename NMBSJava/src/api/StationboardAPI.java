package api;

import java.util.ArrayList;

import org.json.JSONArray;

import controller.URLCon;
import gui.LangageHandler;

public class StationboardAPI {
	private String station;
	private JSONArray json;
	private ArrayList<TrainAPI> trains = new ArrayList<TrainAPI>();

	public StationboardAPI(String station) {
		try {
			this.station = station;
			json = new JSONArray(URLCon.readUrl("https://traintracks.online/api/stationboard/" + station, "GET"));
			for (int i = 0; i < json.length(); i++) {
				TrainAPI t = new TrainAPI(json.getJSONObject(i));
				trains.add(t);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ArrayList<TrainAPI> getTrains() {
		return trains;
	}

	public void setTrains(ArrayList<TrainAPI> trains) {
		this.trains = trains;
	}

	public JSONArray getJson() {
		return json;
	}

	public void setJson(JSONArray json) {
		this.json = json;
	}

	public String toString() {
		String ss = "";
		ss += LangageHandler.chooseLangage("treinVanuit") + " " + station + "\n\n";
		for (int i = 0; i < trains.size() || i < 10; i++) {
			ss += trains.get(i).getTime().getDeparture().substring(11, 16) + " " + trains.get(i).getTerminusStation();
			ss += "\n";
		}
		return ss;
	}
}
