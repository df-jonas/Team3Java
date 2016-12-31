package panels;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import gui.LangageHandler;

@SuppressWarnings("serial")
public class NavPanel extends JPanel {

	private JButton btnRouteZoek;
	private JButton btnTreinZoek;
	private JButton btnStationZoek;
	private JButton btnVerlorenVoorwerpenZoek;
	private JButton btnbtnVerlorenVoorwerpenVoegToe;
	private JButton btnBiljetKoop;
	private JButton btnPassKoop;
	private JButton btnAbonnementKoop;
	private JButton btnAbonnementVerlengen;
	private JButton btnGroepsreservatie;
	private JButton btnPrijzenAanpassen;
	private JButton btnStaffBeheer;
	private JButton btnLogout;
	private JButton btnKlantZoek;
	private JButton btnKlantPasAan;
	private JLabel madeBy;
	private JButton btnSync;

	private JPanel customPane;

	public NavPanel() {
		initialize();
		createComponents();
		addComponents();
	}

	private void initialize() {
		this.setLayout(new GridLayout(17, 1, 5, 5));
	}

	private void createComponents() {
		btnRouteZoek = new JButton();
		LangageHandler.chooseLangageBtn(btnRouteZoek, "zoekRoute");
		btnRouteZoek.setHorizontalAlignment(SwingConstants.LEFT);

		btnTreinZoek = new JButton();
		LangageHandler.chooseLangageBtn(btnTreinZoek, "zoekTrein");
		btnTreinZoek.setHorizontalAlignment(SwingConstants.LEFT);

		btnStationZoek = new JButton();
		LangageHandler.chooseLangageBtn(btnStationZoek, "zoekStation");
		btnStationZoek.setHorizontalAlignment(SwingConstants.LEFT);

		btnVerlorenVoorwerpenZoek = new JButton();
		LangageHandler.chooseLangageBtn(btnVerlorenVoorwerpenZoek, "zoekVoorwerp");
		btnVerlorenVoorwerpenZoek.setHorizontalAlignment(SwingConstants.LEFT);

		btnbtnVerlorenVoorwerpenVoegToe = new JButton();
		LangageHandler.chooseLangageBtn(btnbtnVerlorenVoorwerpenVoegToe, "voegVoorwerp");
		btnbtnVerlorenVoorwerpenVoegToe.setHorizontalAlignment(SwingConstants.LEFT);

		btnBiljetKoop = new JButton();
		LangageHandler.chooseLangageBtn(btnBiljetKoop, "koopBiljet");
		btnBiljetKoop.setHorizontalAlignment(SwingConstants.LEFT);

		btnPassKoop = new JButton("Koop Pass");
		btnPassKoop.setHorizontalAlignment(SwingConstants.LEFT);

		btnAbonnementKoop = new JButton("Koop abonnement");
		LangageHandler.chooseLangageBtn(btnAbonnementKoop, "koopAbonnement");
		btnAbonnementKoop.setHorizontalAlignment(SwingConstants.LEFT);

		btnAbonnementVerlengen = new JButton();
		LangageHandler.chooseLangageBtn(btnAbonnementVerlengen, "verlengAbonnement");
		btnAbonnementVerlengen.setHorizontalAlignment(SwingConstants.LEFT);

		btnGroepsreservatie = new JButton("Groepsreservatie");
		btnGroepsreservatie.setHorizontalAlignment(SwingConstants.LEFT);

		btnPrijzenAanpassen = new JButton();
		LangageHandler.chooseLangageBtn(btnPrijzenAanpassen, "pasPrijzen");
		btnPrijzenAanpassen.setHorizontalAlignment(SwingConstants.LEFT);

		btnStaffBeheer = new JButton();
		LangageHandler.chooseLangageBtn(btnStaffBeheer, "voegMedewerker");
		btnStaffBeheer.setHorizontalAlignment(SwingConstants.LEFT);

		btnKlantZoek = new JButton("Zoek een klant");
		btnKlantZoek.setHorizontalAlignment(SwingConstants.LEFT);
		
		btnKlantPasAan = new JButton("Pas een klant aan");
		btnKlantPasAan.setHorizontalAlignment(SwingConstants.LEFT);
		
		btnLogout = new JButton();
		LangageHandler.chooseLangageBtn(btnLogout, "Uitloggen");
		btnLogout.setHorizontalAlignment(SwingConstants.LEFT);

		madeBy = new JLabel("By Groep 3");
		madeBy.setHorizontalAlignment(SwingConstants.CENTER);

		customPane = new JPanel();
		customPane.setLayout(new GridLayout(2, 1, 0, 0));
		btnSync = new JButton("Sync");
		customPane.add(new JLabel());
		customPane.add(btnSync);

	}

	private void addComponents() {
		this.add(btnRouteZoek);
		this.add(btnTreinZoek);
		this.add(btnStationZoek);
		this.add(btnBiljetKoop);
		this.add(btnPassKoop);
		this.add(btnAbonnementKoop);
		this.add(btnAbonnementVerlengen);
		this.add(btnVerlorenVoorwerpenZoek);
		this.add(btnbtnVerlorenVoorwerpenVoegToe);
		this.add(btnGroepsreservatie);
		this.add(btnPrijzenAanpassen);
		this.add(btnStaffBeheer);
		this.add(btnKlantZoek);
		this.add(btnKlantPasAan);
		this.add(btnLogout);
		this.add(madeBy);
		this.add(customPane);
	}

	public JButton getBtnRouteZoek() {
		return btnRouteZoek;
	}

	public JButton getBtnTreinZoek() {
		return btnTreinZoek;
	}

	public JButton getBtnStationZoek() {
		return btnStationZoek;
	}

	public JButton getBtnVerlorenVoorwerpenZoek() {
		return btnVerlorenVoorwerpenZoek;
	}

	public JButton getBtnbtnVerlorenVoorwerpenVoegToe() {
		return btnbtnVerlorenVoorwerpenVoegToe;
	}

	public JButton getBtnBiljetKoop() {
		return btnBiljetKoop;
	}

	public JButton getBtnAbonnementKoop() {
		return btnAbonnementKoop;
	}

	public JButton getBtnAbonnementVerlengen() {
		return btnAbonnementVerlengen;
	}

	public JButton getBtnPrijzenAanpassen() {
		return btnPrijzenAanpassen;
	}

	public JButton getBtnLogout() {
		return btnLogout;
	}

	public JButton getBtnStaffBeheer() {
		return btnStaffBeheer;
	}

	public JButton getBtnPassKoop() {
		return btnPassKoop;
	}

	public JButton getBtnGroepsreservatie() {
		return btnGroepsreservatie;
	}

	public JButton getBtnSync() {
		return btnSync;
	}
	
	public JButton getBtnKlantZoek() {
		return btnKlantZoek;
	}
	
	public JButton getBtnKlantPasAan() {
		return btnKlantPasAan;

	}
}
