package model.api;

import org.json.JSONObject;

import gui.LangageHandler;

public class Station
{
	private String name;
	private String coordinates;
	private String arrivalPlatform;
	private String departurePlatform;
	private TrainTime time;
	private String platform;

	public Station()
	{}
	
	public Station(String name, String coordinates, String arrivalPlatform, String departurePlatform, TrainTime time,
			String platform)
	{
		this.name = name;
		this.coordinates = coordinates;
		this.arrivalPlatform = arrivalPlatform;
		this.departurePlatform = departurePlatform;
		this.time = time;
		this.platform = platform;
	}

	public Station(JSONObject json)
	{
		this.name = json.getString("Name");
		this.coordinates = json.getString("Coordinates");
		if (!json.get("ArrivalPlatform").equals(null))
			this.arrivalPlatform = json.getString("ArrivalPlatform");
		else
			this.arrivalPlatform = LangageHandler.chooseLangage("nietGekend");
		
		if (!json.get("DeparturePlatform").equals(null))
			this.departurePlatform = json.getString("DeparturePlatform");
		else
			this.departurePlatform = LangageHandler.chooseLangage("nietGekend");
		
		if (!json.get("Time").equals(null))
			this.time = new TrainTime(json.getJSONObject("Time"));
		else
			this.time = new TrainTime();
		
		if (!json.get("Platform").equals(null))
			this.platform = json.getString("Platform");
		else
			this.platform = LangageHandler.chooseLangage("nietGekend");
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getCoordinates()
	{
		return coordinates;
	}

	public void setCoordinates(String coordinates)
	{
		this.coordinates = coordinates;
	}

	public String getArrivalPlatform()
	{
		return arrivalPlatform;
	}

	public void setArrivalPlatform(String arrivalPlatform)
	{
		this.arrivalPlatform = arrivalPlatform;
	}

	public String getDeparturePlatform()
	{
		return departurePlatform;
	}

	public void setDeparturePlatform(String departurePlatform)
	{
		this.departurePlatform = departurePlatform;
	}

	public TrainTime getTime()
	{
		return time;
	}

	public void setTime(TrainTime time)
	{
		this.time = time;
	}

	public String getPlatform()
	{
		return platform;
	}

	public void setPlatform(String platform)
	{
		this.platform = platform;
	}
	
}
