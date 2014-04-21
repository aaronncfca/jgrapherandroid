package com.aaronncfca.jgrapherandroid.pieces;

public abstract class Constant extends Piece {

	@Override
	public Boolean isConstant() { return true; }
	@Override
	public Boolean isConstant(String var) { return true; }
	
	@Override
	protected Piece[] getPieces() {
		return new Piece[] { };
	}

}