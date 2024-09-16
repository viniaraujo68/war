package views;

import java.awt.Color;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;

import enums.GamePhase;
import enums.PlayerColor;
import enums.Shape;

public class Utils {
	
	
	public static Map<TextAttribute, Object> textAttributes = new HashMap<>();
	
	public static Color getPlayerColor(PlayerColor color) {
		
		Color cor;
		
		switch(color) {
			case Yellow:
				cor = new Color(250, 194, 27);
				break;
			case Black:
				cor = Color.BLACK;
				break;
			case Red:
				cor = new Color(196, 33, 27);
				break;
			case Blue:
				cor = new Color(71, 213, 252);
				break;
			case Green:
				cor = new Color(40, 166, 40);
				break;
			case White:
				cor = Color.WHITE;
				break;
			default:
				cor = new Color(207, 39, 109);
				break;
		}
		return cor;
	}
	
	public static String getColorString(PlayerColor color) {
		String colorString;
		switch(color) {
		case Yellow:
			colorString = "Amarelo";
			break;
		case Black:
			colorString = "Preto";
			break;
		case Red:
			colorString = "Vermelho";
			break;
		case Blue:
			colorString = "Azul";
			break;
		case Green:
			colorString = "Verde";
			break;
		case White:
			colorString = "Branco";
			break;
		default:
			colorString = "NULL";
			break;
		}
		return colorString;
	}
	
	public static PlayerColor getPlayerColorFromString(String str) {
		switch (str) {
			case "Yellow":
				return PlayerColor.Yellow;
			case "Black":
				return PlayerColor.Black;
			case "Red":
				return PlayerColor.Red;
			case "Blue":
				return PlayerColor.Blue;
			case "Green":
				return PlayerColor.Green;
			case "White":
				return PlayerColor.White;
		}
		return null;
	}
	
	public static Shape getShapeFromString(String str) {
		switch (str) {
			case "Triangle":
				return Shape.Triangle;
			case "Square":
				return Shape.Square;
			case "Circle":
				return Shape.Circle;
			case "Joker":
				return Shape.Joker;
		}
		return null;
	}
	
	public static GamePhase getPhaseFromString(String str) {
		switch (str) {
			case "Place":
				return GamePhase.Place;
			case "Attack":
				return GamePhase.Attack;
			case "Move":
				return GamePhase.Move;
		}
		return null;
	}
	
	public static String getPhaseString(GamePhase phase) {
		switch (phase) {
			case Place:
				return "Posicionar tropas";
			case Attack:
				return "Atacar";
			case Move:
				return "Deslocar tropas";
			default:
				return "";
		}
	}
	
	
	

}
