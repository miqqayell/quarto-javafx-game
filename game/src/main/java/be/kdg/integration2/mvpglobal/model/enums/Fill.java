package be.kdg.integration2.mvpglobal.model.enums;

public enum Fill {
    SOLID, HOLLOW;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}