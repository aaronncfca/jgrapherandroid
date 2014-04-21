package com.aaronncfca.jgrapherandroid.pieces;

import com.aaronncfca.jgrapherandroid.exceptions.DeriveException;
import com.aaronncfca.jgrapherandroid.function.FB;

public class Sine extends SingleVarOp {

	public Sine(Piece innerFunction) {
		super(innerFunction);
	}

	public double Calculate() {
		return Math.sin(innerPiece.Calculate());
	}

	@Override
	public Piece Derive(String var) throws DeriveException {
		return FB.multiply(FB.cos(innerPiece), innerPiece.Derive(var));
	}
	
	@Override
	public String toString() {
		return ("sin(" + innerPiece.toString() + ")");
	}
	
	@Override
	public Boolean isSine() { return true; }

}
