package models.events;

import java.util.Hashtable;

public class PhaseInfo {
	public int normal;
	public Hashtable<String, Integer> continentTroops;
	
	public PhaseInfo(int normal, Hashtable<String, Integer> continent) {
		this.normal = normal;
		this.continentTroops = continent;
	}
}
