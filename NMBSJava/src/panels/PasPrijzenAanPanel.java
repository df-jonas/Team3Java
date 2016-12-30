package panels;

import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import gui.LangageHandler;
import gui.PassTypesAutoCompletor;
import gui.TicketTypesAutoCompletor;

public class PasPrijzenAanPanel extends JPanel {

	private static final long serialVersionUID = -607765881092828393L;

	private JLabel lblTitle;
	private JComboBox<String> cboAanpasKeuze;
	private PassTypesAutoCompletor autPassType;
	private TicketTypesAutoCompletor autTicketType;
	private JLabel lblName;
	private JLabel lblPrice;
	private JLabel lblKlasse;
	
	private JLabel lblOldPrice;
	private JTextField txtOldPrice;
	
	private JTextField txtName;
	private JTextField txtPrice;
	private ButtonGroup grpKlasse;
	private JPanel classpane;
	private JRadioButton rdbFirstclass;
	private JRadioButton rdbSecondclass;
	private JButton btnPasAan;

	public PasPrijzenAanPanel() {
		this.setLayout(new GridLayout(8, 2, 5, 5));

		lblTitle = new JLabel();
		LangageHandler.chooseLangageLbl(lblTitle, "wijzigPrijs");
		lblTitle.setFont(new Font("Tahoma", Font.PLAIN, 18));

		cboAanpasKeuze = new JComboBox<>();
		cboAanpasKeuze.addItem("");
		cboAanpasKeuze.addItem(LangageHandler.chooseLangage("nieuweTicket"));
		cboAanpasKeuze.addItem(LangageHandler.chooseLangage("aanpasTicket"));
		cboAanpasKeuze.addItem(LangageHandler.chooseLangage("nieuwePass"));
		cboAanpasKeuze.addItem(LangageHandler.chooseLangage("passAanpassen"));
		
		lblName = new JLabel();
		LangageHandler.chooseLangageLbl(lblName, "naam");
		lblPrice = new JLabel();
		LangageHandler.chooseLangageLbl(lblPrice, "prijs");
		lblKlasse = new JLabel();
		LangageHandler.chooseLangageLbl(lblKlasse, "klasse");
		lblOldPrice = new JLabel();
		LangageHandler.chooseLangageLbl(lblOldPrice, "oudePrijs");
		
		txtName = new JTextField();
		txtPrice = new JTextField();
		txtOldPrice = new JTextField();
		txtOldPrice.setEnabled(false);
		rdbFirstclass = new JRadioButton();
		LangageHandler.chooseLangageRdb(rdbFirstclass, "1eKlasse");
		rdbFirstclass.setMnemonic(1);
		rdbSecondclass = new JRadioButton();
		LangageHandler.chooseLangageRdb(rdbSecondclass, "2eKlasse");
		rdbFirstclass.setMnemonic(2);
		grpKlasse = new ButtonGroup();
		grpKlasse.add(rdbFirstclass);
		grpKlasse.add(rdbSecondclass);
		classpane = new JPanel();
		classpane.setLayout(new GridLayout(2, 1, 5, 5));
		classpane.add(rdbFirstclass);
		classpane.add(rdbSecondclass);
		
		btnPasAan = new JButton();
		LangageHandler.chooseLangageBtn(btnPasAan, "pasAan");
		
		autPassType = new PassTypesAutoCompletor();
		autTicketType = new TicketTypesAutoCompletor();
		
		this.add(lblTitle);
		this.add(new JLabel());
		
		this.add(new JLabel(LangageHandler.chooseLangage("maakKeuze")));
		this.add(cboAanpasKeuze);
		
		this.add(new JLabel());
		this.add(new JLabel());
		
		this.add(new JLabel());
		this.add(new JLabel());
		
		this.add(new JLabel());
		this.add(new JLabel());
		
		this.add(new JLabel());
		this.add(new JLabel());
		
		this.add(new JLabel());
		this.add(new JLabel());
	}

	public void initAanpassen(int aanpaskeuze) {

		this.removeAll();

		this.add(lblTitle);
		this.add(new JLabel());
		
		this.add(new JLabel(LangageHandler.chooseLangage("maakKeuze")));
		this.add(cboAanpasKeuze);
		
		this.add(new JLabel());
		this.add(new JLabel());

		switch (aanpaskeuze) {
		case 0:
			break;
		case 1:
			this.add(lblName);
			this.add(txtName);
			
			this.add(lblKlasse);
			this.add(classpane);
			
			this.add(lblPrice);
			this.add(txtPrice);
			
			this.add(new JLabel());
			this.add(new JLabel());
			
			this.add(new JLabel());
			this.add(btnPasAan);
			break;
		case 2:
			this.add(new JLabel(LangageHandler.chooseLangage("type")));
			this.add(autTicketType);
			
			this.add(lblKlasse);
			this.add(classpane);
			
			this.add(lblOldPrice);
			this.add(txtOldPrice);
			
			this.add(lblPrice);
			this.add(txtPrice);
			
			this.add(new JLabel());
			this.add(btnPasAan);
			break;
		case 3:
			this.add(lblName);
			this.add(txtName);
			
			this.add(lblPrice);
			this.add(txtPrice);
			
			this.add(new JLabel());
			this.add(new JLabel());
			
			this.add(new JLabel());
			this.add(new JLabel());
			
			this.add(new JLabel());
			this.add(btnPasAan);
			break;
		case 4:
			this.add(new JLabel(LangageHandler.chooseLangage("type")));
			this.add(autPassType);
			
			this.add(lblOldPrice);
			this.add(txtOldPrice);
			
			this.add(lblPrice);
			this.add(txtPrice);
			
			this.add(new JLabel());
			this.add(new JLabel());
			
			this.add(new JLabel());
			this.add(btnPasAan);
			break;

		default:
			break;
		}
	}

	public JLabel getLblTitle() {
		return lblTitle;
	}

	public JComboBox<String> getCboAanpasKeuze() {
		return cboAanpasKeuze;
	}

	public PassTypesAutoCompletor getAutPassType() {
		return autPassType;
	}

	public TicketTypesAutoCompletor getAutTicketType() {
		return autTicketType;
	}

	public JLabel getLblName() {
		return lblName;
	}

	public JLabel getLblPrice() {
		return lblPrice;
	}

	public JLabel getLblKlasse() {
		return lblKlasse;
	}

	public JTextField getTxtName() {
		return txtName;
	}

	public JTextField getTxtPrice() {
		return txtPrice;
	}

	public ButtonGroup getGrpKlasse() {
		return grpKlasse;
	}

	public JRadioButton getRdbFirstclass() {
		return rdbFirstclass;
	}

	public JRadioButton getRdbSecondclass() {
		return rdbSecondclass;
	}

	public JButton getBtnPasAan() {
		return btnPasAan;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public JLabel getLblOldPrice() {
		return lblOldPrice;
	}

	public JTextField getTxtOldPrice() {
		return txtOldPrice;
	}

	public JPanel getClasspane() {
		return classpane;
	}
}