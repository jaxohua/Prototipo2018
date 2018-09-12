package com.TanQuery;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;

import org.jespxml.excepciones.TagHijoNotFoundException;

import com.agente.Agente;
import com.agente.Pyfo;
import com.agente.Sensor;
import TUIO.TuioBlob;
import TUIO.TuioCursor;
import TUIO.TuioListener;
import TUIO.TuioObject;
import TUIO.TuioTime;


public class TanQuery implements TuioListener{	
	JPanel parent;
	public Sensor sensor;
	public Agente agente;
	
	Configuration getDatos = new Configuration();
	ArrayList<String> datosXml = new ArrayList<String>();
	float counter;
	boolean verbose = true;
	boolean callback = true; // updates only after callbacks

	// Carga de parametros de configuración
	String dataBase;
	String server;// Dirección del servidor BD
	String port;// puerto donde se ejecuta el servicio MySQL
	String user;// usuario MySQL
	String password;// contraseña para usuario MySQL
	public String logpath; // directorio donde se guardaran los logs
	Log log;
	int momento;
	int dispX=-350;
	int dispY=-350;
	public ConectorBD conex;
	ArrayList<Intervalo> intervalos;
	public ArrayList<Token> tokens;
	public static ArrayList<Pyfo> pyfos;
	

	ArrayList<String> dataBases;
	ArrayList<String> relations;
	ArrayList<Operadores> operadores;
	ArrayList<Operadores> oper_comp;
	public ArrayList<TuioObject> ambiente;
	//public ArrayList<Token> tokensActivos; // Array List con tokens que se
											// encuentran
										// dentro del área de trabajo.
	public String query = "AR: ";
	public String queryAR = "";
	public String querySQL = "";
	public String anterior = "";

	public String qTemp = "";
	public String AR = "";
	ArbolConsulta arbolConsul;
	public Token raiz;
	float contador;

	boolean DATABASE = true;
	String DB;
	float distY = 120;
	
	Graphics2D dib;

	public TanQuery() {
		cargarConfig();
		inicializar();
	}

	public void cargarConfig() {
		try {
			datosXml = getDatos.loadXML(); // Obtiene datos de configuracion de archivo configuracion.xml
		} catch (TagHijoNotFoundException e) {
			e.printStackTrace();
		}
		// Asigna valores de configuración obtenidos
		dataBase = "escuela"; // Base de datos principal.
		server = datosXml.get(0); //dirección IP del servidor BD
		port = datosXml.get(1); //puerto del servidor BD
		user = datosXml.get(2); //usuario de servidor BD
		password = datosXml.get(3); //contraseña de acceso a BD
		logpath = datosXml.get(4); //ruta donde se guardan los logs
		
	}
	
	
	public void inicializar() {
				log = new Log();
				this.agente=new Agente(this); //inicia el Agente para obtener los objetos del ambiente
				conex = new ConectorBD(server, port, dataBase, user, password);
				intervalos = new ArrayList<Intervalo>();
	//A partir de 11º  hasta 360º, cada 20 grados
				intervalos = Busquedas.generaIntervalo(11, 360, 20);
				ambiente = new ArrayList<TuioObject>();
				pyfos=new ArrayList<Pyfo>();
				tokens = new ArrayList<Token>();
				//tokensActivos = new ArrayList<Token>();
				relations=new ArrayList<String>();
				dataBases=new ArrayList<String>();
				
				
				Valores.fillRelations(this);
				Valores.fillDatabases(this.dataBases,this.conex);
				
				operadores = Valores.fillOperadores();
				oper_comp = Valores.fillComparison();
				
				arbolConsul = new ArbolConsulta();
				contador = 0;
								
				/* Se asignan Tokens temporales para pruebas rápidas, se puede comentar la linea, 
				pero se tienen que asignar valor a todos los objetos*/
				//Pruebas.test(this);

	}

	public void runTQ(Graphics2D g,JPanel parent) {
			this.dib=g;                 //Recibe el elemento Graphics de Simulation
			this.parent=parent;
			//this.consulta.clear();
			this.pyfos.clear();
			agente.tokens_activos.clear();
			// Limpiar cadenas de consultas
			
			this.query = "";
			this.queryAR = "";
			this.querySQL = "";
			
			if (dataBase != "")
				dib.setFont(new Font("Helvetica", Font.PLAIN, 12));
				dib.drawString("BD:"+dataBase,500,20);
				System.out.println("BD actual: " + dataBase);
			if (this.tokens != null && this.tokens.size() > 0) {
				//dib.drawString("Tokens Agregados:", 700, 70);
				//Valores.showTokens(this);
				//Valores.showListaTokens(tokens);
			}
			//System.out.println("Objetos captados:"+sensor.getAmbiente().size());
	// ** Agente (Sensor) obtiene lista de objetos y lo guarda en sensor.objetos ... return lista de objetos **//
			agente.monitor();
			ambiente=agente.objetos;
			pyfos=agente.pyfos;
			tokens=agente.tokens;

			
			//System.out.println("Ambiente TQ:"+this.ambiente.size()+"  Pyfos TQ:"+this.pyfos.size()+"  Tokens en TQ:"+this.tokens.size());
			System.out.println("AGENTE==> ambiente:"+agente.objetos.size()+"  Pyfos:"+agente.pyfos.size()+"  Tokens:"+agente.tokens.size() + " Tokens_Acti:"+agente.tokens_activos.size());
			
			String valor=null;
			ArrayList<String> lista;
			
			//parent.repaint();
			parent.setVisible(true);

			for (TuioObject objeto : ambiente) {
				Pyfo pyfo=agente.getPyfo(objeto);
				
				if(pyfo!=null) {
					agente.updateTokens(pyfo); //se busca el token que corresponde al pyfo para actualizar ubicación
					
					if(pyfo.tipo.equals("oper_comp") || pyfo.tipo.equals("oper_rel") || pyfo.tipo.equals("relacion") || pyfo.tipo.equals("bd"))  {
				
						int indice = Busquedas.getIndex(this.intervalos, pyfo.angulo);
				//# Valida si el pyfo puede obtener valor sin dependencia de otro pyfo.		
						if (pyfo.tipo.equals("oper_comp")) {
							valor = Valores.getValorOper(oper_comp, indice);
						}
						else if(pyfo.tipo.equals("oper_rel")) {
							valor = Valores.getValorOper(this.operadores, indice);
						}
						else if(pyfo.tipo.equals("relacion")) {
							valor = Valores.getValor(this.relations, indice);					
						}
						else if(pyfo.tipo.equals("bd")) {
							valor = Valores.getValor(this.dataBases, indice);					
						}
						if(valor!=null) {
							agente.agregarToken(pyfo,valor);
							System.out.println("token agregado:"+pyfo.id);
						}
					}
				
					
			// valida si pyfo es de tipo atributo o valor (necesitan token relacion y atributo respectivamente
			//para poder obtener un valor)
					else if(pyfo.tipo.equals("atributo") || pyfo.tipo.equals("valor")){
							Token tokenTocado = agente.buscarToqueValido(pyfo);
						// 4x	System.out.println("Token Tocado:"+tokenTocado.valor);
							if(tokenTocado!=null) {  // 4x ** revisar validación del token
								int indice = Busquedas.getIndex(this.intervalos, pyfo.angulo);
								if(pyfo.tipo.equals("atributo")) {
									String relacion=tokenTocado.valor;
									lista=Valores.getValores(conex, "SHOW COLUMNS FROM "+ this.dataBase + "." + relacion, "Field");
									valor = Valores.getValor(lista, indice);
									valor=relacion+"."+valor;
								}
								else if(pyfo.tipo.equals("valor")) {
									String atributo=tokenTocado.valor;
									String[] valores = atributo.split("\\.");
									String query="SELECT "+ valores[1] + " FROM " + valores[0];
									lista=Valores.getValores(conex, query,valores[1]);
									valor = Valores.getValor(lista, indice);	
								}
								if(valor!=null) {
									agente.agregarToken(pyfo,valor);
									Token agregado = agente.getToken(pyfo.id);
									System.out.println("Token agregado ==> "+agregado.id+":"+agregado.valor);
								}
							}
// 4x 							System.out.println("VALOR*********" + valor); //imprime valor asignado
							
					}
					
				}
				else {
					//si el objeto identificado no esta asociado a un pyfo
					System.out.println("Null para:"+objeto.getSymbolID());
				}
			}//Fin del recorrido de objetos en ambiente
			agente.relacionesTokens();
			agente.crearTokensActivos();
			
			
			pintaTokenVal();

			String listAmbiente="";
			String listPyfos="";
			String listTokens="";
			String listTokensActivos="";
		//	System.out.println("AGENTE==> ambiente:"+agente.objetos.size()+"  Pyfos:"+agente.pyfos.size()+"  Tokens:"+agente.tokens.size() + " Tokens_Acti:"+agente.tokens_activos.size());

			for (TuioObject objeto : ambiente) {
				listAmbiente+=objeto.getSymbolID()+",";
			}
			for (Pyfo pyfo : pyfos) {
				listPyfos+=pyfo.id+":"+pyfo.tipo+",";
			}
			for (Token token : tokens) {
				listTokens+=token.valor+",";
			}
			for (Token token : agente.tokens_activos) {
				listTokensActivos+=token.valor+",";
			}
			
			
			System.out.println("Ambiente:"+listAmbiente);
			System.out.println("Pyfos:"+listPyfos);
			System.out.println("Tokens:"+listTokens);
			System.out.println("Tokens-Activos:"+listTokensActivos);
			
			/*System.out.println("Ambiente:"+ambiente);
			System.out.println("Pyfos:"+pyfos);
			System.out.println("Tokens:"+tokens);
			System.out.println("Tokens-Activos:"+agente.tokens_activos);*/
			
			
			if(agente.tokens_activos.size()>0) {
				// 4x operaciones con Arbol de consulta
		//## 4x revisar la creación del árbol para que se actualicen los valores de los nodos.  		
				crear_arbol();
				System.out.println("Contenido del arbol");
				arbolConsul.recorridoLog(this);
				System.out.println("---Consulta AR---");
				arbolConsul.recorridoAlgebraRelacional(this);
				System.out.println(queryAR);
				System.out.println("---Consulta SQL:---");
				arbolConsul.recorridoSQL(this);
				System.out.println("SQL Query:" + querySQL);
				conexiones();
				pintaQuerys();
			}
			if (pyfos.size() == 0) {
				this.arbolConsul.setRaiz(null);
			}
			
			//Se imprimen las cadenas de consulta generadas por el árbol.
			//System.out.println("AR Query:" + queryAR);1
			//System.out.println("SQL Query:" + querySQL);
			
	}//Fin método runTQ
	
	public void pintaQuerys() {
		dib.setPaint(Color.WHITE);
		dib.drawString("AR Query:" + queryAR, 20, 40);
		dib.setPaint(Color.YELLOW);
		dib.drawString("SQL Query:" + querySQL, 20, 70);
	}
	

	
	
	
	private void pintaTokenVal() {
		String listTokens="";

		for (Token token : tokens) {
			dib.setPaint(Color.WHITE);
			
			listTokens += token.id + ":" + token.tipo + ":" + token.valor + ", ";
			//if(token.valor!=null && agente.buscaObjeto(token.id)) {
			if(token.valor!=null && agente.buscaObjeto(token.id)) {
				int menos=token.valor.length();
				if(token.tipo.equals("atributo")) {
					String[] attribute = token.valor.split("\\.");
					String valor=attribute[1];
					menos=valor.length();
					dib.drawString(valor, (float)(token.xPos-(menos/2)*8), (float)token.yPos+13);
				}
				
				else if(token.tipo.equals("oper_rel")) {
					dib.setPaint(Color.RED);
					dib.drawString(token.valorSql, (float)(token.xPos-(menos/2)*8), (float)token.yPos+13);
				}
				else{
					dib.drawString(token.valor, (float)(token.xPos-(menos/2)*8), (float)token.yPos+13);
				
				}
				//System.out.println("Impresion de valor de token:"+token.id+" "+token.valor);	
			}
		}
		//System.out.println("Tokens:"+listTokens);
	}
	
	public  int getPosicionX(int id) {
		// TODO Auto-generated method stub
		for (Pyfo pyfo : pyfos) {
			if (pyfo.id == id) {
				return (int)pyfo.xPos;
			}
		}
		return -1;
	}
	public  int getPosicionY(int id) {
		// TODO Auto-generated method stub
		for (Pyfo pyfo : pyfos) {
			if (pyfo.id == id) {
				return (int) pyfo.yPos;
			}
		}
		return -1;
	}
	
	public synchronized void conexiones() {
		dibujaL(arbolConsul.getRaiz());
	}

	public void dibujaL(Token nodo) { // dibuja la linea de padre
														// a hijo
		int radio = 30;
		
		if (nodo == null)
			return;

		if (nodo.hijoIzq != null) {
			// obtener posición de padre e hijo izquierdo
			
			int fx = getPosicionX(nodo.id);
			int fy = getPosicionY(nodo.id);
			int sx = getPosicionX(nodo.hijoIzq.id);
			int sy = getPosicionY(nodo.hijoIzq.id);
			if(fx!=-1 && fy!=-1 && sx !=-1 && sy!=-1)
				dib.drawLine(fx, fy+radio, sx, sy-radio);
			dibujaL( nodo.hijoIzq);

		}
		if (nodo.hijoDer != null) {
			// obtener posición de padre e hijo Derecho
			int fx = getPosicionX(nodo.id);
			int fy = getPosicionY(nodo.id);
			int sx = getPosicionX(nodo.hijoDer.id);
			int sy = getPosicionY(nodo.hijoDer.id);
			if(fx!=-1 && fy!=-1 && sx !=-1 && sy!=-1)
				dib.drawLine(fx, fy+radio, sx, sy-radio);
			dibujaL( nodo.hijoDer);
		}

	}
	
	
	public void imprimeTipoPyfo(Pyfo py) {
		// draw the object id
					dib.setPaint(Color.white);
					String tipo=py.tipo;
					if(tipo!="") {
						//dib.drawString(tipo+"\n"+py.id,py.xPos-12,py.yPos+12);
					}
					else {
						//dib.drawString(tipo+"\n"+py.id,py.xPos-12,py.yPos+12);
					}
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
	
	private void crear_arbol() {
		//Consulta.crearArbol(this);
		//ordenar tokens por yPos, obtener el que está más arriba "nueva_raiz"
		// si arbol está vacio definir nueva raiz como raiz.
		// si arbol ya tiene raiz "old_raiz" comparar con "nueva_raiz" si != set(nuevaraiz.hijoIz(old_raiz)
		
		Token newRoot = getTop(agente.tokens_activos);
		Token temporal = new Token();
		Token nodoEncontrado = new Token();
		Token nodoPadre = new Token();
		String ubicacion = "";

		
		if (this.arbolConsul.getRaiz() == null ) { // si el arbol esta vacio o el valor de la raiz es diferente
			this.arbolConsul.setRaiz(newRoot);
		}
		else if(newRoot.valor != arbolConsul.getRaiz().valor && newRoot.id == arbolConsul.getRaiz().id){
			this.arbolConsul.setRaiz(newRoot);
		}
		else {
			if (newRoot.valor != arbolConsul.getRaiz().valor && newRoot.tipo == "oper_rel") {
			//if (newRoot.valor != arbolConsul.getRaiz().valor || newRoot.tipo == "oper_rel") {	
				
				temporal = arbolConsul.getRaiz(); // copiar raiz actual a nodo temporal
				arbolConsul.setRaiz(newRoot); // el nuevo nodo es insertado en la raíz del arbol
				
				// Si el valor de la nueva raiz es diferente al que tiene la raíz
				// actual y es de tipo operador, se realiza el cambio de raiz
				if (Token.buscar_en_lista(agente.tokens_activos, temporal) && temporal.id != newRoot.id) {
					ubicacion = temporal.getUbicacion(newRoot, temporal);
					System.out.println("Nuevo Valor de raíz=>" + arbolConsul.getRaiz().valor);

					if (ubicacion == "izquierda") {// Si el nodo temporal está a la izquierda, se fija como hijoIzquierdo de la
													// nueva raiz
						arbolConsul.getRaiz().hijoIzq = temporal;
						temporal.padre = arbolConsul.getRaiz();
					}

					if (ubicacion == "derecha") {
						// Si el nodo temporal está a la derecha, se fija como hijoDerecho de la nueva raiz
						arbolConsul.getRaiz().hijoDer = temporal;
						temporal.padre = arbolConsul.getRaiz();
					}

				} 
				else {
					
				}
				
				
				
			}
			else {
				// 4x retroalimentación 
			}
			
		}
		
		for (Token t1 : agente.tokens_activos) {
			t1.printTokensTocados();
			if(t1.puedeSerPadre() || t1.tieneAtributos()) {
				for (Token t2 : t1.tokensTocados) {
					if(t1.puedeSerPadre() ){	//agrega hijos al operador relacional
						if(t2.tipo.equals("oper_rel") || t2.tipo.equals("relacion")){
								ubicacion = t1.getUbicacion(t1, t2);// retrieve t2 position refer to t1
								temporal = t2;
								nodoPadre = arbolConsul.getNodo(t1.id);
								nodoEncontrado = arbolConsul.getNodo(temporal.id);
								if (nodoEncontrado != null) {
									arbolConsul.removeNodo(nodoEncontrado.id); //if son node is already in tree then delete
								}
	
								if(nodoPadre!=null){
									arbolConsul.insertarNodo(temporal, ubicacion,
										nodoPadre);
								}
						}
					} // fin de asignar hijo o atributos a operador relacional
					
					if(t1.tieneAtributos()) {
						if (t2.tipo.equals("atributo") || t2.tipo.equals("valor") || t2.tipo.equals("oper_comp")) {
							ubicacion = t1.getUbicacion(t1, t2);// retrieve t2 position refer to t1
							if(ubicacion.equals("derecha")) {	
								nodoEncontrado = arbolConsul.getNodo(t2.id);
								nodoPadre=arbolConsul.getNodo(t1.id);
								if (nodoEncontrado != null) {
									arbolConsul.removeNodo(nodoEncontrado.id);
								}
								// frame.arbolConsul.insertAttribute(t1, t2);
								if(nodoPadre!=null){
									addAttribute(t1, t2);
								}
							}
						}
					}
				}// for t2
			}//Fin if puedeSerPadre
		}//For t1
		
		
	}//Fin Método crear árbol
	
	
	
	public void addAttribute(Token t1, Token t2) { //  4x Eliminar atributos cuando dejan de tocarse.
		Token hijo = t2;
		Token padre = arbolConsul.getNodo(t1.id);
		String nodoEs = t2.tipo;
			if (nodoEs.equals("oper_com") || nodoEs.equals("atributo") ) {
				arbolConsul.insertAttribute(padre, hijo);
			}
			
	}
	
	public static Token getTop(ArrayList<Token> tokens) {
		Collections.sort(tokens, new Comparator<Token>() {
			@Override
			public int compare(Token t1, Token t2) {
				int t1Ypos=(int) t1.yPos;
				int t2Ypos=(int) t2.yPos;
				return new Integer(t1Ypos).compareTo(new Integer(t2Ypos));
			}
		});
		return tokens.get(0);
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
		parent.repaint();

		System.out.println(" ####### objeto eliminado..quitando del arbol"+arg0.getSymbolID()+"  ############");
		Token nodo=arbolConsul.getNodo(arg0.getSymbolID()); //Busca si existe dentro del árbol
		if (nodo!=null) { // si lo encuentra, lo elimina
			if(arbolConsul.removeNodo(nodo.id)) {  //si fue posible eliminarlo
				System.out.println("Nodo:"+nodo.valor+" ELIMINADO");
			}
			else {
				System.out.println("ERROR al eliminar el Nodo:"+nodo.valor);
			}
		}
		else {
			System.out.println("El elemento:"+ arg0.getSymbolID()+" No estaba en el árbol");
		}
		System.out.println("######## removeObjetctContenido del arbol:###################");
		arbolConsul.recorridoLog(this);
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