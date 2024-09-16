package models;

import java.util.ArrayList;

import enums.GamePhase;
import models.dispatchers.Dispatcher;
import models.events.PhaseInfo;
import models.observers.PhaseObserver;

class PhaseManager implements Dispatcher {
	
	GamePhase phase = GamePhase.Place;
	ActivePlayer playerManager = new ActivePlayer();
	ArrayList<PhaseObserver> observers = new ArrayList<PhaseObserver>();
	private static PhaseManager phaseManager;
	
	public static PhaseManager getManager() {
		if (PhaseManager.phaseManager == null) {
			PhaseManager.phaseManager = new PhaseManager();
		}
		return PhaseManager.phaseManager;
	}
	
	private PhaseManager() {
		
	}
	
	public void dispatch() {
		PhaseInfo event = null;
		if (this.phase == GamePhase.Place) {
			Player player = this.playerManager.playerTurn();
			event = new PhaseInfo(player.availableTroops.normal, player.availableTroops.continentTroops);
		}
		for (PhaseObserver o : observers) {
			o.trigger(phase, event);
		}
	}
	
	public void subscribe(Object obj) {
		this.observers.add((PhaseObserver)obj);
	}
	
	public void nextPhase() {
		switch(this.phase) {
			case Attack:
				this.phase = GamePhase.Move;
				break;
			case Place:
				this.phase = GamePhase.Attack;
				break;
			case Move:
				this.phase = GamePhase.Place;
				break;
			default:
				break;
		}
		this.dispatch();
	}
	
	public void completeTrade(Card[] cards, int bonus) {
		this.playerManager.removePlayerCard(cards, bonus);
		this.dispatch();
	}
	
	public String toString() {
		String result = "PhaseManager\n";
		result += String.join("-", this.phase.toString(), this.playerManager.playerTurn().name);
		result += "\n";
		return result;
	}
}
