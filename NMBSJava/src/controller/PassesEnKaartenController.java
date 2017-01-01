package controller;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

import dao.PassDAO;
import model.Pass;
import panels.PassesEnKaartenPanel;

public class PassesEnKaartenController {
	public static void startListening(PassesEnKaartenPanel passes){
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					passes.getBtnPrint().addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							String startDatum = passes.getDteStartDatum().getJFormattedTextField().getText();
							UUID typePass = passes.getCbxPassType().getSelectedPassType();
							int mnem = passes.getGrpKlasses().getSelection().getMnemonic();

							DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
							Calendar cal = Calendar.getInstance();
							String date = dateFormat.format(cal);
							
							Pass pass = new Pass(typePass, date, startDatum, mnem);
							PassDAO passHandler = new PassDAO();
							passHandler.insert(pass);
						
						}
					});
				}
				catch(Exception pe){
					pe.printStackTrace();
				}
			}
		});		
	}
}
