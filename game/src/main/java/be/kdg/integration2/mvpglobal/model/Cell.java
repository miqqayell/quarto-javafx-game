package be.kdg.integration2.mvpglobal.model;

/**
 * Represents a single cell on the game board grid for a 4x4 Quarto-like game.
 * <p>
 * Each cell knows its fixed coordinates (row and column) and may hold a piece, identified by a valueId.
 * A cell is considered empty when its valueId is {@code null}. When occupied, the valueId follows the
 * format "<color>-<shape>-<height>-<fill>".
 * </p>
 *
 * @author ACS101 - TEAM 2
 * @version 1.0
 */
public class Cell {
    /**
     * Row index of this cell (0-based).
     */
    private final int row;

    /**
     * Column index of this cell (0-based).
     */
    private final int col;

    /**
     * Identifier of the piece occupying this cell, or {@code null} if empty.
     */
    private String valueId;

    /**
     * Constructs a new, empty Cell at the specified coordinates.
     *
     * @param row the row index of this cell (0-based)
     * @param col the column index of this cell (0-based)
     */
    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        this.valueId = null;
    }

    /**
     * Determines whether this cell is empty (no piece placed).
     *
     * @return {@code true} if {@code valueId} is {@code null}, {@code false} otherwise
     */
    public boolean isEmpty() {
        return valueId == null;
    }

    /**
     * Retrieves the identifier of the piece in this cell.
     *
     * @return the {@code valueId} string of the occupying piece, or {@code null} if the cell is empty
     */
    public String getValueId() {
        return valueId;
    }

    /**
     * Assigns a piece identifier to this cell, or clears it.
     *
     * @param valueId the identifier of the piece in the format "<color>-<shape>-<height>-<fill>",
     *                or {@code null} to mark this cell as empty
     */
    public void setValueId(String valueId) {
        this.valueId = valueId;
    }

    /**
     * Gets the row index of this cell.
     *
     * @return the 0-based row coordinate
     */
    public int getRow() {
        return row;
    }

    /**
     * Gets the column index of this cell.
     *
     * @return the 0-based column coordinate
     */
    public int getCol() {
        return col;
    }
}
