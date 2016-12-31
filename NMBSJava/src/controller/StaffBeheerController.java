package controller;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.UUID;

import panels.StaffBeheerPanel;
import services.JBcryptVerifier;
import model.Station;
import model.Address;
import model.Staff;
import dao.StationDAO;
import gui.Popup;
import dao.AddressDAO;
import dao.StaffDAO;

public class StaffBeheerController {
	public static void startListening(StaffBeheerPanel staff) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				staff.getBtnVoegToe().addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						String voornaam = staff.getTxtVoornaam().getText();
						String achternaam = staff.getTxtAchternaam().getText();
						String datum = staff.getDatePicker().getJFormattedTextField().getText();
						String email = staff.getTxtEmail().getText();
						String straat = staff.getTxtStraat().getText();
						String gemeente = staff.getTxtGemeente().getText();
						String station = (String) staff.getTxtStation().getSelectedItem();
						String username = staff.getTxtUsername().getText();
						String password = (String)staff.getTxtPassword().getText();

						int rechten = 0;

						if(voornaam.equals("") || achternaam.equals("") || email.equals("")|| 
								straat.equals("") || staff.getTxtNummer().getText().equals("")|| staff.getTxtPostcode().getText().equals("")||gemeente.equals("") ||
								staff.getTxtPostcode().toString().equals("") || username.equals("") || password.equals(""))
						{
							Popup.warningMessage("WarningPopup", "WarningPopupTitel");
						}
						else
						{

							boolean check = true;


							int nummer = 0;
							try {
								nummer=Integer.parseInt(staff.getTxtNummer().getText());
							}catch (NumberFormatException e1){
								Popup.warningMessage("nummerWarningPopup", "WarningPopupTitel");
								check = false;
							} 

							int postcode = 0;
							try {
								postcode = Integer.parseInt(staff.getTxtPostcode().getText());
							}catch (NumberFormatException e1){
								Popup.warningMessage("postcodeWarningPopup", "WarningPopupTitel");	
								check = false;
							} 


							if(staff.getRdbJa().isSelected())
							{
								rechten = 1;
							}
							else if(staff.getRdbNee().isSelected())
							{
								rechten = 0;
							}

							

							if (check == true)
							{
								try {
									Address adres = new Address(straat,nummer,gemeente,postcode,null);
									AddressDAO adresD = new AddressDAO();
									adresD.insert(adres);

									Station stat= new Station();
									StationDAO statD = new StationDAO();
									ArrayList<Station> statList = new ArrayList<Station>();
									statList = statD.selectAll();

									for (int i = 0; i <statList.size() ;i++)
									{
										if(station.equals(statList.get(i).getStationName()))
										{
											stat = statList.get(i);
										}
									}
									System.out.println(password);
									password = JBcryptVerifier.hashPassword(password);
									System.out.println(password);
									Staff staff = new Staff(adres,stat,voornaam,achternaam,username,password,rechten,datum,email);
									staff.setAddressID(adres.getAddressID());
									staff.setStationID(stat.getStationID());

									StaffDAO staffD = new StaffDAO();
									staffD.insert(staff);
									
									SyncController.SyncStaff();
									
								} catch(NullPointerException e1) {
									Popup.errorMessage("voorwerpWarningPopup", "voorwerpWarningPopupTitel");
								}
							}

							if(check == true)
							{
								Popup.plainMessage("staffSuccesvolPopup", "staffSuccesvolPopupTitel");
							}
						}
					}
				});

				staff.getBtnWijzig().addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						/**String voornaam = staff.getTxtVoornaam().getText();
						String achternaam = staff.getTxtAchternaam().getText();
						//String datum = staff.getDatePicker().getJFormattedTextField().getText();
						String datum="";
						String email = "";
						String straat = "";
						int nummer = 0;
						String gemeente = "";
						int postcode = 0;
						UUID stationID = null;
						String username = "";
						char[] password = staff.getTxtPassword().getPassword();
						String strPassword = "";

						int rechten = 0;

						if(voornaam.equals("") || achternaam.equals(""))
						{
							Popup.warningMessage("staffModifyVulPopup", "staffModifyVulPopupTitel");
						}
						else
						{
							ArrayList<Staff> staffList = new ArrayList<Staff>();
							StaffDAO staffD = new StaffDAO();
							staffList = staffD.selectAll();

							ArrayList<Address> adressList = new ArrayList<Address>();
							AddressDAO adressD = new AddressDAO();
							adressList = adressD.selectAll();
							Address adres = null;

							ArrayList<Station> stationList = new ArrayList<Station>();
							StationDAO stationD = new StationDAO();
							stationList = stationD.selectAll();
							Station stat = new Station();

							for (int i = 0; i < staffList.size(); i++)
							{
								if(voornaam.equals(staffList.get(i).getFirstName()) && achternaam.equals(staffList.get(i).getLastName()))
								{
									if (email.equals(""))
									{
										email = staffList.get(i).getEmail();
									}
									else
									{
										email = staff.getTxtEmail().getText();
									}

									for (int j = 0; j < adressList.size();j++)
									{
										if (staffList.get(i).getAddressID().equals(adressList.get(j).getAddressID()))
										{
											if(straat.equals(""))
											{
												straat = adressList.get(j).getStreet();
											}
											else
											{
												straat = staff.getTxtStraat().getText();
											}

											if(staff.getTxtNummer().getText().equals(""))
											{
												nummer = adressList.get(j).getNumber();
											}
											else
											{
												try {
													nummer=Integer.parseInt(staff.getTxtNummer().getText());
												}catch (NumberFormatException e1){
													Popup.warningMessage("nummerWarningPopup", "WarningPopupTitel");
													//check = false;
												} 
											}

											if(gemeente.equals(""))
											{
												gemeente = adressList.get(j).getCity();
											}
											else
											{
												gemeente = staff.getTxtGemeente().getText();
											}

											if(staff.getTxtPostcode().equals(""))
											{
												postcode = adressList.get(j).getZipCode();
											}
											else
											{
												try {
													postcode = Integer.parseInt(staff.getTxtPostcode().getText());
												}catch (NumberFormatException e1){
													Popup.warningMessage("postcodeWarningPopup", "WarningPopupTitel");	
													//check = false;
												} 
											}

											adres = new Address(straat,nummer,gemeente,postcode,null);
											adressD.update(adres);
										}
									}

									for (int j = 0; i < stationList.size();j++)
									{
										if(stationList.get(i).equals(staff.getTxtStation().getSelectedItem()))
										{
											stationID = stationList.get(i).getStationID();
											stat = stationList.get(i);
										}
									}

									if (username.equals(""))
									{
										username = staffList.get(i).getUserName();
									}
									else
									{
										username = staff.getTxtUsername().getText();
									}

									if(strPassword.equals(""))
									{
										strPassword = staffList.get(i).getPassword();
									}
									else
									{
										strPassword = String.copyValueOf(password);
									}

									rechten = staffList.get(i).getRights();

									if(staff.getRdbJa().isSelected())
									{
										rechten = 1;
									}
									if(staff.getRdbNee().isSelected())
									{
										rechten = 0;
									}
									
									datum = staffList.get(i).getBirthDate();
								}
								Staff mStaff=new Staff(adres,stat,voornaam,achternaam,username,strPassword,rechten,datum,email);
								mStaff.setAddressID(adres.getAddressID());
								mStaff.setStationID(stat.getStationID());
								staffD.insert(mStaff);
							}
						}

						*/
					}

				});
			}
		});

	}
}
