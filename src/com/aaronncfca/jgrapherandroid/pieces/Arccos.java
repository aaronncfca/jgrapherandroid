package com.aaronncfca.jgrapherandroid.pieces;

import com.aaronncfca.jgrapherandroid.exceptions.DeriveException;
import com.aaronncfca.jgrapherandroid.function.FB;

public class Arccos extends SingleVarOp {

	public Arccos(Piece innerFunction) {
		super(innerFunction);
		A = new DefinedConstant(1);
	}
	
	public Arccos(Piece innerFunction, DefinedConstant divisor) {
		super(innerFunction);
		A = divisor;
	}

	@Override
	public double Calculate() {
		return Math.acos(innerPiece.Calculate() / A.Calculate());
	}

	@Override
	public Piece Derive(String var) throws DeriveException {
		Piece A2 = FB.num(A.Calculate() * A.Calculate()); //Squared
		return FB.divide(FB.from(innerPiece.Derive(var))
							.times(FB.num(-1))
							.times(A)
							.get(),
					     FB.sqrt(FB.add(A2,
				        		 FB.pow(innerPiece,
				        		 			   FB.num(2)))));
	}

	@Override
	public Boolean isArccos() {
		return true;
	}
	
	private DefinedConstant A;
}
