package models;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import enums.Shape;

public class CardTest {
    private Card card;

    @Before
    public void setUp() {
        card = new Card("Tile1", Shape.Circle);
    }

    @Test
    public void testTileName() {
        assertEquals("Tile1", card.tileName);
    }

    @Test
    public void testShape() {
        assertEquals(Shape.Circle, card.shape);
    }
}
