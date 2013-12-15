package sif.IO.spreadsheet.poi;

import java.util.Iterator;

import org.apache.poi.POIXMLProperties;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import sif.IO.spreadsheet.IPOIOutput;
import sif.model.elements.basic.spreadsheet.Spreadsheet;
import sif.model.elements.basic.spreadsheet.SpreadsheetProperties;
import sif.model.elements.basic.worksheet.Worksheet;

/**
 * Provides means to create a POI {@link Workbook} from a SIF {@link Spreadsheet}.
 * 
 * 
 * @author Manuel Lemcke
 *
 */
public class POIOutput implements IPOIOutput {
	
	private final EPOISpreadsheetType poiFormat;
    
    public POIOutput(EPOISpreadsheetType poiFormatParam) {
    	this.poiFormat = poiFormatParam;
    }        
    
    /**
     * Fills some Entries of the {@link SummaryInformation} of a {@link HSSFWorkbook}.
     * 
     * @param spreadsheet
     */
    private void createCoreProperties(Spreadsheet spreadsheet, Workbook workbook, EPOISpreadsheetType poiFormat) {
    	// The SIF SpreadsheetProperties
    	SpreadsheetProperties sifProperties = spreadsheet.getProperties();
    	
    	if (poiFormat.equals(EPOISpreadsheetType.HSSF)) { 
    		// If SummaryInformation or DocumentSummaryInformation are empty, create them
			((HSSFWorkbook)workbook).createInformationProperties();
			
    		// The HSSF SummaryInformation
			SummaryInformation sumInfo = ((HSSFWorkbook)workbook).getSummaryInformation();	
		
			if (sifProperties.getComments() != null) {
				sumInfo.setComments(sifProperties.getComments());
			}			
			
			if (sifProperties.getAuthor() != null) {
				sumInfo.setAuthor(sifProperties.getAuthor());
			}	
			
			if (sifProperties.getCreationDate() != null) {
				sumInfo.setCreateDateTime(sifProperties.getCreationDate());
			}		
			
			if (sifProperties.getSubject() != null) {
				sumInfo.setSubject(sifProperties.getSubject());
			}		
			
			if (sifProperties.getKeywords() != null) {
				sumInfo.setKeywords(sifProperties.getKeywords());
			}		
			
			if (sifProperties.getRevision() != null) {
				sumInfo.setRevNumber(sifProperties.getRevision());
			}	
			
			if (sifProperties.getTitle() != null) {
				sumInfo.setTitle(sifProperties.getTitle());
			}	
			
			if (sifProperties.getCategory() != null) {
				// The only Entry in DocumentSummaryInformation used by SIF is Category
				((HSSFWorkbook)workbook).getDocumentSummaryInformation().setCategory(sifProperties.getCategory());
			}
    	}
    	else if (poiFormat.equals(EPOISpreadsheetType.XSSF)) { 
    		 // The XSSF Properties
    	   	 POIXMLProperties xmlProps = ((XSSFWorkbook)workbook).getProperties();
    	   	 
    	     if(xmlProps != null) {

    	         // ..and from that. it's core properties
    	         POIXMLProperties.CoreProperties coreProps = 
    	 		 xmlProps.getCoreProperties();
    	         
    	         if(coreProps!= null) {    	        	 
    	        	 coreProps.setCreator(sifProperties.getAuthor());
    	        	 coreProps.setSubjectProperty(sifProperties.getSubject());
    	             coreProps.setCategory(sifProperties.getCategory());
    	        	 coreProps.setKeywords(sifProperties.getKeywords());
    	        	 coreProps.setRevision(sifProperties.getRevision());
    	        	 coreProps.setTitle(sifProperties.getTitle());
    	        	 //TODO remove
    	        	 System.out.println(sifProperties.getTitle());
    	         }
    	         else {
    	        	 //TODO replace
    	             System.out.println("Null core properties.");
    	         }
    	     }
    	     else {
    	    	 //TODO replace
    	         System.out.println("Null xmlProperties.");
    	     }
    	}
    }
    
    /**
     * Creates a POI {@link Workbook} from the given SIF {@link Spreadsheet}
     * 
     * @param spreadsheet The {@link Spreadsheet} to be converted
     * @return The created {@link Workbook}
     * @throws Exception 
     */
    public Workbook createPOIWorkbook(Spreadsheet spreadsheet) {
    	Workbook workbook;

        if (poiFormat.equals(EPOISpreadsheetType.XSSF)) {
            workbook = new XSSFWorkbook();
        } else {
            workbook = new HSSFWorkbook();
        }
        
        Iterator<Worksheet> worksheetIterator = spreadsheet.getWorksheetIterator();
        Worksheet currentWorksheet = null;
        Sheet currentSheet = null;
        String currentName = null;
        
        //Worksheets durchlaufen
        while(worksheetIterator.hasNext()) {
            currentWorksheet = worksheetIterator.next();
            currentName = WorkbookUtil.createSafeSheetName(currentWorksheet.getName());
            currentSheet = workbook.createSheet(currentName);
            
//            Iterator<sif.model.elements.basic.cell.Cell> sifCellIterator = currentWorksheet.getCellIterator();
//            sif.model.elements.basic.cell.Cell currentSifCell = null;
            
            Iterator<sif.model.elements.basic.worksheet.Row> sifRowIterator = currentWorksheet.getRowIterator();
            sif.model.elements.basic.worksheet.Row currentSifRow = null;
            
            // Zeilen durchlaufen
            while(sifRowIterator.hasNext()) {
                currentSifRow = sifRowIterator.next();
                
                Iterator<sif.model.elements.basic.cell.Cell> sifCellIterator = currentSifRow.getCellIterator();
                sif.model.elements.basic.cell.Cell currentSifCell = null;
                
                Row currentPoiRow = currentSheet.createRow(currentSifRow.getIndex() - 1);
                
                // Zellen der Zeile durchlaufen
                while (sifCellIterator.hasNext()) {
                    currentSifCell = sifCellIterator.next();
                    
                    transformCell(currentSifCell, currentPoiRow);
                }
            }            
        }
        
        // Metadaten schreiben
        createCoreProperties(spreadsheet, workbook, poiFormat);
        
        return workbook;
    }
    
    /**
     * Adds a POI {@link Cell} from a SIF {@link sif.model.elements.basic.cell.Cell} to the POI {@link Row}
     * 
     * @param sifCell The {@link sif.model.elements.basic.cell.Cell} from which the {@link Cell} is generated
     * @param poiRow The {@link Row} to which the {@link Cell} is added
     * @return the POI {@link Cell} created from the SIF {@link sif.model.elements.basic.cell.Cell} 
     */
    private Cell transformCell(sif.model.elements.basic.cell.Cell sifCell, Row poiRow) {
        
        Cell currentPoiCell = poiRow.createCell(sifCell.getColumnIndex() - 1);
        
        switch (sifCell.getCellContentType()) {
        case BLANK:
            currentPoiCell.setCellType(Cell.CELL_TYPE_BLANK);            
//            currentPoiCell.setCellValue(sifCell.getValueAsString());
            break;
        case BOOLEAN:
            currentPoiCell.setCellType(Cell.CELL_TYPE_BOOLEAN); 
            currentPoiCell.setCellValue(Boolean.parseBoolean(sifCell.getValueAsString()));
            break;
        case ERROR:             
            currentPoiCell.setCellType(Cell.CELL_TYPE_ERROR);            
            break;
        case NUMERIC:
            currentPoiCell.setCellType(Cell.CELL_TYPE_NUMERIC);
            currentPoiCell.setCellValue(Double.parseDouble(sifCell.getValueAsString()));
            break;
        case TEXT:
            currentPoiCell.setCellType(Cell.CELL_TYPE_STRING); 
            currentPoiCell.setCellValue(sifCell.getValueAsString());
            break;
        default:
            break;
        
        }
        
        sif.model.elements.basic.tokencontainers.Formula formula = sifCell.getFormula();
        if (formula != null) {        
            try {
    
                String formulaString = sifCell.getFormula().getValueAsString(); // .getStringRepresentation().replace("Formula[", "").replace("]", "").replace("=", "");
                
                //TODO kann das ueberhaupt eintreten?
                if (formulaString.startsWith("=")) {
                	formulaString = formulaString.substring(1);
                }
//                System.out.println("OK");
//                System.out.println("Zelle " + poiRow.getRowNum() + "," + sifCell.getColumnIndex() + ": ");
//                System.out.println(sifCell.getLocation() + ": " 
//                        + sifCell.getFormula().getStringRepresentation());
    //            FormulaParsingWorkbook fw = null;
    //            FormulaType type = sifCell.getFormula().get
    //            
    //            fw = XSSFEvaluationWorkbook.create((XSSFWorkbook) this.workbook);
    //            
    //            FormulaParser.parse(formulaString, fw, FormulaType.ARRAY, 0);
    //            // if cell has a formula, transform it
                currentPoiCell.setCellFormula(formulaString);                   
            } catch(org.apache.poi.ss.formula.FormulaParseException e) {
                System.out.println("ERROR");
                System.out.println(sifCell.getLocation() + ": " 
                        + sifCell.getFormula().getStringRepresentation());
                System.out.println(e.getLocalizedMessage());
            }
        }   
        
        return currentPoiCell;        
    }   
}
