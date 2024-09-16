package models.dispatchers;

public interface Dispatcher {
	public void dispatch();
	public void subscribe(Object obs);
}
