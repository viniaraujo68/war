package models.observers;

import models.events.TileEvent;

public interface BoardObserver {
	public void trigger(TileEvent[] tileList);
}
