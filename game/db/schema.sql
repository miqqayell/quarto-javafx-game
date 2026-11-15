-- Quarto game database schema.
-- Reconstructed from the SQL used in DatabaseConnection.java and website/query.sql;
-- the original database ran on the university network.

CREATE TABLE IF NOT EXISTS players (
    player_id      SERIAL PRIMARY KEY,
    player_name    VARCHAR(50) NOT NULL UNIQUE,
    password       VARCHAR(255) NOT NULL,   -- salted PBKDF2 hash (see PasswordHasher.java)
    player_country VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS game_info (
    game_id      SERIAL PRIMARY KEY,
    player_id    INTEGER NOT NULL REFERENCES players (player_id) ON DELETE CASCADE,
    start_time   TIMESTAMP NOT NULL,
    end_time     TIMESTAMP,
    game_outcome VARCHAR(10)                -- 'Win', 'Lose' or 'Draw'
);

CREATE TABLE IF NOT EXISTS move_info (
    move_id  SERIAL PRIMARY KEY,
    game_id  INTEGER NOT NULL REFERENCES game_info (game_id) ON DELETE CASCADE,
    duration INTEGER NOT NULL               -- seconds spent on the move
);
