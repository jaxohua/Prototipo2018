package com.simulador;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class PanelDatos extends JPanel {
	public JTable tabla;
	public DefaultTableModel model;	
	
	public PanelDatos() {
		//inicializar componentes de panel de datos
		tabla = new JTable();
		tabla.setBorder(null);
		tabla.setForeground(Color.WHITE);
		tabla.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		tabla.setBounds(0, 0, 486, 302);
		tabla.setOpaque(true);
		tabla.setFillsViewportHeight(true);
		//Color ivory=new Color(34,34,34);
		tabla.setBackground(Color.BLACK);
		
		JScrollPane scrollPane = new JScrollPane(tabla);
		add(scrollPane);
	}
	
	public static void main(String[] args) {
		JFrame jf = new JFrame("Tutorial");
        PanelDatos t = new PanelDatos();
        jf.setSize(500, 500);
        jf.setVisible(true);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.add(t);
        jf.setVisible(true);
		
	}
	

}
