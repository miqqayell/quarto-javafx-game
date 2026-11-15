package be.kdg.integration2.mvpglobal.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Manages a single game session between a human player and the computer AI.
 * <p>
 * Tracks the board state, move history, players, timing, and statistics for the session.
 * Provides utilities for calculating durations and formatting timestamps.
 * </p>
 * <p>
 * Note: Calculation of individual move durations is not yet implemented.
 * </p>
 *
 * @author ACS101 - TEAM 2
 * @version 1.0
 */
public class GameSession {

    /** The computer-controlled player. */
    private ComputerPlayer computer;
    /** The human player. */
    private HumanPlayer player;
    /** The game board for this session. */
    private Board board;
    /** The move info.*/
    private Move move;

    /** Unique identifier for this game session. */
    private int gameID;
    /** Counter for moves made by the human player. */
    private int playerMovesCounter;
    /** Counter for moves made by the computer player. */
    private int computerMovesCounter;
    /** Timestamp when the game session started. */
    private LocalDateTime startTime;
    /** Timestamp when the game session ended. */
    private LocalDateTime endTime;
    /** History of all moves made during the session. */
    private LinkedList<Move> moveHistory;
    /** List of piece types used or available during the session. */
    private List<PieceType> pieceType;
    /** List of durations of the moves for current session. */
    private ArrayList<Integer> movesDurationStorage;

    /**
     * Constructs a new GameSession for the given human player.
     * Initializes board, AI player, move history, and counters.
     *
     * @param player the human player participating in this session
     */
    public GameSession(HumanPlayer player) {
        this.move = new Move();
        this.board = new Board();
        this.player = player;
        this.computer = new ComputerPlayer();
        this.moveHistory = new LinkedList<>();
        this.pieceType = new ArrayList<>();
        this.movesDurationStorage = new ArrayList<>();
        this.playerMovesCounter = 0;
        this.computerMovesCounter = 0;
    }

    /**
     * Starts or continues the game play loop.
     * <p>
     * Determines the AI's next move and applies it to the board.
     * Actual loop control and human move logic are not shown here.
     * </p>
     */
    public void play() {
        // ...
        // Determine move for AI
        Move move = computer.getMove(this);
        // ...
    }

    /**
     * Calculates the total game duration between two timestamps.
     *
     * @param startTime the timestamp when the game started
     * @param endTime   the timestamp when the game ended
     * @return formatted duration string in "minutes:seconds" (e.g., "2:05")
     */
    public String calculateGameDuration(LocalDateTime startTime, LocalDateTime endTime) {
        Duration duration = Duration.between(startTime, endTime);
        long totalSeconds = duration.getSeconds();
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;

        return String.format("%d:%02d", minutes, seconds);
    }

    /**
     * Calculates the duration of the most recent move.
     * <p>
     * TODO: Implement logic to compute move duration based on move timestamps.
     * </p>
     *
     * @return move duration in seconds; currently returns 0
     */
    public int calculateMoveDuration() {
        if (move.getStartMove() == null || move.getEndMove() == null) {
            return 0;
        }
        Duration duration = Duration.between(move.getStartMove(), move.getEndMove());
        return (int) duration.getSeconds();
    }

    /**
     * Formats a LocalDateTime to a standard string pattern.
     *
     * @param time the date-time to format
     * @return formatted string in "yyyy-MM-dd HH:mm:ss" pattern
     */
    public String formatTime(LocalDateTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return time.format(formatter);
    }

    public String calculateAvgMoveDuration() {
        if (movesDurationStorage == null || movesDurationStorage.isEmpty()) {
            return "0:00";
        }

        int sum = 0;
        for (int num : movesDurationStorage) {
            sum += num;
        }

        double average = (double) sum / movesDurationStorage.size();
        int roundedAverage = (int) Math.round(average);

        int minutes = roundedAverage / 60;
        int seconds = roundedAverage % 60;

        return String.format("%d:%02d", minutes, seconds);
    }

    /** @return the game board for this session */
    public Board getBoard() {
        return board;
    }

    /** @return the history of moves made */
    public LinkedList<Move> getMoveHistory() {
        return moveHistory;
    }

    /** @param moveHistory the move history to set */
    public void setMoveHistory(LinkedList<Move> moveHistory) {
        this.moveHistory = moveHistory;
    }

    /** @return the list of piece types in use */
    public List<PieceType> getPieceType() {
        return pieceType;
    }

    /** @param pieceType the list of piece types to set */
    public void setPieceType(List<PieceType> pieceType) {
        this.pieceType = pieceType;
    }

    /** @return the unique game session ID */
    public int getGameID() {
        return gameID;
    }

    /** @return the timestamp when the session started */
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /** @return the timestamp when the session ended */
    public LocalDateTime getEndTime() {
        return endTime;
    }

    /** @param startTime the start time to set */
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    /** @param endTime the end time to set */
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    /** @param gameID the game session ID to set */
    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    /** @param player the human player to set */
    public void setPlayer(HumanPlayer player) {
        this.player = player;
    }

    /** @return the number of moves made by the computer */
    public int getComputerMovesCounter() {
        return computerMovesCounter;
    }

    /** @return the number of moves made by the human player */
    public int getPlayerMovesCounter() {
        return playerMovesCounter;
    }

    /** @param computerMovesCounter the computer moves counter to set */
    public void setComputerMovesCounter(int computerMovesCounter) {
        this.computerMovesCounter = computerMovesCounter;
    }

    /** @param playerMovesCounter the human player moves counter to set */
    public void setPlayerMovesCounter(int playerMovesCounter) {
        this.playerMovesCounter = playerMovesCounter;
    }

    public Move getMove() {
        return move;
    }

    public ArrayList<Integer> getMovesDurationStorage() {
        return movesDurationStorage;
    }
}
