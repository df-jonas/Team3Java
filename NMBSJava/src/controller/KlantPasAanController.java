package controller;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import dao.CustomerDAO;
import dao.AddressDAO;

import model.Customer;
import model.Address;

import panels.KlantPasAanPanel;


public class KlantPasAanController {
	
	
	
	
	public static void startListening(KlantPasAanPanel klantPasAan) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
				klantPasAan.getBtnPasAan().addActionListener(new ActionListener() {
				
					public void actionPerformed(ActionEvent e) {
						
					String customerID = klantPasAan.getTxtID().getText();
					String voorNaam = klantPasAan.getTxtFirst().getText();
					String achterNaam = klantPasAan.getTxtLast().getText();
					String geboorteDatum = klantPasAan.getDatePicker().getJFormattedTextField().getText();
					String email = klantPasAan.getTxtEmail().getText();
					String straat = klantPasAan.getTxtStreet().getText();
					int nummer = Integer.parseInt(klantPasAan.getTxtNumber().getText());
					String stad = klantPasAan.getTxtCity().getText();
					int postCode = Integer.parseInt(klantPasAan.getTxtZipCode().getText());
					String coordinaten = klantPasAan.getTxtCoordinates().getText();
					String adresID = klantPasAan.getTxtAddressID().getText();
					
					   
					
				       CustomerDAO handler = new CustomerDAO();
				       Customer c = handler.selectOne(customerID);
				       c.setFirstName(voorNaam);
				       c.setLastName(achterNaam);
				       c.setBirthDate(geboorteDatum);
				       c.setEmail(email);
				       c.update();
					   handler.update(c);
					   
					   AddressDAO handler1 = new AddressDAO();
					   Address a = handler1.selectOne(adresID);
					   a.setStreet(straat);
					   a.setNumber(nummer);
					   a.setCity(stad);
					   a.setZipCode(postCode);
					   a.setCoordinates(coordinaten);
					   a.update();
					   handler1.update(a);
					   
					}
				});
				
				
				
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
