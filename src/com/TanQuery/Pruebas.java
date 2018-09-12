package com.TanQuery;

import java.util.ArrayList;
//import java.util.Collections;


import processing.data.Table;

public class Pruebas {
	String cadena="";

	public static void deleteToken(ArrayList<Token> tokens, int id) {
		for (Token token : tokens) {
			if (token.getId() == id) {
				System.out.println("Elemento encontrado en posicion: "
						+ tokens.indexOf(token));
				;
				// i=tokens.indexOf(token);
				tokens.remove(token);
				return;
			}
		}
	}

	@SuppressWarnings("unused")
	public static boolean saveCSV(TanQuery frame, String lista) {
		Table csv = new Table();
		ArrayList<String> datos = new ArrayList<String>();
		return false;
	}

	public static void test(TanQuery frame) {
		frame.DATABASE = true;
		frame.dataBase = "escuela";
		Valores.fillRelations(frame);
		frame.tokens.add(new Token(1, "DB", "ESCUELA", "ESCUELA", -1, -1, 0,1000));

		frame.tokens.add(new Token(6, "relation", "PROFESORES_XA", "PROFESORES_XA", -1,-1, 0, 1021));
		frame.tokens.add(new Token(7, "relation", "ASIGNADAS", "ASIGNADAS", -1,-1, 0, 1001));
		frame.tokens.add(new Token(8, "relation", "MATERIAS", "MATERIAS",-1, -1, 0, 1003));
		frame.tokens.add(new Token(9, "relation", "PROFESORES_MX", "PROFESORES_MX", -1,-1, 0, 1021));

		/*frame.tokens.add(new Token(16, "attribute", "NOMBRE_MAT", "NOMBRE_MAT", -1,-1, 0, 1045));
		frame.tokens.add(new Token(17, "attribute", "MAT", "MAT", -1,-1, 0, 1045));
		frame.tokens.add(new Token(18, "attribute", "CVE_PROF", "CVEMAT", -1,-1, 0, 1045));
		frame.tokens.add(new Token(19, "attribute", "PROF", "PROF", -1,-1, 0, 1045));
		frame.tokens.add(new Token(20, "attribute", "PROF", "PROF", -1,-1, 0, 1045));
		frame.tokens.add(new Token(21, "attribute", "PROF", "PROF", -1,-1, 0, 1045));
		frame.tokens.add(new Token(22, "attribute", "CVE_PROF", "CVE_PROF", -1,-1, 0, 1045));
		//frame.tokens.add(new Token(23, "attribute", "NOMBRE_PROF", "NOMBRE_PROF", -1,-1, 0, 1045));
		frame.tokens.add(new Token(24, "attribute", "NOMBRE_MAT", "NOMBRE_MAT", -1,-1, 0, 1045));*/

		frame.tokens.add(new Token(26, "constant", "RED01", "RED01", -1,-1, 0, 1045));
		frame.tokens.add(new Token(27, "constant", "101", "101", -1,-1, 0, 1045));

		frame.tokens.add(new Token(36, "operator", "\uu03C3", "SELECCION", -1,-1, 0, 1045));
		frame.tokens.add(new Token(37, "operator", "\u03C0", "PROYECCION", -1,-1, 0, 1045));
		frame.tokens.add(new Token(38, "operator", "\u222A", "UNION", -1,-1, 0, 1045));
		frame.tokens.add(new Token(39, "operator", "\u2A1D", "JOIN", -1,-1, 0, 1045));
		frame.tokens.add(new Token(40, "operator", "\u2A1D", "SEMI-JOIN", -1,-1, 0, 1045));
		frame.tokens.add(new Token(41, "operator", "\u03C0", "PROYECCION", -1,-1, 0, 1045));

		frame.tokens.add(new Token(46, "comparation", "=", "=", -1,-1, 0, 1045));
		frame.tokens.add(new Token(47, "comparation", "=", "=", -1,-1, 0, 1045));


	}

	public static void showOperators() {

	}

	public static void addToConsulta(TanQuery frame){

		for (Token token : frame.tokens) {
			frame.consulta.add(new Consulta(token.getId(), 0, 0, token
					.getTipo(), token.getValorDisp(), token.getValorSql(),token.getValorSql(), token
					.getIdSesion()));
		}


	}
	public static Consulta getConsulta(ArrayList<Consulta> consulta , int id){

	for (Consulta consulta2 : consulta) {
		if(consulta2.id==id)
			return consulta2;
	}
	return null;

	}

	public static void crearArbol(TanQuery frame){
		/*Nodo nodo = new Nodo();
		Nodo nodoPadre=new Nodo();

		Consulta padre = getConsulta(frame.consulta,22);
		nodo = Consulta.construirNodo(padre);
		frame.arbolConsul.insertarPadre(nodo);


		nodoPadre=frame.arbolConsul.getNodo(22);
		nodo=Consulta.construirNodo(getConsulta(frame.consulta,21));
		frame.arbolConsul.insertarNodo(nodo, "izquierda",nodoPadre);

		nodoPadre=frame.arbolConsul.getNodo(21);
		nodo=Consulta.construirNodo(getConsulta(frame.consulta,7));
		frame.arbolConsul.insertarNodo(nodo, "izquierda",nodoPadre);

		nodoPadre=frame.arbolConsul.getNodo(21);
		nodo=Consulta.construirNodo(getConsulta(frame.consulta,6));
		frame.arbolConsul.insertarNodo(nodo, "derecha",nodoPadre);*/
	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub
	/*	ArrayList<Token> tokens;
		ArrayList<Consulta> consulta;
		tokens=new ArrayList<Token>();
		consulta=new ArrayList<Consulta>();
		String consultaAR="";*/

		//ArbolConsulta arbol=new ArbolConsulta();

		//Crean Tokens Temporales
		//tokens.add(new Token(1, "DB", "escuela", "escuela", -1, -1, 0,1000));
	//	tokens.add(new Token(8, "relation", "estudiantes", "estudiantes",-1, -1, 0, 1003));
/*		tokens.add(new Token(7, "relation", "maestros", "maestros", -1,-1, 0, 1001));
		tokens.add(new Token(21, "operator", "X", "X", -1,-1, 0, 1020));
		tokens.add(new Token(6, "relation", "materia", "materia", -1,-1, 0, 1021));
		tokens.add(new Token(22, "operator", "seleccion", "Select", -1,-1, 0, 1045));

		System.out.println("Valores de tokens:");

		for (Token token : tokens) {
			System.out.println(token.getValorDisp());
		}

		//addToConsulta(consulta, tokens);
		System.out.println("Valores de Consulta:");
		for (Consulta con : consulta) {
			System.out.println(con.valorDisp+"-"+con.getId());
		}*/



	}
}
