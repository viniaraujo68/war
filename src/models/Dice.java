package models;

import java.util.Random;

public class Dice {
	
	public DicePerformer performer = DicePerformer.getPerformer();
	
	public Dice() {}
	
	public static Integer[] roll(int amount) {
		Integer[] results = new Integer[amount];
		Random random = new Random();
		for (int i = 0; i < amount; i++) {
			results[i] = random.nextInt(6)+1;
		}
		return results;
	}
	

}
