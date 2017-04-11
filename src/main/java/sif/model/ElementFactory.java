package sif.model;

import com.google.inject.assistedinject.Assisted;

public interface ElementFactory {

    Worksheet createWorksheet(@Assisted String worksheetKey, @Assisted Integer index);

    Row createRow(@Assisted String rowKey);

    Column createColumn(@Assisted String columnKey);

    Cell createCell(@Assisted CellAddress address);

    Formula createFormula(@Assisted Cell cell);

    CellMatrix createCellMatrix(@Assisted CellMatrixAddress address);
}
