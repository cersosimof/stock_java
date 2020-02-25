package com.pseguros.xlsToCsv.bean;

public class HojaProcesada {

	private String nroHoja;
	private String nombreArchivoSalida;
	private String cantidadDeRegistros;

	public String getNroHoja() {
		return nroHoja;
	}

	public void setNroHoja(String nroHoja) {
		this.nroHoja = nroHoja;
	}

	public String getNombreArchivoSalida() {
		return nombreArchivoSalida;
	}

	public void setNombreArchivoSalida(String nombreArchivoSalida) {
		this.nombreArchivoSalida = nombreArchivoSalida;
	}

	public String getCantidadDeRegistros() {
		return cantidadDeRegistros;
	}

	public void setCantidadDeRegistros(String cantidadDeRegistros) {
		this.cantidadDeRegistros = cantidadDeRegistros;
	}

}
