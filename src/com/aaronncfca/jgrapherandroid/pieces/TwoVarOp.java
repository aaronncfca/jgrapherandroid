package com.aaronncfca.jgrapherandroid.pieces;

public abstract class TwoVarOp extends Operator {

	public TwoVarOp(Piece A, Piece B) {
		pieceA = A;
		pieceB = B;
	}

	@Override
	public Boolean isTwoVarOp() { return true; }
	
	@Override
	public Piece Simplify() {
		pieceA = pieceA.Simplify();
		pieceB = pieceB.Simplify();
		return this;
	}
	
	@Override
	protected Piece[] getPieces() {
		return new Piece[] { pieceA, pieceB };
	}
	
	protected Piece pieceA;
	protected Piece pieceB;
	public Piece PieceA() {
		return pieceA;
	}
	public Piece PieceB() {
		return pieceB;
	}
}
