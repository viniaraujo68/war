package views;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;

import controllers.GameManager;
import enums.GamePhase;
import models.observers.ReadyObserver;

public class SkipButton extends JButton implements ReadyObserver {
	
	public GameManager controller;
	int x, y, width, height;

    public SkipButton(int x, int y, int width, int height) {
        super("Proximo");
        this.controller = new GameManager();
        this.controller.subscribe((ReadyObserver) this);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        
        this.setEnabled(false);
        
        setBounds(x, y, width, height);

        
        addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
                skip();
			}
        });
    }

    public void skip() {
    	if (isEnabled()) {
    		this.controller.next();
    	}
    }
    
    public void trigger(boolean ready) {
        this.setEnabled(ready);
    }
    
    public void reset() {
    	setBounds(x, y, width, height);
    }
}