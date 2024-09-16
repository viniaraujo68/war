package models;

import java.util.ArrayList;

import models.dispatchers.Dispatcher;
import models.events.PlayerEvent;
import models.observers.PlayerObserver;

class ActivePlayer implements Dispatcher {
	private Player playerTurn;
	private ArrayList<PlayerObserver> observers = new ArrayList<PlayerObserver>();
	
	public void dispatch() {
		PlayerEvent event = new PlayerEvent(playerTurn.name, playerTurn.color, playerTurn.getGoalDesc(), playerTurn.getCardsInfo(), playerTurn.victory);
		for (PlayerObserver observer: observers) {
			observer.trigger(event);
		}
	}
	
	public void setWin() {
		this.playerTurn.setVictory();
		this.dispatch();
	}
	
	public void subscribe(Object obj) {
		observers.add((PlayerObserver)obj);
	}
	
	public void setPlayerTurn(Player player) {
		this.playerTurn = player;
		this.dispatch();
	}
	
	public Player playerTurn() {
		return this.playerTurn;
	}
	
	public void addCardToPlayer(Card card) {
		this.playerTurn.cards.add(card);
		this.dispatch();
	}
	
	public void removePlayerCard(Card[] cardsToRemove, int bonus) {
		if (cardsToRemove != null) {
	    	for (Card c : cardsToRemove) {
	    		this.playerTurn.cards.remove(c);
	    	}
	    	this.playerTurn.availableTroops.normal += bonus;
	    }
		this.dispatch();
	}
}
