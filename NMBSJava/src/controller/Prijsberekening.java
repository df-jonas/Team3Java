package controller;

import dao.TypePassDAO;
import dao.TypeTicketDAO;
import model.Station;
import model.TypePass;
import model.TypeTicket;

public abstract class Prijsberekening {

	public enum TypeKeuze {
		PASS, TICKET, ABONNEMENT
	}

	public static double berekenAfstand(Station s1, Station s2) {

		// x en y voor Station 1
		double x1 = Double.parseDouble(s1.getCoX());
		double y1 = Double.parseDouble(s1.getCoY());

		// x en y voor Station 2
		double x2 = Double.parseDouble(s2.getCoX());
		double y2 = Double.parseDouble(s2.getCoY());

		return radToDeg(Math.acos(Math.sin(degToRad(y1)) * Math.sin(degToRad(y2))
				+ Math.cos(degToRad(y1)) * Math.cos(degToRad(y2)) * Math.cos(degToRad(x1 - x2)))) * 60 * 1.1515
				* 1.609344;
		/*
		 * acos((sin(y1) * sin(y2) + cos(y1) * cos(y2) * cos(x1 - x2) * 60 *
		 * 1.1515 * 1.609344
		 * 
		 * where
		 * 
		 * 1.609344 = km in 1 mile; 60 = minutes in 1 degree; 1.1515 = statute
		 * miles in 1 nautical mile
		 * 
		 */
	}

	public static double berekenPrijs(Station s1, Station s2, TypeKeuze typeKeuze, String typeID) {

		double distance = berekenAfstand(s1, s2);
		double price = 0;
		int zones = 1;

		switch (typeKeuze) {
		case PASS: {
			TypePassDAO handler = new TypePassDAO();
			TypePass type = handler.selectOne(typeID);
			price = type.getPrice();

			zones = Math.floorDiv((int) distance, 15);
			if (zones < 1)
				zones = 1;
			price = price + (zones*(price / 10));
		}
			break;
		case TICKET: {
			TypeTicketDAO handler = new TypeTicketDAO();
			TypeTicket type = handler.selectOne(typeID);
			price = type.getPrice();
			
			zones = Math.floorDiv((int) distance, 15);
			if (zones < 1)
				zones = 1;
			price = price + (zones*(price / 10));
		}
			break;
		case ABONNEMENT: {
			TypeTicketDAO handler = new TypeTicketDAO();
			TypeTicket type = handler.selectOne(typeID);
			price = type.getPrice();
			
			zones = Math.floorDiv((int) distance, 15);
			if (zones < 1)
				zones = 1;
			
			price = ((price + (zones*(price / 10)))*365)/12;
		}
			break;
		default:
			break;
		}
		return price;
	}

	public static double degToRad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	public static double radToDeg(double rad) {
		return (rad * 180 / Math.PI);
	}
}
