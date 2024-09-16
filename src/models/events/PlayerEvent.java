package models.events;

import enums.PlayerColor;

public class PlayerEvent {
	public String name;
	public PlayerColor color;
	public String goalDescription;
	public CardRepresent[] cards;
	public boolean victory = false;

	public PlayerEvent(String name, PlayerColor color, String goal, CardRepresent[] cards, boolean victory) {
		this.name = name;
		this.color = color;
		this.goalDescription = goal;
		this.cards = cards;
		this.victory = victory;
	}
}
