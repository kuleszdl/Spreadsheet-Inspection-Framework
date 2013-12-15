package sif.IO.spreadsheet.poi;

import org.apache.poi.hpsf.DocumentSummaryInformation;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hssf.extractor.ExcelExtractor;
import org.apache.poi.hssf.usermodel.HSSFEvaluationWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import sif.IO.spreadsheet.ISpreadsheetIO;
import sif.model.elements.basic.spreadsheet.SpreadsheetProperties;

/***
 * {@link ISpreadsheetIO} for .xls spreadsheet files. *
 * 
 * @author Sebastian Zitzelsberger
 * 
 */
public class POISpreadsheetIO_HSSF extends AbstractPOISpreadsheetIO {

	private HSSFWorkbook hssfWorkbook;

	@Override
	public void createCoreProperties() {
		SpreadsheetProperties spreadsheetProperties = new SpreadsheetProperties();
		ExcelExtractor extractor = new ExcelExtractor(hssfWorkbook);
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

	@Override
	public void initialize() {
		this.hssfWorkbook = (HSSFWorkbook) poiWorkbook;
		this.formulaParsingWorkbook = HSSFEvaluationWorkbook
				.create(hssfWorkbook);

	}
}
