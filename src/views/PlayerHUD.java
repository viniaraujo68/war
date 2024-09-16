package views;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import controllers.GameManager;
import enums.PlayerColor;
import models.events.CardRepresent;
import models.events.PlayerEvent;
import models.observers.PlayerObserver;

public class PlayerHUD extends JPanel implements PlayerObserver{
	
	public CardContainer[] cardContainers = new CardContainer[5];
	
	public String playerName;
	public PlayerColor color;
	public String goal;
	
	public int goalY = 90;
	
	public JPanel cardsPanel;
	public int cardsPanelX = 30;
	public int cardsPanelY = RECT_HEIGHT/2 - 30;
	
	private GameManager controller;
	
	
	private static final int RECT_WIDTH = 620;
	private static final int RECT_HEIGHT = 340;
	private static final int RECT_X = PrimFrame.WIDTH_DEFAULT/2 - RECT_WIDTH/2;
   	private static final int RECT_Y = PrimFrame.HEIGHT_DEFAULT/2 - RECT_HEIGHT/2;

   	public void trigger(PlayerEvent player) {
   		this.playerName = player.name;
   		this.color = player.color;
   		this.goal = player.goalDescription;
   		for (int i=0; i<cardContainers.length;i++) {
   			if(i < player.cards.length) {
   				cardContainers[i].setCard(player.cards[i]);   				
   			} else {
   				cardContainers[i].setCard(null);
   			}
   		}
   		System.out.printf("%s - %s\n", player.name, player.color);
   		//this.repaint();
   	}
   	
   	public PlayerHUD() {
   		
   		this.controller = new GameManager();
		
   		
   		this.setBackground(Color.DARK_GRAY);
   		this.setBounds(RECT_X, RECT_Y, RECT_WIDTH, RECT_HEIGHT);
   		this.setLayout(null);
   		this.setVisible(true);
   		
   		
   		cardsPanel = new JPanel();
   		cardsPanel.setBounds(30, RECT_HEIGHT/2 - 30, RECT_WIDTH - 60, CardContainer.CARD_HEIGHT);
   		cardsPanel.setBackground(Color.DARK_GRAY);
   		
   		for (int i = 0; i < cardContainers.length; i++) {
   			CardContainer container = new CardContainer((CardContainer.CARD_WIDTH*i) + (i+1)*(560/21));
   			cardsPanel.add(container);
   			cardContainers[i] = container;
   		}
   		
   		this.add(cardsPanel);
   		
   		
		JButton hideHUDButton = new JButton("X");
		hideHUDButton.setFont(new Font("Times New Roman", Font.PLAIN, 8));
		hideHUDButton.setBounds(RECT_WIDTH - 25, 0, 25, 25);
		this.add(hideHUDButton);
		
		hideHUDButton.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				PlayerHUD playerHUD = (PlayerHUD)e.getComponent().getParent();
				BoardScreen boardScreen = (BoardScreen)playerHUD.getParent();
				boardScreen.remove(playerHUD);
				boardScreen.repaint();
				
				
			}
		});
		
		
		JButton tradeButton = new JButton("Trocar");
		tradeButton.setBounds(RECT_WIDTH/2 - 55, RECT_HEIGHT - 60, 110 , 50);
		this.add(tradeButton);
		tradeButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				PlayerHUD playerHUD = (PlayerHUD)e.getComponent().getParent();
				String[] cardsToTrade = playerHUD.clickedCards();
				if (cardsToTrade != null) {
					if (cardsToTrade.length == 3) {
						playerHUD.controller.trade(cardsToTrade);
					}
				}
				playerHUD.repaint();
			}
		});
		
   	}
   	
   	public String[] clickedCards() {
   		int j = 0;
   		for(CardContainer c : cardContainers) {
   			if (c.clicked == true) {
   				j++;
   			}
   		}
   		String[] cardsToTrade = new String[j];
   		
   		int i = 0;
   		for (CardContainer c : cardContainers) {
   			if (c.clicked == true) {
   				cardsToTrade[i++] = c.getCardName();
   			}
   			c.clicked = false;
   		}
   		
   		if (cardsToTrade.length != 3) { return null;}
   		return cardsToTrade;
   	}
   	
   	
   	@Override
   	protected void paintComponent(Graphics g) {
   		super.paintComponent(g);
   		Graphics2D g2d = (Graphics2D)g;
   		
   		for (CardContainer c : cardContainers) {
   			if(c.clicked) {
   				g2d.setColor(new Color(73,196,147));
   				g2d.fillRect(this.cardsPanelX + c.cardPosX - 10, this.cardsPanelY + c.CARD_Y - 10, CardContainer.CARD_WIDTH +20, 20);
   			}
   		}
   		
   		g2d.setPaint(Color.WHITE);
   		
        g.setFont(new Font("default", Font.BOLD, 18));
		g2d.drawString(playerName, 35, 50);
		
   		int i = 0;
		g.setFont(new Font("default", Font.BOLD, 18));
		for (String line : goal.split("<>")) {
			g2d.drawString(line, 35, goalY + 20*i);
			i++;
		}
   	}

}

