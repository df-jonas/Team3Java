package controller;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import dao.AddressDAO;
import dao.CustomerDAO;
import dao.RailCardDAO;
import dao.RouteDAO;
import dao.SubscriptionDAO;
import gui.GUIDateFormat;
import model.Address;
import model.Customer;
import model.RailCard;
import model.Route;
import model.Subscription;
import panels.NieuwAbonnementPanel;

public class KoopAbonnementController {
	private static UUID vanID;
	private static UUID naarID;
	private static UUID routeID;
	private static UUID railcardID;

	
	public static void startListening(NieuwAbonnementPanel abonnement) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				abonnement.getBtnVerzenden().setEnabled(false);
				
				
				try {
					String startDatum = abonnement.getDteStartDatum().getJFormattedTextField().getText();
					Calendar c = GUIDateFormat.dateToCalendar((Date) GUIDateFormat.stringToObject(startDatum));
					c.add(Calendar.MONTH, 1);
					startDatum = GUIDateFormat.objectToString(c);
					abonnement.getLblBerekendeVervaldatum().setText(startDatum);
				} catch (ParseException pe) {
					pe.printStackTrace();
				}

				abonnement.getBtnCustomerID().addActionListener(new ActionListener() {
					// automatisch customer gegevens invullen adhv customerID
					@Override
					public void actionPerformed(ActionEvent e) {
						abonnement.getLblFoutmelding().setText("");
						String customerID =abonnement.getTxtCustomerID().getText();


						CustomerDAO daoCustomer = new CustomerDAO();
						Customer c = daoCustomer.selectOne(customerID);
						
						if (c != null)
						{
							abonnement.setTxtVoornaam(c.getFirstName());
							abonnement.setTxtNaam(c.getLastName());
							abonnement.setTxtEmail(c.getEmail());
							abonnement.setDteGeboorteDatum(c.getBirthDate().toString());
							abonnement.setTxtGemeente(c.getAddress().getCity());
							abonnement.setTxtStraat(c.getAddress().getStreet());
							abonnement.setTxtNummer(Integer.toString(c.getAddress().getNumber()));
							abonnement.setTxtPostcode(Integer.toString(c.getAddress().getZipCode()));							
						}
						

					}
				});

				abonnement.getTxtNaam().getDocument().addDocumentListener(new DocumentListener() {
					public void changedUpdate(DocumentEvent e) {
						abonnement.getBtnVerzenden().setEnabled(false);
					}
					public void removeUpdate(DocumentEvent e) {
						abonnement.getBtnVerzenden().setEnabled(false);
					}
					public void insertUpdate(DocumentEvent e) {
						abonnement.getBtnVerzenden().setEnabled(false);
					}
				});

				abonnement.getTxtVoornaam().getDocument().addDocumentListener(new DocumentListener() {
					public void changedUpdate(DocumentEvent e) {
						abonnement.getBtnVerzenden().setEnabled(false);
					}
					public void removeUpdate(DocumentEvent e) {
						abonnement.getBtnVerzenden().setEnabled(false);
					}
					public void insertUpdate(DocumentEvent e) {
						abonnement.getBtnVerzenden().setEnabled(false);
					}
				});

				abonnement.getTxtEmail().getDocument().addDocumentListener(new DocumentListener() {
					public void changedUpdate(DocumentEvent e) {
						abonnement.getBtnVerzenden().setEnabled(false);
					}
					public void removeUpdate(DocumentEvent e) {
						abonnement.getBtnVerzenden().setEnabled(false);
					}
					public void insertUpdate(DocumentEvent e) {
						abonnement.getBtnVerzenden().setEnabled(false);
					}
				});

				abonnement.getTxtStraat().getDocument().addDocumentListener(new DocumentListener() {
					public void changedUpdate(DocumentEvent e) {
						abonnement.getBtnVerzenden().setEnabled(false);
					}
					public void removeUpdate(DocumentEvent e) {
						abonnement.getBtnVerzenden().setEnabled(false);
					}
					public void insertUpdate(DocumentEvent e) {
						abonnement.getBtnVerzenden().setEnabled(false);
					}
				});

				abonnement.getTxtNummer().getDocument().addDocumentListener(new DocumentListener() {
					public void changedUpdate(DocumentEvent e) {
						abonnement.getBtnVerzenden().setEnabled(false);
					}
					public void removeUpdate(DocumentEvent e) {
						abonnement.getBtnVerzenden().setEnabled(false);
					}
					public void insertUpdate(DocumentEvent e) {
						abonnement.getBtnVerzenden().setEnabled(false);
					}
				});

				abonnement.getTxtPostcode().getDocument().addDocumentListener(new DocumentListener() {
					public void changedUpdate(DocumentEvent e) {
						abonnement.getBtnVerzenden().setEnabled(false);
					}
					public void removeUpdate(DocumentEvent e) {
						abonnement.getBtnVerzenden().setEnabled(false);
					}
					public void insertUpdate(DocumentEvent e) {
						abonnement.getBtnVerzenden().setEnabled(false);
					}
				});

				abonnement.getTxtGemeente().getDocument().addDocumentListener(new DocumentListener() {
					public void changedUpdate(DocumentEvent e) {
						abonnement.getBtnVerzenden().setEnabled(false);
					}
					public void removeUpdate(DocumentEvent e) {
						abonnement.getBtnVerzenden().setEnabled(false);
					}
					public void insertUpdate(DocumentEvent e) {
						abonnement.getBtnVerzenden().setEnabled(false);
					}
				});

				abonnement.getTxtStation1().addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						abonnement.getBtnVerzenden().setEnabled(false);
					}
				});

				abonnement.getTxtStation2().addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						abonnement.getBtnVerzenden().setEnabled(false);
					}
				});

				abonnement.getDteGeboorteDatum().addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						abonnement.getBtnVerzenden().setEnabled(false);
					}
				});

				abonnement.getDteStartDatum().addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						try {
							String startDatum = abonnement.getDteStartDatum().getJFormattedTextField().getText();
							Calendar c = GUIDateFormat.dateToCalendar((Date) GUIDateFormat.stringToObject(startDatum));
							c.add(Calendar.MONTH, 1);
							startDatum = GUIDateFormat.objectToString(c);
							abonnement.getLblBerekendeVervaldatum().setText(startDatum);
						} catch (ParseException pe) {
							pe.printStackTrace();
						}
						abonnement.getBtnVerzenden().setEnabled(false);
					}
				});

				abonnement.getCbxDuur().addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						abonnement.getBtnVerzenden().setEnabled(false);
						abonnement.getLblBerekendeVervaldatum().setText(getVervalDatum(abonnement));

					}
				});

				abonnement.getRdbEersteKlasse().addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						abonnement.getBtnVerzenden().setEnabled(false);
					}
				});

				abonnement.getRdbTweedeKlasse().addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						abonnement.getBtnVerzenden().setEnabled(false);
					}
				});

				abonnement.getCbxDiscount().addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						abonnement.getBtnVerzenden().setEnabled(false);
					}
				});

				abonnement.getCbxTreinkaart().addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						abonnement.getBtnVerzenden().setEnabled(false);
					}
				});

				abonnement.getBtnValideer().addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent ae) {
						if (correctFormulier(abonnement)) {
							// prijsberekening
							abonnement.getLblPrint().setText("100");
							abonnement.getBtnVerzenden().setEnabled(true);
						} else {
							abonnement.getLblPrint().setText("0");
						}
					}
				});

				abonnement.getBtnVerzenden().addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
					
						
						if (correctFormulier(abonnement)) {
							// customer aanmaken
							if (!customerCheck(abonnement)) {
								createCustomer(abonnement);
							}
							
							vanID = abonnement.getTxtStation1().getSelectedStation();
							naarID = abonnement.getTxtStation2().getSelectedStation();
							
							readRouteID();
							createAbonnement(abonnement);

						}
					}
				});
			}
		});
	}

	public static Boolean customerCheck(NieuwAbonnementPanel abonnement) {
		
		String abonnementCustomerID = abonnement.getLblCustomerID().getText();
		
		CustomerDAO daoCustomer = new CustomerDAO();
		Customer customer = daoCustomer.selectOne(abonnementCustomerID);

		if (customer == null)
			return false;
		else 
			railcardID = customer.getRailCard().getRailCardID();
		
		return true;
	} 

	public static Boolean correctFormulier(NieuwAbonnementPanel abonnement) {
		String naam = abonnement.getTxtNaam().getText();
		String voornaam = abonnement.getTxtVoornaam().getText();
		String geboorteDatum = abonnement.getDteGeboorteDatum().getJFormattedTextField().getText();
		String email = abonnement.getTxtEmail().getText();
		String straat = abonnement.getTxtStraat().getText();
		String nummer = abonnement.getTxtNummer().getText();
		String postcode = abonnement.getTxtPostcode().getText();
		String gemeente = abonnement.getTxtGemeente().getText();
		String startDatum = abonnement.getDteStartDatum().getJFormattedTextField().getText();
		String treinkaart = abonnement.getCbxTreinkaart().getSelectedItem().toString();
		String korting = abonnement.getCbxDiscount().getSelectedItem().toString();
		String station1 = abonnement.getTxtStation1().getSelectedItem().toString();
		String station2 = abonnement.getTxtStation2().getSelectedItem().toString();

		if (
				!naam.equals("") 
				&& !voornaam.equals("") 
				&& DateTimeConverter.checkDate(geboorteDatum) 
				&& !email.equals("")
				&& !straat.equals("") && !nummer.equals("") 
				&& !postcode.equals("")
				&& !gemeente.equals("") 
				&& !treinkaart.equals(null) 
				&& DateTimeConverter.checkDate(startDatum)
				&& !korting.equals(null) 
				&& !station1.equals("") 
				&& !station2.equals("")
			) {
			
			if (!email.contains("@")) {
				abonnement.getLblFoutmelding().setText("Geen geldig e-mailadres.");
				return false;
			}
			abonnement.getLblFoutmelding().setText("Het formulier is correct");
			return true;
		} else {
			abonnement.getLblFoutmelding().setText("Het formulier is niet volledig.");
			return false;
		}
	}

	public static String getVervalDatum(NieuwAbonnementPanel abonnement) {
		String vervalDatum = "";
		try {
			String duur = abonnement.getCbxDuur().getSelectedItem().toString();
			String startDatum = abonnement.getDteStartDatum().getJFormattedTextField().getText();

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

			startDatum = GUIDateFormat.objectToString(c);
			vervalDatum = startDatum;

		} catch (ParseException pe) {
			pe.printStackTrace();
		}
		return vervalDatum;

	}
	
	public static void readRouteID() {
		//readStationID();

		RouteDAO daoRoute = new RouteDAO();
		Route route = daoRoute.selectOneOnRoute(vanID.toString(), naarID.toString());
		
		if (route == null)
			createRoute();
		else
			routeID = route.getRouteID();

	}


	public static void createRoute() {

		Route r = new Route(vanID, naarID);
		RouteDAO daoRoute = new RouteDAO();
		daoRoute.insert(r);

	}

	public static void createAbonnement(NieuwAbonnementPanel abonnement) {
		String vervalDatum = getVervalDatum(abonnement);
		String startDatum = abonnement.getDteStartDatum().getJFormattedTextField().getText();
		String korting = abonnement.getCbxDiscount().getSelectedItem().toString();

		HashMap<String, UUID> discounts = abonnement.getDiscounts();
		UUID discountID = discounts.get(korting);
		
		System.out.println(railcardID);
		System.out.println(routeID);
		
		Subscription sub = new Subscription(railcardID, routeID, discountID, startDatum, vervalDatum);
		SubscriptionDAO daoSubscription = new SubscriptionDAO();
		daoSubscription.insert(sub);

	}

	public static void createCustomer(NieuwAbonnementPanel abonnement) {
		int nummer =  Integer.parseInt(abonnement.getTxtNummer().getText());
		int postcode =  Integer.parseInt(abonnement.getTxtPostcode().getText());
		String geboorteDatum = abonnement.getDteGeboorteDatum().getJFormattedTextField().getText();
		
		// Aanmaken new models
		Address address = new Address(abonnement.getTxtStraat().getText(), nummer , abonnement.getTxtGemeente().getText(), postcode, "");
		RailCard railcard = new RailCard();
		Customer customer = new Customer(abonnement.getTxtVoornaam().getText(), abonnement.getTxtNaam().getText(), geboorteDatum,  abonnement.getTxtEmail().getText(), address, railcard);
				
		railcardID = railcard.getRailCardID();
		
		// populaten extra variabelen voor dao gemakelijkheid
		
		//alle modelen wegschrijven naar de database in correcte order
		
		AddressDAO addressHandler = new AddressDAO();
		RailCardDAO railCardHandler = new RailCardDAO();
		CustomerDAO customerHandler = new CustomerDAO();
		
		addressHandler.insert(address);
		railCardHandler.insert(railcard);
		customerHandler.insert(customer);
	}
}