package views;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;

import controllers.GameManager;
import enums.GamePhase;
import models.events.PhaseInfo;
import models.observers.PhaseObserver;

public class ConfirmButton extends JButton implements PhaseObserver {
	
	public GameManager controller;
	int x, y, width, height;
	private boolean isSuper = false;
	private GamePhase phase;
	Integer[] dices = new Integer[6];

    public ConfirmButton(int x, int y, int width, int height) {
        super("Add tropa");
        this.controller = new GameManager();
        this.controller.subscribe((PhaseObserver) this);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        setBounds(x, y, width, height);

        
        addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				BoardScreen boardScreen = (BoardScreen) e.getComponent().getParent();
				DiceSelector diceSelector = boardScreen.diceSelector;
				dices = diceSelector.getDiceValues();
				
                confirm();
			}
        });
    }

    public void confirm() {
    	
    	this.controller.confirmAction(this.isSuper, dices);
    	dices = new Integer[6];
    	
    }
    
    public void setSuper(boolean b) {
    	this.isSuper = b;
    }
    
    public void trigger(GamePhase phase, PhaseInfo info) {
    	this.phase = phase;
        switch(phase) {
        	case Place:
        		setText("Adicionar");
        		break;
        	case Attack:
        		setText("Atacar!");
        		break;
        	case Move:
        		setText("Mover");
        		break;
        	default:
        		break;
        }
    }   
    
    public void reset() {
    	setBounds(x, y, width, height);
    }
}