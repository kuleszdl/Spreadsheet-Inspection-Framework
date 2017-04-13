package sif.model;

public interface Addressable {

    /***
     * Gets the worksheet of this address.
     *
     * @return the worksheet of this address.
     */
    Worksheet getWorksheet();

    /***
     * Gets the address of this element.
     *
     * @return the address of this element.
     */
    Address getAddress();

    /***
     * Gets the address of this element formatted in excel notation.
     *
     * @return excel notation
     */
    String getExcelNotation();

    /***
     * Gets the address of this element formatted in simple notation.
     *
     * @return simple notation
     */
    String getSimpleNotation();

    /**
     * Returns true if the Addressable is a single cell
     *
     * @return true if this is a single cell
     */
    boolean isSingleCell();
}
