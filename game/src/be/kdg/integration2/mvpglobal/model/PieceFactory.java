package be.kdg.integration2.mvpglobal.model;

import be.kdg.integration2.mvpglobal.model.enums.Color;
import be.kdg.integration2.mvpglobal.model.enums.Fill;
import be.kdg.integration2.mvpglobal.model.enums.Height;
import be.kdg.integration2.mvpglobal.model.enums.Shape;

import java.util.ArrayList;
import java.util.List;

/**
 * Factory class for generating all unique piece identifiers used in the Quarto-like game.
 * <p>
 * Combines every possible value of the {@link Color}, {@link Shape}, {@link Height},
 * and {@link Fill} enums to produce the full set of piece ID strings in the format
 * "COLOR-SHAPE-HEIGHT-FILL".
 * </p>
 *
 * <p>Example piece ID: {@code RED-OVAL-TALL-SOLID}</p>
 *
 * @author ACS101 - TEAM 2
 * @version 1.0
 */
public class PieceFactory {

    /**
     * Generates a list of all piece ID strings by iterating through each combination
     * of color, shape, height, and fill attributes.
     *
     * @return a {@link List} of piece ID strings in the form "COLOR-SHAPE-HEIGHT-FILL"
     */
    public static List<String> allPieceIds() {
        List<String> ids = new ArrayList<>();
        for (Color c : Color.values()) {
            for (Shape s : Shape.values()) {
                for (Height h : Height.values()) {
                    for (Fill f : Fill.values()) {
                        ids.add(String.join("-",
                                c.toString(),
                                s.toString(),
                                h.toString(),
                                f.toString()
                        ));
                    }
                }
            }
        }
        return ids;
    }
}
