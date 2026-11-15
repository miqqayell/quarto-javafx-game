package be.kdg.integration2.mvpglobal.model;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.nio.file.Path;
import java.nio.file.Paths;

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

    /** File separator character for resource paths. */
    private final char FILE_SEPARATOR = System.getProperty("file.separator").charAt(0);
    /** Path to the background music file. */
    private final Path backgroundMusicPath = Paths.get("resources" + FILE_SEPARATOR + "sounds" + FILE_SEPARATOR + "bgMusic.mp3");
    /** Path to the button click sound file. */
    private final Path buttonClickSoundPath = Paths.get("resources" + FILE_SEPARATOR + "sounds" + FILE_SEPARATOR + "buttonClick.mp3");
    /** Path to the piece placed sound file. */
    private final Path piecePlacedPath = Paths.get("resources" + FILE_SEPARATOR + "sounds" + FILE_SEPARATOR + "piecePlace.mp3");
    /** Path to the lose game sound file. */
    private final Path loseGamePath = Paths.get("resources" + FILE_SEPARATOR + "sounds" + FILE_SEPARATOR + "loseGame.mp3");
    /** Path to the win game sound file. */
    private final Path winGamePath = Paths.get("resources" + FILE_SEPARATOR + "sounds" + FILE_SEPARATOR + "winGame.mp3");

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
        try {
            Media backgroundSound = new Media(backgroundMusicPath.toUri().toString());
            backgroundPlayer = new MediaPlayer(backgroundSound);
            backgroundPlayer.setVolume(0.5);
            backgroundPlayer.setCycleCount(MediaPlayer.INDEFINITE);

            Media buttonClickSound = new Media(buttonClickSoundPath.toUri().toString());
            buttonClickPlayer = new MediaPlayer(buttonClickSound);

            Media winGameSound = new Media(winGamePath.toUri().toString());
            winGamePlayer = new MediaPlayer(winGameSound);

            Media loseGameSound = new Media(loseGamePath.toUri().toString());
            loseGamePlayer = new MediaPlayer(loseGameSound);

            Media piecePlacedSound = new Media(piecePlacedPath.toUri().toString());
            piecePlacedPlayer = new MediaPlayer(piecePlacedSound);
        } catch (Exception e) {
            // Loading errors are ignored
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
        if (!isBackgroundPlaying) {
            stopAllMusic();
            backgroundPlayer.play();
            isBackgroundPlaying = true;
        }
    }

    /**
     * Plays the button click sound effect from the beginning.
     */
    public void playButtonClick() {
        buttonClickPlayer.seek(Duration.ZERO);
        buttonClickPlayer.play();
    }

    /**
     * Plays the lose game sound effect from the beginning.
     */
    public void playLoseGame() {
        loseGamePlayer.seek(Duration.ZERO);
        loseGamePlayer.play();
    }

    /**
     * Plays the win game sound effect from the beginning.
     */
    public void playWinGame() {
        winGamePlayer.seek(Duration.ZERO);
        winGamePlayer.play();
    }

    /**
     * Stops the currently playing background music and resets its position.
     */
    public void stopBackgroundMusic() {
        backgroundPlayer.seek(Duration.ZERO);
        isBackgroundPlaying = false;
    }

    /**
     * Plays the piece placed sound effect from the beginning.
     */
    public void playPiecePlace() {
        piecePlacedPlayer.seek(Duration.ZERO);
        piecePlacedPlayer.play();
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
        backgroundPlayer.stop();
    }
}
