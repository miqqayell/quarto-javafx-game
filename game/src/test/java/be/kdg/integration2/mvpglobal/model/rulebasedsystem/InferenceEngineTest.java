package be.kdg.integration2.mvpglobal.model.rulebasedsystem;

import be.kdg.integration2.mvpglobal.model.Board;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InferenceEngineTest {

    @Test
    void aiTakesTheWinningMoveWhenOneExists() {
        Board board = new Board();
        board.placePiece(0, 0, "light-round-tall-solid");
        board.placePiece(0, 1, "light-square-short-hollow");
        board.placePiece(0, 2, "light-round-short-solid");
        String piece = "light-square-tall-hollow";

        InferenceEngine engine = new InferenceEngine();
        engine.determineFacts(board, piece);
        engine.applyRules(board, piece);

        assertEquals(piece, board.getCells()[0][3].getValueId(), "AI should complete the row");
        assertTrue(board.hasWinner());
    }

    @Test
    void aiAlwaysPlacesThePieceItWasGiven() {
        Board board = new Board();
        String piece = "dark-round-tall-solid";

        InferenceEngine engine = new InferenceEngine();
        engine.determineFacts(board, piece);
        engine.applyRules(board, piece);

        assertEquals(15, board.getEmptyCellPositions().size());
        assertFalse(board.getAvailablePieceIds().contains(piece));
    }
}
