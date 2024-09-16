package models;

import static org.junit.Assert.*;
import enums.PlayerColor;
import org.junit.Before;
import org.junit.Test;

public class TileTest {

    private Tile tile1;
    private Tile tile2;
    private Player player1;
    private Goal goal;

    @Before
    public void setUp() {
        tile1 = new Tile("Tile1");
        tile2 = new Tile("Tile2");
        goal = new TileGoal("Conquistar 18 territorios e ocupar \ncada um deles com pelo menos 2 exercitos.", 18, 2);
        player1 = new Player("Player1", PlayerColor.Blue, goal);
        
    }

    @Test
    public void testAddNeighbour() {
        assertTrue(tile1.addNeighbour(tile2));
        assertTrue(tile2.isNeighbour(tile1));
        assertTrue(tile1.isNeighbour(tile2));
        assertFalse(tile1.addNeighbour(tile2));
    }

    @Test
    public void testAddTroops() {
        assertEquals(10, tile1.addTroops(10));
    }

    @Test
    public void testRemoveTroops() {
        tile1.addTroops(10);
        assertEquals(5, tile1.removeTroops(5));
    }

    @Test
    public void testDominate() {
        tile1.addTroops(10);
        tile1.owner = new Player("Player2", PlayerColor.Red, goal);
        assertTrue(tile1.dominate(10, player1));
        assertEquals(player1, tile1.owner);
        assertEquals(10, tile1.troops);
    }

    @Test
    public void testIsNeighbour() {
        tile1.addNeighbour(tile2);
        assertTrue(tile1.isNeighbour(tile2));
        assertFalse(tile1.isNeighbour(new Tile("Tile3")));
    }
}