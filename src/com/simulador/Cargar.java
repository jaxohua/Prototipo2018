package com.simulador;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

public class Cargar {
	//String nombre_archivo;
	public static final String slash = System.getProperty("file.separator");
	//public static final String resources = slash+"resources"+slash;
	private static String config_file = slash+"config.xml";
	//static String ruta = Cargar.class.getResource(config_file).toString();
	
	File getFile() {
		
		File archivo = null;
		URI ruta;
		try {
			ruta=getClass().getResource(config_file).toURI();
			System.out.println("RutagetResoruce()="+ruta.toString());
			archivo = new File(ruta);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return archivo;
	}
	
	public static void main(String args []) {
		Document doc = null;
		Cargar carga=new Cargar();
		
		try {
			
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			doc = docBuilder.parse (carga.getFile());
		//	ruta=Manager.class.getResource("/resources/config_file").toString();
			///doc = docBuilder.parse (new File((Manager.class.getResource("/resources/config_file").toURI())));
			
			String docType = doc.getDocumentElement().getNodeName();
			System.out.println("docType:"+docType);
			if (!docType.equalsIgnoreCase("tuio")) {
				System.out.println("error parsing configuration file");
				return;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("error reading configuration file");
			return;
		}

		
		
	}

}
