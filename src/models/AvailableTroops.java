package models;

import java.util.Hashtable;
import java.util.Map;

public class AvailableTroops {
    public int normal;
    public Hashtable<String, Integer> continentTroops = new Hashtable<String, Integer>();
    
    public boolean hasContinentTroops() {
    	for (Map.Entry<String, Integer> entry : continentTroops.entrySet()) {
    		int troops = entry.getValue();
    		if (troops > 0) {
    			return true;
    		}
    	}
    	return false;
    }
    
    public String toString() {
    	String result = "";
        String continentDetails = String.join("/", continentTroops.entrySet().stream().map(entry -> entry.getKey() +":"+ entry.getValue()).toArray(String[]::new));
        result += String.join("-", Integer.toString(normal), continentDetails);
        return result;
    }
}
