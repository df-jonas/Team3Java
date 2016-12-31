package panels;

import java.awt.Font;
import java.awt.GridLayout;
import java.util.Properties;
import javax.swing.*;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import com.github.lgooddatepicker.components.TimePicker;
import gui.GUIDateFormat;
import gui.LangageHandler;
import gui.StationsAutoCompletor;

@SuppressWarnings("serial")
public class GroepsReservatiePanel extends JPanel {

	private StationsAutoCompletor autVan;
	private JLabel lblVan;
	private StationsAutoCompletor autNaar;
	private JLabel lblNaar;
	private JComboBox<String> cboTrein;
	private JSpinner aantPersonen;
	private JDatePickerImpl dteDate;
	private JButton btnPrint;
	private JLabel lblPrijs;
	private JLabel lblTime;
	private TimePicker tmeTime;
	private JLabel lblTitle;

	public GroepsReservatiePanel() {
		this.setLayout(new GridLayout(9, 2, 5, 5));

		// title
		lblTitle = new JLabel();
		LangageHandler.chooseLangageLbl(lblTitle, "reservatie");
		lblTitle.setFont(new Font("Tahoma", Font.PLAIN, 18));

		// labelVan
		lblVan = new JLabel();
		LangageHandler.chooseLangageLbl(lblVan, "van");
		autVan = new StationsAutoCompletor();

		// labelNaar
		lblNaar = new JLabel();
		LangageHandler.chooseLangageLbl(lblNaar, "naar");
		autNaar = new StationsAutoCompletor();

		// datepicker properties
		Properties properties = new Properties();
		properties.put("text.today", "Today");
		properties.put("text.month", "Month");
		properties.put("text.year", "Year");

		// datepicker
		JDatePanelImpl datePanel1 = new JDatePanelImpl(new UtilDateModel(), properties);
		dteDate = new JDatePickerImpl(datePanel1, new GUIDateFormat());
		dteDate.getJFormattedTextField().setText(GUIDateFormat.getDate());

		// time
		tmeTime = new TimePicker();
		tmeTime.setText(GUIDateFormat.getTime());

		// combobox
		cboTrein = new JComboBox<String>();

		// Spinner
		SpinnerModel model = new SpinnerNumberModel(0, 0, 200, 1);
		aantPersonen = new JSpinner(model);

		// print
		btnPrint = new JButton();
		LangageHandler.chooseLangageBtn(btnPrint, "print");
		// prijs
		lblPrijs = new JLabel(" € 0 ");
		lblPrijs.setHorizontalAlignment(SwingConstants.CENTER);
		lblPrijs.setFont(new Font("Tahoma", Font.PLAIN, 18));

		// add's
		this.add(lblTitle);
		this.add(new JLabel());
		this.add(lblVan);
		this.add(autVan);
		this.add(lblNaar);
		this.add(autNaar);
		this.add(cboTrein);
		this.add(aantPersonen);
		this.add(dteDate);
		this.add(tmeTime);
		this.add(new JLabel());
		this.add(lblPrijs);
		this.add(new JLabel());
		this.add(btnPrint);
		this.add(new JLabel());
		this.add(new JLabel());
		this.add(new JLabel());
		this.add(new JLabel());

	}

	public StationsAutoCompletor getAutVan() {
		return autVan;
	}

	public JLabel getLblVan() {
		return lblVan;
	}

	public StationsAutoCompletor getAutNaar() {
		return autNaar;
	}

	public JLabel getLblNaar() {
		return lblNaar;
	}

	public JComboBox<String> getCboTrein() {
		return cboTrein;
	}

	public JSpinner getAantPersonen() {
		return aantPersonen;
	}

	public JDatePickerImpl getDteDate() {
		return dteDate;
	}

	public JButton getBtnPrint() {
		return btnPrint;
	}

	public JLabel getLblPrijs() {
		return lblPrijs;
	}

	public JLabel getLblTime() {
		return lblTime;
	}

	public TimePicker getTmeTime() {
		return tmeTime;
	}
}