package models;

import java.util.ArrayList;

import enums.PlayerColor;

public abstract class Goal {
    
    String description;
    Player player;

    Goal(String desc) {
        this.description = desc;
    }
    
    public boolean setPlayer(Player player) {
    	if (player != null) {
    		this.player = player;
    		return true;
    	}
    	return false;
    }
    
    public static Goal[] getGoals(Board board) {
    	
    	Goal[] arr = new Goal[14];
    	arr[0] = new KillGoal("Destruir totalmente os exercitos azuis. <>Se nao for possivel dominar 24 territorios.", PlayerColor.Blue);
    	arr[1] = new KillGoal("Destruir totalmente os exercitos vermelhos. <>Se nao for possivel dominar 24 territorios.", PlayerColor.Red);
    	arr[2] = new KillGoal("Destruir totalmente os exercitos amarelos. <>Se nao for possivel dominar 24 territorios.", PlayerColor.Yellow);
    	arr[3] = new KillGoal("Destruir totalmente os exercitos pretos. <>Se nao for possivel dominar 24 territorios.", PlayerColor.Black);
    	arr[4] = new KillGoal("Destruir totalmente os exercitos brancos. <>Se nao for possivel dominar 24 territorios.", PlayerColor.White);
    	arr[5] = new KillGoal("Destruir totalmente os exercitos verdes. <>Se nao for possivel dominar 24 territorios.", PlayerColor.Green);
    	arr[6] = new ContinentGoal("Conquistar na totalidade a EUROPA,<>a OCEANIA e mais um terceiro.",new Continent[] {board.continents.get(3), board.continents.get(5)}, true);
    	arr[7] = new ContinentGoal("Conquistar na totalidade a EUROPA,<>a AMERICA DO SUL e mais um terceiro", new Continent[] {board.continents.get(3), board.continents.get(0)}, true);
    	arr[8] = new ContinentGoal("Conquistar na totalidade a ASIA e a AMERICA DO SUL", new Continent[] {board.continents.get(4), board.continents.get(0)},false);
    	arr[9] = new ContinentGoal("Conquistar na totalidade a ASIA e a AFRICA", new Continent[] {board.continents.get(4), board.continents.get(2)},false);
    	arr[10] = new ContinentGoal("Conquistar na totalidade a AMERICA DO NORTE e AFRICA.", new Continent[] {board.continents.get(1), board.continents.get(2)},false);
    	arr[11] = new ContinentGoal("Conquistar na totalidade a AMERICA DO NORTE e OCEANIA.", new Continent[] {board.continents.get(1), board.continents.get(5)},false);
    	arr[12] = new TileGoal("Conquistar 18 territorios e ocupar <>cada um deles com pelo menos 2 exercitos.", 18, 2);
    	arr[13] = new TileGoal("Conquistar 24 territorios.", 24, 1);
    	return arr;
    }

    abstract boolean checkWin(ArrayList<Player> players, Player playerTurn);
    
    public abstract String toString();
}