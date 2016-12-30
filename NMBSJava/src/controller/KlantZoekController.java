package controller;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import dao.CustomerDAO;
import model.Customer;

import panels.KlantZoekPanel;

public class KlantZoekController {

	public static void startListening(KlantZoekPanel klantZoek) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {

				klantZoek.getBtnZoek().addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {

						String klantNr = klantZoek.getTxtKlantNr().getText();

						CustomerDAO handler = new CustomerDAO();
						Customer c = handler.selectOne(klantNr);

						klantZoek.getLblResultat().setText(c.getCustomerID() + " " + c.getFirstName() + " "
								+ c.getLastName() + " " + c.getBirthDate() + " " + c.getEmail() + " " + c.getAddress());

					}
				});

				klantZoek.getBtnZoek1().addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {

						String kaartNr = klantZoek.getTxtKaartNr().getText();
						CustomerDAO handler = new CustomerDAO();
						Customer c = handler.selectOneOnRailCardID(kaartNr);

						klantZoek.getLblResultat().setText(c.getCustomerID() + " " + c.getFirstName() + " "
								+ c.getLastName() + " " + c.getBirthDate() + " " + c.getEmail() + " " + c.getAddress());

					}
				});

				klantZoek.getBtnZoek2().addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {

						String klantAchterNaam = klantZoek.getTxtKlantNaam().getText();
						CustomerDAO handler = new CustomerDAO();
						Customer c = handler.selectOneOnLastName(klantAchterNaam);

						klantZoek.getLblResultat().setText(c.getCustomerID() + " " + c.getFirstName() + " "
								+ c.getLastName() + " " + c.getBirthDate() + " " + c.getEmail() + " " + c.getAddress());

					}
				});

				klantZoek.getBtnZoek3().addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {

						String klantVoorNaam = klantZoek.getTxtKlantVoorNaam().getText();
						CustomerDAO handler = new CustomerDAO();
						Customer c = handler.selectOneOnFirstName(klantVoorNaam);

						klantZoek.getLblResultat().setText(c.getCustomerID() + " " + c.getFirstName() + " "
								+ c.getLastName() + " " + c.getBirthDate() + " " + c.getEmail() + " " + c.getAddress());

					}
				});
			}
		});

	}

}