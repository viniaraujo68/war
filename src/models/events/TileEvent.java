package models.events;

import enums.PlayerColor;

public class TileEvent {
	public PlayerColor color;
	public int troops;
	public String name;
	public boolean selected = false;
	public boolean lastSelected = false;
	
	public TileEvent(PlayerColor color, int troops, String name, boolean selected, boolean lastSelected) {
		this.color = color;
		this.troops = troops;
		this.name = name;
		this.selected = selected;
		this.lastSelected = lastSelected;
	}
}
