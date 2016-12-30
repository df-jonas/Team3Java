package controller;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.HashMap;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

import controller.APIController.APIUrl;
import controller.APIController.RequestType;
import gui.GUIDateFormat;
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
						String timeSel;
						
						if (route.getGrpTimeSel().getSelection().getMnemonic() == 1) {
							timeSel = "depart";
						} else {
							timeSel = "arrive";
						}
						
						if (!van.equals("") && !naar.equals("") && DateTimeConverter.checkTime(tijd) && DateTimeConverter.checkDate(datum)) {
							
							try {

								HashMap<String, String> params = new HashMap<String, String>();
								params.put("to", naar);
								params.put("from", van);
								params.put("date", GUIDateFormat.getRawDate(datum));
								params.put("time", GUIDateFormat.getRawTime(tijd));
								params.put("timeSel", timeSel);
								params.put("format", "json");
								params.put("lang", "NL");
	
	
								UUID requestID = UUID.randomUUID();
								APIRequest request = new APIRequest(requestID, APIUrl.IRAILS, "", RequestType.GET, params);
								ThreadListener listener = new ThreadListener() {
	
									@Override
									public void setResult(JSONArray data)
									{
										if (data != null)
										{
											JSONObject json = data.getJSONObject(0);
											
											RouteBerekening rb = new RouteBerekening(json);
											
											String ss = rb.toString();
											
											if (!ss.contains("null"))
												route.getLblResult().setText(rb.toStringHTML());
											else
												route.getLblResult().setText("Dit verzoek kon niet verwerkt worden.");
									}
								}
								
								};
								
								APIThread apiThread = APIThread.getThread();
								apiThread.addListener(requestID, listener);
								apiThread.addAPIRequest(request);
								
							}
							catch (ParseException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							
							
							
							
						} else {
							route.getLblResult().setText("Formulier werd niet correct ingevuld.");
						}

					}
				});
			}
		});
	}
}
