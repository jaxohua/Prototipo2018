package com.agente;

import java.util.ArrayList;

import com.TanQuery.Token;
import com.simulador.Simulation;

import TUIO.TuioObject;

public class Pyfo {
	public int id;
	public float xPos;
	public float yPos;
	public float angulo;
	public String tipo;
	public ArrayList<Pyfo> tocando;
	
	
	public Pyfo(TuioObject obj) {
        this.id = obj.getSymbolID();
        this.xPos = obj.getX()*Simulation.table_width; // * Ambiente.ANCHO
        this.yPos = obj.getY()*Simulation.table_height; //* Ambiente.ALTO
        this.angulo = obj.getAngleDegrees();
        this.tipo=asignar_tipo(id);
        this.tocando = new ArrayList<Pyfo>(); //  # lista de pyfos a los que toca
	}
	public Pyfo(Pyfo pyfo) {
        this.id = pyfo.id;
        this.xPos = pyfo.xPos; // * Ambiente.ANCHO
        this.yPos = pyfo.yPos; //* Ambiente.ALTO
        this.angulo = pyfo.angulo;
        this.tipo=pyfo.tipo;
        this.tocando=new ArrayList<Pyfo>(); //  # lista de pyfos a los que toca
	}
	
	public Pyfo(int i, int j, int k, int l) {
		// TODO Auto-generated constructor stub
		this.id = i;
        this.xPos = j; // * Ambiente.ANCHO
        this.yPos = k; //* Ambiente.ALTO
        this.angulo = l;
        this.tipo="";
        this.tocando = new ArrayList<Pyfo>(); //  # lista de pyfos a los que toca
	}

	public Pyfo() {
		
		
	}

	public static boolean toca_a(Pyfo a, Pyfo b) {
		 Agente.umbral_toque=68;
		if(!a.equals(b)) {
			double distancia = Math.sqrt(Math.pow((b.xPos - a.xPos),2) + Math.pow((b.yPos - a.yPos),2));
	// 4x		System.out.println("Distancia entre:"+ a.id+" y "+b.id+" es:"+distancia);

			if (distancia < Agente.umbral_toque) {
				if(!buscar_en_lista(a.tocando, b)) {
					a.tocando.add(b);
					// AGREGAR CONSULTA A PROLOG PARA DETERMINAR SI EL TOQUE ESTA PERMITIDO
				}
				return true;
			}

		}
		else {
			//System.out.println("Son el mismo pyfo:"+a.id);
		}
		return false;
	}
	
	public static boolean buscar_en_lista(ArrayList<Pyfo> pyfos,Pyfo elemento) {
		for (Pyfo pyfo : pyfos) {
			if(pyfo.equals(elemento)) {
				System.out.println("El elemento:"+ elemento.id+" ya está en la lista:"+pyfo.id);
				return true;
			}
		}
		
		return false;
	}
	
	public void printPyfosTocados() {
		String pyfos="";
		for (Pyfo pyfoTocado : this.tocando) {
			pyfos+=pyfoTocado.id+":"+pyfoTocado.tipo+",";
		}
		System.out.println("pyfo:"+this.id+" toca a:"+pyfos);
	}
	
	public String asignar_tipo(int id) {
		if (id == 1)
			return "bd";

		if (id >= 6 && id <= 15)// green
			return "relacion";

		if (id >= 16 && id <= 25)// blue
			return "atributo";

		if (id >= 26 && id <= 35)// brown
			return "valor";
		
		if (id >= 36 && id <= 45)// yellow (relational operators)
			return "oper_rel";
		
		if (id >= 46 && id <= 55)// (<,<=,>,>=;==;!=)
			return "oper_comp";

		return null;
	}	
	
	

}