package panels;

import java.awt.GridLayout;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import gui.GUIDateFormat;
import gui.LangageHandler;
import gui.StationsAutoCompletor;

@SuppressWarnings("serial")
public class StaffBeheerPanel  extends JPanel{


	private JLabel lblTitel;
	private JLabel lblVoornaam;
	private JLabel lblAchternaam;
	private JLabel lblDatum;
	private JLabel lblEmail;
	private JLabel lblStraat;
	private JLabel lblNummer;
	private JLabel lblGemeente;
	private JLabel lblPostcode;
	private JLabel lblStation;
	private JLabel lblUsername;
	private JLabel lblPassword;
	private JLabel lblAdmin;

	private JTextField txtVoornaam;
	private JTextField txtAchternaam;
	private JTextField txtEmail;
	private JTextField txtStraat;
	private JTextField txtNummer;
	private JTextField txtGemeente;
	private JTextField txtPostcode;
	private JTextField txtUsername;
	private JPasswordField txtPassword;

	private JButton btnVoegToe;
	private JButton btnWijzig;

	private StationsAutoCompletor txtStation;

	private JDatePickerImpl datePicker;

	private JRadioButton rdbJa;
	private JRadioButton rdbNee;

	public StaffBeheerPanel() {
		setLayout(new GridLayout(1, 1, 5, 5));

		JPanel content = new JPanel();
		content.setLayout(new GridLayout(15,2,5,5));

		////1
		lblTitel = new JLabel();
		LangageHandler.chooseLangageLbl(lblTitel, "titelStaff");
		content.add(lblTitel);
		content.add(new JLabel());

		/////2
		lblVoornaam = new JLabel();
		LangageHandler.chooseLangageLbl(lblVoornaam, "naam");
		txtVoornaam = new JTextField();
		txtVoornaam.setColumns(1);
		content.add(lblVoornaam);
		content.add(txtVoornaam);

		/////3
		lblAchternaam = new JLabel();
		LangageHandler.chooseLangageLbl(lblAchternaam, "voornaam");
		txtAchternaam = new JTextField();
		txtAchternaam.setColumns(1);
		content.add(lblAchternaam);
		content.add(txtAchternaam);

		/////4
		lblDatum = new JLabel();
		LangageHandler.chooseLangageLbl(lblDatum, "geboortedatum");
		content.add(lblDatum);

		Properties properties = new Properties();
		properties.put("text.today", "Today");
		properties.put("text.month", "Month");
		properties.put("text.year", "Year");

		JDatePanelImpl datePanel = new JDatePanelImpl(new UtilDateModel(), properties);
		datePicker = new JDatePickerImpl(datePanel, new GUIDateFormat());
		datePicker.getJFormattedTextField().setText(GUIDateFormat.getDate());
		content.add(datePicker);
		
		///////////////
		lblEmail = new JLabel();
		LangageHandler.chooseLangageLbl(lblEmail, "email");
		txtEmail = new JTextField();
		txtEmail.setColumns(1);
		content.add(lblEmail);
		content.add(txtEmail);

		/////5
		lblStraat = new JLabel();
		LangageHandler.chooseLangageLbl(lblStraat, "straat");
		txtStraat = new JTextField();
		txtStraat.setColumns(1);
		content.add(lblStraat);
		content.add(txtStraat);

		//////6
		lblNummer = new JLabel();
		LangageHandler.chooseLangageLbl(lblNummer, "nummer");
		txtNummer = new JTextField();
		txtNummer.setColumns(1);
		content.add(lblNummer);
		content.add(txtNummer);

		//////7
		lblGemeente = new JLabel();
		LangageHandler.chooseLangageLbl(lblGemeente, "gemeente");
		txtGemeente = new JTextField();
		txtGemeente.setColumns(1);	
		content.add(lblGemeente);
		content.add(txtGemeente);

		///////8
		lblPostcode = new JLabel();
		LangageHandler.chooseLangageLbl(lblPostcode, "postcode");
		txtPostcode = new JTextField();
		txtPostcode.setColumns(1);
		content.add(lblPostcode);
		content.add(txtPostcode);

		///////9
		lblStation = new JLabel();
		LangageHandler.chooseLangageLbl(lblStation, "station");
		txtStation = new StationsAutoCompletor();
		content.add(lblStation);
		content.add(txtStation);

		////////10
		lblUsername = new JLabel();
		LangageHandler.chooseLangageLbl(lblUsername, "username");
		txtUsername = new JTextField();
		txtUsername.setColumns(1);
		content.add(lblUsername);
		content.add(txtUsername);

		/////////11
		lblPassword = new JLabel();
		LangageHandler.chooseLangageLbl(lblPassword, "password");
		txtPassword = new JPasswordField();
		txtPassword.setColumns(1);
		content.add(lblPassword);
		content.add(txtPassword);

		/////////12
		lblAdmin = new JLabel();
		LangageHandler.chooseLangageLbl(lblAdmin,"rechten");
		content.add(lblAdmin);

		rdbJa = new JRadioButton();
		LangageHandler.chooseLangageRdb(rdbJa, "ja");
		content.add(rdbJa);
		rdbNee=new JRadioButton();
		LangageHandler.chooseLangageRdb(rdbNee, "nee");
		content.add(new JLabel());
		content.add(rdbNee);

		/////////13
		btnWijzig = new JButton();
		LangageHandler.chooseLangageBtn(btnWijzig, "wijzigStaff");
		
		btnVoegToe = new JButton();
		LangageHandler.chooseLangageBtn(btnVoegToe, "voegStaff");
		
		content.add(btnWijzig);
		content.add(btnVoegToe);



		this.add(content);


	}

	public JLabel getLblTitel() {
		return lblTitel;
	}

	public JLabel getLblGemeente() {
		return lblGemeente;
	}

	public JLabel getLblVoornaam() {
		return lblVoornaam;
	}

	public JLabel getLblAchternaam() {
		return lblAchternaam;
	}

	public JButton getBtnVoegToe() {
		return btnVoegToe;
	}

	public JLabel getLblStraat() {
		return lblStraat;
	}

	public JLabel getLblNummer() {
		return lblNummer;
	}


	public JLabel getLblPostcode() {
		return lblPostcode;
	}

	public JLabel getLblStation() {
		return lblStation;
	}

	public JLabel getLblUsername() {
		return lblUsername;
	}

	public JLabel getLblPassword() {
		return lblPassword;
	}

	public JTextField getTxtVoornaam() {
		return txtVoornaam;
	}

	public JTextField getTxtAchternaam() {
		return txtAchternaam;
	}

	public JTextField getTxtStraat() {
		return txtStraat;
	}

	public JTextField getTxtNummer() {
		return txtNummer;
	}

	public JTextField getTxtGemeente() {
		return txtGemeente;
	}

	public JTextField getTxtPostcode() {
		return txtPostcode;
	}

	public JTextField getTxtUsername() {
		return txtUsername;
	}

	public JPasswordField getTxtPassword() {
		return txtPassword;
	}

	public StationsAutoCompletor getTxtStation() {
		return txtStation;
	}

	public JLabel getLblDatum() {
		return lblDatum;
	}

	public JLabel getLblAdmin() {
		return lblAdmin;
	}

	public JDatePickerImpl getDatePicker() {
		return datePicker;
	}

	public JRadioButton getRdbJa() {
		return rdbJa;
	}

	public JRadioButton getRdbNee() {
		return rdbNee;
	}

	public JLabel getLblEmail() {
		return lblEmail;
	}

	public JTextField getTxtEmail() {
		return txtEmail;
	}

	public JButton getBtnWijzig() {
		return btnWijzig;
	}

	
	

}
