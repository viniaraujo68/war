package models;

import enums.Shape;

class Card {
    String tileName;
    Shape shape;

    Card(String tileName, Shape shape) {
        this.tileName = tileName;
        this.shape = shape;
    }
    
    @Override
    public String toString() {
    	String result = String.join(":", this.tileName, this.shape.toString());
    	return result;
    }
}