package controller;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

import controller.Prijsberekening.TypeKeuze;
import dao.RouteDAO;
import dao.StationDAO;
import dao.TicketDAO;
import dao.TypeTicketDAO;
import gui.GUIDateFormat;
import gui.Popup;
import model.Route;
import model.Station;
import model.Ticket;
import model.TypeTicket;
import panels.BiljetPanel;

public class KoopBiljetController {

	private static String van;
	private static String naar;
	private static UUID routeID;
	private static String begindatum;
	private static String einddatum;
	private static int typeKlasseSelectedMnem;
	private static int vanID;
	private static int naarID;
	private static String prijs;

	public static void startListening(BiljetPanel biljet)  {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				biljet.getBtnPrint().addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						van = (String) biljet.getTxtVan().getSelectedItem();
						naar = (String) biljet.getTxtNaar().getSelectedItem();

						begindatum = biljet.getDteGaanDatum().getJFormattedTextField().getText();
						einddatum = biljet.getDteTerugDatum().getJFormattedTextField().getText();
						//int typeHeenTerugSelectedMnem = biljet.getGrpHeenTerug().getSelection().getMnemonic();
						String soortBiljet = (String)biljet.getCboBiljet().getSelectedItem();
						prijs = biljet.getLblPrijs().getText();

						if (!van.equals("") && !naar.equals("") && DateTimeConverter.checkDate(begindatum)
								&& DateTimeConverter.checkDate(einddatum) && !soortBiljet.equals(null)) {
							readRouteID();
							//ticket aanmaken
							TypeTicketDAO handler3 = new TypeTicketDAO();
							TypeTicket type =handler3.selectOneOnName(soortBiljet);
							Ticket t = new Ticket(routeID,type.getTypeTicketID(),GUIDateFormat.getDate(),begindatum,einddatum);
							TicketDAO handler4 = new TicketDAO();
							
							boolean check = false;
							
							try{
								handler4.insert(t);
								check = true;
							} catch(NullPointerException e1) {
								Popup.errorMessage("voorwerpWarningPopup", "voorwerpWarningPopupTitel");
							}
							
							if(check == true)
							{
								Popup.plainMessage("koopBiljetPlainPopup", "koopBiljetPlainPopupTitel");
							}
							
							//System.out.println("Ticket succesvol aangemaakt.");
						}
					}
				});
				biljet.getCboBiljet().addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						van = (String)biljet.getTxtVan().getSelectedItem();
						naar = (String) biljet.getTxtNaar().getSelectedItem();
						
						StationDAO handler = new StationDAO();
						Station s1 = handler.selectOneOnName(van);
						Station s2 = handler.selectOneOnName(naar);
						
						//berekening via prijsberekening en binnenhalen via dao
						TypeTicketDAO handler2 = new TypeTicketDAO();
						
						TypeTicket type =handler2.selectOneOnName((String)biljet.getCboBiljet().getSelectedItem());
						
						biljet.getLblPrijs().setText(Prijsberekening.berekenPrijs(s1, s2,TypeKeuze.TICKET , type.getTypeTicketID().toString())+ " euro");
						
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
			routeID = route.getRouteID();
			r.insert(route);
		}
	}
}
