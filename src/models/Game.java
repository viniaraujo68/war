package models;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Random;
import java.util.Scanner;

import enums.GamePhase;
import enums.PlayerColor;
import models.dispatchers.Dispatcher;
import models.dispatchers.ReadyDispatcher;
import models.events.RollEvent;
import models.observers.BoardObserver;
import models.observers.DiceObserver;
import models.observers.PhaseObserver;
import models.observers.PlayerObserver;
import models.observers.ReadyObserver;
import views.Utils;

public class Game implements ReadyDispatcher {
    
    private static Game game = new Game();
    
    public ArrayList<ReadyObserver> observers = new ArrayList<ReadyObserver>();

    private Tile secondClicked;
    private Tile clicked;
    
	private PhaseManager phaseManager;
	private Board board;
	private DicePerformer dicePerformer;
	
    private ArrayList<Player> players;
    private int numPlayers;
    private Deck deck;
	private ArrayList<Goal> availableGoals;
	private boolean attackSucceed = false;
	private ArrayList<PlayerColor> colors = new ArrayList<PlayerColor>();
	private Hashtable<Tile, Integer> movedTroops = new Hashtable<Tile, Integer>();
    
    public int trade(String[] cards) {
		if (cards.length != 3 || this.phaseManager.phase != GamePhase.Place) {
			return -1;
		}
		Card[] cardsToTrade = new Card[3];
		for (int i=0;i<cards.length;i++) {
			for (Card card: this.phaseManager.playerManager.playerTurn().cards) {
				if (card.tileName == cards[i]) {
					cardsToTrade[i] = card;
				}
			}
			if (cards[i] == null) {
				return -1;
			}
		}
		int tradeBonus = this.deck.trade(cardsToTrade);
		if (tradeBonus == -1) {
			return -1;
		}
		this.phaseManager.completeTrade(cardsToTrade, tradeBonus);
		return tradeBonus;
   }
    
    public static Game start() {
    	if (Game.game == null) {
    		Game.game = new Game(); 	
    	}
		return Game.game;
    }
    
    public void reset(int numPlayers, String[] playersNames) {
    	this.board.reset();
    	this.phaseManager = PhaseManager.getManager();
    	this.dicePerformer = DicePerformer.getPerformer();
    	this.observers = new ArrayList<ReadyObserver>();
        this.secondClicked = null;
        this.clicked = null;
        this.numPlayers = numPlayers;
    	this.attackSucceed = false;
    	this.colors = new ArrayList<PlayerColor>();
    	this.movedTroops = new Hashtable<Tile, Integer>();
    	this.createBoardStructure();
    	this.setupGame(numPlayers, playersNames);
    }
    
    public void dispatchAll() {
    	this.board.dispatch();
    	this.phaseManager.dispatch();
    	this.phaseManager.playerManager.dispatch();
    	this.dispatch(false);
    	this.dicePerformer.dispatch(null);
    }
    
    public void setupGame(int numPlayers, String[] playersNames) {
    	System.out.printf("Número de jogadores: %d\n", numPlayers);
    	this.players = new ArrayList<Player>();
        this.numPlayers = numPlayers;
        this.secondClicked = null;
        
        this.createDeck();
        
        for(int i = 0; i < this.numPlayers; i++) {
        	Player newPlayer = this.createPlayer(playersNames[i]);
        	System.out.printf("Player: %s, Color: %s\n", newPlayer.name, newPlayer.color);
        	this.players.add(newPlayer);
        }
        
        Collections.shuffle(this.players);
        
        int deckSize = deck.cards.size();
        for(int i = 0; i < deckSize; i++) {
        	Card card = deck.cards.remove(deck.cards.size() - 1);
        	int p = i % this.numPlayers;
        	Tile cardTile = board.findTile(card.tileName);
        	cardTile.addTroops(1);
        	cardTile.owner = this.players.get(p);
        	this.players.get(p).takenTiles.add(cardTile);
        }
        
        for (Tile t : board.tiles) {
			Dispatcher d = (Dispatcher) t;
			d.subscribe(board);
		}
        
        this.createDeckJokers();
        
        
        this.phaseManager.playerManager.setPlayerTurn(this.players.get(0));
        this.phaseManager.playerManager.playerTurn().calculateTroopsByTiles();
        this.phaseManager.playerManager.playerTurn().calculateTroopsByContinents();
    }

    private Game() {
    	createBoardStructure();
    	this.phaseManager = PhaseManager.getManager();
    	this.dicePerformer = DicePerformer.getPerformer();
    }
    
    
    private boolean createBoardStructure() {
    	this.board = Board.getBoard();
    	this.availableGoals = new ArrayList<Goal>(Arrays.asList(Goal.getGoals(board)));
    	Collections.shuffle(this.availableGoals);
		return true;
    }
    
    private void createDeck() {
    	this.deck = Deck.getDeck();
    	Collections.shuffle(this.deck.cards);
    }
    
    public void createDeckJokers() {
    	this.deck = Deck.getDeck();
    	this.deck.addJokers();
    	Collections.shuffle(this.deck.cards);
    }
    
    
    private Player createPlayer(String name) {
    	Random r = new Random();
    	int colorIndex = r.nextInt(6);
    	PlayerColor drawnColor = PlayerColor.values()[colorIndex];
    	
    	
    	while (colors.contains(drawnColor)) {
    		colorIndex = r.nextInt(6);
        	drawnColor = PlayerColor.values()[colorIndex];
    	}
    	colors.add(drawnColor);
    	int goalIndex = r.nextInt(availableGoals.size());
    	Goal chosenGoal = availableGoals.remove(goalIndex);
    	return new Player(name, drawnColor, chosenGoal);
    }
    
    
    public int place() {
    	if(clicked == null) return -1;
    	Tile tile = clicked;
    	Continent continent = this.board.getTileContinent(tile);
    	Player player = this.phaseManager.playerManager.playerTurn();
    	ArrayList<String> availableCont = new ArrayList<String>();
    	for (String key : player.availableTroops.continentTroops.keySet()) {
    		Integer troops = player.availableTroops.continentTroops.get(key);
    		if (troops > 0) {
    			availableCont.add(key);
    		}
    	}
    	if (availableCont.contains(continent.name) == true) {
    		return this.placeContinent();
    	}
    	else {
    		return this.placeNormal();
    	}
    }

	public int placeNormal() {
		Tile tile = clicked;
		if (tile == null) {
			return -1;
		}
		
		if (tile.owner != this.phaseManager.playerManager.playerTurn()) {
			return -1;
		}

		if (this.phaseManager.playerManager.playerTurn().placeTroop(1, null) == -1) {
			return -1;
		};
		clicked.addTroops(1);
		return this.phaseManager.playerManager.playerTurn().availableTroops.normal;
	}
    
    
    public int attack() {
    	Tile atkTile = clicked;
    	Tile defTile = secondClicked;
    	
    	if (defTile == null || atkTile == null) {
    		return -1;
    	}
    	
    	if (atkTile.troops <= 1 || atkTile.owner != this.phaseManager.playerManager.playerTurn() || defTile.owner == this.phaseManager.playerManager.playerTurn() || defTile.isNeighbour(atkTile) == false) {
    		return -1;
    	}
    	
    	int troopsDef = defTile.troops;
    	int troopsAtk = atkTile.troops;
    	
    	int dicesAtk = troopsAtk -1;
    	int dicesDef = troopsDef;
    	
    	if(dicesAtk >= 3) {
    		dicesAtk = 3;
    	}
    	if(dicesDef >= 3) {
    		dicesDef = 3;
    	}
    	
    	Integer[] atkResults = Dice.roll(dicesAtk);
    	Integer[] defResults = Dice.roll(dicesDef);
    	
    	Arrays.sort(atkResults, Collections.reverseOrder());
    	Arrays.sort(defResults, Collections.reverseOrder());
    	
    	RollEvent results = new RollEvent(atkResults, defResults);
    	this.dicePerformer.dispatch(results);
    	
    	for (int i=0; i<atkResults.length && i< defResults.length;i++) {
    		if (atkResults[i] > defResults[i]) {
    			defTile.removeTroops(1);
    		} else {
    			atkTile.removeTroops(1);
    		}
    	}
    	
    	if (defTile.troops == 0) {
    		int amount = (atkTile.troops >= 4)? 3 : atkTile.troops -1;
			defTile.dominate(amount, this.phaseManager.playerManager.playerTurn());
			atkTile.removeTroops(amount);
			if (!attackSucceed) {
				Card drawnCard = this.deck.draw();
				this.phaseManager.playerManager.addCardToPlayer(drawnCard);
				attackSucceed = true;
			}
    	}
    	return defTile.troops;
    	
    }
    
    public int attackSuper(Integer[] dices) {
    	Tile atkTile = clicked;
    	Tile defTile = secondClicked;
    	
    	if (defTile == null || atkTile == null) {
    		return -1;
    	}
    	
    	if (atkTile.troops <= 1 || atkTile.owner != this.phaseManager.playerManager.playerTurn() || defTile.owner == this.phaseManager.playerManager.playerTurn() || defTile.isNeighbour(atkTile) == false) {
    		return -1;
    	}
    	
    	int troopsDef = defTile.troops;
    	int troopsAtk = atkTile.troops;
    	
    	int dicesAtk = troopsAtk -1;
    	int dicesDef = troopsDef;
    	
    	if(dicesAtk >= 3) {
    		dicesAtk = 3;
    	}
    	if(dicesDef >= 3) {
    		dicesDef = 3;
    	}
    	
    	Integer[] atkResults = new Integer[dicesAtk];
    	Integer[] defResults = new Integer[dicesDef];
    	
    	for (int i = 0; i < dices.length; i++) {
    		if (i <= 2 && i <= dicesAtk-1) {
    			atkResults[i] = dices[i];
    		} else if (i%3 <= dicesDef-1){
    			defResults[i%3] = dices[i]; 
    		}
    	}
    	
    	Arrays.sort(atkResults, Collections.reverseOrder());
    	Arrays.sort(defResults, Collections.reverseOrder());
    	
    	RollEvent results = new RollEvent(atkResults, defResults);
    	this.dicePerformer.dispatch(results);
    	
    	for (int i=0; i<atkResults.length && i< defResults.length;i++) {
    		if (atkResults[i] > defResults[i]) {
    			defTile.removeTroops(1);
    		} else {
    			atkTile.removeTroops(1);
    		}
    	}
    	
    	if (defTile.troops == 0) {
    		int amount = (atkTile.troops >= 4)? 3 : atkTile.troops -1;
			defTile.dominate(amount, this.phaseManager.playerManager.playerTurn());
			atkTile.removeTroops(amount);
			if (!attackSucceed) {
				Card drawnCard = this.deck.draw();
				this.phaseManager.playerManager.addCardToPlayer(drawnCard);
				attackSucceed = true;
			}
    	}
    	return defTile.troops;
    }

	public int move(){
		Tile origin = clicked;
    	Tile destination = secondClicked;
    	
    	if (origin == null || destination == null) {
    		return -1;
    	}

		if (origin.owner != this.phaseManager.playerManager.playerTurn() || destination.owner != this.phaseManager.playerManager.playerTurn() || origin.isNeighbour(destination) == false) {
			return -1;
		}
		
		int originMovedAmount = (this.movedTroops.get(origin) != null)? this.movedTroops.get(origin) : 0;
		
		if (origin.troops - originMovedAmount > 1){
			origin.removeTroops(1);
			destination.addTroops(1);
			int destinationMoved = (this.movedTroops.get(destination) != null)? this.movedTroops.get(destination) : 0;
			destinationMoved += 1;
			this.movedTroops.put(destination, destinationMoved);
		}
		
		return destination.troops;
	}
    
	public int placeContinent() {
		Continent continent = board.getTileContinent(clicked);
		if (clicked.owner != this.phaseManager.playerManager.playerTurn()) {
			return -1;
		}
		int troops = this.phaseManager.playerManager.playerTurn().placeTroop(1, continent.name);
		if (troops == -1) {
			return -1;
		}
		clicked.addTroops(1);
		return troops;
	}
	
	private void removeDeadPlayers() {
		for (int i=0; i<this.players.size();i++) {
			Player p = this.players.get(i);
			if (!p.isAlive()) {
				this.players.remove(i);
			}
		}
	}

	public void nextTurn() {
		this.removeDeadPlayers();
		this.nextRound();
		this.phaseManager.nextPhase();
	}
	
	public void nextRound() {
		if (clicked != null) {			
			clicked.setSelected(false);
		}
		if (secondClicked != null) {			
			secondClicked.setLastSelected(false);
		}
		clicked = null;
		secondClicked = null;
		
		this.movedTroops = new Hashtable<Tile, Integer>();
		
		int playerIndex = players.indexOf(this.phaseManager.playerManager.playerTurn());
		playerIndex = playerIndex < players.size()-1 ? playerIndex + 1 : 0;
		Player nextPlayer = players.get(playerIndex);
		this.phaseManager.playerManager.setPlayerTurn(nextPlayer);
		nextPlayer.calculateTroopsByContinents();
		nextPlayer.calculateTroopsByTiles();
		this.phaseManager.dispatch();
		this.attackSucceed = false;
	}
	
	public void nextPhase() {
		if (clicked != null) {			
			clicked.setSelected(false);
		}
		if (secondClicked != null) {			
			secondClicked.setLastSelected(false);
		}
		clicked = null;
		secondClicked = null;
		
		if (this.phaseManager.playerManager.playerTurn().firstTurn == true) {
			this.phaseManager.playerManager.playerTurn().firstTurn = false;
			this.nextRound();
		} else {
			this.phaseManager.nextPhase();
		}
	}
	
	
	public void checkPlayerWin() {
		Player playerTurn = this.phaseManager.playerManager.playerTurn();

		for (Player p : players) {
			System.out.printf("Jogador sendo analisado: %s\n", p.color);
			if (p.goal.checkWin(players, playerTurn) == true) {
				System.out.printf("Jogador ganhou: %s\n", p.color);
				this.phaseManager.playerManager.setWin();
			}
		}
	}
	
	public void clickTile(String tileName) {
		Tile tileClicked = board.findTile(tileName);
		if (this.phaseManager.phase != GamePhase.Place) {			
			if (this.secondClicked == null && this.clicked != null) {
				this.secondClicked = tileClicked;
				this.secondClicked.setLastSelected(true);
			} else {
				if (this.secondClicked != null) this.secondClicked.setLastSelected(false);
				this.secondClicked = null;
				if (this.clicked != null) this.clicked.setSelected(false);
				this.clicked = tileClicked;
				this.clicked.setSelected(true);
			}
		} else {
			if (this.secondClicked != null) this.secondClicked.setLastSelected(false);
			this.secondClicked = null;
			if (this.clicked != null) this.clicked.setSelected(false);
			this.clicked = tileClicked;
			this.clicked.setSelected(true);
		}
	}
	
	
	public void currentPlayerSubscribe(PlayerObserver playerObserver) {
		this.phaseManager.playerManager.subscribe((Object)playerObserver);
	}
	
	public void phaseManagerSubscribe(PhaseObserver phaseObserver) {
		this.phaseManager.subscribe((Object)phaseObserver);
	}
	
	public void boardSubscribe(BoardObserver boardObserver) {
		this.board.subscribe((Object)boardObserver);
	}
	
	public void diceSubscribe(DiceObserver diceObserver) {
		this.dicePerformer.subscribe(diceObserver);
	}
	
	public void readySubscribe(Object observer) {
		observers.add((ReadyObserver)observer);
	}
	
	public void isReady() {
		switch(this.phaseManager.phase) {
			case Place:
				boolean hasNormalTroops = this.phaseManager.playerManager.playerTurn().availableTroops.normal != 0;
				boolean hasContinentTroops = this.phaseManager.playerManager.playerTurn().availableTroops.hasContinentTroops();
				dispatch(!(hasNormalTroops || hasContinentTroops));
				break;
			default:
				dispatch(true);
				break;
		}
	}
	
	public void dispatch(boolean ready) {
		for (ReadyObserver o : observers) {
			o.trigger(ready);
		}
	}
	
	public boolean loadGame(File file) {
		try {
			Scanner reader;
			reader = new Scanner(file);
			this.createBoardStructure();
			
			Hashtable<String, String[]> playerInfo = new Hashtable<String, String[]>();
			Hashtable<String, Goal> playersGoal = new Hashtable<String, Goal>();
			String line = reader.nextLine();
			line = reader.nextLine();
			while (reader.hasNextLine() && !(line.substring(0, 4).equals("Deck"))) {
				String[] args = line.split("-");
				playerInfo.put(args[0], Arrays.copyOfRange(args, 1, args.length-1));
				Goal playerGoal = null;
				line = reader.nextLine();
				String[] goalArgs = line.split("-");
				switch (args[args.length-1]) {
					case "KillGoal":
						KillGoal kGoal = new KillGoal(goalArgs[0], Utils.getPlayerColorFromString(goalArgs[1]));
						kGoal.setWinnable(Boolean.parseBoolean(goalArgs[2]));
						playerGoal = kGoal;
						break;
					case "ContinentGoal":
						String[] continentNames = Arrays.copyOfRange(goalArgs, 2, goalArgs.length);
						Continent[] continents = new Continent[continentNames.length];
						for (int i=0;i<continentNames.length;i++) {
							for (Continent c: this.board.continents) {							
								if (c.name.equals(continentNames[i])) {
									continents[i] = c;
									break;
								}
							}
						}
						ContinentGoal cGoal = new ContinentGoal(goalArgs[0],continents, Boolean.parseBoolean(goalArgs[1]));
						playerGoal = cGoal;
						break;
					case "TileGoal":
						TileGoal tGoal = new TileGoal(goalArgs[0], Integer.parseInt(goalArgs[1]), Integer.parseInt(goalArgs[2]));
						playerGoal = tGoal;
						break;
				}
				playersGoal.put(args[0], playerGoal);
				line = reader.nextLine();
			}
			
			this.setupGame(playerInfo.keySet().size(), playerInfo.keySet().toArray(String[]::new));
			
			for (Player p: this.players) {
				String[] info = playerInfo.get(p.name);
				p.color = Utils.getPlayerColorFromString(info[0]);
				p.victory = Boolean.parseBoolean(info[1]);
				p.firstTurn = Boolean.parseBoolean(info[2]);
				p.availableTroops.normal = Integer.parseInt(info[3]);
				if (!info[4].equals("")) {
					String[] contArgs = info[4].split("/");
					for (String cont: contArgs) {
						String[] entry = cont.split(":");
						p.availableTroops.continentTroops.put(entry[0], Integer.parseInt(entry[1]));
					}
				}
				if (!info[5].equals("")) {
					String[] cardArgs = info[5].split("/");
					for (String card: cardArgs) {
						String[] entry = card.split(":");
						p.cards.add(new Card(entry[0], Utils.getShapeFromString(entry[1])));
					}
				}
				p.setGoal(playersGoal.get(p.name));
				p.takenTiles = new ArrayList<Tile>();
			}
			
			this.deck.cards = new ArrayList<Card>();
			this.deck.tradeBonus = Integer.parseInt(line.split("-")[1]);
			line = reader.nextLine();
			while (reader.hasNext() && !line.equals("Board")) {
				String[] args = line.split(":");
				Card card = new Card(args[0], Utils.getShapeFromString(args[1]));
				this.deck.cards.add(card);
				line = reader.nextLine();
			}
			
			line = reader.nextLine();
			
			while (reader.hasNext() && !line.equals("PhaseManager")) {
				String[] args = line.split("-");
				Tile t = this.board.findTile(args[0]);
				t.owner = this.findPlayer(args[1]);
				t.owner.addTakenTiles(t);
				t.troops = Integer.parseInt(args[2]);
				t.movedTroops = Integer.parseInt(args[3]);
				line = reader.nextLine();
			}
			
			line = reader.nextLine();
			String[] phaseArgs = line.split("-");
			
			Player p = this.findPlayer(phaseArgs[1]);
			this.phaseManager.phase = Utils.getPhaseFromString(phaseArgs[0]);
			this.phaseManager.playerManager.setPlayerTurn(p);
			
			line = reader.nextLine();
			if (!line.equals("MovedTroops")) {				
				String[] mArgs = line.split("-");
				for(int i = 1; i < mArgs.length; i++) {
					String[] entryArgs = mArgs[i].split(":");
					this.movedTroops = new Hashtable<Tile, Integer>();
					Tile t = this.board.findTile(entryArgs[0]);
					this.movedTroops.put(t, Integer.parseInt(entryArgs[1]));
				}
			}
			line = reader.nextLine();
			line = reader.nextLine();
			this.attackSucceed = Boolean.parseBoolean(line);
			reader.close();
			this.dispatchAll();
			return true;
		} catch(IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public String toString(File file) {
		String result = "Players\n";
		for (Player p: players) {
			result += p.toString();
		}
		
		result += this.deck.toString();
		
		result += this.board.toString();
		
		result += this.phaseManager.toString();
		
		result += "MovedTroops";
		if(this.movedTroops.entrySet().size()==0) {
			result += "\n";
		} else {
			result += "-";
			result += String.join("-", this.movedTroops.entrySet().stream().map(e -> e.getKey().name + "-" + Integer.toString(e.getValue().intValue())).toArray(String[]::new));
		}
		
		result += "Others\n";
		result += Boolean.toString(attackSucceed) + "-";
		System.out.printf("%s\n",result);
		
		try {
			FileWriter writer = new FileWriter(file);
			writer.write(result);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	private Player findPlayer(String name) {
		for (Player p: this.players) {
			if (p.name.equals(name)) return p;
		}
		return null;
	}
}