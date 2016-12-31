package panels;

import javax.swing.*;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.util.Properties;
import java.awt.Font;
import org.jdatepicker.impl.*;
import gui.GUIDateFormat;

@SuppressWarnings("serial")
public class KlantPasAanPanel extends JPanel {

	private JLabel lblTitel;
	private JLabel lblID;
	private JLabel lblAddress;
	private JLabel lblFirst;
	private JLabel lblLast;
	private JLabel lblBirth;
	private JLabel lblEmail;
	private JLabel lblStreet;
	private JLabel lblNumber;
	private JLabel lblCity;
	private JLabel lblZipCode;
	private JLabel lblCoordinates;
	private JLabel lblAddressID;
	
	
	private JTextField txtID;
	private JTextField txtAddress;
	private JTextField txtFirst;
	private JTextField txtLast;
	private JTextField txtEmail;
	private JTextField txtStreet;
	private JTextField txtNumber;
	private JTextField txtCity;
	private JTextField txtZipCode;
	private JTextField txtCoordinates;
	private JTextField txtAddressID;	
	
	private JButton btnPasAan;
	
	private JDatePickerImpl datePicker;
	
	public KlantPasAanPanel () {
        setLayout(new GridLayout(1, 1, 5, 5));
		
		JPanel edit = new JPanel();
		edit.setLayout(new GridLayout(15, 1, 5, 5));
		
		//1
		lblTitel = new JLabel();
		lblTitel.setText("Pas klant aan");
		lblTitel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		edit.add(lblTitel);
		
		edit.add(new JLabel());
		
		
		//2
		lblID = new JLabel();
		lblID.setText("Aanpassen op ID");
		lblID.setToolTipText(lblID.getText());
		lblID.setPreferredSize(new Dimension(80,40));
		edit.add(lblID);
		
		txtID = new JTextField();
		edit.add(txtID);
		
		lblAddressID = new JLabel();
		lblAddressID.setText("Geef ook Adres ID");
		edit.add(lblAddressID);
		
		txtAddressID = new JTextField();
		edit.add(txtAddressID);
		
		
		edit.add(new JLabel());
		edit.add(new JLabel());
	    
		
		
		
		//4
		lblFirst = new JLabel();
		lblFirst.setText("Voornaam");
		edit.add(lblFirst);
		
		
		
		txtFirst = new JTextField();
		edit.add(txtFirst);
		
		//5
		lblLast = new JLabel();
		lblLast.setText("Achternaam");
		edit.add(lblLast);
		
		
		
		txtLast = new JTextField();
		edit.add(txtLast);
		
		
		//6
		lblBirth = new JLabel();
		lblBirth.setText("Geboortedatum");
		edit.add(lblBirth);
		
		
		
		Properties properties = new Properties();
		properties.put("text.today", "Today");
		properties.put("text.month", "Month");
		properties.put("text.year", "Year");
		JDatePanelImpl datePanel1 = new JDatePanelImpl(new UtilDateModel(), properties);
		datePicker = new JDatePickerImpl(datePanel1, new GUIDateFormat());
		datePicker.getJFormattedTextField().setText(GUIDateFormat.getDate());
		JDatePanelImpl datePanel2 = new JDatePanelImpl(new UtilDateModel(), properties);
		datePicker = new JDatePickerImpl(datePanel2, new GUIDateFormat());
		datePicker.getJFormattedTextField().setText(GUIDateFormat.getDate());
		edit.add(datePicker);
		
		//7
		lblEmail = new JLabel();
		lblEmail.setText("Emailadres");
		edit.add(lblEmail);
		
		txtEmail = new JTextField();
		edit.add(txtEmail);
		
		//8
		lblStreet = new JLabel();
		lblStreet.setText("Straat");
		edit.add(lblStreet);
		
		txtStreet = new JTextField();
		edit.add(txtStreet);
		
		//9
		lblNumber = new JLabel();
		lblNumber.setText("Huisnummer");
		edit.add(lblNumber);
		
		txtNumber = new JTextField();
		edit.add(txtNumber);
		
		//10
		lblCity = new JLabel();
		lblCity.setText("Stad");
		edit.add(lblCity);
		
		txtCity = new JTextField();
		edit.add(txtCity);
		
		//11
		lblZipCode = new JLabel();
		lblZipCode.setText("Postcode");
		edit.add(lblZipCode);
		
		txtZipCode = new JTextField();
		edit.add(txtZipCode);
		
		//12
		lblCoordinates = new JLabel();
		lblCoordinates.setText("Coordinaten");
		edit.add(lblCoordinates);
		
		txtCoordinates = new JTextField();
		edit.add(txtCoordinates);
		//
		edit.add(new JLabel());
		
		btnPasAan = new JButton();
		btnPasAan.setText("Pas Aan");
		edit.add(btnPasAan);
		
		this.add(edit);
		
	}
	
	public JButton getBtnPasAan() {
		return btnPasAan;
	}


	public JLabel getLblID() {
		return lblID;
	}

	public JLabel getLblAddress() {
		return lblAddress;
	}

	public JLabel getLblFirst() {
		return lblFirst;
	}

    public JLabel getLblLast() {
		return lblFirst;
	}
    
    public JLabel getLblTitel() {
		return lblTitel;
    }
    
    public JLabel getLblBirth() {
    	return lblBirth;
    }
    
    public JLabel getLblEmail() {
    	return lblEmail;
    }
    
    public JLabel getLblStreet() {
		return lblStreet;
	}

    public JLabel getLblNumber() {
		return lblNumber;
	}
    
    public JLabel getLblCity() {
		return lblCity;
    }
    
    public JLabel getLblZipCode() {
    	return lblZipCode;
    }
    
    public JLabel getLblCoordinates() {
    	return lblEmail;
    }

    public JLabel getLblAddressID() {
    	return lblAddressID;
    }
    
    
    
	public JTextField getTxtID() {
		return txtID;
	}

	public JTextField getTxtAddress() {
		return txtAddress;
	}
	
	public JTextField getTxtFirst() {
		return txtFirst;
	}
	
	public JTextField getTxtLast() {
		return txtLast;
	}
	
	public JTextField getTxtEmail() {
		return txtEmail;
	}
	
	public JTextField getTxtStreet() {
		return txtStreet;
	}

	public JTextField getTxtCity() {
		return txtCity;
	}
	
	public JTextField getTxtNumber() {
		return txtNumber;
	}
	
	public JTextField getTxtZipCode() {
		return txtZipCode;
	}
	
	public JTextField getTxtCoordinates() {
		return txtCoordinates;
	}

	public JTextField getTxtAddressID() {
		return txtAddressID;
	}
	
	
	
	
	
	public JDatePickerImpl getDatePicker() {
		return datePicker;
	}
}