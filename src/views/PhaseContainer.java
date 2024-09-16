package views;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.text.Normalizer;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import controllers.GameManager;
import enums.GamePhase;
import enums.PlayerColor;
import models.events.PhaseInfo;
import models.events.PlayerEvent;
import models.observers.PhaseObserver;
import models.observers.PlayerObserver;
//implements PhaseObserver, PlayerObserver

public class PhaseContainer extends JPanel implements PlayerObserver, PhaseObserver {
	
	public static int PC_WIDTH = PrimFrame.WIDTH_DEFAULT;
	public static int PC_HEIGHT = 60;
	
	public GameManager controller;
	public String phaseLabel;
	public String troopsLabel;
	PlayerColor playerColor;
	public int troops;
	public int amNorte;
	public int amSul;
	public int africa;
	public int europe;
	public int asia;
	public int oceania;
	
	
	
	public PhaseContainer() {
		controller = new GameManager();
		this.controller.subscribe((PlayerObserver) this);
		this.controller.subscribe((PhaseObserver) this);
		phaseLabel = "Posicionar tropas";
		troopsLabel = "Tropas Genericas ["+troops+"] - Am.Norte ["+amNorte+"] - Am.Sul ["+amSul+
				"] - Africa ["+africa+"] - Europa ["+europe+"] - Asia ["+asia+"] - Oceania ["+oceania+"]";
		this.setLayout(null);
		this.setBounds(PrimFrame.WIDTH_DEFAULT/2 - PC_WIDTH/2,0,PC_WIDTH, PC_HEIGHT);
		this.setVisible(true);
		
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.setBounds(PrimFrame.WIDTH_DEFAULT/2 - PC_WIDTH/2,0,PC_WIDTH, PC_HEIGHT);
		Graphics2D g2d = (Graphics2D)g;
		troopsLabel = "Tropas Genericas ["+troops+"] - Am.Norte ["+amNorte+"] - Am.Sul ["+amSul+
				"] - Africa ["+africa+"] - Europa ["+europe+"] - Asia ["+asia+"] - Oceania ["+oceania+"]";
		
		int strlen = (int) g2d.getFontMetrics().getStringBounds(phaseLabel, g2d).getWidth();
        int startTurn = PC_WIDTH/2 - strlen / 2;
        strlen = (int) g2d.getFontMetrics().getStringBounds(troopsLabel, g2d).getWidth();
        int startTroops = PC_WIDTH/2 - strlen / 2;

        Color color = Utils.getPlayerColor(playerColor);
        this.setBackground(color);
		
        if (playerColor == PlayerColor.Black) {
        	g2d.setPaint(Color.WHITE);
        } else {
        	g2d.setPaint(Color.BLACK);
        }

        g.setFont(new Font("default", Font.BOLD, 18));
		g2d.drawString(phaseLabel, startTurn - 20, PC_HEIGHT/2 - 5);
		if (phaseLabel == "Posicionar tropas") {
			g.setFont(new Font("default", Font.BOLD, 13));
			g2d.drawString(troopsLabel, startTroops - 5, PC_HEIGHT/2 + 15);
		}
	}
	
	public void trigger(PlayerEvent event) {
		this.playerColor = event.color;
		repaint();
	}
	
	public void trigger(GamePhase phase, PhaseInfo info) {
		this.phaseLabel = Utils.getPhaseString(phase);
		
		if (info != null) {
			troops = info.normal;
			
			Integer continentTroop = info.continentTroops.get("America do Norte");
			amNorte = continentTroop != null ? continentTroop : 0;
			
			continentTroop = info.continentTroops.get("America do Sul");
			amSul = continentTroop != null ? continentTroop : 0;
			
			continentTroop = info.continentTroops.get("Africa");
			africa = continentTroop != null ? continentTroop : 0;
			
			continentTroop = info.continentTroops.get("Europa");
			europe = continentTroop != null ? continentTroop : 0;
			
			continentTroop = info.continentTroops.get("Asia");
			asia = continentTroop != null ? continentTroop : 0;
			
			continentTroop = info.continentTroops.get("Oceania");
			oceania = continentTroop != null ? continentTroop : 0;			
		}
		
		repaint();
	}
	

}
