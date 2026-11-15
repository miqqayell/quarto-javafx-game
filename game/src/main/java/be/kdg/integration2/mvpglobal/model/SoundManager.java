package be.kdg.integration2.mvpglobal.model;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.net.URL;

/**
 * Singleton manager for handling sound effects and background music.
 * Provides methods to play, stop, and adjust volume levels for various sound categories.
 */
public class SoundManager {
    /**
     * Singleton instance of SoundManager.
     */
    private static SoundManager instance;

    /** MediaPlayer for background music. */
    private MediaPlayer backgroundPlayer;
    /** MediaPlayer for button click sound. */
    private MediaPlayer buttonClickPlayer;
    /** MediaPlayer for piece placed sound. */
    private MediaPlayer piecePlacedPlayer;
    /** MediaPlayer for lose game sound. */
    private MediaPlayer loseGamePlayer;
    /** MediaPlayer for win game sound. */
    private MediaPlayer winGamePlayer;

    /** Master volume level (0–100). */
    private int mainVolume = 100;
    /** Background music volume level (0–100). */
    private int backgroundVolume = 50;
    /** Button click sound volume level (0–100). */
    private int buttonClickVolume = 70;
    /** Piece placed sound volume level (0–100). */
    private int piecePlacedVolume = 70;
    /** Win/lose sound volume level (0–100). */
    private int winLoseVolume = 80;

    /** Flag indicating if background music is currently playing. */
    private boolean isBackgroundPlaying = false;

    /** Classpath locations of the sound files. The background track is optional (not shipped). */
    private static final String BACKGROUND_MUSIC = "/sounds/bgMusic.mp3";
    private static final String BUTTON_CLICK = "/sounds/buttonClick.mp3";
    private static final String PIECE_PLACED = "/sounds/piecePlace.mp3";
    private static final String LOSE_GAME = "/sounds/loseGame.mp3";
    private static final String WIN_GAME = "/sounds/winGame.mp3";

    /**
     * Private constructor to enforce singleton pattern.
     * Initializes the MediaPlayer instances and loads sound resources.
     */
    private SoundManager() {
        initializeSounds();
    }

    /**
     * Retrieves the singleton instance of SoundManager.
     *
     * @return the single SoundManager instance
     */
    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    /**
     * Loads all media files and creates MediaPlayer instances.
     * Exceptions during loading are caught silently.
     */
    private void initializeSounds() {
        backgroundPlayer = load(BACKGROUND_MUSIC);
        if (backgroundPlayer != null) {
            backgroundPlayer.setVolume(0.5);
            backgroundPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        }
        buttonClickPlayer = load(BUTTON_CLICK);
        winGamePlayer = load(WIN_GAME);
        loseGamePlayer = load(LOSE_GAME);
        piecePlacedPlayer = load(PIECE_PLACED);
    }

    /**
     * Loads one sound from the classpath. Returns {@code null} when the file is missing
     * or cannot be played, so one missing sound does not disable the others.
     */
    private MediaPlayer load(String resource) {
        URL url = SoundManager.class.getResource(resource);
        if (url == null) {
            return null;
        }
        try {
            return new MediaPlayer(new Media(url.toExternalForm()));
        } catch (Exception e) {
            return null;
        }
    }

    private static void restart(MediaPlayer player) {
        if (player != null) {
            player.seek(Duration.ZERO);
            player.play();
        }
    }

    /**
     * Returns the raw background volume setting (0–100).
     *
     * @return the stored background volume level
     */
    public int getRawBackgroundVolume() { return backgroundVolume; }

    /**
     * Returns the raw button click volume setting (0–100).
     *
     * @return the stored button click volume level
     */
    public int getRawButtonClickVolume() { return buttonClickVolume; }

    /**
     * Returns the raw piece placed volume setting (0–100).
     *
     * @return the stored piece placed volume level
     */
    public int getRawPiecePlacedVolume() { return piecePlacedVolume; }

    /**
     * Returns the raw win/lose volume setting (0–100).
     *
     * @return the stored win/lose volume level
     */
    public int getRawWinLoseVolume() { return winLoseVolume; }

    /**
     * Plays the background music if it is not already playing.
     */
    public void playBackgroundMusic() {
        if (!isBackgroundPlaying && backgroundPlayer != null) {
            stopAllMusic();
            backgroundPlayer.play();
            isBackgroundPlaying = true;
        }
    }

    /**
     * Plays the button click sound effect from the beginning.
     */
    public void playButtonClick() {
        restart(buttonClickPlayer);
    }

    /**
     * Plays the lose game sound effect from the beginning.
     */
    public void playLoseGame() {
        restart(loseGamePlayer);
    }

    /**
     * Plays the win game sound effect from the beginning.
     */
    public void playWinGame() {
        restart(winGamePlayer);
    }

    /**
     * Stops the currently playing background music and resets its position.
     */
    public void stopBackgroundMusic() {
        if (backgroundPlayer != null) {
            backgroundPlayer.seek(Duration.ZERO);
        }
        isBackgroundPlaying = false;
    }

    /**
     * Plays the piece placed sound effect from the beginning.
     */
    public void playPiecePlace() {
        restart(piecePlacedPlayer);
    }

    /**
     * Gets the master volume level.
     *
     * @return the master volume (0–100)
     */
    public int getMainVolume() {
        return mainVolume;
    }

    /**
     * Sets the master volume (0–100) and updates all sound volumes accordingly.
     *
     * @param volume the new master volume level
     */
    public void setMainVolume(int volume) {
        this.mainVolume = Math.max(0, Math.min(100, volume));
        updateAllVolumes();
    }

    /**
     * Gets the current background music volume level (0–100).
     *
     * @return the current background music volume
     */
    public int getBackgroundVolume() {
        if (backgroundPlayer != null) {
            return (int) (backgroundPlayer.getVolume() * 100);
        }
        return backgroundVolume;
    }

    /**
     * Sets the background music volume (0–100) relative to the master volume.
     *
     * @param volume the new background volume level
     */
    public void setBackgroundVolume(int volume) {
        this.backgroundVolume = Math.max(0, Math.min(100, volume));
        if (backgroundPlayer != null) {
            backgroundPlayer.setVolume(this.backgroundVolume * this.mainVolume / 10000.0);
        }
    }

    /**
     * Gets the current button click sound volume level (0–100).
     *
     * @return the current button click volume
     */
    public int getButtonClickVolume() {
        if (buttonClickPlayer != null) {
            return (int) (buttonClickPlayer.getVolume() * 100);
        }
        return buttonClickVolume;
    }

    /**
     * Sets the button click sound volume (0–100) relative to the master volume.
     *
     * @param volume the new button click volume level
     */
    public void setButtonClickVolume(int volume) {
        this.buttonClickVolume = Math.max(0, Math.min(100, volume));
        if (buttonClickPlayer != null) {
            buttonClickPlayer.setVolume(this.buttonClickVolume * this.mainVolume / 10000.0);
        }
    }

    /**
     * Gets the current piece placed sound volume level (0–100).
     *
     * @return the current piece placed volume
     */
    public int getPiecePlacedVolume() {
        if (piecePlacedPlayer != null) {
            return (int) (piecePlacedPlayer.getVolume() * 100);
        }
        return piecePlacedVolume;
    }

    /**
     * Sets the piece placed sound volume (0–100) relative to the master volume.
     *
     * @param volume the new piece placed volume level
     */
    public void setPiecePlacedVolume(int volume) {
        this.piecePlacedVolume = Math.max(0, Math.min(100, volume));
        if (piecePlacedPlayer != null) {
            piecePlacedPlayer.setVolume(this.piecePlacedVolume * this.mainVolume / 10000.0);
        }
    }

    /**
     * Gets the current win/lose sound volume level (0–100).
     *
     * @return the current win/lose volume
     */
    public int getWinLoseVolume() {
        if (winGamePlayer != null) {
            return (int) (winGamePlayer.getVolume() * 100);
        }
        return winLoseVolume;
    }

    /**
     * Sets the win/lose sound volume (0–100) relative to the master volume
     * for both win and lose sound effects.
     *
     * @param volume the new win/lose volume level
     */
    public void setWinLoseVolume(int volume) {
        this.winLoseVolume = Math.max(0, Math.min(100, volume));
        if (winGamePlayer != null) {
            winGamePlayer.setVolume(this.winLoseVolume * this.mainVolume / 10000.0);
        }
        if (loseGamePlayer != null) {
            loseGamePlayer.setVolume(this.winLoseVolume * this.mainVolume / 10000.0);
        }
    }

    /**
     * Updates volume levels for all MediaPlayer instances based on current settings.
     */
    private void updateAllVolumes() {
        setBackgroundVolume(this.backgroundVolume);
        setButtonClickVolume(this.buttonClickVolume);
        setPiecePlacedVolume(this.piecePlacedVolume);
        setWinLoseVolume(this.winLoseVolume);
    }

    /**
     * Stops all music playback immediately.
     */
    public void stopAllMusic() {
        if (backgroundPlayer != null) {
            backgroundPlayer.stop();
        }
    }
}
