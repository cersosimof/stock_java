package com.pseguros.xlsToCsv.bean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ArchivoProcesado {

	private long idArchivo;
	private String nombreArchivoProcesado;
	private String cantidadDeHojas;
	private String estado;
	private String observaciones;
	private List<HojaProcesada> hojas = new ArrayList<HojaProcesada>();

	public ArchivoProcesado(long idFile, File inputFile) {
		this.idArchivo = idFile;
		this.nombreArchivoProcesado = inputFile.getName();
	}

	public long getIdArchivo() {
		return idArchivo;
	}

	public void setIdArchivo(long id) {
		this.idArchivo = id;
	}

	public String getNombreArchivoProcesado() {
		return nombreArchivoProcesado;
	}

	public void setNombreArchivoProcesado(String nombreArchivoProcesado) {
		this.nombreArchivoProcesado = nombreArchivoProcesado;
	}

	public List<HojaProcesada> getHojas() {
		return hojas;
	}

	public void setHojas(List<HojaProcesada> hojas) {
		this.hojas = hojas;
	}

	public String getCantidadDeHojas() {
		return cantidadDeHojas;
	}

	public void setCantidadDeHojas(String cantidadDeHojas) {
		this.cantidadDeHojas = cantidadDeHojas;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

}
