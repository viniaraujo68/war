package views;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JPanel;

import controllers.GameManager;
import enums.GamePhase;
import models.events.PhaseInfo;
import models.events.RollEvent;
import models.observers.DiceObserver;
import models.observers.PhaseObserver;
import models.observers.ReadyObserver;

public class DiceDisplay extends JPanel implements DiceObserver, PhaseObserver {
	
	private GameManager controller;
	
	private static final int RECT_WIDTH = 130;
	private static final int RECT_HEIGHT = 130;
	private static final int RECT_X = PrimFrame.WIDTH_DEFAULT/2 - RECT_WIDTH - 15;
   	private static final int RECT_Y = PrimFrame.HEIGHT_DEFAULT/2 - RECT_HEIGHT/2 + 115;
	
	public String[] atkDices = new String[3];
	public String[] defDices = new String[3];
	
	public String[] diceImgs = new String[] {
		"src/assets/images/dado_ataque_1.png",
		"src/assets/images/dado_ataque_2.png",
		"src/assets/images/dado_ataque_3.png",
		"src/assets/images/dado_ataque_4.png",
		"src/assets/images/dado_ataque_5.png",
		"src/assets/images/dado_ataque_6.png",
		"src/assets/images/dado_defesa_1.png",
		"src/assets/images/dado_defesa_2.png",
		"src/assets/images/dado_defesa_3.png",
		"src/assets/images/dado_defesa_4.png",
		"src/assets/images/dado_defesa_5.png",
		"src/assets/images/dado_defesa_6.png"
	};
	
	public DiceDisplay() {
        this.controller = new GameManager();
        this.controller.subscribe((DiceObserver)this);
        this.controller.subscribe((PhaseObserver)this);
        this.setOpaque(false);
        this.setBounds(RECT_X, RECT_Y, RECT_WIDTH, RECT_HEIGHT);
   		this.setLayout(null);
   		this.setVisible(true);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.setBounds(RECT_X, RECT_Y, RECT_WIDTH, RECT_HEIGHT);
		Image diceImage = null;
		Image scaledDiceImage = null;
		
		for (int i = 0; i < this.atkDices.length; i++) {
			String imagePath = this.atkDices[i];
			if (this.atkDices[i] == null) break;
			try {
				diceImage = ImageIO.read(new File(imagePath));
				scaledDiceImage = diceImage.getScaledInstance(25, 25, Image.SCALE_SMOOTH);
				g.drawImage(scaledDiceImage, 25 , 10 + (i*40), null);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		for (int i = 0; i < this.defDices.length; i++) {
			String imagePath = this.defDices[i];
			if (this.defDices[i] == null) break;
			try {
				diceImage = ImageIO.read(new File(imagePath));
				scaledDiceImage = diceImage.getScaledInstance(25, 25, Image.SCALE_SMOOTH);
				g.drawImage(scaledDiceImage, 80 , 10 + (i*40), null);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void trigger(RollEvent e) {
		if (e == null) return;
		this.atkDices = new String[3];
		this.defDices = new String[3];
		
		for (int i = 0; i < e.atkDices.length; i++) {
			this.atkDices[i] = this.diceImgs[e.atkDices[i]-1];
		}
		
		for (int i = 0; i < e.defDices.length; i++) {
			this.defDices[i] = this.diceImgs[e.defDices[i]+5];
		}
		this.repaint();
	}

	
	public void trigger(GamePhase phase, PhaseInfo info) {
		if (phase == GamePhase.Attack) {
			this.setVisible(true);
		} else {
			this.atkDices = new String[3];
			this.defDices = new String[3];
			this.setVisible(false);
		}
		
	}
	
}
