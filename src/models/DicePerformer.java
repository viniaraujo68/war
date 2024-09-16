package models;

import java.util.ArrayList;

import models.dispatchers.DiceDispatcher;
import models.events.RollEvent;
import models.observers.DiceObserver;

public class DicePerformer implements DiceDispatcher {
	
	private ArrayList<DiceObserver> observers = new ArrayList<DiceObserver>();
	
	public static DicePerformer performer;
	
	private DicePerformer() { }
	
	public void dispatch(RollEvent e) {
		for (DiceObserver observer : observers) {
			observer.trigger(e);
		}
	}
	
	public void subscribe(DiceObserver observer) {
		this.observers.add(observer);
	}
	
	public static DicePerformer getPerformer() {
		if (DicePerformer.performer == null) {
			DicePerformer.performer = new DicePerformer();
		}
		return DicePerformer.performer;
	}
	
}
