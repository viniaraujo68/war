package models;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ContinentTest {
    private Continent continent;
    private Tile tile1;
    private Tile tile2;

    @Before
    public void setUp() {
        tile1 = new Tile("Tile1");
        tile2 = new Tile("Tile2");
        continent = new Continent("Continent1", new Tile[] {tile1 , tile2 }, 5);
    }

    @Test
    public void testContinentName() {
        assertEquals("Continent1", continent.name);
    }

    @Test
    public void testTroopBonus() {
        assertEquals(5, continent.troopBonus);
    }

    @Test
    public void testTilesInContinent() {
        assertEquals(2, continent.tiles.size());
        assertTrue(continent.tiles.contains(tile1));
        assertTrue(continent.tiles.contains(tile2));
    }
}
