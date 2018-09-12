package com.TanQuery;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import TUIO.TuioObject;

public class Valores {

	public static void getListas() {

	}

	public final static ArrayList<String> getValores(ConectorBD conex,
			String query, String atributo) {
	// 4x	System.out.println("getValores for " + query + " atributo " + atributo);

		ArrayList<String> valores = new ArrayList<String>();
// 4x		System.out.println("getValores=>" + query);
		try {
			conex.setSt(conex.con.createStatement());
			conex.setRs(conex.getSt().executeQuery(query));
			while (conex.getRs().next()) {
				valores.add(conex.getRs().getString(atributo));
			}
			for (String valor : valores) {
	// 4x 			System.out.println(valor);  // imprime lista de calores obtenidos de la consulta
			}

			return valores;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

	public static boolean fillDatabases(ArrayList<String> dataBases, ConectorBD conex) {
		ArrayList<String> listaVal;
		listaVal = getValores(conex, "show databases", "Database");
	//	System.out.println("Obteniendo Bases de Datos");
		for (String valor : listaVal) {
			dataBases.add(valor);
		}

		if (dataBases.size() > 0)
			return true;
		return false;

	}

	public static boolean fillRelations(TanQuery tq) {
		// Cuando se tiene la base de datos, se rellena la lista con sus tablas
		ArrayList<String> listaVal;
		if (tq.DATABASE) {

			listaVal = getValores(tq.conex, "show tables from "
					+ tq.dataBase, "Tables_in_" + tq.dataBase);
			//System.out.println("Obteniendo Tablas de " + frame.dataBase);

			if (tq.relations.size() > 0) {
				tq.relations.clear();
			}
			for (String valor : listaVal) {
				tq.relations.add(valor);
			}
			if (tq.relations.size() > 0) {
				System.out.println("Tablas Generadas :)  de la Base de Datos "
						+ tq.dataBase);
				return true;

			}

		}
		return false;

	}

	public static void setValor(TanQuery frame, TuioObject objeto) {
		int id = objeto.getSymbolID();
		int indice = -1;
		ArrayList<String> lista = null;
		ArrayList<Operadores> listaOper = null;
		String valor = null;
		String tipo = whatIs(id);
		String nomRelation = "";
		String nomAttribute = "";
		System.out.println("TIPO*********" + tipo);

		if (tipo.equals("comparation")) {
			listaOper = frame.oper_comp;
			indice = Busquedas.getIndex(frame.intervalos,
					objeto.getAngleDegrees());
			valor = Valores.getValorOper(listaOper, indice);
			System.out.println("VALOR*********" + valor);
		}

		if (tipo == "DB") {
			// frame.text("Seleccionar Base Datos:", 5, 30);
			Display.mensaje(frame, "Selecciona una Base de Datos:",
					"asignación");
			lista = frame.dataBases;
			// verifyDB(frame);
		} else if (tipo == "relation") {
			if (frame.DATABASE) {
				if (!Busquedas.isToSave(frame)) {
					frame.fill(255, 0, 0);
					// frame.text( "Gira los objetos para cambiar su valor.",
					// 10,340);
				}
				lista = frame.relations;
			} else {
				frame.fill(255, 0, 0);
				Display.mensaje(frame,
						"Debes seleccionar primero una Base de datos.",
						"asignación");
				return;
			}

		} else if (tipo == "attribute") {
			// busca si esta un objeto del tipo "relation" dentro del espacio de
			// modificación
			int idRelation = Busquedas.buscaId(frame, "relation");
			if (idRelation != -1) {
				nomRelation = getValorToken(frame, idRelation);//obtiene el nombre de la relación
				if (nomRelation != null) { //si el nombre de la relación es diferente a null
					frame.fill(255, 0, 0);
					// frame.text("Selecciona una relacion:", 5, 150);
					lista = getValores(frame.conex, "show columns from "
							+ frame.dataBase + "." + nomRelation, "Field");//obtiene la lista de valores de los atributos que contiene una relación

				}
			} else {

				frame.text(
						"Coloca la relación de la que requieres los atributos",
						130, 45);
			}

		} else if (tipo == "operator") {
			listaOper = frame.operadores;
			indice = Busquedas.getIndex(frame.intervalos,
					objeto.getAngleDegrees());
			valor = Valores.getValorOper(listaOper, indice);
			// setValorOperador(frame, objeto);
		} else if (tipo == "constant") {
			// busca si esta un objeto del tipo "attribute" dentro del espacio
			// de
			// modificación
			int idAttribute = Busquedas.buscaId(frame, "attribute");//obtiene el id del atributo que se encuentre en el área de asignación

			if (idAttribute != -1) {
				//String rel = getValorToken(frame.tokens, idAttribute);
				String rel = getValorToken(frame, idAttribute);
				String[] relation = rel.split("\\.");
				System.out.println("Longitud de relation:= " + relation.length);
				nomAttribute = getValorToken(frame, idAttribute);
				if (nomAttribute != null) {
					Display.mensajeXY(frame,
							"Selecciona el valor del attributo", 30, 270);
					// frame.text("Selecciona una relacion:", 5, 50);
					//relation[1]=nombre del atributo
					//relation[0]=nombre de la relacion
					String query = "select " + relation[1] + " from "
							+ relation[0];
					System.out.println(query);
					lista = getValores(frame.conex, query, relation[1]);
					// lista = getValores(frame.conex,
					// "select "+nomAttribute+" from "+relation[0]
					// + frame.dataBase + "." + nomAttribute, nomAttribute);
				}
			} else {
				frame.fill(30, 240, 35);
				frame.text(
						"Coloca dentro del recuadro el attributo del que requieres los valores",
						30, 240);
			}

		}

		if ((tipo != "operator") && (tipo != "comparation")) {
			indice = Busquedas.getIndex(frame.intervalos,
					objeto.getAngleDegrees());
			valor = Valores.getValor(lista, indice);
		}

		if (valor != null) {
			System.out.println("Valor seleccionado:" + valor + " para Objeto:"
					+ objeto.getSymbolID());
			if (frame.tokens.size() > 0) {
				String val = getValorToken(frame, objeto.getSymbolID());
				if (val != null) {
					if (tipo.equals("relation")) {
						frame.fill(0, 153, 0);
						// frame.text("Valor actual= "+val, 150, 55);
					}
					if (tipo.equals("attribute")) {
						frame.fill(51, 51, 255); // Attribute
						// frame.text("Valor actual= "+val, 10, 75);
					}
				}

			}
			frame.fill(255, 255, 153);
			frame.textSize(12);
			frame.noFill();// Sin relleno
			frame.ellipse(objeto.getScreenX(frame.width) - valor.length() / 2,
					objeto.getScreenY(frame.height) + 30, 100, 100);
			int tRango = Busquedas.objectsInRange(frame);
			System.out.println("Objetos en Rango: " + tRango);
			if (tipo == "DB" || tipo == "relation" || tipo == "operator"
					|| tipo == "comparation") {
				if (tipo == "DB") {
					limpiarListas(frame);
				}
				saveValue(frame, objeto, valor);
				System.out.println("DB,Relation,Operator");
			}
			else{
				if (tipo.equals("attribute")){
					saveValue(frame, objeto, nomRelation + "." + valor);
					System.out.println("Attributo guardado");
				}
				if(tipo.equals("constant")) {
					saveValue(frame, objeto, valor);
					System.out.println("Constante guardado");
				}
			}


//				else {
//				if (tipo.equals("attribute") || tipo.equals("constant")) {
//					if (tRango == 2) {
//						// saveValue(frame, objeto, nomRelation + "." + valor);
//						saveValue(frame, objeto, valor);
//						System.out.println("Attribute guardado");
//					}
//				}
//			}

		} else {
			System.out.println("Valor nulo");
			frame.fill(255, 0, 0);
			frame.textFont(frame.font, 14);
			frame.text("Este Objeto no tiene valor asignado.", 220, 30);
		}
	}

	private static void saveValue(TanQuery frame, TuioObject objeto,
			String valor) {
		int id = objeto.getSymbolID();
		String tipo = whatIs(id);
		System.out.println("Guardado valor de token...");
		System.out.println("SetValor " + valor + "para Objeto tipo " + tipo
				+ " id=" + id);
		if (Busquedas.isInToken(frame.tokens, id)) {
			if (!Valores.borraToken(frame, id)) {
				System.out.println("no se pudo borrar el token de " + tipo
						+ " id:" + id);
			}
		}
		if (tipo == "operator") {
			showOperators(frame.operadores);
			frame.tokens.add(new Token(id, tipo, getSimOper(frame.operadores,
					valor), valor, -1, -1, 0, objeto.getSessionID()));
		}

		else {
			frame.tokens.add(new Token(id, tipo, valor, valor, -1, -1, 0,
					objeto.getSessionID()));
		}
		frame.fill(0, 153, 0); // Verde
		frame.textFont(frame.font, 18);
		// frame.text("SetValor " + valor + " para Objeto tipo:" + tipo + " id="
		// + id, 220, 30);
		frame.text("¡Guardado exitoso!", 220, 30);

		if (tipo == "DB") {
			frame.DATABASE = true;
			frame.dataBase = valor;
			//frame.conex = new DBConnect("localhost", 3306, frame.dataBase,
				//	"tanquery", "$tanquery$");
			fillRelations(frame);
		}

	}

	private static void showOperators(ArrayList<Operadores> operadores) {
		System.out.println("Lista de Operadores: ");
		for (Operadores operadores2 : operadores) {
			System.out.println("Nombre: " + operadores2.getNombre()
					+ " simbolo: " + operadores2.getSimbolo() + " Tipo: "
					+ operadores2.getTipo());
		}
	}

	public static String getSimOper(ArrayList<Operadores> operadores,
			String valor) {
		String simbolo = "";// 4x
		for (Operadores oper : operadores) {
			if (oper.getNombre() == valor) {
				return oper.getSimbolo();
			}
		}
		return simbolo;

	}

	public static String whatIs(int id) {// return pyfo type 
		if (id == 1)
			return "DB";

		if (id >= 6 && id <= 15)// green
			return "relation";

		if (id >= 16 && id <= 25)// blue
			return "attribute";

		if (id >= 26 && id <= 35)// brown
			return "constant";
		
		if (id >= 36 && id <= 45)// yellow (relational operators)
			return "operator";
		
		if (id >= 46 && id <= 55)// (<,<=,>,>=;==;!=)
			return "comparation";

		return null;
	}

	@SuppressWarnings({ "unused", "static-access" })
	public static void showToken(TanQuery frame, TuioObject objeto) {
		// muestra los valores de los Tokens cuando estan fuera del area de asignación
		int id = objeto.getSymbolID();
		int objetoX = objeto.getScreenX(frame.width);
		int objetoY = objeto.getScreenY(frame.height);
		
		float fobjetoX = objeto.getX();
		float fobjetoY = objeto.getY();
		
		ArrayList<Token> lista = frame.tokens;
		String valor = null;
		String tipo = null;
		String descrip = "";

		try {
			if(onList(id,lista)){
				valor = Valores.getValorToken(lista, id);
				tipo = Valores.getToken(id, lista).getTipo();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (valor != null) {
			// System.out.println("Valor encontrado:" + valor + " para Objeto:"
			 // + objeto.getSymbolID());

			frame.fill(255, 0, 0);

			//Display.showValor(frame, valor, fobjetoX*1000, fobjetoY*1000, tipo);
			Display.showValor(frame, valor, objetoX, objetoY, tipo);
			//System.out.println("getX:"+fobjetoX+" getY:"+fobjetoY);
			//System.out.println("X:"+objetoX+" Y:"+objetoY);
			frame.ellipseMode(frame.CORNER);
			frame.fill(frame.random(255), 20, 50, 2);// these are the colors
			//Display.dibujaElipse(frame, objetoX, objetoY, tipo);
		}

	}

	public static String getValorToken(TanQuery frame, int id) {
		ArrayList<Token> listaTokens = frame.tokens;
		// String valor = null;

		if (listaTokens.size() > 0) {
			for (Token token : listaTokens) {
				if (token.getId() == id) {
					return token.getValorDisp();
				}
			}
		}

		return null;
	}

	public static String getValor(ArrayList<String> valores, int indice) {
		// System.out.println("indice:" + indice + " Tamaño valores: "
		// + valores.size());

		if ((indice != -1) && (valores != null) && valores.size() > 0) {
			if (indice < valores.size()) {
				return valores.get(indice);
			} else {
				int cont = 0;
				for (int i = 0; cont <= indice; i++) {
					if (i >= valores.size()) {
						i = 0;
					}
					if (cont == indice) {
						System.out.println("Tamaño de la lista de valores: "
								+ valores.size());
						System.out.println("valores en posicion:" + i);
						return valores.get(i);
					}
					cont++;
				}
			}
		}

		return null;

	}

	public static String getValorOper(ArrayList<Operadores> operadores,
			int indice) {
		System.out.println("indice:" + indice + " Tamaño valores: "
				+ operadores.size());

		if (indice != -1) {
			if (indice < operadores.size()) {
				return operadores.get(indice).getNombre();
			} else {
				int cont = 0;
				for (int i = 0; cont <= indice; i++) {
					if (i >= operadores.size()) {
						i = 0;
					}
					if (cont == indice) {
						System.out.println("valores en posicion:" + i);
						return operadores.get(i).getNombre();
					}
					cont++;
				}
			}
		}
		return null;

	}

	public static String getValorToken(ArrayList<Token> valores, int id) {
		String valor="";
		if (valores.size() > 0) {
			for (Token token : valores) {
				if (token.id == id) {
					valor=token.valor;
					// System.out.println("getValorToken: encontrado " +valor);
					if(token.tipo.equals("atributo")){
						//String rel = getValorToken(frame.tokens, idAttribute);
						String[] attribute = valor.split("\\.");
						valor=attribute[1];
						System.out.println("El valor del atributo es: "+ attribute[1]+" relación:"+attribute[0]);
					}
						return valor; // 4x
					// return token.getValorSql();
				}
			}
			System.out.println("no se encontro Valor en Lista de Tokens");
		}
		return null;
	}

	public static boolean borraToken(TanQuery frame, int id) {// Remueve un
																// token de la
																// lista
		if (frame.tokens.size() > 0) {
			for (Token token : frame.tokens) {
				if (token.getId() == id) {
					frame.tokens.remove(token);
					return true;
				}
			}
		}

		return false;

	}

	public static ArrayList<Operadores> fillOperadores() {// Llena el contenido
															// de la lista de
															// operadores con su
															// descripción
		ArrayList<Operadores> operadores = new ArrayList<Operadores>();
		operadores
				.add(new Operadores(
						"SELECCION",
						"Selección(Select): Este operador se aplica "
								+ "a una relación R produciendo una nueva relación con un subconjunto de tuplas de R."
								+ " Las tuplas de la relación resultante son las que satisfacen una condición C sobre algún atributo de R. "
								+ "Es decir selecciona filas (tuplas) de una tabla según un cierto criterio C."
								+ " El criterio C es una expresión condicional, similar a las declaraciones del tipo “if”, "
								+ "es “booleana” esto quiere decir que para cada tupla de R toma el valor Verdad(true) o Falso(false)."
								+"\n\nNotación en Álgebra Relacional: Para representar Select en álgebra relacional se utiliza la letra griega sigma σ. "
								+ "Por lo tanto, si utilizamos la notación σc R queremos decir que se aplica la condición C a cada tupla de R",
						"\u03C3", "unario"));

		operadores
				.add(new Operadores(
						"PROYECCION",
						"Proyección(Project): Se utiliza para producir una nueva relación desde R. Esta nueva relación contiene sólo algunos de "
						+ "los atributos de R, es decir, realiza la selección de algunas de las columnas de una tabla R."
								+ "\n\nNotación en Álgebra Relacional Project en Álgebra Relacional se representa por la letra griega pi: \n"
								+ "π(A1,...,An)R",
						"\u03C0", "unario"));

		operadores
				.add(new Operadores(
						"PRODUCTO CRUZADO",
						"Producto Carteciano(Cross-product): Define una relación que es la concatenación de cada una de las filas de la relación R con "
						+"cada una de las filas de la relación S.\n\nNotación en Álgebra Relacional:Para representar Cross-product en Álgebra Relacional se utiliza"
						+ " la siguiente terminología:\n    R×S",
						"X", "binario"));
		operadores
				.add(new Operadores(
						"JOIN",
						"Join: Define una relación que contiene las tuplas que satisfacen el predicado CC en el Cross-Product de R×S. "
						+ "Conecta relaciones cuando los valores de determinadas columnas tienen una interrelación específica. "
						+ "La condición C es de la forma R.ai <operador_de_comparación> S.bi, esta condición es del mismo tipo que se utiliza Select."
						+ " El predicado no tiene por que definirse sobre atributos comunes"
						+ "\n\n La notación de ThetaJoin es el mismo símbolo que se utiliza para NaturalJoin, la diferencia radica en que ThetaJoin lleva "
						+ "el predicado C: \n R \u2A1DC S",
						"\u2A1D", "binario"));
		
		operadores
		.add(new Operadores(
				"SEMI-JOIN",
				"SEMI-JOIN: Un semi-join entre dos relaciones, devuelve las filas de la primera relación donde una o más coincidencias son encontradas en la segunda relación,"+
				" la diferencia entre un SEMI-JOIN y un JOIN convencional, es que las filas de la primera relación solo aparecen una sola vez. Este operador es utilizado para reducir el costo de la comunicación en sistemas de bases de datos distribuidos"
				,"\u22C9", "binario"));
		
		operadores
				.add(new Operadores(
						"UNION",
						"Unión: La unión de dos relaciones R y S, es otra relación que contiene las tuplas que están en R, o en S, o en ambas, eliminándose las "
						+ "tuplas duplicadas. R y S deben ser unión-compatible, es decir, definidas sobre el mismo conjunto de atributo "
						+ "(R y S deben tener esquemas idénticos. Deben poseer las mismas columnas y su orden debe ser el mismo)."
						+ "\n\nNotación en álgebra relacional \n R∪S","\u222A", "binario"));
		operadores
				.add(new Operadores(
						"DIFERENCIA",
						"Diferencia: La diferencia de dos relaciones RR y SS, es otra relación que contiene las tuplas que están en la relación R, pero no están en S. "
						+ "RR y SS deben ser unión-compatible (deben tener esquemas idénticos).\n\nNotación en álgebra relacional \n R−S "
						+ "\nEs importante resaltar que R−S es diferente a S−R.",
						"-", "binario"));

		return operadores;
	}

	public static ArrayList<Operadores> fillComparison() {
		// Defining comparison operators
		ArrayList<Operadores> comparison = new ArrayList<Operadores>();
		comparison.add(new Operadores(">", "Mayor que", ">", ""));
		comparison.add(new Operadores(">=", "Mayor o igual que", ">=", ""));
		comparison.add(new Operadores("<", "Menor que", "<", ""));
		comparison.add(new Operadores("<=", "Menor ó igual que", "<=", ""));
		comparison.add(new Operadores("=", "Igual que", "=", ""));
		comparison.add(new Operadores("!=", "Diferente ó no es igual que",
				"!=", ""));
		return comparison;
	}
	public static ArrayList<String> fillRelation() {
		return null;
		
	}
	public static ArrayList<String> fillDatabases() {
		re
	}

	public static void saveTokens(ArrayList<Token> tokens) {// Guardar Tokens en
															// un archivo
		FileWriter fichero = null;
		PrintWriter pw = null;
		try {
			fichero = new FileWriter("data/tokens.txt");
			pw = new PrintWriter(fichero);

			for (Token token : tokens) {
				pw.println("ID: " + token.getId());
				pw.println("Tipo: " + token.getTipo());
				pw.println("Valor: " + token.getValorDisp());
				pw.println("SQL: " + token.getValorSql());
				pw.println("Izq: " + token.getIzq());
				pw.println("Der: " + token.getDer());
				pw.println("Padre: " + token.getPadre());
				pw.println("IDSESION: " + token.getIdSesion());
				pw.println("");
				pw.println("");
				pw.println("__________________________________________________________");

				System.out.println("ID: " + token.getId());
				System.out.println("Tipo: " + token.getTipo());
				System.out.println("Valor: " + token.getValorDisp());
				System.out.println("SQL: " + token.getValorSql());
				System.out.println("Izq: " + token.getIzq());
				System.out.println("Der: " + token.getDer());
				System.out.println("Padre: " + token.getPadre());
				System.out.println("IDSESION: " + token.getIdSesion());
				System.out.println("");
				System.out.println("");
				System.out
						.println("__________________________________________________________");

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// Nuevamente aprovechamos el finally para
				// asegurarnos que se cierra el fichero.
				if (null != fichero)
					fichero.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	@SuppressWarnings("unused")
	public static void showTokens(TanQuery frame) {// Muestra en pantalla el
													// contenido de los tokens
													// que se han agregado
		int y = 0;
		int id;
		String tipo;
		String valor, valor2;
		/*
		 * frame.textFont(frame.font, 13); frame.text("Tokens Agregados:", 700,
		 * 70); frame.textFont(frame.font, 10);
		 */

		for (Token token : frame.tokens) {
			id = token.getId();
			tipo = token.getTipo();
			valor = token.getValorDisp();
			valor2 = token.getValorSql();

			switch (tipo) {
			case "relation":
				frame.fill(0, 153, 0);
				break;
			case "attribute":
				frame.fill(51, 51, 255); // Attribute
				break;
			case "constant":
				frame.fill(255,255,0);// Constant
//				frame.fill(139, 69, 19);// Constant

				break;

			case "operator":
				frame.fill(255, 255, 0);// Operator
				break;
			case "comparation":
				frame.fill(255, 0, 0);// Comparation
				break;

			}

			// *SE IMPRIMEN LOS TOQUENS QUE SE VAN A GREGANDO
			// frame.rect(540, 95+y, 5,5);
			// frame.fill(255,255,255);
			// frame.text("id: " + id + " tipo: " + tipo + " valorDisp: " +
			// valor+" ValorSql: "+valor2,
			// 550, 100 + y);
			y += 20;

		}

	}

	public static void clearConsulta(TanQuery frame) {
		frame.consulta.clear();
	}

	public static Token getToken(int id, ArrayList<Token> tokens) {
		//System.out.println("Se quiere mostrar el Token: " + id
		//		+ "Tamaño de lista:" + tokens.size());
		Token object = null;

		for (Token objeto : tokens) {
			if (id == objeto.getId()) {
				return objeto;
			}

		}
		return object;

	}

	//Este método valida que un id se encuentre dentro de la lista de tokens.
	public static boolean onList(int id, ArrayList<Token> tokens){
		if (tokens!=null){
			for(Token objeto:tokens){
				if(id==objeto.getId()){
					return true;
				}
			}
		}
		return false;
	}

	public static String getDescOper(ArrayList<Operadores> oper, String valor) {

		for (Operadores operador : oper) {
			if (operador.getSimbolo().equals(valor)) {
				return operador.getDescripcion();
			}
		}
		return "vacio";
	}
	public static String showListaTokens(ArrayList<Token> tokens) {

		for (Token token : tokens) {
			System.out.println("Token.ValorDip: "+token.getValorDisp());
			System.out.println("Token.valorSql: "+token.getValorSql());
		}
		return "vacio";
	}


	public static void limpiarListas(TanQuery frame) {
		// frame.dataBases.clear();
		frame.relations.clear();
		frame.tokens.clear();
	}
}
