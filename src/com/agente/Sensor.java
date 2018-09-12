package com.agente;

import java.util.ArrayList;
import TUIO.TuioClient;
import TUIO.TuioListener;
import TUIO.TuioObject;

public class Sensor {
	/*
	 * Atributo que guarda la lista de objetos que son captados por la cámara.
	 */
	//private ArrayList<TuioObject> objetos;
	public TuioClient cliente;
	
	public Sensor(TuioListener recep) {
		cliente = new TuioClient();
		cliente.addTuioListener(recep);
		cliente.connect();
		//getAmbiente();
	}
	
	public ArrayList<TuioObject> getAmbiente() {
		return cliente.getTuioObjectList(); 
	}
	
}
	
	
	