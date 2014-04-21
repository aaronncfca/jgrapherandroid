package com.aaronncfca.jgrapherandroid.pieces;

import com.aaronncfca.jgrapherandroid.exceptions.DeriveException;
import com.aaronncfca.jgrapherandroid.function.FB;

public class Arcsine extends SingleVarOp {

	public Arcsine(Piece innerFunction) {
		super(innerFunction);
		A = new DefinedConstant(1);
	}
	
	public Arcsine(Piece innerFunction, DefinedConstant divisor) {
		super(innerFunction);
		A = divisor;
	}

	@Override
	public double Calculate() {
		return Math.asin(innerPiece.Calculate() / A.Calculate());
	}

	@Override
	public Piece Derive(String var) throws DeriveException {
		Piece A2 = FB.num(A.Calculate() * A.Calculate()); //Squared
		return FB.divide(innerPiece.Derive(var),
				         FB.sqrt(FB.add(A2,
				        		 		FB.pow(innerPiece,
				        		 			   FB.num(2)))));
	}

	@Override
	public Boolean isArcsine() {
		return true;
	}
	
	private DefinedConstant A;
}
