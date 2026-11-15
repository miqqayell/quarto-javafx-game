package be.kdg.integration2.mvpglobal.model;

import java.time.LocalDateTime;

/**
 * Represents a single move in a game session of the Quarto-like game.
 * <p>
 * Tracks which {@link Player} made the move, the {@link Cell} targeted,
 * timestamps for when the move started and ended, and the sequential move number.
 * </p>
 *
 * @author ACS101 - TEAM 2
 * @version 1.0
 */
public class Move {
    /**
     * The player who is making this move.
     */
    private Player playerMakingMove;

    /**
     * The board cell where the move is applied.
     */
    private Cell cell;

    /**
     * Timestamp when the player started thinking/making this move.
     */
    private LocalDateTime startMove;

    /**
     * Timestamp when the move was completed.
     */
    private LocalDateTime endMove;

    /**
     * Sequential number of this move within the game session.
     */
    private int moveNumber;

    /**
     * Constructs a complete {@code Move} with all properties specified.
     *
     * @param player      the {@link Player} making the move
     * @param cell        the {@link Cell} targeted by the move
     * @param startMove   the timestamp when move thinking began
     * @param endMove     the timestamp when move execution completed
     */
    public Move(Player player, Cell cell, LocalDateTime startMove, LocalDateTime endMove) {
        this.playerMakingMove = player;
        this.cell = cell;
        this.startMove = startMove;
        this.endMove = endMove;
    }

    /**
     * Default constructor for frameworks or serialization.
     * <p>
     * Properties should be set via setters before use.
     * </p>
     */
    public Move() {
        // no-arg constructor
    }

    /**
     * Sets the player who is making this move.
     *
     * @param player the {@link Player} object making the move
     */
    public void setPlayerMakingMove(Player player) {
        this.playerMakingMove = player;
    }

    /**
     * Assigns a sequential number to this move.
     *
     * @param moveNumber the position of this move in the session
     */
    public void setMoveNumber(int moveNumber) {
        this.moveNumber = moveNumber;
    }

    /**
     * Records the completion of the move by setting {@code endMove}
     * to the current timestamp.
     */
    public void finish() {
        this.endMove = LocalDateTime.now();
    }

    /**
     * Sets the timestamp when the move thinking started.
     *
     * @param startMove the {@link LocalDateTime} when move thinking began
     */
    public void setStartMove(LocalDateTime startMove) {
        this.startMove = startMove;
    }

    /**
     * Explicitly sets the timestamp when the move completed.
     *
     * @param endMove the {@link LocalDateTime} when move execution finished
     */
    public void setEndMove(LocalDateTime endMove) {
        this.endMove = endMove;
    }

    /**
     * Retrieves the player who made this move.
     *
     * @return the {@link Player} who executed the move
     */
    public Player getPlayerMakingMove() {
        return playerMakingMove;
    }

    /**
     * Retrieves the board cell targeted by this move.
     *
     * @return the {@link Cell} of this move
     */
    public Cell getCell() {
        return cell;
    }

    /**
     * Retrieves when the move thinking started.
     *
     * @return the {@link LocalDateTime} of move start
     */
    public LocalDateTime getStartMove() {
        return startMove;
    }

    /**
     * Retrieves when the move was completed.
     *
     * @return the {@link LocalDateTime} of move completion
     */
    public LocalDateTime getEndMove() {
        return endMove;
    }

    /**
     * Retrieves the sequential number of this move in the session.
     *
     * @return the integer move number
     */
    public int getMoveNumber() {
        return moveNumber;
    }
}
