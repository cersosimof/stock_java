package com.pseguros.xlsToCsv.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.extractor.ExcelExtractor;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.pseguros.xlsToCsv.bean.HojaProcesada;
import com.pseguros.xlsToCsv.bean.SalidaProcesamiento;

/**
 * Facu va a ponerse a escribir
 * 
 * @author cersosimof
 *
 */
public class XlsToCsvUtil {

	 private static Logger logger = LogManager.getLogger(XlsToCsvUtil.class);
	 
	/**
	 * 
	 * @param idFile
	 * @param inputFile
	 * @param separador
	 * @param salida
	 * @return
	 * @throws Exception
	 */
	public static List<HojaProcesada> procesarHojasArchivo(long idFile, File inputFile, String separador, SalidaProcesamiento salida) throws Exception {
		List<HojaProcesada> hojasProcesadas = xlsx(idFile, inputFile, separador, salida);

		return hojasProcesadas;
	}

	
	/**
	 * 
	 * @param idFile
	 * @param inputFile
	 * @param outputFile
	 * @param separador
	 * @param salida
	 * @return
	 * @throws Exception
	 */
	public static List<HojaProcesada> xlsx(long idFile, File inputFile, String separador, SalidaProcesamiento salida) throws Exception {

		List<HojaProcesada> hojasProcesadas = new ArrayList<HojaProcesada>();
		
		try {

			FileInputStream fis = new FileInputStream(inputFile);

			Workbook workbook = getVersionExcel(inputFile, fis);

			FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

			int numberOfSheets = workbook.getNumberOfSheets();

			int cantidadTotalRegistros = 0; // Conteo total de todos los registros.

			// Iterate through each rows from first sheet
			for (int i = 0; i < numberOfSheets; i++) {

				HojaProcesada hojaSeleccionada = procesarHoja(idFile, inputFile, separador, salida, workbook, evaluator, i, cantidadTotalRegistros);

				if (hojaSeleccionada != null) {
					hojasProcesadas.add(hojaSeleccionada);
				}

			}
		} catch (Exception e) {
			logger.error("Error en proceso con ID " + idFile + " - archivo " + inputFile + ".", e);
			throw e;
			
		}

		return hojasProcesadas;

	}

	
	private static Workbook getVersionExcel(File inputFile, FileInputStream fis) throws IOException {
		Workbook workbook = null;

		String ext = FilenameUtils.getExtension(inputFile.toString());

		if (ext.equalsIgnoreCase("xlsx")) {
			workbook = new XSSFWorkbook(fis);
		} else if (ext.equalsIgnoreCase("xls")) {
			try {
				workbook = new HSSFWorkbook(fis);
				
				

				
			} catch (Exception e) {
				POIFSFileSystem excelFS = new POIFSFileSystem(new FileInputStream(inputFile));
				ExcelExtractor excelExtractor = new ExcelExtractor(excelFS);
				StringBuffer sb = new StringBuffer();
				sb.append(excelExtractor.getText());
					logger.error("Error de conversion" , e);	
			}
			
		}
		return workbook;
	}

	
	private static HojaProcesada procesarHoja(long idFile, File inputFile, String separador, SalidaProcesamiento salida, Workbook workbook, FormulaEvaluator evaluator, int i, int cantTotalRegistros)
			throws FileNotFoundException, IOException {
		
		logger.info("Se comienza a convertir hoja " + i + " ID: " + idFile + ".");
		HojaProcesada hoja = null;

		try {
			int conteoRegistros = 0;
			StringBuffer data = new StringBuffer();

			Row row;
			Cell cell;

			Sheet sheet = workbook.getSheetAt(i);
			Iterator<Row> rowIterator = sheet.iterator();
			
			while (rowIterator.hasNext()) {
				conteoRegistros++;
				row = rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();

				while (cellIterator.hasNext()) {
					
					cell = cellIterator.next();

					CellValue cellNew = evaluator.evaluate(cell);

					switch (cell.getCellType()) {
					case Cell.CELL_TYPE_BOOLEAN:
						data.append(cell.getBooleanCellValue() + separador);
						break;

					case Cell.CELL_TYPE_NUMERIC:
						data.append(getValueNumeric(cell, cellNew, separador));
						break;

					case Cell.CELL_TYPE_STRING:
						data.append(eliminarCaracteres(cellNew.getStringValue()) + separador);
						break;

					case Cell.CELL_TYPE_BLANK:
						data.append("" + separador);
						break;
					default:
						try {
							data.append(cellNew.getNumberValue() + separador);
						} catch (Exception e) {
							data.append(cell + separador);
						}

					}
				}
				data.append('\n'); // appending new line after each row
			}
			if (!(conteoRegistros == 0)) {
			
				File outputFile = creaArchivoDeSalida(idFile, salida, i, data);
				hoja = generarInfoHoja(i, conteoRegistros, outputFile);
				salida.setCantidadTotalRegistros(salida.getCantidadTotalRegistros() + conteoRegistros);
			
				logger.info("Finalizo conversion hoja " + i + " ID: " + idFile + " con " + conteoRegistros +  " registro.");
			} else {
				logger.info("Hoja " + i + " ID: " + idFile + " no posee registro.");
			}

		} catch (Exception e) {
			logger.error("Error al intentar convertir hoja " + i + " ID: " + idFile + ".", e);
			throw  e;
		} 
	

		return hoja;

	}

	
	private static File creaArchivoDeSalida(long idFile, SalidaProcesamiento salida, int i, StringBuffer data) throws FileNotFoundException, IOException {

		FileOutputStream fos = null;
		try {
			File outputFile = new File(salida.getCarpetaFin() + "\\" + salida.getIdProceso() +"_"+ idFile + "_" + i + ".csv");
			fos = new FileOutputStream(outputFile.getAbsoluteFile());
			fos.write(data.toString().getBytes());
			return outputFile;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				fos.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	
	private static HojaProcesada generarInfoHoja(int i, int conteoRegistros, File outputFile) {
		HojaProcesada hoja;
		hoja = new HojaProcesada();
		hoja.setNroHoja("" + i);
		hoja.setNombreArchivoSalida(outputFile.getName());
		hoja.setCantidadDeRegistros("" + conteoRegistros);
		return hoja;
	}

	
	/**
	 * Transforma un valor en numerico
	 * 
	 * @param cell
	 * @param cellNew
	 * @param separador
	 * @return
	 */
	private static Object getValueNumeric(Cell cell, CellValue cellNew, String separador) {
		if (HSSFDateUtil.isCellDateFormatted(cell)) {
			DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			return sdf.format(cell.getDateCellValue()) + separador;
		} else {
			NumberFormat formatter = new DecimalFormat("#0.00");
			return formatter.format(cellNew.getNumberValue()) + separador;
		}
	}

	
	/**
	 * Elimina caraceres problematicos
	 * 
	 * @param stringValue
	 * @return
	 */
	private static String eliminarCaracteres(String stringValue) {
		if (stringValue != null) {
			stringValue = stringValue.replaceAll(";", "");
			stringValue = stringValue.replaceAll("\n", "");
			return stringValue;
		}
		return stringValue;
	}

}
