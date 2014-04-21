package com.aaronncfca.jgrapherandroid.pieces;

import com.aaronncfca.jgrapherandroid.exceptions.DeriveException;
import com.aaronncfca.jgrapherandroid.function.FB;

public class Cosine extends SingleVarOp {

	public Cosine(Piece innerFunction) {
		super(innerFunction);
	}

	@Override
	public double Calculate() {
		return Math.cos(innerPiece.Calculate());
	}

	@Override
	public Piece Derive(String var) throws DeriveException {
		return FB.from(FB.sin(innerPiece))
				.times(innerPiece.Derive(var))
				.times(FB.num(-1))
				.get();
	}
	
	@Override
	public String toString() {
		return ("cos(" + innerPiece.toString() + ")");
	}
	
	@Override
	public Boolean isCosine() { return true; }

}
