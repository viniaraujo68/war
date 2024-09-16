package models;

import java.util.ArrayList;

import enums.PlayerColor;

class KillGoal extends Goal {
	
	PlayerColor targetColor;
	boolean winnable = true;

    KillGoal(String desc, PlayerColor targetColor) {
        super(desc);
        this.targetColor = targetColor;
    }

    boolean checkWin(ArrayList<Player> players, Player playerTurn) {
    	
    	if (!winnable) {
    		return player.takenTiles.size() >= 24;
    	}
    	
    	Player target = this.findPlayerByColor(players);
    	
    	if (target == player || target == null) {
    		winnable = false;
    		return false;
    	}
    	
    	if (!target.isAlive()) {
    		if (playerTurn != player) {
    			winnable = false;
    		} else {
    			System.out.println("Finalizou o killGoal\n");
    			return true;
    		}
    	}
        return false;
    }
    
    
    private Player findPlayerByColor(ArrayList<Player> players) {
    	for (Player p : players) {
    		if (p.color == targetColor) {
    			return p;
    		}
    	}
    	return null;
    }
    
    public String toString() {
    	String result = "KillGoal\n";
    	result += String.join("-", this.description, targetColor.toString(), Boolean.toString(winnable));
        return result + "\n";
    }
    
    public void setWinnable(boolean b) {
    	winnable = b;
    }
}