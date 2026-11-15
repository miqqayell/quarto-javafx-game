package be.kdg.integration2.mvpglobal.model;

/**
 * Represents a human-controlled player in the Quarto-like game.
 * <p>
 * Stores user credentials, country of origin, and a unique player identifier.
 * </p>
 * <p>
 * Note: the raw password is only kept in memory for the session; the database
 * stores only a salted hash.
 * </p>
 *
 * @author ACS101 - TEAM 2
 * @version 1.0
 */
public class HumanPlayer extends Player {
    /**
     * The unique username of the player.
     */
    private String username;

    /**
     * The player's password for this session (the database stores only a hash).
     */
    private String password;

    /**
     * The country code or name of the player's origin.
     */
    private String country;

    /**
     * Unique identifier for the player.
     */
    private int id;

    /**
     * Constructs a new, empty HumanPlayer.
     */
    public HumanPlayer() {
        super();
    }

    /**
     * Constructs a new HumanPlayer with the specified attributes.
     *
     * @param username the unique username of the player
     * @param password the player's password
     * @param country  the player's country of origin
     * @param id       the unique identifier for this player
     */
    public HumanPlayer(String username, String password, String country, int id) {
        super();
        this.username = username;
        this.password = password;
        this.country = country;
        this.id = id;
    }

    /**
     * Placeholder for any initialization logic after player creation.
     * <p>
     * TODO: Implement player creation workflow (e.g., database registration).
     * </p>
     */
    public void playerCreation() {
        // TODO: implement registration logic
    }

    /**
     * Retrieves the player's username.
     *
     * @return the username string
     */
    public String getUsername() {
        return username;
    }

    /**
     * Retrieves the player's country of origin.
     *
     * @return the country string
     */
    public String getCountry() {
        return country;
    }

    /**
     * Retrieves the player's password.
     *
     * @return the password string
     */
    public String getPassword() {
        return password;
    }

    /**
     * Retrieves the player's unique identifier.
     *
     * @return the player ID
     */
    public int getID() {
        return id;
    }

    /**
     * Updates the player's username.
     *
     * @param username the new username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Updates the player's country of origin.
     *
     * @param country the new country string to set
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Updates the player's password.
     *
     * @param password the new password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Updates the player's unique identifier.
     *
     * @param id the new player ID to set
     */
    public void setID(int id) {
        this.id = id;
    }
}
