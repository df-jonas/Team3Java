package controller;

import java.awt.EventQueue;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;
import java.util.UUID;

import gui.Popup;

import model.Station;
import panels.VerlorenVoorwerpZoekPanel;
import dao.LostObjectDAO;
import dao.StationDAO;
import model.LostObject;

public class VerlorenVoorwerpZoekController {
	public static void startListening(VerlorenVoorwerpZoekPanel verlorenVoorwerpZoek) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				verlorenVoorwerpZoek.getBtnToonAlles().addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String station = verlorenVoorwerpZoek.getTxtStation().getSelectedItem().toString();

						ArrayList<LostObject> lostObjList = new ArrayList<LostObject>();
						LostObjectDAO lostObjD = new LostObjectDAO();
						lostObjList = lostObjD.selectAll();

						ArrayList<Station> stationList = new ArrayList<Station>();
						StationDAO stationD = new StationDAO();
						stationList = stationD.selectAll();

						UUID stationID = null;

						for (int i = 0; i<stationList.size();i++)
						{
							if (station.equals(stationList.get(i).getStationName()))
							{
								stationID = stationList.get(i).getStationID();
							}
						}

						ArrayList<String> strVoorwerp = new ArrayList<String>();

						boolean check = false;
						for (int i=0;i<lostObjList.size();i++)
						{
							if(stationID.equals(lostObjList.get(i).getStationID()))
							{
								String gevonden = "";
								if (lostObjList.get(i).getFound() == true)
								{
									gevonden = "Gevonden";
								}
								else
								{
									gevonden = "Niet gevonden";
								}
								
								String temp =  lostObjList.get(i).getTrainID() + "&nbsp;&nbsp;&nbsp;&nbsp;" + lostObjList.get(i).getDescription() + "&nbsp;&nbsp;&nbsp;&nbsp;" + lostObjList.get(i).getDate() + "&nbsp;&nbsp;&nbsp;&nbsp;" + gevonden;
								strVoorwerp.add(temp);
								check = true;
								System.out.println("tetestestest");
							}
						}

						String ss = "";
						for(String temp: strVoorwerp)
						{
							ss += temp + "\n";
						}

						verlorenVoorwerpZoek.getLblResultat().setText("<html>" + ss.replaceAll("<", "&lt;")
						.replaceAll(">", "&gt;").replaceAll("\n", "<br/>").replaceAll("\t", "    ") + "</html>");		

						if (check == false)
						{
							Popup.plainMessage("lostObjToonAllesPopup", "lostObjToonAllesPopupTitel");
						}
					}
				});

				verlorenVoorwerpZoek.getBtnZoek().addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {

						String treinnummer = verlorenVoorwerpZoek.getTxtTreinNummer().getText();
						//String datum = verlorenVoorwerpZoek.getDatePicker().getJFormattedTextField().getText();
						//String omschrijving = verlorenVoorwerpZoek.getTxtOmschrijving().getText();

						if (treinnummer.equals("")) 
						{
							Popup.warningMessage("zoekErrorPopup", "zoekErrorPopupTitel");
						} 
						else 
						{
							String station = verlorenVoorwerpZoek.getTxtStation().getSelectedItem().toString();

							ArrayList<LostObject> lostObjList = new ArrayList<LostObject>();
							LostObjectDAO lostObjD = new LostObjectDAO();
							lostObjList = lostObjD.selectAll();

							ArrayList<Station> stationList = new ArrayList<Station>();
							StationDAO stationD = new StationDAO();
							stationList = stationD.selectAll();

							UUID stationID = null;

							for (int i = 0; i<stationList.size();i++)
							{
								if (station.equals(stationList.get(i).getStationName()))
								{
									stationID = stationList.get(i).getStationID();
								}
							}

							ArrayList<String> strVoorwerp = new ArrayList<String>();

							boolean check = false;
							for (int i=0;i<lostObjList.size();i++)
							{
								if(stationID.equals(lostObjList.get(i).getStationID()) && treinnummer.equals(lostObjList.get(i).getTrainID()))
								{
									String gevonden = "";
									if (lostObjList.get(i).getFound() == true)
									{
										gevonden = "Gevonden";
									}
									else
									{
										gevonden = "Niet gevonden";
									}
									
									String temp =  lostObjList.get(i).getTrainID() +"&nbsp;&nbsp;&nbsp;&nbsp;" +  lostObjList.get(i).getDescription() +"&nbsp;&nbsp;&nbsp;&nbsp;" + lostObjList.get(i).getDate() +"&nbsp;&nbsp;&nbsp;&nbsp;"+ gevonden;
									strVoorwerp.add(temp);
									check = true;
								}
							}

							String ss = "";
							for(String temp: strVoorwerp)
							{
								ss += temp + "\n";
							}

							verlorenVoorwerpZoek.getLblResultat().setText("<html>" + ss.replaceAll("<", "&lt;")
							.replaceAll(">", "&gt;").replaceAll("\n", "<br/>").replaceAll("\t", "    ") + "</html>");		

							if (check == false)
							{
								Popup.plainMessage("lostObjToonAllesPopup", "lostObjToonAllesPopupTitel");
							}
						}
					}
				});
				
				verlorenVoorwerpZoek.getBtnGevonden().addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String station = verlorenVoorwerpZoek.getTxtStation().getSelectedItem().toString();
						String treinnummer = verlorenVoorwerpZoek.getTxtTreinNummer().getText();
						String omschrijving = verlorenVoorwerpZoek.getTxtOmschrijving().getText();
						
						if (treinnummer.equals("") || omschrijving.equals("")) 
						{
							Popup.warningMessage("WarningPopup", "WarningPopupTitel");
						} 
						else 
						{
							ArrayList<Station> stationList = new ArrayList<Station>();
							StationDAO stationD = new StationDAO();
							stationList = stationD.selectAll();

							UUID stationID = null;

							for (int i = 0; i<stationList.size();i++)
							{
								if (station.equals(stationList.get(i).getStationName()))
								{
									stationID = stationList.get(i).getStationID();
								}
							}
							
							ArrayList<LostObject> lostObjList = new ArrayList<LostObject>();
							LostObjectDAO lostObjD = new LostObjectDAO();
							lostObjList = lostObjD.selectAll();
							LostObject lostObject = new LostObject();
							
							boolean check = false;
							
							for (int i = 0 ; i<lostObjList.size();i++)
							{
								if (stationID.equals(lostObjList.get(i).getStationID()) || treinnummer.equals(lostObjList.get(i).getTrainID()) || omschrijving.equals(lostObjList.get(i).getDescription()))
								{
									lostObject = lostObjList.get(i);
									lostObject.setFound(true);
									check = true;
								}
							}
							
							if (check == true)
							{
								try{
									lostObjD.update(lostObject);
								} catch(NullPointerException e1) {
									Popup.errorMessage("voorwerpWarningPopup", "voorwerpWarningPopupTitel");
								}
								Popup.plainMessage("zoekGevondenPopup", "zoekGevondenPopupTitel" );
							}
						}
					}
				});
				
			}
		});
	}
}

