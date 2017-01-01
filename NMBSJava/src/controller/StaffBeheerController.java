package controller;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.UUID;

import dao.AddressDAO;
import dao.StaffDAO;
import dao.StationDAO;
import gui.Popup;
import model.Address;
import model.Staff;
import model.Station;
import panels.StaffBeheerPanel;
import services.JBcryptVerifier;

public class StaffBeheerController {
	public static void startListening(StaffBeheerPanel staff) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				staff.getBtnVoegToe().addActionListener(new ActionListener() {
					@SuppressWarnings("deprecation")
					public void actionPerformed(ActionEvent e) {
						String voornaam = staff.getTxtVoornaam().getText();
						String achternaam = staff.getTxtAchternaam().getText();
						String datum = staff.getDatePicker().getJFormattedTextField().getText();
						String email = staff.getTxtEmail().getText();
						String straat = staff.getTxtStraat().getText();
						String gemeente = staff.getTxtGemeente().getText();
						String station = (String) staff.getTxtStation().getSelectedItem();
						String coordinates = staff.getTxtCoordinates().getText();
						String username = staff.getTxtUsername().getText();
						String password = (String) staff.getTxtPassword().getText();

						int rechten = 0;

						if (voornaam.equals("") || achternaam.equals("") || email.equals("") || straat.equals("")
								|| staff.getTxtNummer().getText().equals("")
								|| staff.getTxtPostcode().getText().equals("") || gemeente.equals("")
								|| staff.getTxtPostcode().toString().equals("") || username.equals("")
								|| password.equals("")) {
							Popup.warningMessage("WarningPopup", "WarningPopupTitel");
						} else {

							boolean check = true;

							if (!email.contains("@")) {
								Popup.warningMessage("emailFout", "WarningPopupTitel");
								check = false;
							}

							int nummer = 0;
							try {
								nummer = Integer.parseInt(staff.getTxtNummer().getText());
							} catch (NumberFormatException e1) {
								Popup.warningMessage("nummerWarningPopup", "WarningPopupTitel");
								check = false;
							}

							int postcode = 0;
							try {
								postcode = Integer.parseInt(staff.getTxtPostcode().getText());
							} catch (NumberFormatException e1) {
								Popup.warningMessage("postcodeWarningPopup", "WarningPopupTitel");
								check = false;
							}

							ArrayList<Staff> staffList = new ArrayList<Staff>();
							StaffDAO staffDa = new StaffDAO();
							staffList = staffDa.selectAll();

							for (int i = 0; i < staffList.size(); i++) {
								if (username.equals(staffList.get(i).getUserName())) {
									Popup.warningMessage("usernameWarning", "usernameWarningTitel");
									check = false;
								}
							}

							if (staff.getRdbJa().isSelected()) {
								rechten = 1;
							} else {
								rechten = 0;
							}

							if (check) {
								try {
									Address adres = new Address(straat, nummer, gemeente, postcode, coordinates);
									AddressDAO adresD = new AddressDAO();
									adresD.insert(adres);

									Station stat = new Station();
									StationDAO statD = new StationDAO();
									ArrayList<Station> statList = new ArrayList<Station>();
									statList = statD.selectAll();

									for (int i = 0; i < statList.size(); i++) {
										if (station.equals(statList.get(i).getStationName())) {
											stat = statList.get(i);
										}
									}
									password = JBcryptVerifier.hashPassword(password);
									Staff staff = new Staff(adres, stat, voornaam, achternaam, username, password,
											rechten, datum, email);
									staff.setAddressID(adres.getAddressID());
									staff.setStationID(stat.getStationID());

									StaffDAO staffD = new StaffDAO();
									staffD.insert(staff);

									SyncController.SyncStaff();

								} catch (NullPointerException e1) {
									Popup.errorMessage("voorwerpWarningPopup", "voorwerpWarningPopupTitel");
								}
							}

							if (check == true) {
								Popup.plainMessage("staffSuccesvolPopup", "staffSuccesvolPopupTitel");
							}
						}
					}
				});

				staff.getBtnWijzig().addActionListener(new ActionListener() {
					@SuppressWarnings("deprecation")
					public void actionPerformed(ActionEvent e) {
						String voornaam = staff.getTxtVoornaam().getText();
						String achternaam = staff.getTxtAchternaam().getText();
						String datum = staff.getDatePicker().getJFormattedTextField().getText();

						ArrayList<Staff> staffList = new ArrayList<Staff>();
						StaffDAO staffD = new StaffDAO();
						staffList = staffD.selectAll();
						Staff staffM = new Staff();

						UUID adresID = null;
						UUID stationID = null;
						UUID staffID = null;

						for (int i = 0; i < staffList.size(); i++) {
							if (voornaam.equals(staffList.get(i).getFirstName())
									&& achternaam.equals(staffList.get(i).getLastName())
									&& datum.equals(staffList.get(i).getBirthDate())) {

								staffM = staffList.get(i);
							}
						}
						staffID = staffM.getStaffID();
						
						adresID = staffM.getAddressID();

						String email = "";

						if (staff.getTxtEmail().getText().equals("")) {
							email = staffM.getEmail();
						} else {
							email = staff.getTxtEmail().getText();
						}

						ArrayList<Address> addressList = new ArrayList<Address>();
						AddressDAO addressD = new AddressDAO();
						addressList = addressD.selectAll();
						Address addressM = new Address();

						for (int i = 0; i < addressList.size(); i++) {
							if (addressList.get(i).getAddressID().equals(staffM.getAddressID())) {
								addressM = addressList.get(i);
							}
						}

						String straat;

						if (staff.getTxtStraat().getText().equals("")) {
							straat = addressM.getStreet();
						} else {
							straat = staff.getTxtStraat().getText();
						}

						int nummer = 0;

						if (staff.getTxtNummer().getText().equals("")) {
							nummer = addressM.getNumber();
						} else {
							try {
								nummer = Integer.parseInt(staff.getTxtNummer().getText());
							} catch (NumberFormatException e1) {
								Popup.warningMessage("nummerWarningPopup", "WarningPopupTitel");
							}
						}

						String gemeente;

						if (staff.getTxtGemeente().getText().equals("")) {
							gemeente = addressM.getCity();
						} else {
							gemeente = staff.getTxtGemeente().getText();
						}

						int postcode = 0;

						if (staff.getTxtPostcode().getText().equals("")) {
							postcode = addressM.getZipCode();
						} else {
							try {
								postcode = Integer.parseInt(staff.getTxtPostcode().getText());
							} catch (NumberFormatException e1) {
								Popup.warningMessage("postcodeWarningPopup", "WarningPopupTitel");
							}
						}

						Address adres = new Address(straat, nummer, gemeente, postcode, null);
						adres.setAddressID(adresID);
						addressD.update(adres);

						ArrayList<Station> stationList = new ArrayList<Station>();
						StationDAO stationD = new StationDAO();
						stationList = stationD.selectAll();
						Station stat = new Station();

						for (int i = 0; i < stationList.size(); i++) {
							if (stationList.get(i).getStationName().equals(staff.getTxtStation().getSelectedItem())) {
								stat = stationList.get(i);
							}
						}

						stationID = stat.getStationID();

						String username;
						if (staff.getTxtUsername().getText().equals("")) {
							username = staffM.getUserName();
						} else {
							username = staff.getTxtUsername().getText();
						}

						String password;

						if (staff.getTxtPassword().getPassword().equals("")) {
							password = staffM.getPassword();
						} else {
							password = staff.getTxtPassword().getText();
						}

						int rechten = staffM.getRights();
						if (staff.getRdbJa().isSelected()) {
							rechten = 1;
						}

						if (staff.getRdbNee().isSelected()) {
							rechten = 0;
						}

						Staff staffU = new Staff(adres, stat, voornaam, achternaam, username, password, rechten,
								datum, email);
						staffU.setAddressID(adresID);
						staffU.setStationID(stationID);
						staffU.setStaffID(staffID);

						staffD.update(staffU);
					}
				});
			}
		});
	}
}