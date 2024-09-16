package views;

import javax.imageio.ImageIO;
import javax.swing.*;

import controllers.GameManager;
import enums.GamePhase;
import enums.PlayerColor;
import models.events.PhaseInfo;
import models.events.PlayerEvent;
import models.events.TileEvent;
import models.observers.BoardObserver;
import models.observers.PhaseObserver;
import models.observers.PlayerObserver;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.TileObserver;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class BoardScreen extends JPanel implements BoardObserver, PlayerObserver, PhaseObserver {

	private Image mapImage;
	private Image scaledMapImage;
	private Image bgImage;
	private Image scaledBgImage;
	private ArrayList<TileElipse> tileElipses;
	private JButton showHUDButton;
	private SkipButton skip;
	private ConfirmButton confirm;
	private PlayerHUD hud;
	private PhaseContainer phaseHud;
	private DiceDisplay diceDisplay;
	private JButton save;
	private JButton superPlayer;
	public DiceSelector diceSelector;
	private boolean isSuper = false;
	private GamePhase lastPhase = GamePhase.Move;
	private int lastTroop;
	
	
	private GameManager controller;
	
	
	public void trigger(TileEvent[] tilesInfo) {
		for (TileEvent tileInfo: tilesInfo) {
			//System.out.printf("%s - %d - %s\n", tileInfo.name, tileInfo.troops, tileInfo.color);
			TileElipse tile = findTileByName(tileInfo.name);
			tile.setColor(tileInfo.color);
			tile.setAmount(tileInfo.troops);
			tile.selected = tileInfo.selected;
			tile.lastSelected = tileInfo.lastSelected;
			this.repaint();
		}
	}
	
	public void trigger(PlayerEvent currentPlayer) {
		if (currentPlayer.victory == true) {
			System.out.printf("Vencedor: %s\n", Utils.getColorString(currentPlayer.color));
			EndPanel endPanel = new EndPanel(
					currentPlayer.name,
					currentPlayer.goalDescription,
					Utils.getPlayerColor(currentPlayer.color));
			this.add(endPanel);
			this.remove(showHUDButton);
			this.remove(skip);
			this.remove(confirm);
			this.repaint();
		}
	}
	
	public void trigger(GamePhase phase, PhaseInfo info) {
		if (info == null) {return;}
		if ((lastPhase == GamePhase.Move || lastTroop < info.normal)) {
			this.save.setVisible(true);
		} else {
			this.save.setVisible(false);
		}
		this.save.repaint();
		lastPhase = phase;
		lastTroop = info.normal;
	}
	
	private TileElipse findTileByName(String tileName) {
		for (TileElipse tile: this.tileElipses) {
			if (tileName.equals(tile.tileName)) return tile;
		}
		return null;
	}
	
	public BoardScreen(File file) {
		this.controller = new GameManager();
		this.setUpBoard();
		this.controller.subscribe((BoardObserver)this);
		this.controller.subscribe((PlayerObserver)this);
		this.controller.subscribe(hud);
		this.controller.load(file);
	}

	public BoardScreen(int numPlayers, String[] playersNames) {
		
		this.controller = new GameManager();
		this.controller.start(numPlayers, playersNames);
		
		this.setUpBoard();
		
		this.controller.subscribe((BoardObserver)this);
		this.controller.subscribe((PlayerObserver)this);
		this.controller.subscribe((PhaseObserver)this);
		this.controller.subscribe(hud);
		this.controller.dispatchAll();
		
	}
	
	public void setUpBoard() {
		diceSelector = new DiceSelector();
		diceDisplay = new DiceDisplay();
		hud = new PlayerHUD();
		phaseHud = new PhaseContainer();
		confirm = new ConfirmButton(PrimFrame.WIDTH_DEFAULT/2 - 50, PrimFrame.HEIGHT_DEFAULT - 150, 110, 50);
		skip = new SkipButton(PrimFrame.WIDTH_DEFAULT/2 - 50, PrimFrame.HEIGHT_DEFAULT - 90, 110, 50);
		showHUDButton = new JButton("<html><p style=\'text-align: center;'>Mostrar<br>cartas</p></html>");
		showHUDButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				e.getComponent().getParent().add(hud);
				hud.repaint();
				
			}
		});
		showHUDButton.setBounds(15, PrimFrame.HEIGHT_DEFAULT - 115, 95, 50);
		this.add(showHUDButton);
		this.add(diceDisplay);
		this.add(diceSelector);
		this.add(phaseHud);
		this.add(confirm);
		this.add(skip);
		
		superPlayer = new JButton("SuperJogador");
		superPlayer.setBounds(PrimFrame.WIDTH_DEFAULT - 270, PrimFrame.HEIGHT_DEFAULT - 95, 130, 40);
		superPlayer.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				BoardScreen boardScreen = (BoardScreen) e.getComponent().getParent();
				boardScreen.isSuper = boardScreen.isSuper ? false : true;
				boardScreen.confirm.setSuper(boardScreen.isSuper);
				boardScreen.diceSelector.setSuper(boardScreen.isSuper);
			}
		});
		this.add(superPlayer);
		
		save = new JButton("Salvar");
		save.setBounds(PrimFrame.WIDTH_DEFAULT - 130, PrimFrame.HEIGHT_DEFAULT - 95, 100, 40);
		save.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				BoardScreen boardScreen = (BoardScreen) e.getComponent().getParent();
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Salvar um jogo");
				int userSelection = fileChooser.showSaveDialog(null);
				if (userSelection == JFileChooser.APPROVE_OPTION) {
					File loadFile = fileChooser.getSelectedFile();
					boardScreen.controller.save(loadFile);
				}
			}
		});
		this.add(save);
		
		
		this.setBounds(0, 0, PrimFrame.WIDTH_DEFAULT, PrimFrame.HEIGHT_DEFAULT);
		try {

			mapImage = ImageIO.read(new File("src/assets/images/war_tabuleiro_mapa copy.png"));
			scaledMapImage = mapImage.getScaledInstance(860, 640, Image.SCALE_SMOOTH);

			bgImage = ImageIO.read(new File("src/assets/images/war_tabuleiro_fundo.png"));
			scaledBgImage = bgImage.getScaledInstance(860, 640, Image.SCALE_SMOOTH);

		} catch (IOException e) {
			e.printStackTrace();
		}

		tileElipses = new ArrayList<>();
		TileElipse brasil = new TileElipse("Brasil", PlayerColor.Yellow, 0, 229, 366);
		TileElipse argentina = new TileElipse("Argentina", PlayerColor.Yellow, 0, 220, 450);
		TileElipse africaDoSul = new TileElipse("Africa do Sul", PlayerColor.Yellow, 0, 474, 460);
		TileElipse alasca = new TileElipse("Alasca", PlayerColor.Yellow, 0, 69, 107);
		TileElipse angola = new TileElipse("Angola", PlayerColor.Yellow, 0, 454, 408);
		TileElipse argelia = new TileElipse("Argelia", PlayerColor.Yellow, 0, 379, 301);
		TileElipse arabiaSaudita = new TileElipse("Arabia Saudita", PlayerColor.Yellow, 0, 554, 325);
		TileElipse australia = new TileElipse("Australia", PlayerColor.Yellow, 0, 720, 498);
		TileElipse bangladesh = new TileElipse("Bangladesh", PlayerColor.Yellow, 0, 706, 290);
		TileElipse calgary = new TileElipse("Calgary", PlayerColor.Yellow, 0, 141, 114);
		TileElipse california = new TileElipse("California", PlayerColor.Yellow, 0, 87, 205);
		TileElipse cazaquistao = new TileElipse("Cazaquistao", PlayerColor.Yellow, 0, 663, 163);
		TileElipse china = new TileElipse("China", PlayerColor.Yellow, 0, 657, 232);
		TileElipse coreiaDoNorte = new TileElipse("Coreia do Norte", PlayerColor.Yellow, 0, 704, 240);
		TileElipse coreiaDoSul = new TileElipse("Coreia do Sul", PlayerColor.Yellow, 0, 738, 276);
		TileElipse egito = new TileElipse("Egito", PlayerColor.Yellow, 0, 469, 314);
		TileElipse espanha = new TileElipse("Espanha", PlayerColor.Yellow, 0, 369, 225);
		TileElipse estonia = new TileElipse("Estonia", PlayerColor.Yellow, 0, 562, 110);
		TileElipse franca = new TileElipse("Franca", PlayerColor.Yellow, 0, 399, 195);
		TileElipse groenlandia = new TileElipse("Groenlandia", PlayerColor.Yellow, 0, 262, 82);
		TileElipse india = new TileElipse("India", PlayerColor.Yellow, 0, 658, 310);
		TileElipse indonesia = new TileElipse("Indonesia", PlayerColor.Yellow, 0, 745, 408);
		TileElipse ira = new TileElipse("Ira", PlayerColor.Yellow, 0, 589, 261);
		TileElipse iraque = new TileElipse("Iraque", PlayerColor.Yellow, 0, 560, 269);
		TileElipse italia = new TileElipse("Italia", PlayerColor.Yellow, 0, 449, 181);
		TileElipse japao = new TileElipse("Japao", PlayerColor.Yellow, 0, 783, 211);
		TileElipse jordania = new TileElipse("Jordania", PlayerColor.Yellow, 0, 514, 283);
		TileElipse letonia = new TileElipse("Letonia", PlayerColor.Yellow, 0, 541, 146);
		TileElipse mexico = new TileElipse("Mexico", PlayerColor.Yellow, 0, 112, 288);
		TileElipse mongolia = new TileElipse("Mongolia", PlayerColor.Yellow, 0, 708, 199);
		TileElipse nigeria = new TileElipse("Nigeria", PlayerColor.Yellow, 0, 419, 348);
		TileElipse novaYork = new TileElipse("Nova York", PlayerColor.Yellow, 0, 174, 210);
		TileElipse novaZelandia = new TileElipse("Nova Zelandia", PlayerColor.Yellow, 0, 767, 534);
		TileElipse paquistao = new TileElipse("Paquistao", PlayerColor.Yellow, 0, 617, 250);
		TileElipse peru = new TileElipse("Peru", PlayerColor.Yellow, 0, 187, 394);
		TileElipse perth = new TileElipse("Perth", PlayerColor.Yellow, 0, 672, 486);
		TileElipse polonia = new TileElipse("Polonia", PlayerColor.Yellow, 0, 473, 149);
		TileElipse quebec = new TileElipse("Quebec", PlayerColor.Yellow, 0, 231, 150);
		TileElipse reinoUnido = new TileElipse("Reino Unido", PlayerColor.Yellow, 0, 372, 126);
		TileElipse romenia = new TileElipse("Romenia", PlayerColor.Yellow, 0, 482, 214);
		TileElipse russia = new TileElipse("Russia", PlayerColor.Yellow, 0, 652, 116);
		TileElipse siberia = new TileElipse("Siberia", PlayerColor.Yellow, 0, 740, 104);
		TileElipse siria = new TileElipse("Siria", PlayerColor.Yellow, 0, 541, 227);
		TileElipse somalia = new TileElipse("Somalia", PlayerColor.Yellow, 0, 515, 387);
		TileElipse suecia = new TileElipse("Suecia", PlayerColor.Yellow, 0, 451, 88);
		TileElipse tailandia = new TileElipse("Tailandia", PlayerColor.Yellow, 0, 740, 329);
		TileElipse texas = new TileElipse("Texas", PlayerColor.Yellow, 0, 134, 199);
		TileElipse turquia = new TileElipse("Turquia", PlayerColor.Yellow, 0, 588, 186);
		TileElipse ucrania = new TileElipse("Ucrania", PlayerColor.Yellow, 0, 499, 185);
		TileElipse vancouver = new TileElipse("Vancouver", PlayerColor.Yellow, 0, 129, 150);
		TileElipse venezuela = new TileElipse("Venezuela", PlayerColor.Yellow, 0, 155, 344);

		tileElipses.add(brasil);
		tileElipses.add(argentina);
		tileElipses.add(africaDoSul);
		tileElipses.add(alasca);
		tileElipses.add(angola);
		tileElipses.add(argelia);
		tileElipses.add(arabiaSaudita);
		tileElipses.add(australia);
		tileElipses.add(bangladesh);
		tileElipses.add(calgary);
		tileElipses.add(california);
		tileElipses.add(cazaquistao);
		tileElipses.add(china);
		tileElipses.add(coreiaDoNorte);
		tileElipses.add(coreiaDoSul);
		tileElipses.add(egito);
		tileElipses.add(espanha);
		tileElipses.add(estonia);
		tileElipses.add(franca);
		tileElipses.add(groenlandia);
		tileElipses.add(india);
		tileElipses.add(indonesia);
		tileElipses.add(ira);
		tileElipses.add(iraque);
		tileElipses.add(italia);
		tileElipses.add(japao);
		tileElipses.add(jordania);
		tileElipses.add(letonia);
		tileElipses.add(mexico);
		tileElipses.add(mongolia);
		tileElipses.add(nigeria);
		tileElipses.add(novaYork);
		tileElipses.add(novaZelandia);
		tileElipses.add(paquistao);
		tileElipses.add(peru);
		tileElipses.add(perth);
		tileElipses.add(polonia);
		tileElipses.add(quebec);
		tileElipses.add(reinoUnido);
		tileElipses.add(romenia);
		tileElipses.add(russia);
		tileElipses.add(siberia);
		tileElipses.add(siria);
		tileElipses.add(somalia);
		tileElipses.add(suecia);
		tileElipses.add(tailandia);
		tileElipses.add(texas);
		tileElipses.add(turquia);
		tileElipses.add(ucrania);
		tileElipses.add(vancouver);
		tileElipses.add(venezuela);
		
		for (TileElipse t : tileElipses) {
			this.addMouseListener(t);
			//this.add(t);
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(scaledBgImage, 0, 0, null);
		g.drawImage(scaledMapImage, 0, 0, null);
		this.setBounds(0, 0, PrimFrame.WIDTH_DEFAULT, PrimFrame.HEIGHT_DEFAULT);
		for (TileElipse tile : tileElipses) {
			tile.init(g);
		}
		showHUDButton.setBounds(15, PrimFrame.HEIGHT_DEFAULT - 115, 95, 50);
		superPlayer.setBounds(PrimFrame.WIDTH_DEFAULT - 270, PrimFrame.HEIGHT_DEFAULT - 95, 130, 40);
		save.setBounds(PrimFrame.WIDTH_DEFAULT - 130, PrimFrame.HEIGHT_DEFAULT - 95, 100, 40);
		skip.reset();
		confirm.reset();
		phaseHud.repaint();
		diceDisplay.repaint();
		save.repaint();
	}
}
