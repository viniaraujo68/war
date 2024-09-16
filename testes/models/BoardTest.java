package models;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class BoardTest {
    private Board board;
    private Tile tile1;
    private Tile tile2;
    private Tile tile3;
    private Continent continent1;
    private Continent continent2;

    @Before
    public void setUp() {
        tile1 = new Tile("Tile1");
        tile2 = new Tile("Tile2");
        tile3 = new Tile("Tile3");

        continent1 = new Continent("Continent1", new Tile[] { tile1, tile2 }, 2);
        continent2 = new Continent("Continent2", new Tile[] { tile3 }, 3);

        board = Board.getBoard();
    }

    @Test
    public void testFindTile() {
        Tile foundTile = board.findTile("Tile1");
        assertEquals(tile1, foundTile);
    }

    @Test
    public void testGetContinentTiles() {
        ArrayList<Tile> tilesInContinent1 = board.getContinentTiles("Continent1");
        assertTrue(tilesInContinent1.contains(tile1));
        assertTrue(tilesInContinent1.contains(tile2));
        assertFalse(tilesInContinent1.contains(tile3));

        ArrayList<Tile> tilesInContinent2 = board.getContinentTiles("Continent2");
        assertTrue(tilesInContinent2.contains(tile3));
    }
}
