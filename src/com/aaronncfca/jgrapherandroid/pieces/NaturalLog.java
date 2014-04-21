package com.aaronncfca.jgrapherandroid.pieces;

import com.aaronncfca.jgrapherandroid.exceptions.DeriveException;
import com.aaronncfca.jgrapherandroid.function.FB;

public class NaturalLog extends SingleVarOp {

	public NaturalLog(Piece innerFunction) {
		super(innerFunction);
	}

	@Override
	public double Calculate() {
		return Math.log(innerPiece.Calculate());
	}

	@Override
	public Piece Derive(String var) throws DeriveException {
		return FB.divide(innerPiece.Derive(var), innerPiece);
	}
	
	@Override
	public String toString() {
		return ("ln(" + innerPiece.toString() + ")"); 
	}
	
	@Override
	public Boolean isNaturalLog() {
		return true;
	}

}
