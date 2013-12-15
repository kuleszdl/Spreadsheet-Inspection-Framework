package sif.technicalDepartment.equipment.testing.tools;

import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * THIS CLONER IS INCOMPLETE. IT'S PURPOSE IS TO SHOW HOW IT <b>COULD</b> BE SOLVED INSTEAD OF
 * WRITING TO FILES.
 * 
 * Clones the functional parts of a workbook and also some nonfunctional aspects. DataValidations
 * are not supported at the time.
 * 
 * @deprecated Use WorkbookFileCloner instead
 * @author Manuel Lemcke
 *
 */
public class WorkbookMemoryCloner implements IWorkbookCloner {
	
	private Workbook clone;

	/**
	 * 
	 * @deprecated Use WorkbookFileCloner.cloneWorkbook() instead
	 */
	@SuppressWarnings("unused")
	@Override
	@Deprecated
	public Workbook cloneWorkbook(Workbook workbook) {		
		if (workbook instanceof HSSFWorkbook) {
			this.clone = new HSSFWorkbook();
		} else {
			this.clone = new XSSFWorkbook();
		}
		
		// First, create alle sheet "sceletons"
		for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
			Sheet currentSheet = workbook.getSheetAt(i);
			Sheet newSheet = this.clone.createSheet(currentSheet.getSheetName());
		}
		
		// Then populate the sheets
		for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
			Sheet currentSheet = workbook.getSheetAt(i);
			Sheet newSheet = this.clone.getSheetAt(i);
			copySheet(currentSheet, newSheet);
		}
		
		for (int n = 0; n < workbook.getNumberOfNames(); n++) {
			Name fromName = workbook.getNameAt(n);
			Name toName = clone.createName();
			
			copyName(fromName, toName);
		}

		return clone;		
	}
	
	private void copyName(Name fromName, Name toName) {
		toName.setNameName(fromName.getNameName());
		toName.setRefersToFormula(fromName.getRefersToFormula());
		toName.setSheetIndex(fromName.getSheetIndex());
		toName.setComment(fromName.getComment());
	}
	
	private void copySheet(Sheet fromSheet, Sheet toSheet) {
		Iterator<Row> rowIterator = fromSheet.rowIterator();
		
		toSheet.setAutobreaks(fromSheet.getAutobreaks());
		
		
		// Copy column breaks
		for (int colBreak : fromSheet.getColumnBreaks()) {
			toSheet.setColumnBreak(colBreak);
		}
		
		// NO GUTS NO GLORY!
		toSheet.setDisplayGuts(fromSheet.getDisplayGuts());
		
		while (rowIterator.hasNext()) {
			Row current = rowIterator.next();
			Row newRow = toSheet.createRow(current.getRowNum());
			
			copyRow(current, newRow);
		}
	}
	
	private void copyRow(Row fromRow, Row toRow) {
		Iterator<Cell> cellIterator = fromRow.cellIterator();

		// copy Row properties
		if (fromRow.getRowStyle() != null) {
			toRow.setRowStyle(fromRow.getRowStyle());	
		}		
		toRow.setHeight(fromRow.getHeight());
		toRow.setZeroHeight(fromRow.getZeroHeight());
		
		// copy Cells
		while (cellIterator.hasNext()) {
			Cell current = cellIterator.next();
			Cell newCell = toRow.createCell(current.getColumnIndex());
			
			copyCell(current, newCell);
		}
	}
	
	private void copyCell(Cell fromCell, Cell toCell)  {
		
		if (fromCell.getCellComment() != null) {
			toCell.setCellComment(fromCell.getCellComment());
		}		
		if (fromCell.getCellStyle() != null) {
			CellStyle toStyle = clone.createCellStyle();
			toStyle.cloneStyleFrom(fromCell.getCellStyle());
//			copyCellStyle(fromCell.getCellStyle(), toStyle);	
		}		
		if (fromCell.getHyperlink() != null) {
			toCell.setHyperlink(fromCell.getHyperlink());
		}
		
		switch (fromCell.getCellType()) {
		case Cell.CELL_TYPE_NUMERIC:
			toCell.setCellValue(fromCell.getNumericCellValue());
			break;
		case Cell.CELL_TYPE_STRING:
			toCell.setCellValue(fromCell.getStringCellValue());
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			toCell.setCellValue(fromCell.getBooleanCellValue());
			break;
		case Cell.CELL_TYPE_FORMULA:
			//TODO enough?
			System.out.println(fromCell.getRowIndex() + "." + fromCell.getColumnIndex() + ": " 
					+ fromCell.getCellFormula());
			toCell.setCellFormula(fromCell.getCellFormula());			
			break;
		case Cell.CELL_TYPE_BLANK:
			// In this case there is no value so we have to set the type
			toCell.setCellType(Cell.CELL_TYPE_BLANK);
			break;
		case Cell.CELL_TYPE_ERROR:
			toCell.setCellErrorValue(fromCell.getErrorCellValue());
			break;
		default:
			break;
		}
	}
	
//	private void copyCellStyle(CellStyle fromStyle, CellStyle toStyle) {
//		toStyle.cloneStyleFrom(fromStyle);
////		toCell.setAlignment(fromCell.getAlignment());
////		toCell.setBorderBottom(fromCell.getBorderBottom());
////		toCell.setBorderLeft(fromCell.getBorderLeft());
////		toCell.setBorderRight(fromCell.getBorderRight());
////		toCell.setBorderTop(fromCell.getBorderTop());
////		toCell.setBottomBorderColor(fromCell.getBottomBorderColor());
////		toCell.setDataFormat(fromCell.getDataFormat());
////		toCell.setFillBackgroundColor(fromCell.getFillBackgroundColor());
////		toCell.setFillForegroundColor(fromCell.getFillForegroundColor());
//	}

}
