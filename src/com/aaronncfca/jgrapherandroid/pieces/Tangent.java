package com.aaronncfca.jgrapherandroid.pieces;

import com.aaronncfca.jgrapherandroid.exceptions.DeriveException;
import com.aaronncfca.jgrapherandroid.function.FB;

public class Tangent extends SingleVarOp {

	public Tangent(Piece InnerPiece) {
		super(InnerPiece);
	}

	@Override
	public double Calculate() {
		return Math.sin(innerPiece.Calculate()) / Math.cos(innerPiece.Calculate());
	}

	
	@Override
	public Piece Derive(String var) throws DeriveException {
		return FB.divide(innerPiece.Derive(var),
				FB.pow(FB.cos(innerPiece), FB.num(-1)));
	}
	
	@Override
	public String toString() {
		return ("tan(" + innerPiece.toString() + ")");
	}
	
	@Override
	public Boolean isTangent() { return true; }

}
