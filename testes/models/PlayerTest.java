package models;

import static org.junit.Assert.*;

import org.junit.After;

import enums.Shape;
import org.junit.Before;
import org.junit.Test;
import enums.PlayerColor;

public class PlayerTest {
    private Player player;
    private Tile tile1;
	private Tile tile2;
    private Continent continent;

    @Before
    public void setUp() {
    	Goal goal = new TileGoal("Conquistar 18 territorios e ocupar \ncada um deles com pelo menos 2 exercitos.", 18, 2);
        player = new Player("Player1", PlayerColor.Red, goal);
        tile1 = new Tile("Tile1");
		tile2 = new Tile("Tile2");
        continent = new Continent("América do Sul", new Tile[] { tile1, tile2 }, 2);
        Player.board = Board.getBoard();
    }
    
    @After
    public void reset() {
    	player = null;
    	tile1 = null;
    	tile2 = null;
    	continent = null;
    }

    @Test
    public void testAddTroops() {
    	int troops = player.addTroops(10);
        assertEquals(10, troops);
    }

    @Test
    public void testAddTakenTiles() {
        assertTrue(player.addTakenTiles(tile1));
        assertEquals(1, player.getTileCount());
    }

    @Test
    public void testCalculateTroopsByTiles() {
        player.addTakenTiles(tile1);
		player.addTakenTiles(tile2);
        assertEquals(1, player.calculateTroopsByTiles());
    }

    @Test
    public void testCalculateTroopsByContinents() {
        tile1.owner = player;
		tile2.owner = player;
        assertEquals(2, player.calculateTroopsByContinents());
    }

    @Test
    public void testCompleteTrade() {
        Card card1 = new Card("Tile1", Shape.Triangle);
        Card card2 = new Card("Tile2", Shape.Joker);
        player.cards.add(card1);
        player.cards.add(card2);
        Card[] cardsToRemove = {card1, card2};
        assertEquals(2, player.cards.size());
        assertTrue(player.completeTrade(cardsToRemove, 5));
        assertEquals(0, player.cards.size());
        assertEquals(5, player.availableTroops);
    }
}