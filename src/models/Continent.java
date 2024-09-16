package models;

import java.util.ArrayList;

class Continent {

	String name;
	ArrayList<Tile> tiles = new ArrayList<Tile>();
	int troopBonus;

	Continent(String name, Tile[] tiles, int troopBonus) {
		this.name = name;
		this.troopBonus = troopBonus;
		
		for (Tile tile : tiles) {
			this.tiles.add(tile);
		}
	}
	
	public boolean checkIfContinentIsFullyDominated(Player player) {
		for (Tile tile : this.tiles) {
			if (tile.owner != player) {
				return false;
			}
		}
		return true;
	}
}