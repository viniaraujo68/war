package models;

import java.util.ArrayList;
import java.util.Collections;
import enums.Shape;

class Deck {
    public ArrayList<Card> cards = new ArrayList<Card>();
    int tradeBonus = 4;

    void sort() {
        String previousList = cards.toString();
        String newList = previousList;
        while (previousList == newList) {
            Collections.shuffle(cards);
            newList = cards.toString();
        }
    }

    Card draw() {
        Card cardRemoved = cards.remove(0);
        return cardRemoved;
    }

    int trade(Card[] cardsToTrade) {
        if (isTradable(cardsToTrade)) {
            for (Card c : cardsToTrade) {
            	this.cards.add(c);
            }
            int troops = tradeBonus;
			if (tradeBonus < 12) {
            	tradeBonus += 2;
			} else if (tradeBonus % 5 != 0) {
				tradeBonus = 15;
			} else {
				tradeBonus += 5;
			}
            return troops;
        }
        // Retorno padrão caso a troca seja impossível
        return -1;
    }

    public boolean isTradable(Card[] cardsToTrade) {
        Shape[] formas = new Shape[3];
        int i = 0;
        for (Card card: cardsToTrade) {
            formas[i] = card.shape;
            i++;
        }
        if (isShapeEquivalent(formas[0], formas[1]) && isShapeEquivalent(formas[1], formas[2]) && isShapeEquivalent(formas[0], formas[2])) {
            return true;
        } else if (isShapeDiferent(formas[0], formas[1]) && isShapeDiferent(formas[1], formas[2]) && isShapeDiferent(formas[0], formas[2])) {
            return true;
        }
        // Se as cartas não são todas iguais nem todas diferentes, retorna falso
        return false;
    }

    private boolean isShapeEquivalent(Shape shape, Shape compare) {
        return (shape == compare || compare == Shape.Joker || shape == Shape.Joker);
    }

    private boolean isShapeDiferent(Shape shape, Shape compare) {
        return (shape != compare || compare == Shape.Joker || shape == Shape.Joker);
    }
    
    public static Deck getDeck() {
    	Deck deck = new Deck();
    	deck.cards.add(new Card("Africa do Sul", Shape.Triangle));
    	deck.cards.add(new Card("Angola", Shape.Square));
    	deck.cards.add(new Card("Argelia", Shape.Circle));
    	deck.cards.add(new Card("Egito", Shape.Triangle));
    	deck.cards.add(new Card("Nigeria", Shape.Circle));
    	deck.cards.add(new Card("Somalia", Shape.Square));
    	deck.cards.add(new Card("Alasca", Shape.Triangle));
    	deck.cards.add(new Card("Calgary", Shape.Circle));
    	deck.cards.add(new Card("California", Shape.Square));
    	deck.cards.add(new Card("Groenlandia", Shape.Circle));
    	deck.cards.add(new Card("Mexico", Shape.Square));
    	deck.cards.add(new Card("Nova York", Shape.Square));
    	deck.cards.add(new Card("Quebec", Shape.Circle));
    	deck.cards.add(new Card("Texas", Shape.Triangle));
    	deck.cards.add(new Card("Vancouver", Shape.Triangle));
    	deck.cards.add(new Card("Arabia Saudita", Shape.Circle));
    	deck.cards.add(new Card("Bangladesh", Shape.Circle));
    	deck.cards.add(new Card("Cazaquistao", Shape.Circle));
    	deck.cards.add(new Card("China", Shape.Square));
    	deck.cards.add(new Card("Coreia do Norte", Shape.Square));
    	deck.cards.add(new Card("Coreia do Sul", Shape.Triangle));
    	deck.cards.add(new Card("Estonia", Shape.Circle));
    	deck.cards.add(new Card("India", Shape.Triangle));
    	deck.cards.add(new Card("Ira", Shape.Square));
    	deck.cards.add(new Card("Iraque", Shape.Triangle));
    	deck.cards.add(new Card("Japao", Shape.Circle));
    	deck.cards.add(new Card("Jordania", Shape.Square));
    	deck.cards.add(new Card("Letonia", Shape.Square));
    	deck.cards.add(new Card("Mongolia", Shape.Triangle));
    	deck.cards.add(new Card("Paquistao", Shape.Circle));
    	deck.cards.add(new Card("Russia", Shape.Triangle));
    	deck.cards.add(new Card("Siberia", Shape.Square));
    	deck.cards.add(new Card("Siria", Shape.Square));
    	deck.cards.add(new Card("Tailandia", Shape.Triangle));
    	deck.cards.add(new Card("Turquia", Shape.Triangle));
    	deck.cards.add(new Card("Argentina", Shape.Square));
    	deck.cards.add(new Card("Brasil", Shape.Circle));
    	deck.cards.add(new Card("Peru", Shape.Triangle));
    	deck.cards.add(new Card("Venezuela", Shape.Triangle));
    	deck.cards.add(new Card("Espanha", Shape.Circle));
    	deck.cards.add(new Card("Franca", Shape.Triangle));
    	deck.cards.add(new Card("Italia", Shape.Square));
    	deck.cards.add(new Card("Polonia", Shape.Triangle));
    	deck.cards.add(new Card("Reino Unido", Shape.Circle));
    	deck.cards.add(new Card("Romenia", Shape.Triangle));
    	deck.cards.add(new Card("Suecia", Shape.Square));
    	deck.cards.add(new Card("Ucrania", Shape.Circle));
    	deck.cards.add(new Card("Australia", Shape.Triangle));
    	deck.cards.add(new Card("Indonesia", Shape.Triangle));
    	deck.cards.add(new Card("Nova Zelandia", Shape.Square));
    	deck.cards.add(new Card("Perth", Shape.Circle));
    	return deck;
    }
    
    public Deck addJokers() {
    	cards.add(new Card("Coringa", Shape.Joker));
    	cards.add(new Card("Coringa1", Shape.Joker));
    	return this;
    }
    
    public String toString() {
    	String result = "Deck";
    	result += "-" + Integer.toString(tradeBonus) + "\n";
    	result += String.join("\n", cards.stream().map(c -> c.toString()).toArray(String[]::new));
    	result += "\n";
    	return result;
    }
       
}
