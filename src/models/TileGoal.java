package models;

import java.util.ArrayList;

class TileGoal extends Goal {
	
	int numTiles;
	int troopsByTile;
	
    TileGoal(String desc, int numTiles, int troopsByTile) {
        super(desc);
        this.numTiles = numTiles;
        this.troopsByTile = troopsByTile;
    }

    boolean checkWin(ArrayList<Player> players, Player playerTurn) {
    	
    	int count = 0;

    	for (Tile t : player.takenTiles) {
    		if (t.troops >= troopsByTile) {
    			count++;
    		}
    	}
    	if (count >= numTiles) {
    		System.out.println("Finalizou o tileGoal\n");
    		return true;
    	}
        return false;
    }
    
    public String toString() {
    	String result = "TileGoal\n";
    	result += String.join("-", description, Integer.toString(numTiles), Integer.toString(troopsByTile));
        return result + "\n";
    }
}