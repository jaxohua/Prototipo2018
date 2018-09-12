
package com.simulador;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.agente.Aplicacion;


public class TuioSimulator {
	
	public static int width = 1280;
	public static int height = 720;
	
	
	public static void main(String[] argv) {
	
		String host = "127.0.0.1";
		int port = 3333;
		String config=null;
		
		for (int i=0;i<argv.length;i++) {
			if (argv[i].equalsIgnoreCase("-host")) {
				try { host = argv[i+1]; } catch (Exception e) {}
				i++;
			} else if (argv[i].equalsIgnoreCase("-port")) {
				try { port = Integer.parseInt(argv[i+1]); } catch (Exception e) {}
				i++;
			} else if (argv[i].equalsIgnoreCase("-config")) {
				try { config = argv[i+1]; } catch (Exception e) {}
				i++;
			} else {
				System.out.println("Opciones Simulador:");
				System.out.println("\t-host\ttarget IP");
				System.out.println("\t-port\ttarget port");
				System.out.println("\t-config\tconfig file");
				System.exit(0);
			}
		}

		System.out.println("Enviando mensajes TUIO hacia ==> "+host+":"+port);
		JFrame app = new JFrame();		
		app.setTitle("Simulador TanQuery V1.0");

		final Manager manager = new Manager(app,config);
		final Simulation simulation = new Simulation(manager,host,port);
		
		app.getContentPane().add(simulation);
		
		app.addWindowListener( new WindowAdapter() { 
			public void windowClosing(WindowEvent evt) {	
				simulation.reset();
				System.exit(0);
			} 
		});
		
		Insets ins = app.getInsets();
		app.setSize(width + ins.left + ins.right, height + ins.top  + ins.bottom + 20);

		app.setResizable(true);
		app.setVisible(true);
	}
}


