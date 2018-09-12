package com.simulador;


import java.awt.*;
import java.net.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

public class HelpBrowser extends JFrame implements HyperlinkListener {

	private JScrollPane scrollPane;
	private JEditorPane helpPane;
	private String helpIndex = "file:resources/index.html";

	public HelpBrowser() {
		
		try { helpPane = new JEditorPane(helpIndex); }
		catch (Exception e) { helpPane = new JEditorPane("text/html","<h1>error loading documentation ...</h1>"); }
		
		helpPane.setEditable(false);
		helpPane.addHyperlinkListener(this);

		scrollPane = new JScrollPane(helpPane);
		scrollPane.setViewportBorder(BorderFactory.createMatteBorder(10, 10, 10, 10, Color.white));
		getContentPane().add(scrollPane);
		
		setTitle("reacTIVision manual");
		setBackground(Color.white);
	}

	public void reset() {
		try {  helpPane.setPage(helpIndex); }
		catch (Exception e) { helpPane.setText("<h1>error loading documentation ...</h1>"); }
		setSize(640,480);
		setVisible(true);
	}
		
	public void hyperlinkUpdate(HyperlinkEvent e) {
		if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			try { helpPane.setPage(e.getURL()); }
			catch (Throwable t) { helpPane.setText("<h1>error loading page ...</h1>"); }
		}
	}

}
