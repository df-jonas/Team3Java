package controller;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.UUID;

import org.json.JSONArray;

import controller.APIController.APIUrl;
import controller.APIController.RequestType;
import gui.LangageHandler;
import model.api.Train;
import panels.TreinopzoekingPanel;
import services.APIRequest;
import services.APIThread;
import services.ThreadListener;;

public class TreinopzoekingController {

	public static void startListening(TreinopzoekingPanel trein) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				trein.getBtnZoek().addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {

						String tId = trein.getTxtTrein().getText();

						if (!tId.equals("")) {

							UUID requestID = UUID.randomUUID();
							HashMap<String, String> params = new HashMap<String, String>();
							APIRequest request = new APIRequest(requestID, APIUrl.TRAINTRACKS, "train/" + tId, RequestType.GET, params);
							ThreadListener listener = new ThreadListener() {

								@Override
								public void setResult(JSONArray data) {
									if (data != null) {
										Train train = new Train(data.getJSONObject(0));

										String ss = train.toString();

										if (!ss.contains("null"))
											trein.getLblResult()
													.setText("<html>" + ss.replaceAll("<", "&lt;")
															.replaceAll(">", "&gt;").replaceAll("\n", "<br/>")
															+ "</html>");

									} else {
										LangageHandler.chooseLangageLbl(trein.getLblResult(), "verzoek");
									}
								}

							};

							APIThread apiThread = APIThread.getThread();
							apiThread.addListener(requestID, listener);
							apiThread.addAPIRequest(request);

						} else {
							LangageHandler.chooseLangageLbl(trein.getLblResult(), "foutRes");

						}

					}
				});
			}
		});
	}
}