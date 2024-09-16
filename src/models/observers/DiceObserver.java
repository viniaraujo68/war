package models.observers;

import models.events.RollEvent;

public interface DiceObserver {
	public void trigger(RollEvent e);
}
