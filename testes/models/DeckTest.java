package models;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import enums.Shape;

public class DeckTest {
    private Deck deck;
    private Card card1;
    private Card card2;
    private Card card3;
    private Card card4;

    @Before
    public void setUp() {
        deck = new Deck();
        card1 = new Card("Tile1", Shape.Circle);
        card2 = new Card("Tile2", Shape.Square);
        card3 = new Card("Tile3", Shape.Triangle);
        card4 = new Card("Tile4", Shape.Circle);

        deck.cards.add(card1);
        deck.cards.add(card2);
        deck.cards.add(card3);
        deck.cards.add(card4);
    }
    
    @After
    public void reset()  {
    	deck = null;
    	card1 = null;
    	card2 = null;
    	card3 = null;
    	card4 = null;
    }

    @Test
    public void testDraw() {
        Card drawnCard = deck.draw();
        assertEquals(card1, drawnCard);
        assertEquals(3, deck.cards.size());
    }

    @Test
    public void testTradeSuccessful() {
    	Card[] cardsToTrade = new Card[] { card1, card2, card3 };
        int troops = deck.trade(cardsToTrade);
        assertEquals(4, troops);
        assertEquals(7, deck.cards.size());
    }

    @Test
    public void testTradeUnsuccessful() {
        Card[] cardsToTrade = new Card[] { card1, card2, card4 };
        int troops = deck.trade(cardsToTrade);
        assertEquals(-1, troops);
        assertEquals(4, deck.cards.size());
    }

    @Test
    public void testIsTradable() {
    	Card[] cards1 = {card1, card4};        
        assertFalse(deck.isTradable(cards1));

        Card[] cards2 = {card1, card2, card3};
        assertTrue(deck.isTradable(cards2));
   
        Card[] cards3 = {card1, card2, card4};
        assertFalse(deck.isTradable(cards3));
    }

//    @Test
//    public void testIsShapeEquivalent() {
//        assertTrue(deck.isShapeEquivalent(Shape.Circle, Shape.Circle));
//        assertTrue(deck.isShapeEquivalent(Shape.Circle, Shape.Joker));
//        assertTrue(deck.isShapeEquivalent(Shape.Joker, Shape.Circle));
//        assertFalse(deck.isShapeEquivalent(Shape.Circle, Shape.Square));
//    }
//
//    @Test
//    public void testIsShapeDifferent() {
//        assertTrue(deck.isShapeDifferent(Shape.Circle, Shape.Square));
//        assertTrue(deck.isShapeDifferent(Shape.Circle, Shape.Triangle));
//        assertTrue(deck.isShapeDifferent(Shape.Square, Shape.Triangle));
//        assertTrue(deck.isShapeDifferent(Shape.Circle, Shape.Joker));
//        assertTrue(deck.isShapeDifferent(Shape.Square, Shape.Joker));
//        assertTrue(deck.isShapeDifferent(Shape.Triangle, Shape.Joker));
//        assertFalse(deck.isShapeDifferent(Shape.Circle, Shape.Circle));
//    }
}
