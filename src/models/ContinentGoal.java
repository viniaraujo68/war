package models;

import java.util.ArrayList;
import java.util.List;

class ContinentGoal extends Goal {
	
	private ArrayList<Continent> continents = new ArrayList<Continent>();
	private boolean extra;

    ContinentGoal(String desc, Continent[] continents, boolean extra) {
        super(desc);
        for (Continent c : continents) {
        	this.continents.add(c);
        }
        this.extra = extra;
    }

    boolean checkWin(ArrayList<Player> players, Player playerTurn) {
    	
    	for (Continent c : continents) {
    		if (!c.checkIfContinentIsFullyDominated(player)){
    			return false;
    		}
    	}
    	
    	int count = 0;
    	
    	if (extra) {
    		for (Continent c : Player.board.continents) {
    			if (c.checkIfContinentIsFullyDominated(player)) {
    				count++;
    			}
    		}
    		if (count >= 3) {
    			return true;
    		}
    	}
    	System.out.println("Finalizou o continentGoal\n");
        return true;
    }
    
    public String toString() {
    	
    	String result = "ContinentGoal\n";
    	
    	List<String> parts = new ArrayList<>();

    	parts.add(description);
    	parts.add(Boolean.toString(extra));


    	for (Continent c : continents) {
    		parts.add(c.name);
    	}

    	result += String.join("-", parts) + "\n";
    	return result;
	}
    
}