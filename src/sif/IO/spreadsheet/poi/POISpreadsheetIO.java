package sif.IO.spreadsheet.poi;

import org.apache.poi.POIXMLProperties.CoreProperties;
import org.apache.poi.hpsf.DocumentSummaryInformation;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hssf.extractor.ExcelExtractor;
import org.apache.poi.hssf.usermodel.HSSFEvaluationWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.extractor.XSSFExcelExtractor;
import org.apache.poi.xssf.usermodel.XSSFEvaluationWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import sif.IO.spreadsheet.ISpreadsheetIO;
import sif.model.elements.basic.spreadsheet.SpreadsheetProperties;

/***
 * {@link ISpreadsheetIO} for .xls and .xlsx spreadsheet files.
 * 
 * @author Sebastian Zitzelsberger
 * 
 */
public class POISpreadsheetIO extends AbstractPOISpreadsheetIO {

	@Override
	public void createCoreProperties() {
		if (poiWorkbook instanceof XSSFWorkbook){
			createCorePropertiesXssf();
		} else if (poiWorkbook instanceof HSSFWorkbook){
			createCorePropertiesHssf();
		}
	}
	
	private void createCorePropertiesXssf() {
		XSSFExcelExtractor extractor = new XSSFExcelExtractor((XSSFWorkbook) poiWorkbook);
		CoreProperties coreProperties = extractor.getCoreProperties();
		SpreadsheetProperties spreadsheetProperties = new SpreadsheetProperties();
		spreadsheetProperties.setAuthor(coreProperties.getCreator());
		spreadsheetProperties.setCategory(coreProperties.getCategory());
		spreadsheetProperties.setCreationDate(coreProperties.getCreated());
		spreadsheetProperties.setSubject(coreProperties.getSubject());
		spreadsheetProperties.setKeywords(coreProperties.getKeywords());
		spreadsheetProperties.setRevision(coreProperties.getRevision());
		spreadsheetProperties.setTitle(coreProperties.getTitle());
		spreadsheet.setProperties(spreadsheetProperties);
	}
	
	private void createCorePropertiesHssf() {
		SpreadsheetProperties spreadsheetProperties = new SpreadsheetProperties();
		ExcelExtractor extractor = new ExcelExtractor((HSSFWorkbook) poiWorkbook);
		SummaryInformation sumInfo = extractor.getSummaryInformation();
		DocumentSummaryInformation docInfo = extractor
				.getDocSummaryInformation();

		spreadsheetProperties.setComments(sumInfo.getComments());
		spreadsheetProperties.setAuthor(sumInfo.getAuthor());
		spreadsheetProperties.setCategory(docInfo.getCategory());
		spreadsheetProperties.setCreationDate(sumInfo.getCreateDateTime());
		spreadsheetProperties.setSubject(sumInfo.getSubject());
		spreadsheetProperties.setKeywords(sumInfo.getKeywords());
		spreadsheetProperties.setRevision(sumInfo.getRevNumber());
		spreadsheetProperties.setTitle(sumInfo.getTitle());
		
		spreadsheet.setProperties(spreadsheetProperties);
	}
	
	private void createFormulaParsingWorkbook(){
		if (poiWorkbook instanceof XSSFWorkbook){
			this.formulaParsingWorkbook = XSSFEvaluationWorkbook
					.create((XSSFWorkbook) poiWorkbook);
		} else if (poiWorkbook instanceof HSSFWorkbook){
			this.formulaParsingWorkbook = HSSFEvaluationWorkbook
					.create((HSSFWorkbook) poiWorkbook);
		}
	}

	@Override
	public void initialize() {
		createFormulaParsingWorkbook();
	}

}
