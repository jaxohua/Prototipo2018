package com.TanQuery;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;






//jar para crear XML
import org.jespxml.*;
import org.jespxml.excepciones.TagHijoNotFoundException;
import org.jespxml.modelo.Tag;
import org.xml.sax.SAXException;




import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class Configuration extends JFrame {

	private JPanel contentPane;
	JTextField server;
	private JPanel panel_1;
	JTextField user;
	JTextField path;
	JLabel labelPass;
    JPasswordField password;
	JTextField port;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			
			public void run() {
				try {
					Configuration wConfiguration = new Configuration();
					wConfiguration.show();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	
	/**
	 * Create the frame.
	 */
	public Configuration() {
		setTitle("Configuration Settings");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 487, 353);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		//JPanel panel = new JPanel();
		
		panel_1 = new JPanel();
		panel_1.setBounds(0, 0, 400, 350);
		getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		server = new JTextField();
		server.setBounds(134, 27, 173, 26);
		panel_1.add(server);
		server.setColumns(10);
		
		port=new JTextField(); 
		port.setBounds(356, 27, 48, 26);
		panel_1.add(port);
		
		user = new JTextField();
		user.setColumns(10);
		user.setBounds(134, 60, 102, 26);
		panel_1.add(user);
		
		path = new JTextField();
		path.setColumns(10);
		path.setBounds(134, 98, 200, 26);
		panel_1.add(path);
		
		JLabel lblMysqlServer = new JLabel("Mysql Server:");
		lblMysqlServer.setBounds(52, 32, 83, 16);
		panel_1.add(lblMysqlServer);
		
		JLabel labelUser = new JLabel("User:");
		labelUser.setBounds(103, 65, 32, 16);
		panel_1.add(labelUser);
		
		labelPass = new JLabel("Password:");
		labelPass.setBounds(280, 65, 65, 16);
		panel_1.add(labelPass);
		
		password = new JPasswordField();
		password.setBounds(340, 60, 94, 26);
		panel_1.add(password);
		
		JLabel lblLogPath = new JLabel("Log PATH:");
		lblLogPath.setBounds(70, 103, 65, 16);
		panel_1.add(lblLogPath);
		
		JLabel lblPort = new JLabel("Port:");
		lblPort.setBounds(326, 32, 38, 16);
		panel_1.add(lblPort);
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				xmlSave();
			}
		});
		btnSave.setBounds(139, 186, 117, 29);
		panel_1.add(btnSave);
		
		JButton btnLoad = new JButton("Load");
		btnLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					load();
				} catch (TagHijoNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnLoad.setBounds(264, 186, 117, 29);
		panel_1.add(btnLoad);
		try {
			this.load();
		} catch (TagHijoNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	public  void xmlSave(){
		Tag raiz,servidor, puerto, usuario, contra,rutalog;
		raiz=new Tag("Tanqery");
		
		
		servidor=new Tag("mysqlserver");
		puerto=new Tag("port");
		usuario=new Tag("user");
		contra=new Tag("password");
		rutalog=new Tag("logpath");
		
		servidor.addContenido(this.server.getText());
		puerto.addContenido(this.port.getText());
		usuario.addContenido(this.user.getText());
		contra.addContenido(this.password.getText());
		rutalog.addContenido(this.path.getText());
		
		raiz.addTagHijo(servidor);
		raiz.addTagHijo(puerto);
		raiz.addTagHijo(usuario);
		raiz.addTagHijo(contra);
		raiz.addTagHijo(rutalog);
		
		JespXML xml=new JespXML(new File("configuracion.xml"));
		try {
			xml.escribirXML(raiz);
		} catch (FileNotFoundException | ParserConfigurationException
				| TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	@SuppressWarnings("deprecation")
	public void load() throws TagHijoNotFoundException{
		JespXML xmlfile=new JespXML(new File("configuracion.xml"));
		String servidor = "s";// puerto, usuario, password, rutalog;
		try {
			Tag raiz=xmlfile.leerXML();
			servidor=raiz.getTagHijoByName("mysqlserver").getContenido();
			this.server.setText(raiz.getTagHijoByName("mysqlserver").getContenido());
			this.port.setText(raiz.getTagHijoByName("port").getContenido());
			this.user.setText(raiz.getTagHijoByName("user").getContenido());
			this.password.setText(raiz.getTagHijoByName("password").getContenido());
			this.path.setText(raiz.getTagHijoByName("logpath").getContenido());
//			System.out.println("valor server form:"+this.server.getText());
//			System.out.println("valor server:"+servidor);
			
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		this.repaint();
	}
	
	public static ArrayList<String> loadXML() throws TagHijoNotFoundException {
		ArrayList<String> xmlDatos=null;
		JespXML xmlfile=new JespXML(new File("configuracion.xml"));
		try {
			xmlDatos=new ArrayList<String>();
			Tag raiz=xmlfile.leerXML();
			xmlDatos.add(0,raiz.getTagHijoByName("mysqlserver").getContenido());
			xmlDatos.add(1,raiz.getTagHijoByName("port").getContenido());
			xmlDatos.add(2,raiz.getTagHijoByName("user").getContenido());
			xmlDatos.add(3,raiz.getTagHijoByName("password").getContenido());
			xmlDatos.add(4,raiz.getTagHijoByName("logpath").getContenido());
			
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		return xmlDatos;
		
	}
	
	
}
