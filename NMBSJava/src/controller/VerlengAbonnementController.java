package controller;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import controller.Prijsberekening.TypeKeuze;
import dao.CustomerDAO;
import dao.DiscountDAO;
import dao.RouteDAO;
import dao.StationDAO;
import dao.SubscriptionDAO;
import gui.GUIDateFormat;
import model.Customer;
import model.Discount;
import model.Route;
import model.Station;
import model.Subscription;
import panels.VerlengAbonnementPanel;

public class VerlengAbonnementController
{
	private static UUID customerID;
	private static String railCardID = "";

	public static void startListening(VerlengAbonnementPanel abonnement)
	{
		EventQueue.invokeLater(new Runnable() {
			public void run()
			{
				DiscountDAO discountHandler = new DiscountDAO();
				ArrayList<Discount> discountList = discountHandler.selectAll();

				HashMap<String, UUID> kortingMap = new HashMap<String, UUID>();
				for (Discount d : discountList) {
					kortingMap.put(d.getName(), d.getDiscountID());
				}
				abonnement.getBtnMeerInfo().setEnabled(false);
				abonnement.getBtnVerzenden().setEnabled(false);
				try {
					String startDatum = abonnement.getDteStartdatum().getJFormattedTextField().getText();
					Calendar c = GUIDateFormat.dateToCalendar((Date) GUIDateFormat.stringToObject(startDatum));
					c.add(Calendar.MONTH, 1);
					startDatum = GUIDateFormat.objectToString(c);
					abonnement.getLblVervaldatumResult().setText(startDatum);
				}
				catch (ParseException pe) {
					pe.printStackTrace();
				}

				abonnement.getBtnZoek().addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e)
					{
						abonnement.getBtnMeerInfo().setEnabled(true);

						String subsbscriptionID = abonnement.getTxtAbonnementsNummer().getText();

						CustomerDAO customerHandler = new CustomerDAO();
						Customer c = customerHandler.selectOneOnSubscriptionID(subsbscriptionID);

						if (c != null) {
							customerID = c.getCustomerID();
							railCardID = c.getRailCard().getRailCardID().toString();

							abonnement.getLblKLantenNummerResult().setText(c.getCustomerID().toString());

							SubscriptionDAO subscriptionHandler = new SubscriptionDAO();
							Subscription s = subscriptionHandler.selectOne(subsbscriptionID);

							if (s != null) {
								abonnement.getTxtStation1()
										.setSelectedItem(s.getRoute().getArrivalStation().getStationName());
								abonnement.getTxtStation2()
										.setSelectedItem(s.getRoute().getDepartureStation().getStationName());
							}

						}

						abonnement.getBtnMeerInfo().setEnabled(true);
					}
				});

				abonnement.getBtnMeerInfo().addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e)
					{
						if (!abonnement.getLblKLantenNummerResult().getText().equals("")) {

							CustomerDAO daoCustomer = new CustomerDAO();
							Customer c = daoCustomer.selectOne(customerID.toString());

							if (c != null) {
								JFrame frame = new JFrame();
								JOptionPane.showMessageDialog(frame,
										"Naam: " + c.getLastName() + "\n" + "Voornaam: " + c.getFirstName() + "\n"
												+ "Geboortedatum: " + c.getBirthDate() + "\n" + "E-mail: "
												+ c.getEmail() + "\n" + "Straat + nr: " + c.getAddress().getStreet()
												+ " " + c.getAddress().getNumber() + "\n" + "Postcode: "
												+ c.getAddress().getZipCode() + "\n" + "Gemeente: "
												+ c.getAddress().getCity() + "\n",
										"Meer info", JOptionPane.PLAIN_MESSAGE);
							}

						}
					}
				});

				abonnement.getDteStartdatum().addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e)
					{
						try {
							String startDatum = abonnement.getDteStartdatum().getJFormattedTextField().getText();
							Calendar c = GUIDateFormat.dateToCalendar((Date) GUIDateFormat.stringToObject(startDatum));
							c.add(Calendar.MONTH, 1);
							String vervalDatum = GUIDateFormat.objectToString(c);
							abonnement.getLblVervaldatumResult().setText(vervalDatum);
						}
						catch (ParseException pe) {
							pe.printStackTrace();
						}
					}
				});

				abonnement.getCbxDuur().addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e)
					{
						try {
							String duur = abonnement.getCbxDuur().getSelectedItem().toString();
							String startDatum = abonnement.getDteStartdatum().getJFormattedTextField().getText();
							Calendar c = GUIDateFormat.dateToCalendar((Date) GUIDateFormat.stringToObject(startDatum));
							switch (duur) {
							case "1 maand":
								c.add(Calendar.MONTH, 1);
								break;
							case "3 maanden":
								c.add(Calendar.MONTH, 3);
								break;
							case "12 maanden":
								c.add(Calendar.MONTH, 12);
								break;
							}
							String vervalDatum = GUIDateFormat.objectToString(c);
							abonnement.getLblVervaldatumResult().setText(vervalDatum);
						}
						catch (ParseException pe) {
							pe.printStackTrace();
						}
					}
				});

				abonnement.getBtnValideer().addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e)
					{
						String abonnementsNummer = abonnement.getTxtAbonnementsNummer().getText();
						String ticketTypeID = abonnement.getCbxTreinkaart().getSelectedDiscount().toString();
						String kortingID = abonnement.getCbxDiscount().getSelectedDiscount().toString();
						String duur = abonnement.getCbxDuur().getSelectedItem().toString();
						String startdatum = abonnement.getDteStartdatum().getJFormattedTextField().getText();
						String station1ID = abonnement.getTxtStation1().getSelectedStation().toString();
						String station2ID = abonnement.getTxtStation2().getSelectedStation().toString();

						if (!abonnementsNummer.equals("") 
								&& !duur.equals(null)
								&& !ticketTypeID.equals("")
								&& !kortingID.equals("")
								&& DateTimeConverter.checkDate(startdatum) 
								&& !station1ID.equals("")
								&& !station2ID.equals("")
								&& customerID != null
						) {


							StationDAO handler = new StationDAO();
							Station s1 = handler.selectOne(station1ID.toString());
							Station s2 = handler.selectOne(station2ID.toString());

							abonnement.getLblBedrag().setText(Double.toString(
									Prijsberekening.berekenPrijs(s1, s2, TypeKeuze.PASS, ticketTypeID)));
							abonnement.getLblFoutmelding().setText("Het formulier is correct");
							abonnement.getBtnVerzenden().setEnabled(true);

						}
						else {
							abonnement.getLblFoutmelding().setText("Het formulier is niet volledig");
						}
					}
				});

				abonnement.getBtnVerzenden().addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e)
					{
						String abonnementsNummer = abonnement.getTxtAbonnementsNummer().getText();
						String ticketTypeID = abonnement.getCbxTreinkaart().getSelectedDiscount().toString();
						String kortingID = abonnement.getCbxDiscount().getSelectedDiscount().toString();
						String duur = abonnement.getCbxDuur().getSelectedItem().toString();
						String startdatum = abonnement.getDteStartdatum().getJFormattedTextField().getText();
						String station1ID = abonnement.getTxtStation1().getSelectedStation().toString();
						String station2ID = abonnement.getTxtStation2().getSelectedStation().toString();
						String eindDatum = abonnement.getLblVervaldatumResult().getText();
						
						if (!abonnementsNummer.equals("") 
							&& !duur.equals(null)
							&& !ticketTypeID.equals("")
							&& !kortingID.equals("")
							&& DateTimeConverter.checkDate(startdatum) 
							&& !station1ID.equals("")
							&& !station2ID.equals("") 
							&& customerID != null
						) {

							StationDAO handler = new StationDAO();

							Station s1 = handler.selectOne(station1ID);
							Station s2 = handler.selectOne(station2ID);
							
							RouteDAO r = new RouteDAO();
							Route route = r.selectOneOnRoute(station1ID, station2ID);
							if (route == null) {
								System.out.println("Route bestaat niet");
								route = new Route(s1.getStationID(), s2.getStationID());
								r.insert(route);
							}

							
							DiscountDAO handler4 = new DiscountDAO();
							Discount d = handler4.selectOne(kortingID);
							
							
							abonnement.getLblBedrag().setText(Double.toString(
									Prijsberekening.berekenPrijs(s1, s2, TypeKeuze.PASS, ticketTypeID)));
							abonnement.getLblFoutmelding().setText("Het formulier is correct");
							abonnement.getBtnVerzenden().setEnabled(true);

							SubscriptionDAO handler3 = new SubscriptionDAO();
							Subscription s = handler3.selectOne(UUID.fromString(abonnementsNummer).toString());
							s.setRailCardID(UUID.fromString(railCardID));
							s.setRouteID(route.getRouteID());
							s.setDiscountID(d.getDiscountID());
							s.setValidFrom(startdatum);
							s.setValidUntil(eindDatum);
							s.update();
							handler3.update(s);

						}
						else {
							abonnement.getLblFoutmelding().setText("Het formulier is niet volledig");
						}

					}
				});

				abonnement.getTxtAbonnementsNummer().getDocument().addDocumentListener(new DocumentListener() {
					public void changedUpdate(DocumentEvent e)
					{
						abonnement.getBtnVerzenden().setEnabled(false);
						abonnement.getLblKLantenNummerResult().setText("");
						abonnement.getBtnMeerInfo().setEnabled(false);
						customerID = null;
					}

					public void removeUpdate(DocumentEvent e)
					{
						abonnement.getBtnVerzenden().setEnabled(false);
						abonnement.getLblKLantenNummerResult().setText("");
						abonnement.getBtnMeerInfo().setEnabled(false);
						customerID = null;
					}

					public void insertUpdate(DocumentEvent e)
					{
						abonnement.getBtnVerzenden().setEnabled(false);
						abonnement.getLblKLantenNummerResult().setText("");
						abonnement.getBtnMeerInfo().setEnabled(false);
						customerID = null;
					}
				});

			}
		});
	}

}
