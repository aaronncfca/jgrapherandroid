package com.aaronncfca.jgrapherandroid.pieces;

import com.aaronncfca.jgrapherandroid.exceptions.DeriveException;
import com.aaronncfca.jgrapherandroid.function.FB;

public class Arctan extends SingleVarOp {

	public Arctan(Piece innerFunction) {
		super(innerFunction);
		A = new DefinedConstant(1);
	}
	
	public Arctan(Piece innerFunction, DefinedConstant divisor) {
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
		return FB.divide(FB.from(innerPiece.Derive(var))
							.times(A)
							.get(),
				         FB.add(A2,
				        		FB.pow(innerPiece,
				        			   FB.num(2))));
	}

	@Override
	public Boolean isArctan() {
		return true;
	}
	
	private DefinedConstant A;
}
