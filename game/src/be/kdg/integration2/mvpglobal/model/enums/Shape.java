package be.kdg.integration2.mvpglobal.model.enums;

public enum Shape {
    ROUND, SQUARE;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}