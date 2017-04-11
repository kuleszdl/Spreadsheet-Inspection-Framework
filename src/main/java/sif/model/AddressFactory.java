package sif.model;

import com.google.inject.assistedinject.Assisted;

public interface AddressFactory {
    CellAddress createCellAddress(@Assisted("ca-address") String excelNotation);

    CellAddress createCellAddress(@Assisted("ca-worksheet") Worksheet worksheet, @Assisted("ca-simple-address") String excelWorksheetNotation);

    CellMatrixAddress createCMA(@Assisted("cma-cell") Cell c, @Assisted("horizontal") int h, @Assisted("vertical") int v);

    CellMatrixAddress createCMA(@Assisted("cma-address") String s);

    CellMatrixAddress createCMA(@Assisted("cma-worksheet") Worksheet worksheet, @Assisted("cma-simple-address") String s);
}
