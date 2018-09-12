package com.agente;
import TUIO.TuioBlob;
import TUIO.TuioCursor;
import TUIO.TuioListener;
import TUIO.TuioObject;
import TUIO.TuioTime;

public class Aplicacion implements TuioListener,Runnable{
	public Sensor sensor;
	
	public Aplicacion() {
		this.sensor=new Sensor(this);
	}
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Dentro del método run de la clase Aplicación");
		
		while(true) {
			this.sensor.getAmbiente();
			for (TuioObject obj : sensor.getAmbiente()) {
				System.out.println("Elemento:"+obj.getSymbolID());
			}
		}
		
	}

	public static void main(String[] args) {
		/*Aplicacion apli=new Aplicacion();
		sensor=new Sensor(this);
		
		while(true) {
			sensor.sensar();
			for (TuioObject obj : sensor.getObjetos()) {
				System.out.println("Elemento:"+obj.getSymbolID());
			}
		}*/
		
	}

	@Override
	public void addTuioBlob(TuioBlob arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addTuioCursor(TuioCursor arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addTuioObject(TuioObject arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void refresh(TuioTime arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeTuioBlob(TuioBlob arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeTuioCursor(TuioCursor arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeTuioObject(TuioObject arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateTuioBlob(TuioBlob arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateTuioCursor(TuioCursor arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateTuioObject(TuioObject arg0) {
		// TODO Auto-generated method stub
		
	}

	
}
