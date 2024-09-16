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
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import controllers.GameManager;


public class DiceSelector extends JPanel {
	
	private GameManager controller;
	private boolean isSuper = false;
	
	private static final int RECT_WIDTH = 130;
	private static final int RECT_HEIGHT = 130;
	private static final int RECT_X = PrimFrame.WIDTH_DEFAULT/2 - RECT_WIDTH - 85;
   	private static final int RECT_Y = PrimFrame.HEIGHT_DEFAULT/2 + RECT_HEIGHT/2 - 205;
   	private JSpinner[] spinners = new JSpinner[6];
	
	public Integer[] dices = new Integer[6];

	
	public DiceSelector() {
        this.controller = new GameManager();
        this.setOpaque(false);

        this.setBounds(RECT_X, RECT_Y, RECT_WIDTH, RECT_HEIGHT);
   		//this.setLayout(null);
   		this.setVisible(isSuper);
   		for (int i = 0; i < 6; i++) {
   			SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1,1,6,1);
   			JSpinner spinner = new JSpinner(spinnerModel);
   			this.spinners[i] = spinner;
   			this.spinners[i].setBounds( i > 2 ? 75 : 25, 20 + 10*(i%3), 30, 22);
   			this.add(spinners[i]);
   		}
	}
	
	public void setSuper(boolean b) {
		this.isSuper = b;
		this.setVisible(isSuper);
		repaint();
	}
	
	public Integer[] getDiceValues() {
		for (int i = 0; i < 6; i++) {
			dices[i] = (Integer) this.spinners[i].getValue();
		}
		return dices;
	} 
	
	
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.setVisible(isSuper);
		this.setBounds(RECT_X, RECT_Y, RECT_WIDTH, RECT_HEIGHT);
		for (int i = 0; i < 6; i++) {
   			this.spinners[i].setBounds( i > 2 ? 75 : 25, 20 + 40*(i%3), 30, 22);
   		}
	}
	
}