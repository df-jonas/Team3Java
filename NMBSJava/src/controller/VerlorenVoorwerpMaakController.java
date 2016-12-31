package controller;

import java.awt.EventQueue;



import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;
import java.util.UUID;



import gui.Popup;

import model.Station;
import panels.VerlorenVoorwerpMaakPanel;
import dao.LostObjectDAO;
import dao.StationDAO;
import model.LostObject;

public class VerlorenVoorwerpMaakController {
	public static void startListening(VerlorenVoorwerpMaakPanel verlorenVoorwerpMaak) {		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				verlorenVoorwerpMaak.getBtnMaak().addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String station = verlorenVoorwerpMaak.getTxtStation().getSelectedItem().toString();
						String treinnummer = verlorenVoorwerpMaak.getTxtTrein().getText();
						String omschrijving = verlorenVoorwerpMaak.getTxtOmschrijving().getText();
						String datum = verlorenVoorwerpMaak.getDatePicker().getJFormattedTextField().getText();

						UUID stationID = null;
						StationDAO s = new StationDAO();
						ArrayList<Station> statList = new ArrayList<Station>();
						statList = s.selectAll();

						boolean check = true;

						if (treinnummer.equals("") || omschrijving.equals(""))
						{
							Popup.warningMessage("WarningPopup", "WarningPopupTitel");
						}
						else 
						{
							int treinnum = 0;
							try {
								treinnum=Integer.parseInt(verlorenVoorwerpMaak.getTxtTrein().getText());
							}catch (NumberFormatException e1){
								Popup.warningMessage("treinnummerWarningPopup", "WarningPopupTitel");
								check = false;
							} 

							if(check == true)
							{
								for (int i = 0; i <statList.size() ;i++)
								{
									if(station.equals(statList.get(i).getStationName()))
									{
										stationID = statList.get(i).getStationID();
									}
								}

								LostObject lostObject = new LostObject(stationID, omschrijving, datum, treinnummer,false);
								LostObjectDAO lostD = new LostObjectDAO();

								try {
									lostD.insert(lostObject);
								}catch (NumberFormatException e1){
									Popup.errorMessage("voorwerpWarningPopup", "voorwerpWarningPopupTitel");
									check = false;
								} 
							}

							if(check == true)
							{
								Popup.plainMessage("voorwerpPlainPopup", "voorwerpPlainPopupTitel");
							}
						}
					}
				});
			}
		});
	}
}
