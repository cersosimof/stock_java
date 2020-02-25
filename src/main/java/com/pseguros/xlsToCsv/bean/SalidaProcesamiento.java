package com.pseguros.xlsToCsv.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SalidaProcesamiento {

	private long idProceso;
	private String estado;
	private String fechaInicio;
	private String fechaFin;
	private String carpetaInicio;
	private String carpetaFin;
	private String servidor;
	private String demora;
	private int cantidadTotalRegistros;
	



	public int getCantidadTotalRegistros() {
		return cantidadTotalRegistros;
	}

	public void setCantidadTotalRegistros(int cantidadTotalRegistros) {
		this.cantidadTotalRegistros = cantidadTotalRegistros;
	}

	private List<ArchivoProcesado> archivosProcesados = new ArrayList<ArchivoProcesado>();

	public SalidaProcesamiento(String estado, String fechaInicio, String fechaFin, String horaInicio, String horaFin, String carpetaInicio, String carpetaFin, String servidor, String id,
			String nombre, String cantidadRegistros, int cantidadTotalRegistros) {
		super();
		this.estado = estado;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.carpetaInicio = carpetaInicio;
		this.carpetaFin = carpetaFin;
		this.servidor = servidor;
		this.cantidadTotalRegistros = cantidadTotalRegistros;
	}

	public SalidaProcesamiento() {
		this.idProceso = new Date().getTime();

	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public String getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(String fechaFin) {
		this.fechaFin = fechaFin;
	}

	public String getCarpetaInicio() {
		return carpetaInicio;
	}

	public void setCarpetaInicio(String carpetaInicio) {
		this.carpetaInicio = carpetaInicio;
	}

	public String getCarpetaFin() {
		return carpetaFin;
	}

	public void setCarpetaFin(String carpetaFin) {
		this.carpetaFin = carpetaFin;
	}

	public String getServidor() {
		return servidor;
	}

	public void setServidor(String servidor) {
		this.servidor = servidor;
	}

	public List<ArchivoProcesado> getArchivosProcesados() {
		return archivosProcesados;
	}

	public void setArchivosProcesados(List<ArchivoProcesado> archivosProcesados) {
		this.archivosProcesados = archivosProcesados;
	}

	public String getDemora() {
		return demora;
	}

	public void setDemora(String demora) {
		this.demora = demora;
	}

	public long getIdProceso() {
		return idProceso;
	}

	public void setIdProceso(long idProceso) {
		this.idProceso = idProceso;
	}


}
