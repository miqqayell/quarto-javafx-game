package be.kdg.integration2.mvpglobal.model.enums;

public enum Height {
    TALL, SHORT;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}