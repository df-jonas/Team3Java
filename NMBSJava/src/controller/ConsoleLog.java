package controller;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import gui.GUIDateFormat;

public class ConsoleLog extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1385096271372831910L;
	
	private static JLabel console;

	public ConsoleLog() {
		setLayout(new GridLayout(1, 1, 0, 0));
		console = new JLabel();
		Color color = Color.decode("#545454");
		console.setForeground(color);
		console.setFont(Font.decode("console"));
		JScrollPane scroller = new JScrollPane(console, JScrollPane.VERTICAL_SCROLLBAR_NEVER,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		this.add(scroller);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(600, 20);
	};
	
	public static void setText(String text){
		console.setText(GUIDateFormat.getTimeWithSec() + " " + text);
	}
}
