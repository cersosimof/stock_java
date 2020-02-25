package com.pseguros.xlsToCsv.controller;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pseguros.xlsToCsv.bean.ArchivoProcesado;
import com.pseguros.xlsToCsv.bean.SalidaProcesamiento;
import com.pseguros.xlsToCsv.util.XlsToCsvUtil;

@RestController
public class XlsToCsvController {

	 private static Logger logger = LogManager.getLogger(XlsToCsvController.class);

	  
	@RequestMapping("/")
	public SalidaProcesamiento ejecutar() {
		SalidaProcesamiento salida = new SalidaProcesamiento();
		salida.setEstado("OK");
		return salida;
	}

	@RequestMapping("/convert")
	public SalidaProcesamiento convert() {

		Date fechaInicio = new Date();

		SalidaProcesamiento salida = new SalidaProcesamiento();

		try {
			logger.info("Inicia el procesamiento con id" + salida.getIdProceso());
			
			String nameFolder = "\\test";
			String nameFolderOut = "\\salida";
			String separador = ";";
			inicializarDatosSalida(salida, nameFolder, nameFolderOut, fechaInicio);
			procesar(nameFolder, nameFolderOut, separador, salida);

			logger.info("Fin de procesamiento " + salida.getIdProceso());
		} catch (Exception e) {
			logger.error("Error al iniciar procesamiento con id "+ salida.getIdProceso(),e);
			
			salida.setEstado("Error " + e.getMessage());

		} finally {
			Date fechaFin = new Date();
			salida.setFechaFin(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss:S").format(fechaInicio).toString());
			salida.setDemora(calcularDemora(fechaFin.getTime(), fechaInicio.getTime()));
		}

		return salida;
	}

	private String calcularDemora(long time, long time2) {
		long demora = time - time2;
		if (demora < 1000) {
			return demora + " Milisegundos.";
		} else {
			return demora / 1000 + " Segundos.";
		}
	}

	private void inicializarDatosSalida(SalidaProcesamiento salida, String nameFolder, String nameFolderOut, Date fechaInicio) throws UnknownHostException {
		salida.setFechaInicio(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss:S").format(fechaInicio).toString());
		salida.setCarpetaInicio(nameFolder);
		salida.setCarpetaFin(nameFolder + nameFolderOut);
		salida.setServidor(InetAddress.getLocalHost().getHostName());
	}

	private void procesar(String nameFolder, String nameFolderOut, String separador, SalidaProcesamiento salida) throws Exception {
		final File folder = new File(nameFolder);

		for (final File fileEntry : folder.listFiles()) {

			if (!fileEntry.isDirectory()) {

				ArchivoProcesado archivoProcesado = procesarArchivoIndividual(salida, fileEntry, separador);

				salida.getArchivosProcesados().add(archivoProcesado);

			}
		}
		salida.setEstado("OK");
	}

	private ArchivoProcesado procesarArchivoIndividual(SalidaProcesamiento salida, File fileEntry, String separador) {

		long idFile = new Date().getTime();
		File inputFile = new File(salida.getCarpetaInicio() + "\\" + fileEntry.getName());
		ArchivoProcesado archivoProcesado = new ArchivoProcesado(idFile, inputFile);

		try {
			logger.info("########## Inicio proceso archivo " + inputFile + "con ID: " + idFile + ". ##########");

			copiarExcelADestino(salida, idFile, inputFile);
			archivoProcesado.setHojas(XlsToCsvUtil.procesarHojasArchivo(idFile, inputFile, separador, salida));
			archivoProcesado.setCantidadDeHojas(archivoProcesado.getHojas().size() + "");

			archivoProcesado.setEstado("OK");
			
			logger.info("########## Fin procesamiento archivo " + inputFile + " con ID: " + idFile + ". ##########");

		} catch (Exception e) {
			logger.error("Error al procesar el archivo " + fileEntry, e);
			archivoProcesado.setEstado("ERROR");
			archivoProcesado.setObservaciones(e.getMessage());
			

		}

		return archivoProcesado;

	}

	private void copiarExcelADestino(SalidaProcesamiento salida, long idFile, File inputFile) throws IOException {
		File outputFileXLS = new File(salida.getCarpetaFin() + "\\" + salida.getIdProceso() + "_" +  idFile + "_ORIGINAL." + FilenameUtils.getExtension(inputFile.toString()));
		FileUtils.copyFile(inputFile, outputFileXLS);
	}

}
