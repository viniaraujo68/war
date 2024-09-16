package models.observers;

import models.events.PlayerEvent;

public interface PlayerObserver {
	public void trigger(PlayerEvent currentPlayer);
}
