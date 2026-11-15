package be.kdg.integration2.mvpglobal.model;

public class PlayerStats {
    private final int rank;
    private final String name;
    private final int games;
    private final String winLoss;
    private final double winPercentage;
    private final double avgMoves;
    private final double moveDuration;

    public PlayerStats(int rank, String name, int games, String winLoss, double winPercentage, double avgMoves, double moveDuration) {
        this.rank = rank;
        this.name = name;
        this.games = games;
        this.winLoss = winLoss;
        this.winPercentage = winPercentage;
        this.avgMoves = avgMoves;
        this.moveDuration = moveDuration;
    }

    public int getRank() {
        return rank;
    }

    public String getName() {
        return name;
    }

    public int getGames() {
        return games;
    }

    public String getWinLoss() {
        return winLoss;
    }

    public double getWinPercentage() {
        return winPercentage;
    }

    public double getAvgMoves() {
        return avgMoves;
    }

    public double getMoveDuration() {
        return moveDuration;
    }
}