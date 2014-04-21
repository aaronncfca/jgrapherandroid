package com.aaronncfca.jgrapherandroid.pieces;

public abstract class SingleVarOp extends Operator {
	public SingleVarOp(Piece innerFunction ) {
		innerPiece = innerFunction;
	}

	@Override
	public Boolean isSingleVarOp() { return true; }
	
	@Override
	public Piece Simplify() {
		innerPiece = innerPiece.Simplify();
		if(innerPiece.isDefinedConstant()) {
			return new DefinedConstant(this.Calculate());
		}
		else return this;
	}
	
	@Override
	protected Piece[] getPieces() {
		return new Piece[] { innerPiece };
	}
	
	public Piece innerPiece;
}