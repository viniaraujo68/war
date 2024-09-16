package views;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Menu extends JPanel {
	private JButton newGameButton;
	private JButton loadGameButton;
	private JTextField playerCountField;
	private JLabel titleLabel;
	private JLabel titleBorder;
	private JLabel playerCountLabel;
	private JLabel failLabel;
	private JButton playerCountConfirm;
	private JPanel buttonPanel;
	private JButton confirmNamesButton;
	private int buttonsWidth = 250;
	private int buttonsHeight = 100;
	private int buttonPanelWidth;
	private int buttonPanelHeight;
	private ArrayList<JTextField> nameFields = new ArrayList<JTextField>();
	private String[] playersNames;
	private int playerAmount;
	private Image backgroundImage;
	private Image backgroundScaledImage;

	public Menu() {

		this.setBounds(0, 0, PrimFrame.WIDTH_DEFAULT, PrimFrame.HEIGHT_DEFAULT);
		setLayout(null);

		// Add the buttons and input field to the panel
		buttonPanel = new JPanel();

		buttonPanelWidth = PrimFrame.WIDTH_DEFAULT/3;
		buttonPanelHeight = 3*PrimFrame.HEIGHT_DEFAULT/5;
		
		buttonPanel.setBounds(PrimFrame.WIDTH_DEFAULT/3, PrimFrame.HEIGHT_DEFAULT/5, buttonPanelWidth, buttonPanelHeight);
		buttonPanel.setOpaque(false);
		
		titleLabel = new JLabel("WAR", JLabel.CENTER);
		titleLabel.setLocation(PrimFrame.WIDTH_DEFAULT/2 - 150, 30);
		titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 85));
		titleLabel.setSize(300, 200);
		
		titleBorder = new JLabel("WAR", JLabel.CENTER);
		titleBorder.setLocation(PrimFrame.WIDTH_DEFAULT/2 - 150, 30);
		titleBorder.setFont(new Font("Times New Roman", Font.BOLD, 87));
		titleBorder.setSize(300, 200);
		titleBorder.setForeground(Color.WHITE);
		
		newGameButton = new JButton("Novo Jogo");
		newGameButton.setBounds(buttonPanelWidth/2 - this.buttonsWidth/2, 3*buttonPanelHeight/16, this.buttonsWidth, this.buttonsHeight);
		
		JPanel playerCountPanel = new JPanel();
		playerCountPanel.setBounds(buttonPanelWidth, PrimFrame.HEIGHT_DEFAULT/8, buttonPanelWidth+10, buttonPanelHeight);
		playerCountPanel.setOpaque(false);
		playerCountLabel = new JLabel("Quantas pessoas vao jogar?", JLabel.CENTER);
		playerCountLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
		playerCountLabel.setForeground(Color.WHITE);

		playerCountLabel.setLocation(buttonPanelWidth/2 - 150, 0);
		playerCountLabel.setFont(new Font("Times New Roman", Font.PLAIN, 25));
		playerCountLabel.setSize(300, 200);
		
		playerCountField = new JTextField("3");
		playerCountField.setBounds(buttonPanelWidth/2 - 25, 150, 50, 50);
		playerCountField.setHorizontalAlignment(JTextField.CENTER);
		
		playerCountConfirm = new JButton("Confirmar");
		playerCountConfirm.setBounds(buttonPanelWidth/2 - 75, 230, 150, 45);
		
		JPanel nicknamesPanel = new JPanel();
		int nicknamesPanelWidth = PrimFrame.WIDTH_DEFAULT - 200;
		int nicknamesPanelHeight = PrimFrame.HEIGHT_DEFAULT - 140;
		nicknamesPanel.setBounds(
				PrimFrame.WIDTH_DEFAULT/2 - nicknamesPanelWidth/2,
				PrimFrame.HEIGHT_DEFAULT/2 - nicknamesPanelHeight/2,
				nicknamesPanelWidth, 
				nicknamesPanelHeight
		);
		nicknamesPanel.setBackground(new Color(10, 52, 61));
		JLabel nicknameLabel = new JLabel("Insira o nome de cada jogador:");
		nicknameLabel.setForeground(Color.WHITE);
		nicknameLabel.setFont(new Font("Times New Roman", Font.PLAIN, 25));
		nicknameLabel.setSize(400, 200);
		nicknameLabel.setLocation(nicknamesPanelWidth/2 - 150, 0);
		nicknamesPanel.add(nicknameLabel);
		
		
		playerCountPanel.add(playerCountLabel);
		playerCountPanel.add(playerCountField);
		playerCountPanel.add(playerCountConfirm);
		
		newGameButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Menu menu = (Menu)e.getComponent().getParent().getParent();
				menu.remove(buttonPanel);
				menu.remove(titleBorder);
				menu.remove(titleLabel);
				menu.add(playerCountPanel);
				menu.repaint();
			}
		});
		
		
		playerCountConfirm.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Menu menu = (Menu)e.getComponent().getParent().getParent();
				PrimFrame screen= (PrimFrame)menu.getTopLevelAncestor();
				try {
                    playerAmount = Integer.parseInt(menu.playerCountField.getText());
                    if (playerAmount >= 3 && playerAmount <= 6) {
                    	screen.remove(menu);
                    	screen.getContentPane().setBackground(new Color(10, 52, 61));
                    	
                    	for (int i = 0; i < playerAmount; i++) {
                    		JTextField nameField = new JTextField("Jogador" + Integer.toString(i+1));
                    		JLabel nameLabel = new JLabel("Jogador " + Integer.toString(i+1) + ":");
                    		nameLabel.setForeground(Color.WHITE);
                    		nameLabel.setFont(new Font("Times New Roman", Font.PLAIN, 20));
                    		nameLabel.setBounds(35 + 200*(i % 3), i > 2 ? 300 : 200, 90, 35);
                    		nameField.setBounds(125 + 200*(i % 3), i > 2 ? 300 : 200, 100, 35);
                    		nameField.setHorizontalAlignment(JTextField.CENTER);
                    		nameFields.add(nameField);
                    		
                    		confirmNamesButton = new JButton("Comecar guerra!");
                    		confirmNamesButton.setBounds(nicknamesPanelWidth/2 - 105, nicknamesPanelHeight/2 + 180, 210, 80);
                    		
                    		confirmNamesButton.addMouseListener(new MouseAdapter() {
                    			@Override
                    			public void mouseClicked(MouseEvent e) {
                    				PrimFrame screen= (PrimFrame)((JComponent) e.getComponent()).getTopLevelAncestor();
                    				playersNames = new String[playerAmount];
                    				for (int i = 0; i < playerAmount; i++) {
                    					if (nameFields.get(i).getText() == null) {
                    						playersNames[i] = "Jogador" + Integer.toString(i);
                    					} else {
                    						playersNames[i] = nameFields.get(i).getText();
                    					}
                    				}
                    				screen.remove(nicknamesPanel);
                    				screen.startGame(Integer.parseInt(playerCountField.getText()), playersNames);
                    			}
                    		});
                    		
                    		nicknamesPanel.add(confirmNamesButton);
                    		nicknamesPanel.add(nameField);
                    		nicknamesPanel.add(nameLabel);
                    	}
                    	
                    	screen.add(nicknamesPanel);
                    	screen.repaint();
                    } else {
                    	if (failLabel == null) {
                    	failLabel = new JLabel("O valor deve ser um inteiro de 3 a 6.", JLabel.CENTER);
                    	failLabel.setFont(new Font("default", Font.BOLD, 15));
    					failLabel.setForeground(Color.WHITE);
    					failLabel.setBounds(PrimFrame.WIDTH_DEFAULT/2 - 200, menu.buttonPanelHeight, 400, 50);
    					menu.add(failLabel);
    					menu.repaint();
                    	}
                    }
				} catch (NumberFormatException exception) {
					if (failLabel == null) {
					failLabel = new JLabel("O valor deve ser um inteiro de 3 a 6.", JLabel.CENTER);
					failLabel.setFont(new Font("default", Font.BOLD, 15));
					failLabel.setForeground(Color.WHITE);
					failLabel.setBounds(PrimFrame.WIDTH_DEFAULT/2 - 200, menu.buttonPanelHeight, 400, 50);
					menu.add(failLabel);
					menu.repaint();
					}
			}}});
		
		loadGameButton = new JButton("Carregar jogo");
		loadGameButton.setBounds(buttonPanelWidth/2 - this.buttonsWidth/2, 8*buttonPanelHeight/16, this.buttonsWidth, this.buttonsHeight);
		loadGameButton.addMouseListener(new MouseAdapter () {
			@Override
			public void mouseClicked(MouseEvent e) {
				Menu menu = (Menu) e.getComponent().getParent().getParent();
				PrimFrame screen = (PrimFrame) menu.getTopLevelAncestor();
				screen.remove(menu);
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Carregar um jogo");
				int userSelection = fileChooser.showOpenDialog(null);
				if (userSelection == JFileChooser.APPROVE_OPTION) {
					File loadFile = fileChooser.getSelectedFile();
					screen.loadGame(loadFile);
				}
			}
		});
		

		buttonPanel.setLayout(null);
		buttonPanel.add(newGameButton);
		buttonPanel.add(loadGameButton);
		this.add(titleLabel);
		this.add(titleBorder);
		this.add(buttonPanel);
	}

	public int getPlayerCount() {

		int playerCount = Integer.parseInt(playerCountField.getText());

		return playerCount;
	}
	
	public void paintComponent(Graphics g) {
		try {
			backgroundImage = ImageIO.read(new File("src/assets/images/wallpaper.jpg"));
			backgroundScaledImage = backgroundImage.getScaledInstance(PrimFrame.WIDTH_DEFAULT, PrimFrame.HEIGHT_DEFAULT, Image.SCALE_SMOOTH);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		g.drawImage(backgroundScaledImage, 0, 0, null);
	}
}
