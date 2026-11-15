package be.kdg.integration2.mvpglobal.model;

import javafx.concurrent.Task;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Manages the JDBC connection and CRUD operations for player and game data
 * in a PostgreSQL database for the Quarto-like game.
 * <p>
 * Provides methods to open and close the database connection, insert and query
 * player records, verify credentials, and record game sessions.
 * </p>
 * <p>
 * Passwords are stored as salted PBKDF2 hashes (see {@link PasswordHasher})
 * and all queries use bound parameters.
 * </p>
 *
 * @author ACS101 - TEAM 2
 * @version 1.0
 */
public class DatabaseConnection {
    /**
     * JDBC URL for connecting to the PostgreSQL database.
     * Read from the {@code QUARTO_DB_URL} environment variable.
     */
    private static final String URL = env("QUARTO_DB_URL", "jdbc:postgresql://localhost:5432/quarto");

    /**
     * Database user name. Read from {@code QUARTO_DB_USER}.
     */
    private static final String USER = env("QUARTO_DB_USER", "quarto");

    /**
     * Database user password. Read from {@code QUARTO_DB_PASSWORD}.
     */
    private static final String PASSWORD = env("QUARTO_DB_PASSWORD", "quarto");

    private static String env(String key, String fallback) {
        String value = System.getenv(key);
        return (value == null || value.isBlank()) ? fallback : value;
    }

    /**
     * Active JDBC connection; null if not yet established or closed.
     */
    private Connection connection;

    /**
     * Retrieves an active JDBC {@link Connection}, opening a new one if necessary.
     *
     * @return an open {@link Connection}
     * @throws RuntimeException if a database access error occurs
     */
    public Connection getConnection() throws SQLException {
        Task<Connection> task = new Task<>() {
            protected Connection call() throws Exception {
                DriverManager.setLoginTimeout(1); // 1 second timeout
                return DriverManager.getConnection(URL, USER, PASSWORD);
            }
        };
        new Thread(task).start();
        try {
            return task.get(1, TimeUnit.SECONDS);
        } catch (Exception e) {
            EventManager.triggerError("Connection Timeout", "Could not connect to the database!");
            throw new SQLException("Connection timeout", e);
        }
    }

    /**
     * Closes the current database connection if it is open.
     */
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                System.out.println("Error closing database connection: " + e.getMessage());
            }
        }
    }

    /**
     * Inserts a new player record into the database.
     *
     * @param playerName the unique username of the player
     * @param password   the player's raw password (stored as a salted PBKDF2 hash)
     * @param country    the player's country of origin
     * @return the generated player ID, or -1 if insertion failed
     */
    public int insertPlayer(String playerName, String password, String country) {
        String sql = "INSERT INTO players (player_name, password, player_country) VALUES (?, ?, ?) RETURNING player_id";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setString(1, playerName);
            ps.setString(2, PasswordHasher.hash(password));
            ps.setString(3, country);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("player_id");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error inserting player: " + e.getMessage());
        }
        return -1;
    }

    /**
     * Checks whether a username already exists in the database.
     *
     * @param username the username to check
     * @return {@code true} if the username exists; {@code false} otherwise
     */
    public boolean checkUsername(String username) {
        String query = "SELECT player_name FROM players WHERE player_name = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.out.println("Error checking username: " + e.getMessage());
            return false;
        }
    }

    /**
     * Verifies that the provided password matches the stored password for a username.
     *
     * @param username the player's username
     * @param password the password to verify
     * @return {@code true} if the password matches; {@code false} otherwise
     */
    public boolean checkPassword(String username, String password) {
        String query = "SELECT password FROM players WHERE player_name = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String stored = rs.getString("password");
                    return PasswordHasher.verify(password, stored);
                }
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error checking password: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves the player ID for a given username.
     *
     * @param username the player's username
     * @return the player ID, or -1 if not found or on error
     */
    public int getPlayerIdByUsername(String username) {
        String query = "SELECT player_id FROM players WHERE player_name = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("player_id");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching player ID: " + e.getMessage());
        }
        return -1;
    }

    /**
     * Retrieves the country of a player by their ID.
     *
     * @param id the player ID
     * @return the player's country code, or {@code null} if not found or on error
     */
    public String getPlayerCountryByID(int id) {
        String query = "SELECT player_country FROM players WHERE player_id = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("player_country");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching player country: " + e.getMessage());
        }
        return null;
    }

    /**
     * Retrieves the username of a player by their ID.
     *
     * @param id the player ID
     * @return the player's username, or {@code null} if not found or on error
     */
    public String getNameById(int id) {
        String query = "SELECT player_name FROM players WHERE player_id = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("player_name");
                }
            }
        } catch (SQLException e) {
            System.out.println("Failed to get the player's name: " + e.getMessage());
        }
        return null;
    }

    /**
     * Inserts a game session record into the database.
     *
     * @param playerId  the ID of the player who played
     * @param startTime the timestamp when the game started
     * @param endTime   the timestamp when the game ended
     * @param outcome   the outcome of the game (e.g., "WIN", "LOSS", "DRAW")
     * @return the generated game ID, or -1 if insertion failed
     */
    public int insertGameInfo(int playerId, LocalDateTime startTime, LocalDateTime endTime, String outcome) {
        String sql = "INSERT INTO game_info (player_id, start_time, end_time, game_outcome) VALUES (?, ?, ?, ?) RETURNING game_id";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, playerId);
            ps.setObject(2, Timestamp.valueOf(startTime));
            ps.setObject(3, Timestamp.valueOf(endTime));
            ps.setString(4, outcome);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("game_id");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error inserting game result: " + e.getMessage());
        }
        return -1;
    }

    public int getLastGameId() {
        String query = "SELECT game_id FROM game_info ORDER BY game_id DESC LIMIT 1";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("game_id");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error getting game ID: " + e.getMessage());
        }
        return -1;
    }

    public int insertMoveInfo(int gameId, ArrayList<Integer> moves) {
        String query = "INSERT INTO move_info (game_id, duration) VALUES (?, ?)";
        int insertedCount = 0;

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            for (int moveDuration : moves) {
                ps.setInt(1, gameId);
                ps.setInt(2, moveDuration);
                int result = ps.executeUpdate();
                if (result >= 0) {
                    insertedCount++;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error inserting move info: " + e.getMessage());
        }

        return insertedCount;
    }

    public List<PlayerStats> getLeaderboardStats(String sortBy, boolean ascending) {
        List<PlayerStats> leaderboard = new ArrayList<>();
        String orderDirection = ascending ? "ASC" : "DESC";

        String orderColumn;
        switch (sortBy) {
            case "wins":
                orderColumn = "wins";
                break;
            case "losses":
                orderColumn = "losses";
                break;
            case "win_percentage":
                orderColumn = "win_percentage";
                break;
            case "avg_moves":
                orderColumn = "avg_moves";
                break;
            case "avg_move_duration":
                orderColumn = "avg_move_duration_seconds";
                break;
            case "games_played":
            default:
                orderColumn = "games_played";
        }

        String query =
                """
                    SELECT
                        p.player_name,
                        COALESCE(COUNT(g.game_id), 0) AS games_played,
                        COALESCE(SUM(CASE WHEN UPPER(g.game_outcome) = 'WIN' THEN 1 ELSE 0 END), 0) AS wins,
                        COALESCE(SUM(CASE WHEN UPPER(g.game_outcome) = 'LOSE' THEN 1 ELSE 0 END), 0) AS losses,
                        ROUND(
                            COALESCE(
                                CAST(SUM(CASE WHEN UPPER(g.game_outcome) = 'WIN' THEN 1 ELSE 0 END) AS numeric)
                                / NULLIF(COUNT(g.game_id), 0) * 100,
                                0
                            ), 2
                        ) AS win_percentage,
                        COALESCE(ROUND(AVG(m.move_count), 2), 0) AS avg_moves,
                        COALESCE(ROUND(AVG(m.avg_duration), 2), 0) AS avg_move_duration_seconds
                    FROM players p
                    LEFT JOIN game_info g ON p.player_id = g.player_id
                    LEFT JOIN (
                        SELECT game_id, COUNT(*) AS move_count, AVG(duration) AS avg_duration
                        FROM move_info
                        GROUP BY game_id
                    ) m ON g.game_id = m.game_id
                    GROUP BY p.player_id, p.player_name
                """;

        query += " ORDER BY " + orderColumn + " " + orderDirection;

        try (PreparedStatement ps = getConnection().prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            int rank = 1;
            while (rs.next()) {
                String name = rs.getString("player_name");
                int games = rs.getInt("games_played");
                int wins = rs.getInt("wins");
                int losses = rs.getInt("losses");
                double winPercentage = rs.getDouble("win_percentage");
                double avgMoves = rs.getDouble("avg_moves");
                double avgMoveDuration = rs.getDouble("avg_move_duration_seconds");

                String winLoss = wins + "/" + losses;

                PlayerStats stats = new PlayerStats(
                        rank++, name, games, winLoss, winPercentage, avgMoves, avgMoveDuration);
                leaderboard.add(stats);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching leaderboard stats: " + e.getMessage());
        }

        return leaderboard;
    }
}
