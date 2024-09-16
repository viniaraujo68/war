package views;

import java.awt.Graphics;
import java.awt.Toolkit;
import java.io.File;

import javax.swing.*;

public class PrimFrame extends JFrame {

	public static final int WIDTH_DEFAULT = 866;
	public static final int HEIGHT_DEFAULT = 670;
	public int numPlayers;
	public String[] playersNames;
	BoardScreen matchScreen;

	public PrimFrame() {
		setSize(WIDTH_DEFAULT, HEIGHT_DEFAULT);
		ImageIcon imgIcon = new ImageIcon("src/assets/images/icon.png");
        this.setIconImage(imgIcon.getImage());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}
	
	public void startGame(int numPlayers, String[] playersNames) {
		this.numPlayers = numPlayers;
		this.playersNames = playersNames;
		matchScreen = new BoardScreen(numPlayers, playersNames);
		this.add(matchScreen);
		this.repaint();
	}
	
	public void loadGame(File file) {
		matchScreen = new BoardScreen(file);
		this.add(matchScreen);
		repaint();
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (matchScreen != null) {
			matchScreen.repaint();
		}
	}
}
