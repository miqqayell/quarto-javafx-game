package be.kdg.integration2.mvpglobal.model;

import be.kdg.integration2.mvpglobal.model.enums.Color;
import be.kdg.integration2.mvpglobal.model.enums.Fill;
import be.kdg.integration2.mvpglobal.model.enums.Height;
import be.kdg.integration2.mvpglobal.model.enums.Shape;

/**
 * Represents a single piece configuration in the Quarto-like game.
 * <p>
 * Encapsulates the four defining attributes of a piece: color, shape, height, and fill.
 * Provides utility methods to retrieve the piece identifier and display text.
 * </p>
 *
 * @author ACS101 - TEAM 2
 * @version 1.0
 */
public class PieceType {
    /** The color attribute of the piece. */
    private final Color color;
    /** The shape attribute of the piece. */
    private final Shape shape;
    /** The height attribute of the piece. */
    private final Height height;
    /** The fill attribute of the piece. */
    private final Fill fill;

    /**
     * Constructs a new PieceType with the specified attributes.
     *
     * @param color  the {@link Color} of the piece
     * @param shape  the {@link Shape} of the piece
     * @param height the {@link Height} of the piece
     * @param fill   the {@link Fill} of the piece
     */
    public PieceType(Color color, Shape shape, Height height, Fill fill) {
        this.color = color;
        this.shape = shape;
        this.height = height;
        this.fill = fill;
    }

    /**
     * Returns the unique identifier for this piece type in the format
     * "COLOR-SHAPE-HEIGHT-FILL".
     *
     * @return a string ID combining the piece attributes
     */
    public String getId() {
        return String.join("-",
                color.toString(),
                shape.toString(),
                height.toString(),
                fill.toString()
        );
    }

    /**
     * Returns a multi-line display text for this piece type,
     * listing each attribute on a separate line.
     *
     * @return a formatted string with piece attributes on individual lines
     */
    public String getDisplayText() {
        return String.join("\n",
                color.toString(),
                shape.toString(),
                height.toString(),
                fill.toString()
        );
    }
}
