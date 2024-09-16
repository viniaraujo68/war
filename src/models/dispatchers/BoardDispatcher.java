package models.dispatchers;

import models.observers.BoardObserver;

public interface BoardDispatcher {
	public void dispatch();
	public void subscribe(BoardObserver obs);
}
