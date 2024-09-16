package controllers;


import java.io.File;

import enums.GamePhase;
import models.Game;
import models.events.PhaseInfo;
import models.observers.BoardObserver;
import models.observers.DiceObserver;
import models.observers.PhaseObserver;
import models.observers.PlayerObserver;
import models.observers.ReadyObserver;

public class GameManager implements PhaseObserver{
	
	private Game model;
	private static GamePhase phase;
	
    public GameManager() {
        this.model = Game.start();
        this.model.phaseManagerSubscribe(this);
    }
    
    public void confirmAction(boolean isSuper, Integer[] dices) {
    	switch (GameManager.phase) {
    		case Place:
    			this.model.place();
    			break;
    		case Attack:
    			if (isSuper) {
    				this.model.attackSuper(dices);
    			} else {
    				this.model.attack();
    			}
    			break;
    		case Move:
    			this.model.move();
    			break;
    	}
    	this.model.isReady();
    }
    
    public void start(int numPlayers, String[] playersNames) {
    	this.model.reset(numPlayers, playersNames);    
    }
    
    public void clickTile(String tileName){
    	this.model.clickTile(tileName);
    }
    
    public void trade(String[] cardsNames) {
    	this.model.trade(cardsNames);
    }
    
    public void subscribe(BoardObserver boardObserver) {
    	this.model.boardSubscribe(boardObserver);
    }
    
    public void subscribe(PlayerObserver playerObserver) {
    	this.model.currentPlayerSubscribe(playerObserver);
    }
    
    public void subscribe(ReadyObserver readyObserver) {
    	this.model.readySubscribe(readyObserver);
    }
    
    public void subscribe(PhaseObserver phaseObserver) {
    	this.model.phaseManagerSubscribe(phaseObserver);
    }
    
    public void subscribe(DiceObserver diceObserver) {
    	this.model.diceSubscribe(diceObserver);
    }
    
    public void dispatchAll() {
    	this.model.dispatchAll();
    }
    
    public void trigger(GamePhase phase, PhaseInfo info) {
    	GameManager.phase = phase;
    }
    
    public void next() {
    	this.model.checkPlayerWin();
    	if(phase == GamePhase.Move) {
    		this.model.nextTurn();
    	}
    	else {
    		this.model.nextPhase();
    	}
    	this.model.isReady();
    }
    
    public void save(File file) {
    	this.model.toString(file);
    }
    
    public void load(File file) {
    	this.model.loadGame(file);
    }
}