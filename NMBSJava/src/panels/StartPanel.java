package panels;

import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import gui.LangageHandler;

@SuppressWarnings("serial")
public class StartPanel extends JPanel {

	private JLabel lblTitle;
	private JLabel lblDescr;

	public StartPanel() {
		init();
	}

	private void init() {

		/* Create all components */
		lblTitle = new JLabel("Start");
		lblTitle.setBounds(10, 0, 440, 48);
		lblTitle.setFont(new Font("Tahoma", Font.PLAIN, 18));
		
		String s = "<html><p>"+ LangageHandler.chooseLangage("start")
				+ "<ul>"
				+ "<li>Jonas D.F.</li>"
				+ "<li>Ian K.</li>"
				+ "<li>Ludovic M.</li>"
				+ "<li>Glenn B.</li>"
				+ "<li>Remco G.</li>"
				+ "<li>Joshua V.D.M.</li>"
				+ "<li>Youssef I.</li>"
				+ "</ul>"
				+ "<br>"
				+ LangageHandler.chooseLangage("klik") + "</p>"
				+ "</html>";
		lblDescr = new JLabel(s);
		lblDescr.setVerticalAlignment(SwingConstants.TOP);
		lblDescr.setBounds(10, 50, 440, 249);
		
		/* Add all components */
		setLayout(null);
		this.add(lblTitle);
		this.add(lblDescr);
	}
}
