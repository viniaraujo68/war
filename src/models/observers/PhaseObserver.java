package models.observers;

import enums.GamePhase;
import models.events.PhaseInfo;

public interface PhaseObserver {
	public void trigger(GamePhase phase, PhaseInfo info);
}
