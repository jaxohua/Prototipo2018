package com.TanQuery;

import java.awt.Frame;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import TUIO.TuioObject;

public class Log {

	// Delimiter used in CSV file
	private static final String COMMA_DELIMITER = ",";
	private static final String NEW_LINE_SEPARATOR = "\n";
	static String nombreLog;
	static Date fecha;
	static DateFormat dateFormat;
	static DateFormat hourFormat;
	// Log registro;

	// CSV file header
	private static final String FILE_HEADER = "Id,Tipo,Xpos,Ypos,Valor,Grados,Evento,Raiz,HijoDer,HijoIzq,Padre,Atributo,Fecha,Hora";

	public Log() {
		// Se establece el nombre que llevará el Log ejemplo
		// Log20022017181850.csv
		fecha = new Date();
		dateFormat = new SimpleDateFormat("ddMMyyyy");
		hourFormat = new SimpleDateFormat("HHmmss");
		// Nombre del log
		nombreLog = "Log" + dateFormat.format(fecha) + hourFormat.format(fecha)
				+ ".csv";
		System.out.println("%%%%%%%%%%%%%%%%%%    Nombre del Log creado: "
				+ nombreLog + "%%%%%%%%%%%%%%%%%%%%%%%");
	}

	public static void addLineCsv(String logpath, ArrayList<Token> tokens,
			String evento, TuioObject tobj, int width, int height, String raiz,
			Token nodo, ArrayList<TuioObject> tuioObjectList) {

		String linea = "";
		String id = Integer.toString(tobj.getSymbolID());
		String tipo = Valores.whatIs(tobj.getSymbolID());
		String x = Integer.toString(tobj.getScreenX(width));
		String y = Integer.toString(tobj.getScreenX(height));
		String valor = "";
		String grados = Float.toString(tobj.getAngleDegrees());
		fecha = new Date();
		dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		hourFormat = new SimpleDateFormat("HH:mm:ss");
		FileWriter fileWriter = null;
		File archivo = new File(logpath, nombreLog);
		String hijoDer = "-", hijoIzq = "-", padre = "-", atributo = "-";

		boolean tieneHijoDer = false;
		boolean tieneHijoIzq = false;
		boolean tieneAtributos = false;
		boolean tienePadre = false;

		// Log.addLineCsv(nombreLog, "primera,linea,de,prueba");

		for (TuioObject objeto : tuioObjectList) {
			if (Valores.onList(objeto.getSymbolID(), tokens)) {// si el id tiene
																// valor
																// asignado en
																// la lista de
																// TOKENS
				if (Valores.whatIs(objeto.getSymbolID()) == "operator") {
					valor = Valores.getToken(objeto.getSymbolID(), tokens)
							.getValorSql();
				} else {
					valor = Valores.getValorToken(tokens, objeto.getSymbolID());
				}
			}
			linea = id + "," + tipo + "," + x + "," + y + "," + valor + ","
					+ grados;


			if (nodo != null) {
				tieneHijoDer = nodo.hijoDer != null ? true : false;
				tieneHijoIzq = nodo.hijoIzq != null ? true : false;
				tieneAtributos = nodo.atributos != null ? true : false;
				tienePadre = nodo.atributos != null ? true : false;

				if (tieneHijoDer) {
					hijoDer = Integer.toString(nodo.hijoDer.id);
				}
				if (tieneHijoIzq) {
					hijoIzq = Integer.toString(nodo.hijoIzq.id);
				}
				if (tienePadre) {
					padre = Integer.toString(nodo.padre.id);
				}
				if (tieneAtributos) {
					atributo = Integer.toString(nodo.atributos.id);
				}
				System.out.println("&&&&&&&&&&&&&&&--------NODO identificado: "
						+ nodo.id + " hijoDer: " + hijoDer + " hijo izq: "
						+ hijoIzq + " Padre: " + padre + " Atributos: "
						+ atributo);
			}

			try {
				if (!archivo.exists()) {
					// System.out.println("getPATH: "+archivo.getPath());
					// System.out.println("getAbso: "+archivo.getAbsolutePath());
					fileWriter = new FileWriter(archivo.getPath());
					System.out.println("No existe el archivo: "
							+ archivo.getPath());
					fileWriter.append(FILE_HEADER);
					fileWriter.append(NEW_LINE_SEPARATOR);
				} else {
					// System.out.println("Guardando en::::::::::::::::::::::::::::::: "+archivo.getAbsolutePath());
					fileWriter = new FileWriter(archivo.getPath(), true);
				}

				fileWriter.append(String.valueOf(linea));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(evento);
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(raiz);
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(hijoDer);
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(hijoIzq);
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(padre);
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(atributo);
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(dateFormat.format(fecha)));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(hourFormat.format(fecha)));
				fileWriter.append(NEW_LINE_SEPARATOR);

			} catch (Exception e) {
				System.out
						.println(".---------------------------------------ERROR!!!!! in CSV-FileWriter !!!");
				e.printStackTrace();
			} finally {

				try {
					fileWriter.flush();
					fileWriter.close();
				} catch (IOException e) {
					System.out
							.println("Error while flushing/closing fileWriter !!!");
					e.printStackTrace();
				}

			}
		}
	}

	public void addLineCsv(TanQuery frame, Token nodo, String evento, String raiz,TuioObject objeto,int momento) {
		String linea, id, tipo, x, y, valor, grados;
		linea = id = tipo = x = y = valor = grados = "";
		String hijoDer, hijoIzq, padre, atributo;
		hijoDer = hijoIzq = padre = atributo = "-";

		FileWriter fileWriter = null;
		File archivo = new File(frame.logpath, nombreLog);

		boolean tieneHijoDer = false;
		boolean tieneHijoIzq = false;
		boolean tieneAtributos = false;
		boolean tienePadre = false;

		id = Integer.toString(objeto.getSymbolID());
		tipo = Valores.whatIs(objeto.getSymbolID());
		x = Integer.toString(objeto.getScreenX(frame.width));
		y = Integer.toString(objeto.getScreenX(frame.height));
		grados = Float.toString(objeto.getAngleDegrees());
		// si el id tiene valor asignado en la lista de TOKENS
		if (Valores.onList(objeto.getSymbolID(), frame.tokens)) {
			if (Valores.whatIs(objeto.getSymbolID()) == "operator") {
				valor = Valores.getToken(objeto.getSymbolID(), frame.tokens)
						.getValorSql();
			} else {
				valor = Valores.getValorToken(frame.tokens,
						objeto.getSymbolID());
			}
		}
		linea = id + "," + tipo + "," + x + "," + y + "," + valor + ","
				+ grados;

		if (nodo != null) {

			tieneHijoDer = nodo.hijoDer != null ? true : false;
			tieneHijoIzq = nodo.hijoIzq != null ? true : false;
			tieneAtributos = nodo.atributos != null ? true : false;
			tienePadre = nodo.padre != null ? true : false;

			if (tieneHijoDer) {
				hijoDer = Integer.toString(nodo.hijoDer.id);
			}
			if (tieneHijoIzq) {
				hijoIzq = Integer.toString(nodo.hijoIzq.id);
			}
			if (tienePadre) {
				padre = Integer.toString(nodo.padre.id);
			}
			if (tieneAtributos) {
				atributo = Integer.toString(nodo.atributos.id);
			}
			System.out.println("&&&&&&&&&&&&&&&--------NODO identificado: "
					+ nodo.id + " hijoDer: " + hijoDer + " hijo izq: "
					+ hijoIzq + " Padre: " + padre + " Atributos: " + atributo);
		}

		try {
			if (!archivo.exists()) {
				System.out.println("getPATH: " + archivo.getPath());
				System.out.println("getAbso: " + archivo.getAbsolutePath());
				fileWriter = new FileWriter(archivo.getPath());
				System.out
						.println("No existe el archivo: " + archivo.getPath());
				fileWriter.append(FILE_HEADER);
				fileWriter.append(NEW_LINE_SEPARATOR);
			} else {
				System.out
						.println("Guardando en::::::::::::::::::::::::::::::: "
								+ archivo.getAbsolutePath());
				fileWriter = new FileWriter(archivo.getPath(), true);
			}

			Date fechaD = new Date();
			dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			hourFormat = new SimpleDateFormat("HH:mm:ss");

			fileWriter.append(String.valueOf(linea));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(evento);
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(raiz);
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(hijoDer);
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(hijoIzq);
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(padre);
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(atributo);
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(dateFormat.format(fechaD)));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(hourFormat.format(fechaD)));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(momento));
			fileWriter.append(NEW_LINE_SEPARATOR);

		} catch (Exception e) {
			System.out
					.println("&&&&&&&&&&&&&&&&&&&&&&&&.---------------------------------------ERROR!!!!! in CSV-FileWriter !!!");
			e.printStackTrace();
		} finally {

			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				System.out
						.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&      &&&&&&&&&&&&&   Error while flushing/closing fileWriter !!!");
				e.printStackTrace();
			}

		}

	}
}
