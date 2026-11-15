package be.kdg.integration2.mvpglobal.model;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents the game board for a 4x4 Quarto-like game.
 * <p>
 * Manages placement of pieces, tracking available pieces, and determining winning or near-winning conditions.
 * </p>
 *
 * @author ACS101 - TEAM 2
 * @version 1.0
 */
public class Board {
    /**
     * 2D array of Cells representing the grid of the board.
     */
    private final Cell[][] cells;

    /**
     * Number of rows and columns of the board (fixed at 4).
     */
    private final int size = 4;

    /**
     * Map of piece IDs to their remaining available counts.
     */
    private final Map<String, Integer> availableCounts;

    /**
     * Constructs a new Board, initializes the cell grid and sets available counts for all pieces to 1.
     */
    public Board() {
        this.cells = new Cell[size][size];
        initializeBoard();
        availableCounts = new HashMap<>();
        for (String id : PieceFactory.allPieceIds()) {
            availableCounts.put(id, 1);
        }
    }

    /**
     * Returns the 2D array of cells.
     *
     * @return the cells array
     */
    public Cell[][] getCells() {
        return cells;
    }

    /**
     * Attempts to place a piece with the given ID at the specified row and column.
     *
     * @param row the row index (0-based)
     * @param col the column index (0-based)
     * @param pieceId the unique identifier of the piece to place
     * @return true if placement succeeded; false otherwise (out of bounds, cell occupied, or piece unavailable)
     */
    public boolean placePiece(int row, int col, String pieceId) {
        Integer cnt = getAvailableCounts().getOrDefault(pieceId, 0);
        if (cnt <= 0) return false;
        if (row < 0 || col < 0 || row >= size || col >= size) return false;
        Cell c = cells[row][col];
        if (!c.isEmpty()) return false;
        c.setValueId(pieceId);
        getAvailableCounts().put(pieceId, cnt - 1);
        return true;
    }

    /**
     * Initializes each cell in the board grid.
     */
    public void initializeBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                cells[i][j] = new Cell(i, j);
            }
        }
    }

    /**
     * Resets the board to its initial empty state and restores all piece availability.
     */
    public void reset() {
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                cells[i][j].setValueId(null);
        this.getAvailablePieceIds().clear();
        for (String id : PieceFactory.allPieceIds()) {
            availableCounts.put(id, 1);
        }
    }

    /**
     * Retrieves a list of piece IDs that are still available for placement.
     *
     * @return list of available piece IDs
     */
    public List<String> getAvailablePieceIds() {
        return availableCounts.entrySet().stream()
                .filter(e -> e.getValue() > 0)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves the map of available piece counts.
     *
     * @return map from piece ID to remaining count
     */
    public Map<String, Integer> getAvailableCounts() {
        return availableCounts;
    }

    /**
     * Gets positions of all empty cells in "row,col" string format.
     *
     * @return list of empty cell positions
     */
    public List<String> getEmptyCellPositions() {
        List<String> empty = new ArrayList<>();
        Cell[][] cells = this.getCells();
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                if (cells[i][j].isEmpty()) {
                    empty.add(i + "," + j);
                }
            }
        }
        return empty;
    }

    /**
     * Checks if there is a winning line on the board (row, column, or diagonal).
     *
     * @return true if a winning condition is met; false otherwise
     */
    public boolean hasWinner() {
        // rows
        for (int i = 0; i < size; i++)
            if (lineWin(cells[i][0], cells[i][1], cells[i][2], cells[i][3]))
                return true;

        // columns
        for (int j = 0; j < size; j++)
            if (lineWin(cells[0][j], cells[1][j], cells[2][j], cells[3][j]))
                return true;

        // main diagonal
        if (lineWin(cells[0][0], cells[1][1], cells[2][2], cells[3][3]))
            return true;

        // anti‑diagonal
        if (lineWin(cells[0][3], cells[1][2], cells[2][1], cells[3][0]))
            return true;

        return false;
    }

    /**
     * Checks if there is any line with exactly one cell empty and three filled cells sharing a trait.
     *
     * @return true if such an "almost winner" line exists; false otherwise
     */
    private boolean hasAlmostWinner() {
        // rows
        for (int i = 0; i < size; i++) {
            if (isAlmostWinningLine(cells[i][0], cells[i][1], cells[i][2], cells[i][3]))
                return true;
        }
        // columns
        for (int j = 0; j < size; j++) {
            if (isAlmostWinningLine(cells[0][j], cells[1][j], cells[2][j], cells[3][j]))
                return true;
        }
        // diagonals
        if (isAlmostWinningLine(cells[0][0], cells[1][1], cells[2][2], cells[3][3])) return true;
        if (isAlmostWinningLine(cells[0][3], cells[1][2], cells[2][1], cells[3][0])) return true;
        return false;
    }

    /**
     * Determines if four given cells form a winning line (all filled and share at least one trait).
     *
     * @param a first cell
     * @param b second cell
     * @param c third cell
     * @param d fourth cell
     * @return true if they form a winning line; false otherwise
     */
    private boolean lineWin(Cell a, Cell b, Cell c, Cell d) {
        // all must be placed
        if (a.isEmpty() || b.isEmpty() || c.isEmpty() || d.isEmpty())
            return false;

        // get the first cell's traits
        String[] traits0 = a.getValueId().split("-");
        // for each of the 4 trait positions...
        for (int k = 0; k < 4; k++) {
            String t = traits0[k];
            // compare against the others
            if (matchesAt(b, k, t) &&
                    matchesAt(c, k, t) &&
                    matchesAt(d, k, t)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if three filled cells share a trait and one is empty in a line of four.
     *
     * @param a first cell
     * @param b second cell
     * @param c third cell
     * @param d fourth cell
     * @return true if exactly one cell is empty and the other three share any trait
     */
    private boolean isAlmostWinningLine(Cell a, Cell b, Cell c, Cell d) {
        Cell[] line = {a, b, c, d};
        int emptyCount = 0;
        List<Cell> filled = new ArrayList<>();
        for (Cell cell : line) {
            if (cell.isEmpty()) emptyCount++;
            else filled.add(cell);
        }
        // exactly one empty, three filled?
        if (emptyCount != 1) return false;

        // check if the three share any trait (there are 4 trait-positions per piece)
        String[] traits0 = filled.get(0).getValueId().split("-");
        for (int k = 0; k < 4; k++) {
            String t = traits0[k];
            boolean allMatch = true;
            for (Cell cell : filled) {
                if (!cell.getValueId().split("-")[k].equals(t)) {
                    allMatch = false;
                    break;
                }
            }
            if (allMatch) return true;
        }
        return false;
    }

    /**
     * Checks if the trait at a given index of a cell matches a provided value.
     *
     * @param cell  the cell to inspect
     * @param index trait position index (0-3)
     * @param trait the trait value to compare
     * @return true if the cell's trait matches; false otherwise
     */
    private boolean matchesAt(Cell cell, int index, String trait) {
        return cell.getValueId().split("-")[index].equals(trait);
    }

    /**
     * Determines whether the AI can win immediately by placing any available piece in any empty cell.
     *
     * @return true if a winning end move exists; false otherwise
     */
    public boolean endMoveAIPossible(String pid) {
            // for each empty cell on the board
            for (String pos : getEmptyCellPositions()) {
                String[] parts = pos.split(",");
                int r = Integer.parseInt(parts[0]), c = Integer.parseInt(parts[1]);

                // simulate placing pid at (r,c)
                cells[r][c].setValueId(pid);

                // if that creates a win, undo and return true
                if (hasWinner()) {
                    cells[r][c].setValueId(null);
                    return true;
                }

                // undo the simulation
                cells[r][c].setValueId(null);
            }


        return false;
    }

    /**
     * Placeholder: Determines if the player can win immediately. Not yet implemented.
     *
     * @return false
     */
    public boolean endMovePlayerPossible() {
        return false;
    }

    /**
     * Placeholder for other heuristic checks. Not yet implemented.
     *
     * @return false
     */
    public boolean otherFactPossible() {
        return false;
    }

    /**
     * Determines whether the AI can achieve an almost-winning position (three in a line) in one move.
     *
     * @return true if such a move exists; false otherwise
     */
    public boolean endWinningPositionAIPossible(String pid) {
            for (String pos : getEmptyCellPositions()) {
                String[] parts = pos.split(",");
                int r = Integer.parseInt(parts[0]), c = Integer.parseInt(parts[1]);
                // simulate
                cells[r][c].setValueId(pid);
                if (hasAlmostWinner()) {
                    cells[r][c].setValueId(null);
                    return true;
                }
                // undo
                cells[r][c].setValueId(null);
            }

        return false;
    }

    /**
     * Placeholder: Determines if the player has an almost-winning position. Not yet implemented.
     *
     * @return false
     */
    public boolean endWinningPositionPlayerPossible() {
        return false;
    }

    /**
     * Placeholder: Determines a blocking end move against the opponent. Not yet implemented.
     *
     * @param pid piece ID under consideration
     */
    public void determineBlockEndMove(String pid) {
        // change attributes of move
    }

    /**
     * Placeholder: Determines a blocking move against an opponent's almost-winning position. Not yet implemented.
     *
     * @param pid piece ID under consideration
     */
    public void determineBlockWinningPositionMove(String pid) {
        // change attributes of move
    }

    /**
     * Determines and executes the AI's winning end move if available.
     *
     * @param pid the ID of the piece to place
     */
    public void determineEndMove(String pid) {
        for (String pos : getEmptyCellPositions()) {
            String[] parts = pos.split(",");
            int r = Integer.parseInt(parts[0]), c = Integer.parseInt(parts[1]);

            // simulate placing the actual piece-to-place
            cells[r][c].setValueId(pid);
            if (hasWinner()) {
                // this is the winning move!
                cells[r][c].setValueId(null);
                placePiece(r, c, pid);
                return;    // stop looping—move is made
            }
            // undo only the simulation
            cells[r][c].setValueId(null);
        }
        // change attributes of move
    }

    /**
     * Placeholder: Determines a good heuristic move. Not yet implemented.
     *
     * @param pid piece ID under consideration
     */
    public void determineGoodMove(String pid) {
        // change attributes of move
    }

    /**
     * Selects and places a random available piece in a random empty cell.
     *
     * @param pid the ID of the piece to place
     */
    public void determineRandomMove(String pid) {
        List<String> emptyCells = getEmptyCellPositions();
        Random rnd = new Random();
        String[] parts = emptyCells.get(rnd.nextInt(emptyCells.size())).split(",");
        int r = Integer.parseInt(parts[0]), c = Integer.parseInt(parts[1]);
        placePiece(r, c, pid);
    }

    /**
     * Determines and executes a move that achieves an almost-winning line for the AI.
     *
     * @param pid the ID of the piece to place
     */
    public void determineWinningPositionMove(String pid) {
        for (String pos : getEmptyCellPositions()) {
            String[] parts = pos.split(",");
            int r = Integer.parseInt(parts[0]), c = Integer.parseInt(parts[1]);
            cells[r][c].setValueId(pid);
            if (hasAlmostWinner()) {
                cells[r][c].setValueId(null);
                placePiece(r, c, pid);
                return;
            }
            cells[r][c].setValueId(null);
        }
    }
}
