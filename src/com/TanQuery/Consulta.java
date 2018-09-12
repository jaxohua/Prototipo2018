package com.TanQuery;

//import java.sql.ResultSet;
//import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
//import java.util.HashMap;

//import com.mysql.jdbc.ResultSetMetaData;

import TUIO.TuioObject;

public class Consulta {

	int id;
	int posX;
	int posY;
	String tipo;
	String simbolo;
	public String getSimbolo() {
		return simbolo;
	}

	public void setSimbolo(String simbolo) {
		this.simbolo = simbolo;
	}

	String valorDisp;
	public String getValorDisp() {
		return valorDisp;
	}

	public void setValorDisp(String valorDisp) {
		this.valorDisp = valorDisp;
	}

	public String getValorSql() {
		return valorSql;
	}

	public void setValorSql(String valorSql) {
		this.valorSql = valorSql;
	}

	String valorSql;
	long idSesion;


	public Consulta(int id, int posX, int posY, String tipo,String simbolo, String valorDisp,
			String valorSql, long idSesion) {
		super();
		this.id = id;
		this.posX = posX;
		this.posY = posY;
		this.tipo = tipo;
		this.simbolo=simbolo;
		this.valorDisp = valorDisp;
		this.valorSql = valorSql;
		this.idSesion = idSesion;
	}

	public Consulta() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPosX() {
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public int getPosY() {
		return posY;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}


	public static void listaContenido(ArrayList<Consulta> lista) {

		for (Consulta object : lista) {
			System.out.print(object.getId() + ", ");
		}

	}

	public static boolean isInConsulta(ArrayList<Consulta> consulta, int id) {
		for (Consulta consulta2 : consulta) {
			if (consulta2.getId() == id) {
				return true;
			}

		}
		return false;

	}

	public static boolean borraDeConsulta(ArrayList<Consulta> consulta, int id) {
		for (Consulta consulta2 : consulta) {
			if (consulta2.getId() == id) {
				consulta.remove(consulta2);
				return true;
			}

		}
		return false;
	}

	// Función que devuelve una lista ordenada por el atributo Y
	public static ArrayList<Consulta> sortConsultaY(ArrayList<Consulta> consulta) {

		ArrayList<Consulta> miConsulta = consulta;
		Collections.sort(miConsulta, new Comparator<Consulta>() {
			@Override
			public int compare(Consulta c1, Consulta c2) {
				return new Integer(c1.getPosY()).compareTo(new Integer(c2
						.getPosY()));
			}
		});
		return miConsulta;
	}

	// Función que devuelve una lista ordenada por el atributo X

	public static ArrayList<Consulta> sortConsultaX(ArrayList<Consulta> consulta) {

		ArrayList<Consulta> miConsulta = consulta;
		Collections.sort(miConsulta, new Comparator<Consulta>() {
			@Override
			public int compare(Consulta c1, Consulta c2) {
				return new Integer(c1.getPosX()).compareTo(new Integer(c2
						.getPosX()));
			}
		});
		return miConsulta;
	}

	// Función que indica la ubicación del objeto "B" con respecto a "A"
	public static String izqDer(int Ax, int Bx) {
		String resp = "";

		if (Bx > Ax)
			resp = "derecha";
		if (Bx < Ax)
			resp = "izquierda";
		if (Ax == Bx)
			resp = "igual";
		return resp;
	}

	

	//	public static int getTokenX(TanQuery frame, int id) {

	public static int getTokenX(int id,int width,ArrayList<TuioObject> tuioObjectList) {
		for (TuioObject objeto : tuioObjectList) {
			if (objeto.getSymbolID() == id) {
				return objeto.getScreenX(width);
			}
		}
		return -1;
	}

	

	public static int abs(int numero) {
		return numero > 0 ? numero : -numero;
	}

	public static Token construirNodo(Consulta padre) {
		Token miNodo = new Token();
		miNodo.hijoDer = null;
		miNodo.hijoIzq = null;
		miNodo.id = padre.id;
		miNodo.valor = padre.valorSql;//padre.valorDisp;
		miNodo.valorSql = padre.valorSql;
		miNodo.simbolo=padre.simbolo;
		miNodo.sqlStm="";
		return miNodo;
	}

	public static void gestionaConsulta(TanQuery frame) {
		//String querySql = "";
		//querySql = frame.querySQL;
		ejecutaQuery(frame, frame.querySQL);
	}

	public static void addAttribute(TanQuery frame, Consulta t1, Consulta t2) {
		Token hijo = construirNodo(t2);
		Token padre = frame.arbolConsul.getNodo(t1.id);
		String nodoEs=Valores.whatIs(t1.id);


			if (nodoEs.equals("operator") || nodoEs.equals("attribute") || nodoEs.equals("constant")|| nodoEs.equals("comparation")) {
				frame.arbolConsul.insertAttribute(padre, hijo);
			}
	}

	public static Consulta getConsulta(ArrayList<Consulta> consultaArray, int id) {
		for (Consulta consulta : consultaArray) {
			if (consulta.id == id) {
				return consulta;
			}

		}
		return null;

	}

	public static void ejecutaQuery(TanQuery frame, String querySQL) {
		if(querySQL!=""){
			//System.out.println("Ejecutando Consulta Mysql=> " + querySQL);
		}
	}


	/*
	public static ArrayList resultSetToArrayList(ResultSet rs)
			throws SQLException {
		ResultSetMetaData md = (ResultSetMetaData) rs.getMetaData();
		int columns = md.getColumnCount();
		ArrayList results = new ArrayList();

		while (rs.next()) {
			HashMap row = new HashMap();
			results.add(row);

			for (int i = 1; i <= columns; i++) {
				row.put(md.getColumnName(i), rs.getObject(i));
			}
		}
		return results;
	}*/

}
