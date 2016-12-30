package controller;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

import controller.APIController.APIUrl;
import controller.APIController.RequestType;
import dao.StaffDAO;
import gui.LangageHandler;
import model.SettingsSingleton;
import model.Staff;
import panels.LoginPanel;
import services.APIRequest;
import services.APIThread;
import services.JBcryptVerifier;
import services.ThreadListener;

public class LoginController{

	private static SettingsSingleton settings;

	public static void login(LoginPanel l) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				l.getBtnLogin().addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						settings = SettingsSingleton.getSettings();
						verify(l);

					}
				});
				
				l.getCmbLangage().addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e){
						LangageHandler.setTaal(l.getCmbLangage().getSelectedItem().toString());
						GUIController.reloadLogin();
					}
				});
			}
		});
	}

	private static void verify(LoginPanel l) {
		String usrn = l.getTxtUsername().getText().replaceAll("&", "%26");
		String password = l.getTxtPassword().getText().replaceAll("&", "%26");

		
		
			
		StaffDAO handler = new StaffDAO();
		Staff s = null;
		
		if (settings.getFirstTime())
			s = handler.selectOneOnUsername(usrn);
		
		
		if (s == null)
		{
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("username", usrn);
			params.put("password", password);

			UUID requestID = UUID.randomUUID();
			APIRequest request = new APIRequest(requestID, APIUrl.G3, "staff/login", RequestType.POST, params);
			ThreadListener listener = new ThreadListener() {
				
				@Override
				public void setResult(JSONArray data)
				{
					JSONObject obj = data.getJSONObject(0);
					
					if (obj.getInt("StatusCode") != 200)
					{
						connectionError(l);
					}
					else
					{	
						Staff s = new Staff();
						s.setStaffID(UUID.fromString(obj.getString("StaffID")));
						s.setRights(obj.getInt("Rights"));
						s.setApiToken(obj.getString("Api_token"));
						
						connection(l, s);
					}
				}
				
			};
			
			APIThread apiThread = APIThread.getThread();
			apiThread.addListener(requestID, listener);
			apiThread.addAPIRequest(request);
		}
		else
		{
			if (JBcryptVerifier.checkPassword(password, s.getPassword()))
			{
				connection(l, s);
			}
			else
			{
				LangageHandler.chooseLangageLbl(l.getLblResult(), "foutPass");
				//l.getLblResult().setText("Fout password, probeer opnieuw!");
				l.getTxtPassword().setText("");
			}
				
		} 
	}

	private static void connection(LoginPanel l, Staff s)
	{
		boolean chAdmin = l.getChAdmin().isSelected();
		
		if(chAdmin)
		{
			if (s.getRights() == 1)
				connectionSucceed(l, s, 1);
			else
				LangageHandler.chooseLangageLbl(l.getLblResult(), "geenAdmin");
				//l.getLblResult().setText("Geen admin rechten, probeer opnieuw!");
			
		} else {
			connectionSucceed(l, s, 0);
		}
	}
	
	private static void connectionSucceed(LoginPanel l, Staff s, int rights)
	{
		settings.setRights(rights);
		settings.setApiToken(s.getApiToken());
		settings.setStaffID(s.getStaffID());
		
		if (!settings.getFirstTime())
			SyncController.initDatabase();
			
		l.getLblResult().setText("");
		GUIController.getFrame().getContentPane().removeAll();
		GUIController.showApp();
	}
	
	private static void connectionError(LoginPanel l)
	{
		LangageHandler.chooseLangageLbl(l.getLblResult(), "userOngeldig");
		//l.getLblResult().setText("User ongeldig, probeer opnieuw!");
		l.getTxtUsername().setText("");
		l.getTxtPassword().setText("");
	}
}