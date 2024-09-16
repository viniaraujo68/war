package models;

import models.observers.TileObserver;
import java.util.ArrayList;

import models.dispatchers.Dispatcher;
import models.events.TileEvent;

class Tile implements Dispatcher {
	
	String name;
	ArrayList<Tile> neighbours = new ArrayList<Tile>();
	ArrayList<TileObserver> observers = new ArrayList<TileObserver>();
	Player owner;
	boolean selected = false;
	boolean lastSelected = false;
	int troops;
	int movedTroops = 0;

	public void dispatch() {
		for(TileObserver observer: observers) {
			observer.trigger();
		}
	}
	
	public void subscribe(Object tileObs) {
		this.observers.add((TileObserver)tileObs);
	}
	
    public Tile(String name) {
        this.name = name;
    }
    
    public boolean addNeighbour(Tile tile) {
    	if (neighbours.contains(tile)) {
    		return false;
    	}
    	this.neighbours.add(tile);
    	tile.neighbours.add(this);
    	return true;
    }

    public int addTroops(int amount) {
        this.troops += amount;
        this.dispatch();
        return this.troops;
    }

	public int removeTroops(int amount) {
		this.troops -= amount;
		this.dispatch();
		return this.troops;

	}

	public boolean dominate(int amount, Player newOwner) {
		if (newOwner != null){
			this.owner.removeTakenTiles(this);
            this.owner = newOwner;
            this.owner.addTakenTiles(this);
            this.troops = amount;
            this.dispatch();
			return newOwner == this.owner;
		}
		return false;
	}

	public boolean isNeighbour(Tile tile) {
		if (tile != null){
			for (Tile t : neighbours){
				if (t.name == tile.name){
					return true;
				}
			}
		}
		return false;
	}
	
	public void setSelected(boolean value) {
		this.selected = value;
		dispatch();
	}
	
	public void setLastSelected(boolean value) {
		this.lastSelected = value;
		dispatch();
	}
	
	public int moveTroops(int quant) {
		this.addTroops(quant);
		this.movedTroops += quant;
		return this.troops;
	}
	
	@Override
	public String toString() {
		String result = String.join("-",name, owner.name, Integer.toString(troops), Integer.toString(movedTroops), Boolean.toString(selected), Boolean.toString(lastSelected));
		return result + "\n";
	}
}