package gui;

import java.util.ArrayList;
import java.util.UUID;

import javax.swing.JComboBox;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

import dao.DiscountDAO;
import model.Discount;

public class DiscountAutoCompletor extends JComboBox<String>
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2884078160204578502L;

	private ArrayList<Discount> list;

	public DiscountAutoCompletor()
	{
		DiscountDAO handler = new DiscountDAO();
		list = handler.selectAll();
		for (int i = 0; i < list.size(); i++) {
			this.addItem(list.get(i).getName());
		}
		AutoCompleteDecorator.decorate(this);
	}
	
	public UUID getSelectedDiscount()
	{
		return list.get(this.getSelectedIndex()).getDiscountID();	
	}
}