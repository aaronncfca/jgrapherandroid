package com.aaronncfca.jgrapherandroid.pieces;

public class DefinedConstant extends Constant {
	public DefinedConstant(double value) {
		_value = value; 
	}

	@Override
	public double Calculate() {
		return _value;
	}

	@Override
	public Piece Derive(String var) {
		return new DefinedConstant(0);
	}

	@Override
	public Piece Simplify() {
		return this;
	}
	
	@Override
	public String toString() {
		return Double.toString(_value);
	}

	@Override
	public Boolean isDefinedConstant() { return true; }
	
	private double _value;

}
