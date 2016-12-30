package panels;

import java.awt.Font;
import java.awt.GridLayout;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import gui.GUIDateFormat;
import gui.LangageHandler;
import gui.PassTypesAutoCompletor;

@SuppressWarnings("serial")
public class PassPanel extends JPanel {

	private JPanel buy;
	private JPanel reserved;
	private JLabel lblTitel;
	private JLabel lblKeuze;
	private JLabel lblDate;
	private JDatePickerImpl dteDate;
	private PassTypesAutoCompletor autPass;
	private JButton btnVerkoop;

	public PassPanel() {
		initializeComponents();
		finalizePanel();
		this.add(buy);
		this.add(reserved);
	}

	private void initializeComponents() {
		this.setLayout(new GridLayout(2, 1, 5, 5));
		
		lblTitel = new JLabel();
		LangageHandler.chooseLangageLbl(lblTitel, "verkoopPass");
		lblTitel.setFont(new Font("Tahoma", Font.PLAIN, 18));

		lblKeuze = new JLabel();
		LangageHandler.chooseLangageLbl(lblKeuze, "keuze");

		autPass = new PassTypesAutoCompletor();
		
		Properties properties = new Properties();
		properties.put("text.today", "Today");
		properties.put("text.month", "Month");
		properties.put("text.year", "Year");
		
		lblDate = new JLabel();
		LangageHandler.chooseLangageLbl(lblDate, "datum");
		
		JDatePanelImpl datePane = new JDatePanelImpl(new UtilDateModel(), properties);
		dteDate = new JDatePickerImpl(datePane,new GUIDateFormat());
		dteDate.getJFormattedTextField().setText(GUIDateFormat.getDate());
		
		btnVerkoop = new JButton();
		LangageHandler.chooseLangageBtn(btnVerkoop, "verkoop");

	}

	private void finalizePanel() {
		reserved = new JPanel();
		buy = new JPanel();
		buy.setLayout(new GridLayout(4, 2, 5, 5));
		buy.add(lblTitel);
		buy.add(new JLabel());
		buy.add(lblKeuze);
		buy.add(autPass);
		buy.add(lblDate);
		buy.add(dteDate);
		buy.add(new JLabel());
		buy.add(btnVerkoop);
	}

	public JPanel getBuy() {
		return buy;
	}

	public JPanel getReserved() {
		return reserved;
	}

	public JLabel getLblTitel() {
		return lblTitel;
	}

	public JLabel getLblKeuze() {
		return lblKeuze;
	}

	public PassTypesAutoCompletor getAutPass() {
		return autPass;
	}

	public JButton getBtnVerkoop() {
		return btnVerkoop;
	}

	public JLabel getLblDate() {
		return lblDate;
	}

	public JDatePickerImpl getDteDate() {
		return dteDate;
	}
}
