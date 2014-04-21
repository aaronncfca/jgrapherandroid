package com.aaronncfca.jgrapherandroid.pieces;

import com.aaronncfca.jgrapherandroid.exceptions.DeriveException;
import com.aaronncfca.jgrapherandroid.function.FB;

public class Power extends TwoVarOp {

	public Power(Piece base, Piece exponent) {
		super(base, exponent);
		// TODO Auto-generated constructor stub
	}

	@Override
	public double Calculate() {
		return Math.pow(pieceA.Calculate(), pieceB.Calculate());
	}

	@Override
	public Piece Derive(String var) throws DeriveException{
		if(pieceB.isConstant(var)) {
			Multiply toReturn = new Multiply();
			toReturn.AddPiece(pieceB);
			toReturn.AddPiece(pieceA.Derive(var));
			
			if(pieceB.isDefinedConstant()) {
				Power toAdd = new Power(
						pieceA,
						new DefinedConstant(pieceB.Calculate() - 1));
				toReturn.AddPiece(toAdd);
			}
			else {
				Power toAdd = new Power(
						pieceA,
						new Add(new Piece[]{
								pieceB,
								new DefinedConstant(-1)
						}));
				toReturn.AddPiece(toAdd);
			}
			return toReturn;
		} else if(pieceA.isDefinedConstant()) {
			return FB.from(FB.num(Math.log(pieceA.Calculate())))
						.times(pieceB.Derive(var))
						.times(this)
						.get();
		}
		else {
			return FB.add(
					FB.from(this)
						.times(pieceB.Derive(var))
						.times(FB.ln(pieceA))
						.get(),
					FB.from(FB.pow(pieceA,
								   FB.add(pieceB, FB.num(-1))))
						.times(pieceB)
						.times(pieceB.Derive(var))
						.get());
		}
	}
	
	@Override
	public Piece Simplify() {
		super.Simplify();
		if(pieceB.isDefinedConstant()) {
			if(pieceB.Calculate() == 0) {
				return new DefinedConstant(1);
			} else if(pieceB.Calculate() == 1) {
				return pieceA;
			}
		}
		return this;
	}
	
	@Override
	public String toString() {
		return (pieceA.isConstant() ? pieceA.toString() : "(" + pieceA.toString() + ")")
				+ " ^ "
				+ (pieceB.isConstant()  ? pieceB.toString() : "(" + pieceB.toString() + ")");
	}
	
	@Override
	public Boolean isPower() { return true; }
}
