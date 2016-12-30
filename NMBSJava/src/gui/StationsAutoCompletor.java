package gui;

import java.util.ArrayList;
import java.util.UUID;

import javax.swing.JComboBox;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

import dao.StationDAO;
import model.Station;

public class StationsAutoCompletor extends JComboBox<String> {
	
	private static final long serialVersionUID = -5597294633497864663L;
	
	private ArrayList<Station> list;

	public StationsAutoCompletor() {
		
		StationDAO handler = new StationDAO();
		list = handler.selectAll();
		
		for (int i = 0; i < list.size(); i++) {
			this.addItem(list.get(i).getStationName());
		}
		AutoCompleteDecorator.decorate(this);
	}
	
	public UUID getSelectedStation()
	{
		return list.get(this.getSelectedIndex()).getStationID();
	}
}