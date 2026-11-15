package be.kdg.integration2.mvpglobal.model.enums;

public enum Color {
    LIGHT, DARK;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}