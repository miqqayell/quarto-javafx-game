package be.kdg.integration2.mvpglobal.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board();
    }

    @Test
    void pieceFactoryCreatesSixteenUniquePieces() {
        List<String> ids = PieceFactory.allPieceIds();
        assertEquals(16, ids.size());
        assertEquals(16, new HashSet<>(ids).size());
        assertTrue(ids.contains("light-round-tall-solid"));
    }

    @Test
    void newBoardIsEmptyAndHasNoWinner() {
        assertEquals(16, board.getEmptyCellPositions().size());
        assertEquals(16, board.getAvailablePieceIds().size());
        assertFalse(board.hasWinner());
    }

    @Test
    void placePieceRejectsOccupiedCellUsedPieceAndOutOfBounds() {
        assertTrue(board.placePiece(0, 0, "light-round-tall-solid"));
        assertFalse(board.placePiece(0, 0, "dark-round-tall-solid"), "cell already occupied");
        assertFalse(board.placePiece(1, 1, "light-round-tall-solid"), "piece already used");
        assertFalse(board.placePiece(4, 0, "dark-round-tall-solid"), "out of bounds");
        assertEquals(15, board.getAvailablePieceIds().size());
    }

    @Test
    void rowOfFourSharingOneTraitWins() {
        // all four are "light", every other trait differs at least once
        board.placePiece(0, 0, "light-round-tall-solid");
        board.placePiece(0, 1, "light-square-short-hollow");
        board.placePiece(0, 2, "light-round-short-solid");
        assertFalse(board.hasWinner(), "three pieces are not a win");
        board.placePiece(0, 3, "light-square-tall-hollow");
        assertTrue(board.hasWinner());
    }

    @Test
    void columnAndDiagonalsAreChecked() {
        Board column = new Board();
        column.placePiece(0, 2, "dark-round-tall-solid");
        column.placePiece(1, 2, "light-round-short-hollow");
        column.placePiece(2, 2, "dark-round-short-solid");
        column.placePiece(3, 2, "light-round-tall-hollow");
        assertTrue(column.hasWinner(), "column sharing 'round'");

        Board antiDiagonal = new Board();
        antiDiagonal.placePiece(0, 3, "dark-round-tall-hollow");
        antiDiagonal.placePiece(1, 2, "light-square-short-hollow");
        antiDiagonal.placePiece(2, 1, "dark-square-tall-hollow");
        antiDiagonal.placePiece(3, 0, "light-round-short-hollow");
        assertTrue(antiDiagonal.hasWinner(), "anti-diagonal sharing 'hollow'");
    }

    @Test
    void fullLineWithoutCommonTraitDoesNotWin() {
        board.placePiece(0, 0, "light-round-tall-solid");
        board.placePiece(0, 1, "dark-square-short-hollow");
        board.placePiece(0, 2, "light-square-tall-hollow");
        board.placePiece(0, 3, "dark-round-short-solid");
        assertFalse(board.hasWinner());
    }

    @Test
    void endMoveDetectionDoesNotChangeTheBoard() {
        board.placePiece(0, 0, "light-round-tall-solid");
        board.placePiece(0, 1, "light-square-short-hollow");
        board.placePiece(0, 2, "light-round-short-solid");

        assertTrue(board.endMoveAIPossible("light-square-tall-hollow"));
        assertFalse(board.endMoveAIPossible("dark-square-tall-hollow"));
        assertEquals(13, board.getEmptyCellPositions().size(), "simulation must be undone");
    }
}
