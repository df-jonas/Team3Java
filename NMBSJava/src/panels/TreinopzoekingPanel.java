package panels;

import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import gui.LangageHandler;
@SuppressWarnings("serial")
public class TreinopzoekingPanel extends JPanel {
	
	private JPanel searchPanel;
	private JPanel resultPanel;

	private JLabel lblTitle;
	private JLabel lblTrein;
	private JLabel lblResult;

	private JTextField txtTrein;

	private JButton btnZoek;

	public TreinopzoekingPanel() {
		initSearchPanel();
		initResultPanel();
		initializeCompletePanel();
	}

	private void initializeCompletePanel() {
		/* init pane */
		this.setLayout(new GridLayout(2, 1, 5, 5));

		/* Add all components */
		this.add(searchPanel);
		this.add(resultPanel);
	}

	private void initResultPanel() {
		/* init pane */
		resultPanel = new JPanel();
		resultPanel.setLayout(new GridLayout(1, 1, 5, 5));

		/* Create all components */
		lblResult = new JLabel();
		LangageHandler.chooseLangageLbl(lblResult, "resRoute");
		lblResult.setHorizontalAlignment(SwingConstants.LEFT);
		lblResult.setVerticalAlignment(SwingConstants.TOP);

		
		
		
		/* Add all components */
		resultPanel.add(lblResult);

	}

	private void initSearchPanel() {
		/* init pane */
		searchPanel = new JPanel();
		searchPanel.setLayout(new GridLayout(4, 2, 5, 5));

		/* Create all components */
		lblTitle = new JLabel("Trein opzoeken");
		LangageHandler.chooseLangageLbl(lblTitle, "treinOpzoeken");
		lblTitle.setFont(new Font("Tahoma", Font.PLAIN, 18));

		lblTrein = new JLabel("");
		LangageHandler.chooseLangageLbl(lblTrein, "trein");
		txtTrein = new JTextField("");

		btnZoek = new JButton("Zoek");

		/* Add all components */
		searchPanel.add(lblTitle);
		searchPanel.add(new JLabel());
		searchPanel.add(lblTrein);
		searchPanel.add(txtTrein);
		searchPanel.add(new JLabel());
		searchPanel.add(btnZoek);
		searchPanel.add(new JLabel());
		searchPanel.add(new JLabel());
	}

	public JPanel getSearchPanel() {
		return searchPanel;
	}

	public JPanel getResultPanel() {
		return resultPanel;
	}

	public JLabel getLblTitle() {
		return lblTitle;
	}

	public JLabel getLblTrein() {
		return lblTrein;
	}

	public JLabel getLblResult() {
		return lblResult;
	}

	public JTextField getTxtTrein() {
		return txtTrein;
	}

	public JButton getBtnZoek() {
		return btnZoek;
	}

}