package com.aaronncfca.jgrapherandroid.pieces;

import com.aaronncfca.jgrapherandroid.exceptions.DeriveException;
import com.aaronncfca.jgrapherandroid.function.FB;

public class Divide extends TwoVarOp {

	public Divide(Piece A, Piece B) {
		super(A, B);
		// TODO Auto-generated constructor stub
	}

	@Override
	public double Calculate() {
		return pieceA.Calculate() / pieceB.Calculate();
	}

	@Override
	public Piece Derive(String var) throws DeriveException {
		return FB.from(
				FB.add(FB.multiply(pieceA, pieceB.Derive(var)),
					FB.from(FB.num(-1))
					   	.times(pieceA.Derive(var))
					   	.times(pieceB)
					   	.get()))
				.divideBy(FB.pow(pieceB, FB.num(2)))
				.get();
	}

	@Override
	public Piece Simplify() {
		if(pieceB.isDefinedConstant()) {
			if(pieceB.Calculate() == 0) {
				//Do nothing to a division by zero
				return this;
			} else if (pieceB.Calculate() == 1) {
				return pieceA; // x / 1 = x
			} else if (pieceA.isDefinedConstant()) {
				return new DefinedConstant(this.Calculate());
			}
		}
		else if (pieceA.isDefinedConstant() && pieceA.Calculate() == 0) {
			return new DefinedConstant(0); // 0 / x = 0
			//TODO: update domain
		}
		return this;
	}
	
	@Override
	public String toString() {
		return ("(" + pieceA.toString() + ")/(" + pieceB.toString() + ")");
	}
	
	@Override
	public Boolean isDivide() { return true; }

}
