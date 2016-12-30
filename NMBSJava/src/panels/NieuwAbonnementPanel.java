package panels;

import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.UUID;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import dao.DiscountDAO;
import gui.GUIDateFormat;
import gui.StationsAutoCompletor;
import model.Discount;

public class NieuwAbonnementPanel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4090414931591291748L;

	private HashMap<String, UUID> discountMap;
	
	private JPanel pnlKlasse;
	private JPanel pnlCustomerID;
	private JPanel pnlBedrag;
	private JPanel pnlStraat;
	private JPanel pnlNummer;
	private JLabel lblTitle;
	private JLabel lblNaam;
	private JLabel lblVoornaam;
	private JLabel lblGeboortedatum;
	private JLabel lblEmail;
	private JLabel lblGemeente;
	private JLabel lblPostcode;
	private JLabel lblStraatEnNummer;
	private JLabel lblStartDatum;
	private JLabel lblKlasse;
	private JLabel lblTreinkaart;
	private JLabel lblDiscount;
	private JLabel lblDuur;
	private JLabel lblBerekendeVervaldatum;
	private JLabel lblVervaldatum;
	private JLabel lblBedrag;
	private JLabel lblEuro;
	private JLabel lblStation1;
	private JLabel lblStation2;
	private JLabel lblFoutmelding;
	private JLabel lblCustomerID;
	
	private JButton btnVerzenden;
	private JButton btnValideer;
	private JButton btnCustomerID;

	private JTextField txtNaam;
	private JTextField txtVoornaam;
	private JTextField txtEmail;
	private JTextField txtTelefoonnr;
	private JTextField txtGemeente;
	private JTextField txtPostcode;
	private JTextField txtStraat;
	private JTextField txtNummer;
	private JTextField txtCustomerID;
	
	private StationsAutoCompletor txtStation1;
	private StationsAutoCompletor txtStation2;

	private JRadioButton rdbEersteKlasse;
	private JRadioButton rdbTweedeKlasse;
	private ButtonGroup grpKlasses;
	
	private JDatePickerImpl dteGeboorteDatum;
	private JDatePickerImpl dteStartDatum;

	private JComboBox<String> cbxTreinkaart;
	private JComboBox<String> cbxDuur;
	private JComboBox<String> cbxDiscount;
	
	//@SuppressWarnings({ "rawtypes", "unchecked" })
	public NieuwAbonnementPanel() {

		discountMap = new HashMap<String, UUID>();
		ArrayList<Discount> discountList = new DiscountDAO().selectAll();
		
		for (Discount discount : discountList)
		{
			discountMap.put(discount.getName(), discount.getDiscountID());
		}
		
		lblTitle = new JLabel("Nieuw Abonnement");
		lblTitle.setFont(new Font("Tahoma", Font.PLAIN, 18));

		lblNaam = new JLabel("Naam: ");
		txtNaam = new JTextField();

		lblVoornaam = new JLabel("Voornaam: ");
		txtVoornaam = new JTextField();
		lblGeboortedatum = new JLabel("Geboortedatum: ");
		lblEmail = new JLabel("Email: ");
		txtEmail = new JTextField();
		lblGemeente = new JLabel("Gemeente: ");
		txtGemeente = new JTextField();
		lblPostcode = new JLabel("Postcode: ");
		txtPostcode = new JTextField();
		lblStraatEnNummer = new JLabel("Straat + nr: ");
		txtStraat = new JTextField();
		txtNummer = new JTextField();
		lblStartDatum = new JLabel("Startdatum: ");
		lblKlasse = new JLabel("Klasse: ");
		rdbEersteKlasse = new JRadioButton("1e Klas");
		rdbTweedeKlasse = new JRadioButton("2e Klas");
		rdbTweedeKlasse.setSelected(true);
		lblTreinkaart = new JLabel("Type treinkaart: ");
		lblDiscount = new JLabel("Korting: ");
		lblDuur = new JLabel("Duur: ");
		lblVervaldatum = new JLabel("Vervaldatum: ");
		lblBerekendeVervaldatum = new JLabel("'Vervaldatum'");
		lblCustomerID = new JLabel("Klant ID (Als klant al bestaat)");
		txtCustomerID = new JTextField();
		
		lblStation1 = new JLabel("Station 1: ");
		lblStation2 = new JLabel("Station 2: ");
		txtStation1 = new StationsAutoCompletor();
		txtStation2 = new StationsAutoCompletor();
		btnVerzenden = new JButton("Verzenden");
		lblFoutmelding = new JLabel("");
		
		grpKlasses = new ButtonGroup();
		grpKlasses.add(rdbEersteKlasse);
		grpKlasses.add(rdbTweedeKlasse);
		
		btnValideer = new JButton("Valideer");
		lblBedrag = new JLabel("0");
		lblEuro = new JLabel("euro");
		
		btnCustomerID = new JButton("Zoek");
	

		Properties properties = new Properties();
		properties.put("text.today", "Today");
		properties.put("text.month", "Month");
		properties.put("text.year", "Year");
		JDatePanelImpl datePanel1 = new JDatePanelImpl(new UtilDateModel(), properties);
		dteGeboorteDatum = new JDatePickerImpl(datePanel1, new GUIDateFormat());
		dteGeboorteDatum.getJFormattedTextField().setText(GUIDateFormat.getDate());
		JDatePanelImpl datePanel2 = new JDatePanelImpl(new UtilDateModel(), properties);
		dteStartDatum = new JDatePickerImpl(datePanel2, new GUIDateFormat());
		dteStartDatum.getJFormattedTextField().setText(GUIDateFormat.getDate());


		String[] soortKaart = { "Trajecttreinkaart", "Halftijdstreinkaart", "Nettreinkaart", "Schooltreinkaart" };
		cbxTreinkaart = new JComboBox<String>(soortKaart);
		
		String[] aantalMaanden = { "1 maand", "3 maanden", "12 maanden"};
		cbxDuur = new JComboBox<String>(aantalMaanden);
		
		String[] soortDiscount = discountMap.keySet().toArray(new String[discountMap.size()]);
		cbxDiscount = new JComboBox<String>(soortDiscount);
		
		fullpanel();
		
	}

	private void fullpanel(){
		this.setLayout(new GridLayout(19, 2));
		this.add(lblTitle);
		this.add(new JLabel());
		
		panelCustomer();
		this.add(lblCustomerID);
		this.add(pnlCustomerID);
		
		this.add(lblNaam);
		this.add(txtNaam);
		
		this.add(lblVoornaam);
		this.add(txtVoornaam);
		
		this.add(lblGeboortedatum);
		this.add(dteGeboorteDatum);
		
		this.add(lblEmail);
		this.add(txtEmail);
		
		panelStraat();
		this.add(lblStraatEnNummer);
		this.add(pnlStraat);
		
		this.add(lblPostcode);
		this.add(txtPostcode);
		
		this.add(lblGemeente);
		this.add(txtGemeente);
		
		this.add(lblTreinkaart);
		this.add(cbxTreinkaart);
		
		panelKlasse();
		this.add(lblKlasse);
		this.add(pnlKlasse);
	
		this.add(lblDiscount);
		this.add(cbxDiscount);
		
		this.add(lblStation1);
		this.add(txtStation1);
		
		this.add(lblStation2);
		this.add(txtStation2);
		
		this.add(lblStartDatum);
		this.add(dteStartDatum);
		
		this.add(lblDuur);
		this.add(cbxDuur);
		
		this.add(lblVervaldatum);
		this.add(lblBerekendeVervaldatum);
		
		panelBedrag();
		this.add(btnValideer);
		this.add(pnlBedrag);
		
		
		this.add(btnVerzenden);
		this.add(lblFoutmelding);
		
		
	}
	
	private void panelCustomer(){
		pnlCustomerID = new JPanel();
		pnlCustomerID.setLayout(new GridLayout(1,3));
		pnlCustomerID.add(txtCustomerID);
		pnlCustomerID.add(btnCustomerID);
	}
	
	private void panelKlasse(){
		pnlKlasse = new JPanel();
		pnlKlasse.setLayout(new GridLayout(1, 2));
		pnlKlasse.add(rdbEersteKlasse);
		pnlKlasse.add(rdbTweedeKlasse);
	}
	
	private void panelBedrag(){
		pnlBedrag = new JPanel();
		pnlBedrag.setLayout(new GridLayout(1, 2));
		pnlBedrag.add(lblBedrag);
		pnlBedrag.add(lblEuro);
	}
	
	private void panelStraat(){
		pnlStraat = new JPanel();
		pnlStraat.setLayout(new GridLayout(1, 2,10,5));
		pnlStraat.add(txtStraat);
		panelNummer();
		pnlStraat.add(pnlNummer);
	}
	
	private void panelNummer(){
		pnlNummer = new JPanel();
		pnlNummer.setLayout(new GridLayout(1, 2,40,10));
		pnlNummer.add(txtNummer);
		pnlNummer.add(new JLabel());

	}

	public JLabel getLblTitle() {
		return lblTitle;
	}

	public JLabel getLblNaam() {
		return lblNaam;
	}

	public JLabel getLblVoornaam() {
		return lblVoornaam;
	}

	public JLabel getLblGeboortedatum() {
		return lblGeboortedatum;
	}

	public JLabel getLblEmail() {
		return lblEmail;
	}

	public JLabel getLblGemeente() {
		return lblGemeente;
	}

	public JLabel getLblPostcode() {
		return lblPostcode;
	}

	public JLabel getLblStraatEnNummer() {
		return lblStraatEnNummer;
	}

	public JLabel getLblStartDatum() {
		return lblStartDatum;
	}

	public JLabel getLblKlasse() {
		return lblKlasse;
	}

	public JLabel getLblTreinkaart() {
		return lblTreinkaart;
	}

	public JLabel getLblDiscount() {
		return lblDiscount;
	}

	public JLabel getLblDuur() {
		return lblDuur;
	}

	public JLabel getLblBerekendeVervaldatum() {
		return lblBerekendeVervaldatum;
	}

	public JLabel getLblVervaldatum() {
		return lblVervaldatum;
	}

	public JLabel getLblPrint() {
		return lblBedrag;
	}

	public JLabel getLblStation1() {
		return lblStation1;
	}

	public JLabel getLblStation2() {
		return lblStation2;
	}

	public JLabel getLblFoutmelding() {
		return lblFoutmelding;
	}

	public JButton getBtnVerzenden() {
		return btnVerzenden;
	}

	public JButton getBtnValideer() {
		return btnValideer;
	}

	public JTextField getTxtNaam() {
		return txtNaam;
	}

	public JTextField getTxtVoornaam() {
		return txtVoornaam;
	}

	public JTextField getTxtEmail() {
		return txtEmail;
	}

	public JTextField getTxtTelefoonnr() {
		return txtTelefoonnr;
	}

	public JTextField getTxtGemeente() {
		return txtGemeente;
	}

	public JTextField getTxtPostcode() {
		return txtPostcode;
	}

	public JTextField getTxtStraat() {
		return txtStraat;
	}
	
	public JTextField getTxtNummer() {
		return txtNummer;
	}

	public StationsAutoCompletor getTxtStation1() {
		return txtStation1;
	}

	public StationsAutoCompletor getTxtStation2() {
		return txtStation2;
	}

	public JRadioButton getRdbEersteKlasse() {
		return rdbEersteKlasse;
	}

	public JRadioButton getRdbTweedeKlasse() {
		return rdbTweedeKlasse;
	}

	public ButtonGroup getGrpKlasses() {
		return grpKlasses;
	}

	public JDatePickerImpl getDteGeboorteDatum() {
		return dteGeboorteDatum;
	}

	public JDatePickerImpl getDteStartDatum() {
		return dteStartDatum;
	}

	public JComboBox<String> getCbxTreinkaart() {
		return cbxTreinkaart;
	}

	public JComboBox<String> getCbxDuur() {
		return cbxDuur;
	}

	public JComboBox<String> getCbxDiscount() {
		return cbxDiscount;
	}
	
	public JLabel getLblCustomerID() {
		return lblCustomerID;
	}
	
	public JTextField getTxtCustomerID() {
		return txtCustomerID;
	}
	
	public JButton getBtnCustomerID() {
		return btnCustomerID;
	}

	//setters

	public void setTxtNaam(String txtNaam) {
		this.txtNaam.setText(txtNaam);
	}

	public void setTxtVoornaam(String txtVoornaam) {
		this.txtVoornaam.setText(txtVoornaam); 
	}

	public void setTxtEmail(String txtEmail) {
		this.txtEmail.setText(txtEmail); 
	}

	public void setTxtGemeente(String txtGemeente) {
		this.txtGemeente.setText(txtGemeente);
	}

	public void setTxtPostcode(String txtPostcode) {
		this.txtPostcode.setText(txtPostcode);
	}

	public void setTxtStraat(String txtStraat) {
		this.txtStraat.setText(txtStraat);
	}

	public void setTxtNummer(String txtNummer) {
		this.txtNummer.setText(txtNummer);
	}
	
	public void setDteGeboorteDatum(String dteGeboorteDatum) 
	{
		this.dteGeboorteDatum.getJFormattedTextField().setText(dteGeboorteDatum);
	}
	
	public HashMap<String, UUID> getDiscounts()
	{
		return this.discountMap;
	}
	

}