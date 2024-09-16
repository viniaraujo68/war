package models;

import java.util.ArrayList;
import enums.PlayerColor;
import models.events.CardRepresent;

class Player {

	public static Board board = Board.getBoard();
	public static PhaseManager phaseManager = PhaseManager.getManager();
	String name;
	PlayerColor color;
	public ArrayList<Tile> takenTiles = new ArrayList<Tile>();
	public Goal goal;
	AvailableTroops availableTroops = new AvailableTroops();
	public ArrayList<Card> cards = new ArrayList<Card>();
	public boolean victory = false;
	public boolean firstTurn = true;


	Player(String name, PlayerColor color, Goal goal) {
        this.name = name;
        this.color = color;   
        this.goal = goal;
        this.goal.setPlayer(this);
	}
	
	public void setVictory() {
		this.victory = true;
	}

	public int addTroops(int amount) {
		this.availableTroops.normal += amount;
		phaseManager.dispatch();
		return this.availableTroops.normal;
	}

	public boolean addTakenTiles(Tile tile) {
		if (tile != null) {
			takenTiles.add(tile);
			tile.troops = 1;
			return true;
		}
		return false;
	}
	
	public boolean removeTakenTiles(Tile tile) {
		if (tile != null) {
			return takenTiles.remove(tile);
		}
		return false;
	}

	public int getTileCount() {
		return this.takenTiles.size();
	}
  
	public int calculateTroopsByTiles() {
		int bonus = (this.getTileCount() / 2) < 3 ? 3 : this.getTileCount() / 2;
		this.availableTroops.normal += bonus;
		return bonus;
	}
  
	public int calculateTroopsByContinents() {
		int bonus = 0;
		for (Continent continent: board.continents) {
			bonus = 0;
			if (continent.checkIfContinentIsFullyDominated(this)) {
				bonus += continent.troopBonus;
				Integer continentTroops = this.availableTroops.continentTroops.get(continent.name);
				if (continentTroops != null) {
					bonus += continentTroops;
				}
				this.availableTroops.continentTroops.put(continent.name, bonus);
			}
		}
		return bonus;
	}

	// Se continent for null então adiciona no normal
	public int placeTroop(int amount, String continent) {
		int troops = continent == null ? availableTroops.normal : availableTroops.continentTroops.get(continent);
		if (troops < amount) {
			return -1;
		}
		troops -= amount;
		if (continent != null) {
			availableTroops.continentTroops.put(continent, troops);
			phaseManager.dispatch();
			return troops;
		}
		availableTroops.normal = troops;
		phaseManager.dispatch();
		return troops;
	}

	public boolean completeTrade(Card[] cardsToRemove, int bonus) {
	    if (cardsToRemove != null) {
	    	for (Card c : cardsToRemove) {
	    		this.cards.remove(c);
	    	}
	    	this.availableTroops.normal += bonus;
	    	return true;
	    }
	    return false;
	}
	
	public String getGoalDesc() {
		return this.goal.description;
	}
	
	public void setGoal(Goal goal) {
		this.goal = goal;
		goal.setPlayer(this);
	}

	public CardRepresent[] getCardsInfo() {
		CardRepresent[] arr = new CardRepresent[this.cards.size()];
		int i = 0;
		for (Card card: this.cards) {
			arr[i++] = new CardRepresent(card.tileName, card.shape);
		}
		return arr;
	}
	
	public boolean isAlive() {
		return !takenTiles.isEmpty();
	}
	
	public String toString() {
		String cardsString = String.join("/",this.cards.stream().map(c -> c.toString()).toArray(String[]::new));
		String result = String.join("-", name, color.toString(), Boolean.toString(victory), Boolean.toString(firstTurn), availableTroops.toString(), cardsString, goal.toString());
		return result;
	}
}