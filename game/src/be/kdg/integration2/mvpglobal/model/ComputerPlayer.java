package be.kdg.integration2.mvpglobal.model;

import be.kdg.integration2.mvpglobal.model.rulebasedsystem.InferenceEngine;
import java.time.LocalDateTime;

/**
 * Represents an AI-controlled player in the Quarto-like game.
 * <p>
 * The {@code ComputerPlayer} extends {@link Player} and uses an inference engine
 * to determine moves based on game rules and heuristics. It timestamps
 * the move start and end times for performance tracking.
 * </p>
 *
 * @author ACS101 - TEAM 2
 * @version 1.0
 */
public class ComputerPlayer extends Player {

    /**
     * Constructs a new {@code ComputerPlayer} with default settings.
     */
    public ComputerPlayer() {
        super();
        // Additional AI initialization can be placed here
    }

    /**
     * Determines the next move for this computer player.
     * <p>
     * The method constructs a new {@link Move}, tags it with this player,
     * records the start time, applies inference rules to analyze the board,
     * and records the finish time. Currently, rule application is stubbed out.
     * </p>
     *
     * @param gameSession the current {@link GameSession} containing the board state
     * @return a {@link Move} object representing the AI's chosen move, with timestamps set
     */
    public Move getMove(GameSession gameSession) {
        Board board = gameSession.getBoard();

        Move move = new Move();
        // 1) Tag the move as made by this player
        move.setPlayerMakingMove(this);
        // 2) Record when thinking started
        move.setStartMove(LocalDateTime.now());

        // TODO: Use the inference engine to determine and apply rules
        // InferenceEngine engine = new InferenceEngine();
        // engine.determineFacts(board);
        // engine.applyRules(board, move);

        // 3) Record when thinking completed
        move.finish();

        return move;
    }
}
