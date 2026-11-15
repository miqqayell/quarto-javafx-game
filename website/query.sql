--Select game info
SELECT
    gi.game_id AS ID,
    gi.start_time AS start_date,
    p.player_name AS username,
    gi.game_outcome,
    (LOWER(gi.game_outcome) = 'win') AS is_win,
    COUNT(mi.move_id) AS total_moves,
    ROUND(AVG(mi.duration), 2) AS avg_move_duration
FROM game_info gi
         JOIN players p ON gi.player_id = p.player_id
         JOIN move_info mi ON gi.game_id = mi.game_id
GROUP BY gi.game_id, gi.start_time, p.player_name, gi.game_outcome;

--Select user`s info
SELECT
    p.player_name AS username,
    p.password AS password,
    CASE
        WHEN COUNT(g.game_id) > 10 THEN 'admin'
        ELSE 'player'
    END AS role
FROM players p
LEFT JOIN game_info g ON p.player_id = g.player_id
GROUP BY p.player_name, p.password;
