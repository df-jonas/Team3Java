package controller;

import java.awt.BorderLayout;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;

import gui.GUIFrame;
import gui.LangageHandler;
import model.SettingsSingleton;
import panels.BiljetPanel;
import panels.GroepsReservatiePanel;
import panels.LoginPanel;
import panels.NavPanel;
import panels.NieuwAbonnementPanel;
import panels.PasPrijzenAanPanel;
import panels.PassPanel;
import panels.RouteberekeningPanel;
import panels.StaffBeheerPanel;
import panels.StartPanel;
import panels.StationboardPanel;
import panels.TreinopzoekingPanel;
import panels.VerlengAbonnementPanel;
import panels.VerlorenVoorwerpMaakPanel;
import panels.VerlorenVoorwerpZoekPanel;
import panels.KlantZoekPanel;
import panels.KlantPasAanPanel;

public class GUIController {

	// frame
	private static GUIFrame frame;
	// navbar
	private static NavPanel nav;
	// all panels
	private static LoginPanel l;
	private static ConsoleLog console;
	private static StartPanel start;
	private static RouteberekeningPanel route;
	private static TreinopzoekingPanel trein;
	private static StationboardPanel station;
	private static BiljetPanel biljet;
	private static PassPanel pass;
	private static GroepsReservatiePanel reservatie;
	private static NieuwAbonnementPanel abonnement;
	private static VerlengAbonnementPanel verlengAbonnement;
	private static VerlorenVoorwerpZoekPanel verlorenVoorwerpZoek;
	private static VerlorenVoorwerpMaakPanel verlorenVoorwerpMaak;
	private static StaffBeheerPanel staff;
	private static PasPrijzenAanPanel prijzenAanpassen;
	private static KlantZoekPanel klantZoek;
	private static KlantPasAanPanel klantPasAan;

	private static SettingsSingleton settings;
	
	public static void start() {
		// Make frame after performing all other tasks
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new GUIFrame();
					frame.setVisible(true);
					frame.setTitle("NMBSTeam - Login");
					login();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void login() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				LangageHandler.setTaal("Nederlands");
				LoginPanel l = new LoginPanel();
				
				//GUIController.startListeningForLang(l);
				//taalListener();
				frame.getContentPane().add(l, BorderLayout.CENTER);
				frame.setContentPane(frame.getContentPane());
				LoginController.login(l);
				
			}
		});
	}
	
	public static void logout() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				settings.clearCreds();
				frame.getContentPane().removeAll();
				l = new LoginPanel();
				frame.getContentPane().add(l, BorderLayout.CENTER);
				frame.setContentPane(frame.getContentPane());
				LoginController.login(l);
			}
		});
	}

	public static void showApp() {
		// Make frame after performing all other tasks
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame.getContentPane().removeAll();
					frame.setTitle("NMBSTeam - Start");
					init();

					// start syncing on hourly-base
					SyncController.Start();
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private static void init() {
		// fixed navbar
		nav = new NavPanel();
		settings = SettingsSingleton.getSettings();
		console = new ConsoleLog();
		
		if (settings.getRights() == 0) {
			nav.getBtnPrijzenAanpassen().setEnabled(false);
			nav.getBtnStaffBeheer().setEnabled(false);
		}
		// startpanel
		start = new StartPanel();
		// start listening for actions on navbar
		startListeningOnNav();
		// create startframe
		frame.getContentPane().add(nav, BorderLayout.WEST);
		frame.getContentPane().add(console, BorderLayout.SOUTH);
		frame.getContentPane().add(start, BorderLayout.CENTER);
		frame.setContentPane(frame.getContentPane());
	}

	protected static void reloadLogin() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				l = new LoginPanel();
				frame.getContentPane().removeAll();
				frame.getContentPane().add(l);
				frame.setContentPane(frame.getContentPane());
				LoginController.login(l);
			}
		});
	}
	
	

	public static void startListeningOnNav() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				nav.getBtnRouteZoek().addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						startRouteberekening();
					}
				});

				nav.getBtnTreinZoek().addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						startTreinopzoeking();
					}
				});

				nav.getBtnStationZoek().addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						startStationsbord();
					}
				});

				nav.getBtnBiljetKoop().addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						startKoopBiljet();
					}
				});
				
				nav.getBtnPassKoop().addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						startKoopPass();
					}
				});
				
				nav.getBtnAbonnementKoop().addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						startKoopAbonnement();
					}
				});
				nav.getBtnAbonnementVerlengen().addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						startVerlengAbonnement();
					}
				});

				nav.getBtnVerlorenVoorwerpenZoek().addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						startVerlorenVoorwerpZoek();
					}
				});

				nav.getBtnbtnVerlorenVoorwerpenVoegToe().addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						startVerlorenVoorwerpMaak();
					}
				});
				
				nav.getBtnGroepsreservatie().addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						startReservatieMaak();
					}
				});
				
				nav.getBtnStaffBeheer().addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						startStaffBeheer();
					}
				});
				nav.getBtnPrijzenAanpassen().addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						startPrijzenAanpassen();
					}
				});
				
				nav.getBtnKlantZoek().addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						startKlantZoek();
					}
				});
				
				nav.getBtnKlantPasAan().addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						startKlantPasAan();
					}
				});
				
				nav.getBtnLogout().addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						logout();
					}
				});
				
				nav.getBtnSync().addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						SyncController.Start();
					}
				});
			}
		});
	}
	
	/*public static void login() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				LoginPanel l = new LoginPanel();
				frame.getContentPane().add(l, BorderLayout.CENTER);
				frame.setContentPane(frame.getContentPane());
				LoginController.login(l);
			}
		});
	}
	
	public static void logout() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				settings.clearCreds();
				frame.getContentPane().removeAll();
				frame.setTitle("NMBSTeam - Login");
				
				LoginPanel l = new LoginPanel();
				frame.getContentPane().add(l, BorderLayout.CENTER);
				frame.setContentPane(frame.getContentPane());
				LoginController.login(l);
			}
		});
	}*/

	private static void startRouteberekening() {
		route = new RouteberekeningPanel();
		frame.setTitle("NMBSTeam - Bereken route");
		frame.getContentPane().remove(frame.getContentPane().getComponentCount() - 1);
		frame.getContentPane().add(route);
		frame.setContentPane(frame.getContentPane());
		RouteberekeningController.startListening(route);
	}

	private static void startTreinopzoeking() {
		trein = new TreinopzoekingPanel();
		frame.setTitle("NMBSTeam - Zoek trein");
		frame.getContentPane().remove(frame.getContentPane().getComponentCount() - 1);
		frame.getContentPane().add(trein);
		frame.setContentPane(frame.getContentPane());
		TreinopzoekingController.startListening(trein);
	}

	private static void startStationsbord() {
		station = new StationboardPanel();
		frame.setTitle("NMBSTeam - Bekijk het stationsbord");
		frame.getContentPane().remove(frame.getContentPane().getComponentCount() - 1);
		frame.getContentPane().add(station);
		frame.setContentPane(frame.getContentPane());
		StationsbordController.startListening(station);
	}

	private static void startKoopBiljet() {
		biljet = new BiljetPanel();
		frame.setTitle("NMBSTeam - Koop Biljet");
		frame.getContentPane().remove(frame.getContentPane().getComponentCount() - 1);
		frame.getContentPane().add(biljet);
		frame.setContentPane(frame.getContentPane());
		KoopBiljetController.startListening(biljet);
	}
	
	private static void startKoopPass() {
		pass = new PassPanel();
		frame.setTitle("NMBSTeam - Koop Pass");
		frame.getContentPane().remove(frame.getContentPane().getComponentCount() - 1);
		frame.getContentPane().add(pass);
		frame.setContentPane(frame.getContentPane());
		KoopPassController.startListening(pass);
	}

	private static void startKoopAbonnement() {
		abonnement = new NieuwAbonnementPanel();
		frame.setTitle("NMBSTeam - Koop Abonnement");
		frame.getContentPane().remove(frame.getContentPane().getComponentCount() - 1);
		frame.getContentPane().add(abonnement);
		frame.setContentPane(frame.getContentPane());
		KoopAbonnementController.startListening(abonnement);
	}

	private static void startVerlengAbonnement() {
		verlengAbonnement = new VerlengAbonnementPanel();
		frame.setTitle("NMBSTeam - Koop Abonnement");
		frame.getContentPane().remove(frame.getContentPane().getComponentCount() - 1);
		frame.getContentPane().add(verlengAbonnement);
		frame.setContentPane(frame.getContentPane());
		VerlengAbonnementController.startListening(verlengAbonnement);
	}
	
	private static void startReservatieMaak() {
		reservatie = new GroepsReservatiePanel();
		frame.setTitle("NMBSTeam - Maak reservatie");
		frame.getContentPane().remove(frame.getContentPane().getComponentCount() - 1);
		frame.getContentPane().add(reservatie);
		frame.setContentPane(frame.getContentPane());
		GroepsreservatieController.startListening(reservatie);
	}

	private static void startVerlorenVoorwerpZoek() {
		verlorenVoorwerpZoek = new VerlorenVoorwerpZoekPanel();
		frame.setTitle("NMBSTeam - Zoek verloren voorwerp");
		frame.getContentPane().remove(frame.getContentPane().getComponentCount() - 1);
		frame.getContentPane().add(verlorenVoorwerpZoek);
		frame.setContentPane(frame.getContentPane());
		VerlorenVoorwerpZoekController.startListening(verlorenVoorwerpZoek);
	}

	private static void startVerlorenVoorwerpMaak() {
		verlorenVoorwerpMaak = new VerlorenVoorwerpMaakPanel();
		frame.setTitle("NMBSTeam - Maak verloren voorwerp");
		frame.getContentPane().remove(frame.getContentPane().getComponentCount() - 1);
		frame.getContentPane().add(verlorenVoorwerpMaak);
		frame.setContentPane(frame.getContentPane());
		VerlorenVoorwerpMaakController.startListening(verlorenVoorwerpMaak);
	}
	
	private static void startStaffBeheer() {
		staff = new StaffBeheerPanel();
		frame.setTitle("NMBSTeam - Voeg nieuwe medewerker");
		frame.getContentPane().remove(frame.getContentPane().getComponentCount() - 1);
		frame.getContentPane().add(staff);
		frame.setContentPane(frame.getContentPane());
		StaffBeheerController.startListening(staff);
	}
		
	private static void startPrijzenAanpassen() {
		prijzenAanpassen = new PasPrijzenAanPanel();
		frame.setTitle("NMBSTeam - Pas prijzen aan");
		frame.getContentPane().remove(frame.getContentPane().getComponentCount() - 1);
		frame.getContentPane().add(prijzenAanpassen);
		frame.setContentPane(frame.getContentPane());
		PasPrijzenAanController.startListening(prijzenAanpassen);
	}
	
	private static void startKlantZoek() {
		klantZoek = new KlantZoekPanel();
		frame.setTitle("NMBSTeam - Zoek klant");
		frame.getContentPane().remove(frame.getContentPane().getComponentCount() - 1);
		frame.getContentPane().add(klantZoek);
		frame.setContentPane(frame.getContentPane());
		KlantZoekController.startListening(klantZoek);
	}
	
	private static void startKlantPasAan() {
		klantPasAan = new KlantPasAanPanel();
		frame.setTitle("NMBSTeam - Pas klant aan");
		frame.getContentPane().remove(frame.getContentPane().getComponentCount() - 1);
		frame.getContentPane().add(klantPasAan);
		frame.setContentPane(frame.getContentPane());
		KlantPasAanController.startListening(klantPasAan);
	}

	public static GUIFrame getFrame() {
		return frame;
	}
}
