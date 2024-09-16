package models.events;

public class RollEvent {
	public Integer[] atkDices;
	public Integer[] defDices;
	
	public RollEvent(Integer[] atkDices, Integer[] defDices) {
		this.atkDices = atkDices;
		this.defDices = defDices;
	}
}
