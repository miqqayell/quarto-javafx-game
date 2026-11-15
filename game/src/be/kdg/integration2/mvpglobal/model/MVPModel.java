package be.kdg.integration2.mvpglobal.model;

import be.kdg.integration2.mvpglobal.model.rulebasedsystem.InferenceEngine;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

/**
 * The Model component in the MVP architecture for the Quarto-like game.
 * <p>
 * Manages the human player, game session lifecycle, database interactions,
 * and the rule-based inference engine. Provides accessors for game state
 * and operations such as placing pieces, resetting the board, and user validation.
 * </p>
 *
 * @author ACS101 - TEAM 2
 * @version 1.0
 */
public class MVPModel {

    /** The human player participating in the game. */
    private HumanPlayer player;

    /** The current game session. */
    private GameSession gameSession;

    /** Database connection for persisting player and game data. */
    private DatabaseConnection db;

    private final SoundManager soundManager;

    /** Rule-based inference engine for AI decision support. */
    private InferenceEngine inferenceEngine;

    private PlayerStats playerStats;

    public PlayerStats getPlayerStats() {
        return playerStats;
    }

    /**
     * Creates a new MVPModel, initializing the database connection
     * and inference engine. Further setup (e.g., player registration)
     * should follow.
     */
    public MVPModel() {
        this.db = new DatabaseConnection();
        this.inferenceEngine = new InferenceEngine();
        this.soundManager = SoundManager.getInstance();
        // complete the code here
    }

    /**
     * Starts a new game session for the currently stored human player,
     * initializing the game board and recording the start timestamp.
     */
    public void startGameSession() {
        this.gameSession = new GameSession(player);
        this.gameSession.setStartTime(LocalDateTime.now());
    }

    /**
     * Returns the configured inference engine for rule evaluation.
     *
     * @return the {@link InferenceEngine} instance
     */
    public InferenceEngine getInferenceEngine() {
        return inferenceEngine;
    }

    /**
     * Retrieves the list of piece types used or available in the game session.
     *
     * @return the list of {@link PieceType} for the session
     */
    public List<PieceType> getPieceTypes() {
        return gameSession.getPieceType();
    }

    /**
     * Registers and stores a human player in the model.
     *
     * @param username the unique username of the player
     * @param password the player's password
     * @param country  the player's country of origin
     * @param id       the unique identifier for the player
     */
    public void storePlayer(String username, String password, String country, int id) {
        player = new HumanPlayer(username, password, country, id);
    }


    public SoundManager getSoundManager() {
        return soundManager;
    }

    /**
     * Gets the grid of cells for the current board.
     *
     * @return a 2D array of {@link Cell} objects
     */
    public Cell[][] getCells() {
        return gameSession.getBoard().getCells();
    }

    /**
     * Retrieves the current game board object.
     *
     * @return the {@link Board} for the active session
     */
    public Board getBoard() {
        return gameSession.getBoard();
    }

    /**
     * Provides access to the database connection.
     *
     * @return the {@link DatabaseConnection} instance
     */
    public DatabaseConnection getDb() {
        return db;
    }

    /**
     * Returns the human player stored in this model.
     *
     * @return the {@link HumanPlayer} instance
     */
    public HumanPlayer getPlayer() {
        return player;
    }

    /**
     * Retrieves the active game session.
     *
     * @return the {@link GameSession} object
     */
    public GameSession getGameSession() {
        return gameSession;
    }

    /**
     * Returns IDs of pieces still available for placement.
     *
     * @return list of available piece ID strings
     */
    public List<String> getAvailablePieceIds() {
        return gameSession.getBoard().getAvailablePieceIds();
    }

    /**
     * Checks if the current board has a winning configuration.
     *
     * @return {@code true} if a winner exists; {@code false} otherwise
     */
    public boolean hasWinner() {
        return gameSession.getBoard().hasWinner();
    }

    /**
     * Resets the game board to its initial empty state.
     */
    public void reset() {
        gameSession.getBoard().reset();
    }

    /**
     * Selects a random available piece ID for give-turn logic.
     *
     * @return a randomly chosen piece ID
     */
    public String getCurrentGivenPiece() {
        Random rnd = new Random();
        List<String> avail = getAvailablePieceIds();
        return avail.get(rnd.nextInt(avail.size()));
    }

    /**
     * Attempts to place the specified piece on the board and records the move.
     *
     * @param row     the row index (0-based)
     * @param col     the column index (0-based)
     * @param pieceId the ID of the piece to place
     * @return {@code true} if placement succeeded; {@code false} otherwise
     */
    public boolean placePiece(int row, int col, String pieceId) {
        boolean success = gameSession.getBoard().placePiece(row, col, pieceId);
        gameSession.setEndTime(LocalDateTime.now());
        gameSession.getMoveHistory().add(new Move());
        return success;
    }

    /**
     * Retrieves positions of all empty cells on the board.
     *
     * @return list of strings in "row,col" format
     */
    public List<String> getEmptyCellPositions() {
        return gameSession.getBoard().getEmptyCellPositions();
    }

    /**
     * Validates a username according to length constraints.
     *
     * @param username the username to validate
     * @return {@code true} if valid (3–30 chars); {@code false} otherwise
     */
    public boolean validateUsername(String username) {
        return username != null && !username.isEmpty()
                && username.length() >= 3 && username.length() <= 30;
    }
}
