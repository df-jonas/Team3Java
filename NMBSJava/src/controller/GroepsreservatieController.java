package controller;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

import controller.APIController.APIUrl;
import controller.APIController.RequestType;
import dao.ReservationDAO;
import dao.RouteDAO;
import dao.StationDAO;
import gui.GUIDateFormat;
import gui.LangageHandler;
import model.Reservation;
import model.Route;
import model.Station;
import model.api.Connection;
import model.api.RouteBerekening;
import panels.GroepsReservatiePanel;
import services.APIRequest;
import services.APIThread;
import services.ThreadListener;

public class GroepsreservatieController {

	private static String van;
	private static String naar;
	private static String datum;
	private static String trein;
	private static int aantalReizigers;
	private static String time;
	private static boolean vanOke = true, naarOke = true;
	private static double prijs;
	private static UUID routeID;
	public static void startListening(GroepsReservatiePanel reservatie) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {

				reservatie.getAutVan().addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						vanOke = true;
						if (vanOke && naarOke) {
							zoekTreinen(reservatie);
						}
					}

				});

				reservatie.getAutNaar().addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						naarOke = true;
						if (vanOke && naarOke) {
							zoekTreinen(reservatie);
						}
					}

				});

				reservatie.getBtnPrint().addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						van = (String) reservatie.getAutVan().getSelectedItem();
						naar = (String) reservatie.getAutNaar().getSelectedItem();
						datum = reservatie.getDteDate().getJFormattedTextField().getText();
						trein = (String) reservatie.getCboTrein().getSelectedItem();
						aantalReizigers = (int) reservatie.getAantPersonen().getValue();
						time = reservatie.getTmeTime().getText();

						if (!van.equals("") && !naar.equals("") && DateTimeConverter.checkDate(datum)
								 && !trein.equals(null)) {
							ReservationDAO res = new ReservationDAO();
							readRouteID();
							Reservation r = new Reservation(aantalReizigers, trein, prijs,datum, routeID);
							res.insert(r);
							System.out.println(LangageHandler.chooseLangage("resSucces"));
						}
					}

				});

			}

		});

	}
	
	public static void readRouteID() {
		RouteDAO r = new RouteDAO();
		StationDAO handler = new StationDAO();
		Station s1 = handler.selectOneOnName(van);
		Station s2 = handler.selectOneOnName(naar);
		Route route = r.selectOneOnRoute(s1.getStationID().toString(), s2.getStationID().toString());
		if (route != null) {
			routeID = route.getRouteID();
		} else {
			System.out.println("Route bestaat niet");
			route = new Route(s1.getStationID(), s2.getStationID());
			r.insert(route);
			routeID = route.getRouteID();
		}
	}

	private static void zoekTreinen(GroepsReservatiePanel reservatie) {
		van = reservatie.getAutVan().getSelectedItem().toString();
		naar = reservatie.getAutNaar().getSelectedItem().toString();
		datum = reservatie.getDteDate().getJFormattedTextField().getText();
		time = reservatie.getTmeTime().getText();
		
		try
		{
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("to", naar);
			params.put("from", van);
			params.put("date", GUIDateFormat.getRawDate(datum));
			params.put("time", GUIDateFormat.getRawTime(time));
			params.put("timeSel", "depart");
			params.put("format", "json");
			params.put("lang", "NL");
			
			UUID requestID = UUID.randomUUID();
			APIRequest request = new APIRequest(requestID, APIUrl.IRAILS, "", RequestType.GET,
					params);
			ThreadListener listener = new ThreadListener()
			{
	
				@Override
				public void setResult(JSONArray data)
				{
					if (data != null) {
						JSONObject json = data.getJSONObject(0);
	     
						RouteBerekening rb = new RouteBerekening(json);
	
						if (rb.getConnections() == null)
							return;
						
						for (Connection con : rb.getConnections())
						{
							reservatie.getCboTrein().addItem(con.getDeparture().getVehicle());
						}
					}
				}
	
			};
	
			APIThread apiThread = APIThread.getThread();
			apiThread.addListener(requestID, listener);
			apiThread.addAPIRequest(request);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}
	
}