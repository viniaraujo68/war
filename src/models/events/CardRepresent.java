package models.events;

import enums.Shape;

public class CardRepresent {
	public String tile;
	public Shape shape;
	public CardRepresent(String tileName, Shape shape) {
		this.tile = tileName;
		this.shape = shape;
	}
}
