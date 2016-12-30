package controller;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.HashMap;
import java.util.UUID;

import org.apache.commons.codec.language.bm.Lang;
import org.json.JSONArray;
import org.json.JSONObject;

import api.RouteberekeningAPI;
import api.TimeSelector;
import controller.APIController.APIUrl;
import controller.APIController.RequestType;
import gui.GUIDateFormat;
import gui.LangageHandler;
import model.api.RouteBerekening;
import panels.RouteberekeningPanel;
import services.APIRequest;
import services.APIThread;
import services.ThreadListener;

public class RouteberekeningController {
	public static void startListening(RouteberekeningPanel route) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				route.getBtnZoek().addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {

						String van = (String) route.getTxtVan().getSelectedItem();
						String naar = (String) route.getTxtNaar().getSelectedItem();
						String datum = route.getDatePicker().getJFormattedTextField().getText();
						String tijd = route.getTimePicker().getText();
						String lang;

						if (!van.equals("") && !naar.equals("") && DateTimeConverter.checkTime(tijd)
								&& DateTimeConverter.checkDate(datum)) {

							TimeSelector timeSel;

							if (route.getGrpTimeSel().getSelection().getMnemonic() == 1) {
								timeSel = TimeSelector.VERTREK;
							} else {
								timeSel = TimeSelector.AANKOMST;
							}
							
							RouteberekeningAPI r = new RouteberekeningAPI(van, naar, datum, tijd, timeSel);
							String ss = r.toString();
							
							if (!ss.contains("null")) {
								route.getLblResult().setText(r.toStringHTML());
							} else
								LangageHandler.chooseLangageLbl(route.getLblResult(), "verzoek");

						} else {
							LangageHandler.chooseLangageLbl(route.getLblResult(), "foutRes");
						}

					}
				});
			}
		});
	}
}
