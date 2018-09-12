package com.TanQuery;

public class Operadores {
	private String nombre;
	private String descripcion;
	private String simbolo;
	private String tipo;
	
	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getSimbolo() {
		return simbolo;
	}

	public void setSimbolo(String simbolo) {
		this.simbolo = simbolo;
	}

	
	public Operadores(String nombre, String descripcion, String simbolo, String tipo) {
		super();
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.simbolo = simbolo;
		this.tipo=tipo;
	}

	
	
	
}
