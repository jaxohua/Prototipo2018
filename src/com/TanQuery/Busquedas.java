package com.TanQuery;

import java.util.ArrayList;

import TUIO.TuioObject;

public class Busquedas {
	// Return Regresa el numero de intervalo en donde se encuentra el numero de grados
	public static int getIndex(ArrayList<Intervalo> intervalos, float grados) {
		for (Intervalo inter : intervalos) {
			if (grados >= inter.inicio && grados <= inter.fin) {
				return intervalos.indexOf(inter);
			}

		}
		return 1;

	}

	public static ArrayList<Intervalo> generaIntervalo(int ini, int fin,
			int rango) {
		System.out.println("Generando Intervalos");
		ArrayList<Intervalo> inter = new ArrayList<Intervalo>();
		for (int i = ini; i < fin; i += rango) {
			inter.add(new Intervalo(i, i + rango));
		}
		return inter;
	}

	public static TuioObject buscaId(ArrayList<TuioObject> objectList, int id) {
		for (TuioObject objeto : objectList) {
			if (objeto.getSymbolID() == id) {
				return objeto;
			}
		}
		return null;
	}

}
