package views;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class EndPanel extends JPanel {
	private static int WIDTH = PrimFrame.WIDTH_DEFAULT - 140;
	private static int HEIGHT = PrimFrame.HEIGHT_DEFAULT - 160;
	
	private static int POS_X = PrimFrame.WIDTH_DEFAULT/2 - WIDTH/2;
	private static int POS_Y = PrimFrame.HEIGHT_DEFAULT/2 - HEIGHT/2;
	
	private String winSentence;
	private String playerGoal;
	private Color playerColor;
	
	private int trophyWidth = 250;
	private int trophyHeight = 250;
	
	private Image trophyImage;
	private Image trophyScaledImage;
	
	private JPanel buttonsPanel;
	private int buttonsPanelWidth = 400;
	private int buttonsPanelHeight = 80;
	
	private JButton playAgainButton;
	private JButton homeScreenButton;
	
	public EndPanel(String playerThatWon, String playerGoal, Color playerColor) {
		System.out.println("entrou no EndPanel\n");
		
		this.winSentence = "Contemplem " + playerThatWon + ", o vencedor da guerra!";
		this.playerColor = playerColor;
		this.playerGoal = "Objetivo: " + playerGoal;
		
		this.setBackground(Color.DARK_GRAY);
   		this.setBounds(POS_X, POS_Y, WIDTH, HEIGHT);
   		this.setLayout(null);
   		this.setVisible(true);
   		
   		buttonsPanel = new JPanel();
   		buttonsPanel.setBackground(Color.DARK_GRAY);
   		buttonsPanel.setBounds(
   				WIDTH/2 - buttonsPanelWidth/2,
   				HEIGHT - buttonsPanelHeight - 50,
   				buttonsPanelWidth,
   				buttonsPanelHeight);
   		
   		playAgainButton = new JButton("Jogar novamente");
   		playAgainButton.setBounds(buttonsPanelWidth/20, 0, 150, buttonsPanelHeight);
   		
   		playAgainButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				BoardScreen oldMatchScreen = (BoardScreen)e.getComponent().getParent().getParent().getParent();
				PrimFrame primFrame = (PrimFrame)oldMatchScreen.getTopLevelAncestor();
				System.out.println("Entrou\n");
                
                primFrame.remove(oldMatchScreen);
                
                primFrame.startGame(primFrame.numPlayers, primFrame.playersNames);
			}
        });
   		
   		homeScreenButton = new JButton("Tela inicial");
   		homeScreenButton.setBounds(buttonsPanelWidth - buttonsPanelWidth/20 - 150, 0, 150, buttonsPanelHeight);
   		
   		homeScreenButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				BoardScreen oldMatchScreen = (BoardScreen)e.getComponent().getParent().getParent().getParent();
				PrimFrame primFrame = (PrimFrame)oldMatchScreen.getTopLevelAncestor();
                
                primFrame.remove(oldMatchScreen);
                
                Menu menu = new Menu();
                primFrame.add(menu);
                primFrame.repaint();
			}
        });
   		
   		
   		buttonsPanel.setLayout(null);
   		buttonsPanel.add(playAgainButton);
   		buttonsPanel.add(homeScreenButton);
   		
   		this.add(buttonsPanel);
	}
	
   	public void paintComponent(Graphics g) {
   		super.paintComponent(g);
   		Graphics2D g2d = (Graphics2D)g;

   		this.setBounds(POS_X, POS_Y, WIDTH, HEIGHT);

   		int eliW = 50, eliH = 50;
   		int eliX = WIDTH/2 - (int)(eliW/2);
   		int eliY = 50;
		Ellipse2D colorCircle = new Ellipse2D.Double(eliX, eliY, eliW, eliH);
		g2d.setPaint(this.playerColor);
		g2d.fill(colorCircle);
		
   		try {
			trophyImage = ImageIO.read(new File("src/assets/images/trophy.png"));
			trophyScaledImage = trophyImage.getScaledInstance(this.trophyWidth, this.trophyHeight, Image.SCALE_SMOOTH);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
   		
   		g2d.setPaint(Color.WHITE);
   		g.setFont(new Font("Times New Roman", Font.BOLD, 25));
   		int strlen = (int) g2d.getFontMetrics().getStringBounds(winSentence, g2d).getWidth();
        int startWinSentence = WIDTH/2 - strlen / 2;
   		g2d.drawString(winSentence, startWinSentence, 40);
   		
   		int i = 0;
   		int startGoalLine;
   		g.setFont(new Font("default", Font.PLAIN, 20));
   		for (String line : playerGoal.split("\n")) {
   			strlen = (int) g2d.getFontMetrics().getStringBounds(line, g2d).getWidth();
   	        startGoalLine = WIDTH/2 - strlen / 2;
   	        
   			g2d.drawString(line, startGoalLine, 130 + 20 * i);
   			
   			i++;
   		}
   		g2d.drawImage(trophyScaledImage, WIDTH/2 - this.trophyWidth/2, HEIGHT/2 - this.trophyHeight/2 + 10, null);
	}
	
}

