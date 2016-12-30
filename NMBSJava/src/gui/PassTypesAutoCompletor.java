package gui;

import java.util.ArrayList;
import java.util.UUID;

import javax.swing.JComboBox;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import dao.TypePassDAO;
import model.TypePass;

public class PassTypesAutoCompletor extends JComboBox<String>
{

	private static final long serialVersionUID = 1992628746130586151L;

	private ArrayList<TypePass> list;

	public PassTypesAutoCompletor()
	{
		TypePassDAO handler = new TypePassDAO();
		list = handler.selectAll();
		for (int i = 0; i < list.size(); i++) {
			this.addItem(list.get(i).getName());
		}
		AutoCompleteDecorator.decorate(this);
	}
	
	public UUID getSelectedDiscount()
	{
		return list.get(this.getSelectedIndex()).getTypePassID();	
	}
}