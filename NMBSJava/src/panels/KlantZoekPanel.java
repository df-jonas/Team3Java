package panels;

import javax.swing.*;
import java.awt.GridLayout;
import java.awt.Font;

@SuppressWarnings("serial")
public class KlantZoekPanel extends JPanel {

	
	private JButton btnZoek;
	private JButton btnZoek1;
	private JButton btnZoek2;
	private JButton btnZoek3;
	
	private JLabel lblTitel;
	private JLabel lblKlantNr;
	private JLabel lblKaartNr;
	private JLabel lblKlantVoorNaam;
	private JLabel lblKlantNaam;
	private JLabel lblResultat;
	
	private JTextField txtKlantNr;
	private JTextField txtKaartNr;
	private JTextField txtKlantVoorNaam;
	private JTextField txtKlantNaam;
	
	public KlantZoekPanel () {
		setLayout(new GridLayout(2, 1, 5, 5));
		
		JPanel search = new JPanel();
		search.setLayout(new GridLayout(5, 2, 10, 10));
		
		//1
				lblTitel = new JLabel();
				lblTitel.setText("Zoek klant");
				lblTitel.setFont(new Font("Tahoma", Font.PLAIN, 18));
				search.add(lblTitel);
				
				search.add(new JLabel());
				
	
				search.add(new JLabel());
				
				
				
				
				
		//3
				lblKlantNr = new JLabel();
				lblKlantNr.setText("Klantnummer: ");
				search.add(lblKlantNr);
			
				txtKlantNr = new JTextField();
				search.add(txtKlantNr);
				
				btnZoek = new JButton();
				btnZoek.setText("Zoek");
				search.add(btnZoek);

		//4
				lblKaartNr = new JLabel();
				lblKaartNr.setText("Kaartnummer: ");
				search.add(lblKaartNr);
			
				txtKaartNr = new JTextField();
				search.add(txtKaartNr);
				
				btnZoek1 = new JButton();
				btnZoek1.setText("Zoek");
				search.add(btnZoek1);
				
				
				lblKlantVoorNaam = new JLabel();
				lblKlantVoorNaam.setText("Voornaam: ");
				search.add(lblKlantVoorNaam);
			
				txtKlantVoorNaam = new JTextField();
				search.add(txtKlantVoorNaam);
				
				btnZoek3 = new JButton();
				btnZoek3.setText("Zoek");
				search.add(btnZoek3);

				
		//5
				lblKlantNaam = new JLabel();
				lblKlantNaam.setText("Achternaam: ");
				search.add(lblKlantNaam);
			
				txtKlantNaam = new JTextField();
				search.add(txtKlantNaam);
				
				btnZoek2 = new JButton();
				btnZoek2.setText("Zoek");
				search.add(btnZoek2);
				
		//6

				
				this.add(search);
				
				lblResultat = new JLabel();
				lblResultat.setText("  ");
				lblResultat.setVerticalAlignment(SwingConstants.TOP);

				JScrollPane scroller = new JScrollPane(lblResultat, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
						JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

				this.add(scroller);

	}
	
	

	public JButton getBtnZoek() {
		return btnZoek;
	}
	
	public JButton getBtnZoek1() {
		return btnZoek1;
	}
	
	public JButton getBtnZoek2() {
		return btnZoek2;
	}
	
	public JButton getBtnZoek3() {
		return btnZoek3;
	}

	public JLabel getLblKlantNr() {
		return lblKlantNr;
	}

	public JLabel getLblKlantNaam() {
		return lblKlantNaam;
	}
	
	public JLabel getLblKlantVoorNaam() {
		return lblKlantVoorNaam;
	}

	public JLabel getLblResultat() {
		return lblResultat;
	}

	public JLabel getLblTitel() {
		return lblTitel;
	}

	public JLabel getLblKaartNr() {
		return lblKaartNr;
	}

	public JTextField getTxtKlantNr() {
		return txtKlantNr;
	}

	public JTextField getTxtKlantNaam() {
		return txtKlantNaam;
	}
	
	public JTextField getTxtKaartNr() {
		return txtKaartNr;
	}
	
	public JTextField getTxtKlantVoorNaam() {
		return txtKlantVoorNaam;
	}

}