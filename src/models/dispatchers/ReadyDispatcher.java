package models.dispatchers;

public interface ReadyDispatcher {
	public void readySubscribe(Object observer);
	public void dispatch(boolean ready);
}
