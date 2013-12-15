package sif.IO.spreadsheet;

import org.apache.poi.ss.usermodel.Workbook;

import sif.model.elements.basic.spreadsheet.Spreadsheet;

public interface IPOIOutput {

    public Workbook createPOIWorkbook(Spreadsheet spreadsheet);
    
    
}
