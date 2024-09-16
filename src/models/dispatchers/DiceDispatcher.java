package models.dispatchers;

import models.events.RollEvent;
import models.observers.DiceObserver;

public interface DiceDispatcher {
	public void dispatch(RollEvent e);
	public void subscribe(DiceObserver observer);
}
