package views;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;

import javax.swing.JComponent;

import controllers.GameManager;
import enums.PlayerColor;

public class TileElipse extends JComponent implements MouseListener {
	
	private static final int r = 12;

	public String tileName;
	public PlayerColor playerColor;
	public int troops;
	private int x;
	private int y;
	boolean selected = false;
	boolean lastSelected = false;
	
	public GameManager controller;
	
	public TileElipse(String tileName, PlayerColor playerColor, int troops , int x, int y) {
		this.troops = troops;
		this.playerColor = playerColor;
		this.tileName = tileName;
		this.x = x;
		this.y = y;
		this.controller = new GameManager();
		this.addMouseListener(this);
		this.setBounds(x,y, r * 2, r * 2);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D) g;
		Ellipse2D circle = new Ellipse2D.Double(x, y, r * 2, r * 2);
		
		Ellipse2D border = new Ellipse2D.Double(x-5, y-5, r * 2+10, r * 2+10);


		if (this.selected == true) {
			g2d.setPaint(new Color(169,255,56));
			g2d.fill(border);
		} else if(this.lastSelected == true) {
			g2d.setPaint(new Color(255,138,241));
			g2d.fill(border);
		} else {
			g2d.setPaint(new Color(0,0,0,0));
			g2d.fill(border);
		}
		
		Color color = Utils.getPlayerColor(playerColor);
		g2d.setPaint(color);
		g2d.fill(circle);
		g.setFont(new Font("default", Font.BOLD, 13));
		
		g2d.setColor(Color.BLACK);
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				g2d.drawString(tileName, x + i - 5, y + j + 35);
			}
		}

		g2d.setColor(Color.WHITE);
		g.drawString(tileName, x - 5, y + 35);

		if (playerColor == PlayerColor.Black) {
			g2d.setPaint(Color.WHITE);
		} else {
			g2d.setPaint(Color.BLACK);
		}

		int strlen = (int) g2d.getFontMetrics().getStringBounds(Integer.toString(troops), g2d).getWidth();
		int start = r - strlen / 2;

		g.drawString(Integer.toString(troops), x + start, y + 17);

	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		int mouseX = e.getX();
	    int mouseY = e.getY();
	    if (mouseX >= x && mouseX <= x + r * 2 && mouseY >= y && mouseY <= y + r * 2) {
	    	this.controller.clickTile(tileName);
	    }
		
	}
	public void mouseEntered(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	
	public void setColor(PlayerColor color) {
		this.playerColor = color;
	}
	
	public void setAmount(int amount) {
		this.troops = amount;
	}
	
	public void init(Graphics g) {
		this.paintComponent(g);
	}
	
}
