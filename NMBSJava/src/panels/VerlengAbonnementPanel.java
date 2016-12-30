package panels;

import java.awt.Font;
import java.awt.GridLayout;
import java.util.Properties;

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

import gui.DiscountAutoCompletor;
import gui.GUIDateFormat;
import gui.PassTypesAutoCompletor;
import gui.StationsAutoCompletor;

public class VerlengAbonnementPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 415637730753605814L;
	
	private JPanel pnlZoek;
	private JPanel pnlInfo;
	private JPanel pnlKlasse;
	private JPanel pnlBedrag;
	private JLabel lblTitle;
	private JLabel lblAbonnementsNummer;
	private JLabel lblKlantenNummer;
	private JLabel lblKLantenNummerResult;
	private JLabel lblStartdatum;
	private JLabel lblDuur;
	private JLabel lblDuurResult;
	private JLabel lblVervaldatum;
	private JLabel lblVervaldatumResult;
	private JLabel lblKlasse;
	private JLabel lblDiscount;
	private JLabel lblTreinkaart;
	private JLabel lblStation1;
	private JLabel lblStation2;
	private JLabel lblBedrag;
	private JLabel lblEuro;
	private JLabel lblFoutmelding;

	private JDatePickerImpl dteStartdatum;
	
	private JRadioButton rdbEersteKlasse;
	private JRadioButton rdbTweedeKlasse;
	private ButtonGroup grpKlasses;
	
	private JRadioButton rdbJa;
	private JRadioButton rdbNee;
	private ButtonGroup grpJaNee;
	
	private JButton btnZoek;
	private JButton btnVerzenden;
	private JButton btnMeerInfo;
	private JButton btnValideer;

	private JTextField txtAbonnementsNummer;
	
	private StationsAutoCompletor txtStation1;
	private StationsAutoCompletor txtStation2;
	private DiscountAutoCompletor getCbxDiscount;
	private PassTypesAutoCompletor cbxTypeTicket;
	

	private JComboBox<String> cbxDuur;

	public VerlengAbonnementPanel() {
		
		
		
		lblTitle = new JLabel("Verleng Abonnement");
		lblTitle.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblAbonnementsNummer = new JLabel("Abonnementsnummer: ");
		txtAbonnementsNummer = new JTextField();
		btnZoek = new JButton("Zoek");
		lblKlantenNummer = new JLabel("Klantennumer: ");
		lblKLantenNummerResult = new JLabel(" ");
		btnMeerInfo = new JButton("Meer info");
		lblTreinkaart = new JLabel("Treinkaart: ");
		
		
		
		cbxTypeTicket = new PassTypesAutoCompletor();;
		lblStartdatum = new JLabel("Startdatum: ");
		Properties properties = new Properties();
		properties.put("text.today", "Today");
		properties.put("text.month", "Month");
		properties.put("text.year", "Year");
		JDatePanelImpl datePanel = new JDatePanelImpl(new UtilDateModel(), properties);
		dteStartdatum = new JDatePickerImpl(datePanel, new GUIDateFormat());
		dteStartdatum.getJFormattedTextField().setText(GUIDateFormat.getDate());
		lblDuur = new JLabel("Duur: ");
		String[] aantalMaanden = { "1 maand", "3 maanden", "12 maanden"};
		cbxDuur = new JComboBox<String>(aantalMaanden);
		lblVervaldatum = new JLabel("Vervaldatum: ");
		lblVervaldatumResult = new JLabel(" ");
		lblKlasse = new JLabel("Klasse: ");
		rdbEersteKlasse = new JRadioButton("1e Klas");
		rdbEersteKlasse.setMnemonic(1);
		rdbTweedeKlasse = new JRadioButton("2e Klas");
		rdbTweedeKlasse.setMnemonic(2);
		rdbTweedeKlasse.setSelected(true);
		lblDiscount=new JLabel("Korting: ");
		getCbxDiscount = new DiscountAutoCompletor();
		lblStation1 = new JLabel("Station 1: ");
		txtStation1 = new StationsAutoCompletor();
		lblStation2 = new JLabel("Station 2: ");
		txtStation2 = new StationsAutoCompletor();
		btnVerzenden = new JButton("Verzenden");
		lblBedrag = new JLabel("0");		
		lblEuro = new JLabel("euro");
		btnValideer = new JButton("Valideer");
		lblFoutmelding = new JLabel("");
		
		grpKlasses = new ButtonGroup();
		grpKlasses.add(rdbEersteKlasse);
		grpKlasses.add(rdbTweedeKlasse);
		
		fullpanel();
		
		
	}
	
	public void fullpanel(){
		this.setLayout(new GridLayout(13, 2));
		this.add(lblTitle);
		this.add(new JLabel());

		this.add(lblAbonnementsNummer);
		zoekPanel();
		this.add(pnlZoek);
		
		this.add(lblKlantenNummer);
		infoPanel();
		this.add(pnlInfo);
		
		this.add(lblTreinkaart);
		this.add(cbxTypeTicket);
		
		this.add(lblKlasse);
		klassePanel();
		this.add(pnlKlasse);
		
		this.add(lblStartdatum);
		this.add(dteStartdatum);
		this.add(lblDuur);
		this.add(cbxDuur);
		this.add(lblVervaldatum);
		this.add(lblVervaldatumResult);
		this.add(lblDiscount);
		this.add(getCbxDiscount);
		this.add(lblStation1);
		this.add(txtStation1);
		this.add(lblStation2);
		this.add(txtStation2);
		this.add(btnValideer);
		bedragPanel();
		this.add(pnlBedrag);
		
		this.add(btnVerzenden);
		this.add(lblFoutmelding);
	}
	
	public void zoekPanel(){
		pnlZoek = new JPanel();
		pnlZoek.setLayout(new GridLayout(1, 2));
		
		pnlZoek.add(txtAbonnementsNummer);
		pnlZoek.add(btnZoek);
	}
	
	public void infoPanel(){
		pnlInfo = new JPanel();
		pnlInfo.setLayout(new GridLayout(1, 2));
		
		pnlInfo.add(lblKLantenNummerResult);
		pnlInfo.add(btnMeerInfo);
	}
	
	private void klassePanel(){
		pnlKlasse = new JPanel();
		pnlKlasse.setLayout(new GridLayout(1, 2));
		pnlKlasse.add(rdbEersteKlasse);
		pnlKlasse.add(rdbTweedeKlasse);
	}
	
	private void bedragPanel(){
		pnlBedrag = new JPanel();
		pnlBedrag.setLayout(new GridLayout(1, 2));
		pnlBedrag.add(lblBedrag);
		pnlBedrag.add(lblEuro);
	}

	public JLabel getLblTitle() {
		return lblTitle;
	}

	public JLabel getLblAbonnementsNummer() {
		return lblAbonnementsNummer;
	}

	public JLabel getLblKlantenNummer() {
		return lblKlantenNummer;
	}

	public void setLblKlantenNummer(String lblKlantenNummer) {
		this.lblKlantenNummer.setText(lblKlantenNummer);
	}

	public JLabel getLblKLantenNummerResult() {
		return lblKLantenNummerResult;
	}

	public JLabel getLblStartdatum() {
		return lblStartdatum;
	}

	public JLabel getLblDuur() {
		return lblDuur;
	}

	public JLabel getLblDuurResult() {
		return lblDuurResult;
	}

	public JLabel getLblVervaldatum() {
		return lblVervaldatum;
	}

	public JLabel getLblVervaldatumResult() {
		return lblVervaldatumResult;
	}

	public JLabel getLblKlasse() {
		return lblKlasse;
	}

	public JLabel getLblDiscount() {
		return lblDiscount;
	}

	public JLabel getLblTreinkaart() {
		return lblTreinkaart;
	}

	public JLabel getLblStation1() {
		return lblStation1;
	}

	public JLabel getLblStation2() {
		return lblStation2;
	}

	public JLabel getLblBedrag() {
		return lblBedrag;
	}
	

	public JDatePickerImpl getDteStartdatum() {
		return dteStartdatum;
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

	public JRadioButton getRdbJa() {
		return rdbJa;
	}

	public JRadioButton getRdbNee() {
		return rdbNee;
	}

	public ButtonGroup getGrpJaNee() {
		return grpJaNee;
	}

	public JButton getBtnZoek() {
		return btnZoek;
	}

	public JButton getBtnVerzenden() {
		return btnVerzenden;
	}

	public JButton getBtnMeerInfo() {
		return btnMeerInfo;
	}

	public JButton getBtnValideer() {
		return btnValideer;
	}

	public JTextField getTxtAbonnementsNummer() {
		return txtAbonnementsNummer;
	}

	public StationsAutoCompletor getTxtStation1() {
		return txtStation1;
	}

	public StationsAutoCompletor getTxtStation2() {
		return txtStation2;
	}

	public PassTypesAutoCompletor getCbxTreinkaart() {
		return cbxTypeTicket;
	}

	public JComboBox<String> getCbxDuur() {
		return cbxDuur;
	}

	public DiscountAutoCompletor getCbxDiscount() {
		return getCbxDiscount;
	}

	public JLabel getLblFoutmelding(){
		return lblFoutmelding;
	}

}
