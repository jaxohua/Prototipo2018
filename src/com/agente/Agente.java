package com.agente;

import java.util.ArrayList;

import com.TanQuery.Token;

import TUIO.TuioListener;
import TUIO.TuioObject;

public class Agente {
	
	public Sensor sensor;
	public Actuador actuador;
	public ArrayList<TuioObject> objetos;
	public ArrayList<Pyfo> pyfos;
	public static ArrayList<Token> tokens;
	public  ArrayList<Token> tokens_activos;
	public static int umbral_toque=68;
	
	
	public Agente(TuioListener recep) {
		this.sensor=new Sensor(recep);
		this.actuador=new Actuador();
		this.pyfos=new ArrayList<Pyfo>();
		this.objetos=sensor.getAmbiente();
		this.tokens=new ArrayList<Token>();
		this.tokens_activos=new ArrayList<Token>();
		
		
		
	}
// ## Método que obtiene los objetos del ambiente, los convierte en pyfos y busca relaciones entre ellos
	public void monitor() {
		this.objetos=sensor.getAmbiente();
		System.out.println("Objetos captados:"+objetos.size());
		if(objetos.size()>0) {
			crear_pyfos(objetos); //
			relacionesPyfos(pyfos);	
			//relacionesTokens();
			//crearTokensActivos();
		}
	}
	
	public void crear_pyfos(ArrayList<TuioObject> lista) {
		for (TuioObject tuioObject : lista) {
			Pyfo py=new Pyfo(tuioObject);
				if(!Pyfo.buscar_en_lista(pyfos, py)) {
					pyfos.add(py);
					System.out.println("Agregando pyfo:"+py.id);
				}
		}
		
	}
	
	public Pyfo getPyfo(TuioObject objeto) {
		for (Pyfo py : pyfos) {
			if(py.id==objeto.getSymbolID()) {
				return py;
			}
		}
		return null;
	}
	
	
	
	public boolean borrarPyfo(Pyfo pyfo) {
		if(pyfos.remove(pyfo))
			return true;
		else
			return false;
	}
	
	public void relacionesPyfos(ArrayList<Pyfo> pyfos) {
		for (Pyfo pyfoA : pyfos) {
			for (Pyfo pyfoB : pyfos) {
				Pyfo.toca_a(pyfoA,pyfoB);
			}
			pyfoA.printPyfosTocados();
		}
	}
	
	
	
	public Token getToken(int id) {
		for (Token token : tokens) {
			if(token.id==id) {
				return token;
			}
		}
		return null;
	}
	
	public boolean agregarToken(Pyfo pyfo, String valor) {
		Token token=new Token(pyfo,valor);
		eliminarToken(token.id);
		if(tokens.add(token)) {
			//borrarPyfo(pyfo);
			return true;
		}
		else {
			return false;
		}
	}
	
	public void updateTokens(Pyfo pyfo) {
		for (Token token : tokens) {
			if(token.id==pyfo.id) {
				token.angulo=pyfo.angulo;
				token.xPos=pyfo.xPos;
				token.yPos=pyfo.yPos;
			}
		}
	}
	
	public void updateTokenActivos(Pyfo pyfo) {
		for (Token token : tokens_activos) {
			if(token.id==pyfo.id) {
				token.angulo=pyfo.angulo;
				token.xPos=pyfo.xPos;
				token.yPos=pyfo.yPos;
			}
		}
	}
	
	public boolean eliminarToken(int id) {
		for (Token token : this.tokens) {
			if(token.id==id) {
				tokens.remove(token);
				return true;
			}
		}
		return false;
	}
	
	public void crearTokensActivos() {
		Token token=null;
//		tokens_activos.clear();
		for (Pyfo pyfo : pyfos) {
			token = getToken(pyfo.id);
			if(token != null) {
				eliminarTokenActivo(pyfo.id);
				agragarTokenActivo(token);
			}
		}
	}
	
	private void eliminarTokenActivo(int id) {
		for (Token token : tokens_activos) {
			if (token.id == id) {
				tokens_activos.remove(token);
			}
		}
	}
	private void agragarTokenActivo(Token token) {
		tokens_activos.add(token);

	}
	
	public boolean buscaObjeto( int id) {
		for (TuioObject objeto : objetos) {
			if(objeto.getSymbolID()==id) {
				return true;
			}
		}
		return false;
	}
	public Token buscarToqueValido(Pyfo pyfo) {
		String tipo = pyfo.tipo;
	// 4x	System.out.println("Buscar toque válido para:"+tipo);
		for (Pyfo pyTocado : pyfo.tocando) {
			if(tipo.equals("atributo")) {
				System.out.println("Tipo de pyfo:" + tipo);
				if(pyTocado.tipo.equals("relacion") && (getToken(pyTocado.id)!=null) ) {
					//return pyTocado;
					return getToken(pyTocado.id);
				}
			}
			else if(tipo.equals("valor")) {
					if(pyTocado.tipo.equals("atributo") && (getToken(pyTocado.id)!=null) ) {
						//return pyTocado;
						return getToken(pyTocado.id);
					}
			}
		}
		return null;
	}//fin método buscarToqueValido
	
	public void relacionesTokens() {
		for (Token tokenA : tokens) {
			tokenA.tokensTocados.clear();
			for (Token tokenB : tokens) {
				if(Token.toca_a(tokenA,tokenB)) {
					if(!Token.buscar_en_lista(tokenA.tokensTocados, tokenB)) {
						tokenA.tokensTocados.add(tokenB);
						// AGREGAR CONSULTA A PROLOG PARA DETERMINAR SI EL TOQUE ESTA PERMITIDO
					}
				}
			}
			//System.out.println("==== Relaciones entre tokens con parámetro:=====");
			//tokenA.printTokensTocados(tokenA);
			//System.out.println("#### Relaciones entre tokens(): ########");
			tokenA.printTokensTocados();
		}
	}
	
	public void imprimetoques() {
		for (Token token : this.tokens) {
			String tocados="";
			String tocados2="";
			for (Token tokentocado : token.tokensTocados) {
				tocados=tocados+tokentocado.id+":"+tokentocado.valor+" + ";
				tocados2+=tokentocado.id+":"+tokentocado.valor+" + ";
			}
			System.out.println("Tocados=:"+tocados);
			System.out.println("Tocados2+=:"+tocados2);
			
		}
	}
	
	

}
