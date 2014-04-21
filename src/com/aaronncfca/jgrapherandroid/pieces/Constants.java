package com.aaronncfca.jgrapherandroid.pieces;

public enum Constants {
	E (Math.E),
	PI(Math.PI);
	
	private Constants(double d) {
		this.value = d;
	}
	private double value;
	public double Value() {
		return value;
	}
}
