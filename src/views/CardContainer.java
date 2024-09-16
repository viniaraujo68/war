package views;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.text.Normalizer;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import controllers.GameManager;
import models.events.CardRepresent;

public class CardContainer extends JComponent implements MouseListener {
	public static int CARD_WIDTH = (560/21)*3;
	public static int CARD_HEIGHT = (int)(CARD_WIDTH * (1.65));
	public int CARD_Y = 0;
	private CardRepresent card;
	public int cardPosX;
	private Image cardImage;
	public boolean clicked;
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		
		if (card != null) {
			if (cardImage == null) {
				try {
					String imagePath = card.tile.replaceAll(" ", "");
					imagePath = Normalizer.normalize(imagePath, Normalizer.Form.NFD);
					imagePath.toLowerCase();
					Image scaledCardImage = ImageIO.read(new File("src/assets/images/war_carta_"+imagePath+".png"));
					cardImage = scaledCardImage.getScaledInstance(CARD_WIDTH, CARD_HEIGHT, Image.SCALE_SMOOTH);
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
			g.drawImage(cardImage, 0, 0, null);
			
		} else {
			g2d.setColor(Color.GRAY);
			g2d.fillRect(0, 0, CARD_WIDTH, CARD_HEIGHT);	
		}
	}
	
	public CardContainer(int x) {
		this.cardPosX = x;
		this.setBounds(this.cardPosX, this.CARD_Y, CARD_WIDTH, CARD_HEIGHT);
		addMouseListener(this);
	}
	
	public void setCard(CardRepresent card) {
		this.cardImage = null;
		this.card = card;
		//this.repaint();
	}
	
	public String getCardName() {
		return card.tile;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
	    if (card != null) {
	    	if (this.clicked == false) {
	    		this.clicked = true;
	    	} else {
	    		this.clicked = false;
	    	}
	    	
	    }
	    e.getComponent().getParent().getParent().repaint();
	    
	}
	public void mouseEntered(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
}
