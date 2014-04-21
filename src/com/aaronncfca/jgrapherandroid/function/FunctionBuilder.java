package com.aaronncfca.jgrapherandroid.function;

import com.aaronncfca.jgrapherandroid.pieces.Add;
import com.aaronncfca.jgrapherandroid.pieces.Arccos;
import com.aaronncfca.jgrapherandroid.pieces.Arcsine;
import com.aaronncfca.jgrapherandroid.pieces.Arctan;
import com.aaronncfca.jgrapherandroid.pieces.Cosine;
import com.aaronncfca.jgrapherandroid.pieces.DefinedConstant;
import com.aaronncfca.jgrapherandroid.pieces.Divide;
import com.aaronncfca.jgrapherandroid.pieces.Log;
import com.aaronncfca.jgrapherandroid.pieces.MultiVarOp;
import com.aaronncfca.jgrapherandroid.pieces.Multiply;
import com.aaronncfca.jgrapherandroid.pieces.NaturalLog;
import com.aaronncfca.jgrapherandroid.pieces.Piece;
import com.aaronncfca.jgrapherandroid.pieces.Power;
import com.aaronncfca.jgrapherandroid.pieces.Root;
import com.aaronncfca.jgrapherandroid.pieces.Sine;
import com.aaronncfca.jgrapherandroid.pieces.Tangent;
import com.aaronncfca.jgrapherandroid.pieces.Variable;

public class FunctionBuilder {

	public FunctionBuilder() {
	}
	public FunctionBuilder(Piece StartWith) {
		innerPiece = StartWith;
	}
	public Piece get() {
		return innerPiece;
	}
	public FunctionBuilder newFrom(Piece p) {
		innerPiece = p;
		return this;
	}
	public static FunctionBuilder from(Piece p) {
		return new FunctionBuilder(p);
	}
	private Piece innerPiece;
	
	
	public FunctionBuilder plus(Piece toAdd) {
		if(innerPiece.isAdd()) {
			((Add)innerPiece).AddPiece(toAdd);
		}
		else {
			Add building = new Add();
			building.AddPiece(innerPiece);
			building.AddPiece(toAdd);
			innerPiece = building;
		}
		return this;
	}
	
	public FunctionBuilder minus(Piece toSubtract) {
		if(innerPiece.isAdd()) {
			((Add)innerPiece).AddPiece(FB.multiply(toSubtract, FB.num(-1)));
		}
		else {
			Add building = new Add();
			building.AddPiece(innerPiece);
			building.AddPiece(FB.multiply(toSubtract, FB.num(-1)));
			innerPiece = building;
		}
		return this;
	}
	
	public FunctionBuilder times(Piece toMultiply) {
		if(innerPiece.isMultiply()) {
			((Multiply)innerPiece).AddPiece(toMultiply);
		}
		else {
			Multiply building = new Multiply();
			building.AddPiece(innerPiece);
			building.AddPiece(toMultiply);
			innerPiece = building;
		}
		return this;
	}
	
	public FunctionBuilder pow(Piece exponent) {
		innerPiece = new Power(innerPiece, exponent);
		return this;
	}
	public FunctionBuilder divideBy(Piece divisor) {
		innerPiece = new Divide(innerPiece, divisor);
		return this;
	}
	
	public static Piece add(Piece PieceA, Piece PieceB) {
		Add toReturn;
		if(PieceA.isAdd()) {
			if(PieceB.isAdd()) { //Both are adds already
				for(Piece p:((MultiVarOp)PieceB).Pieces()) {
					((Add)PieceA).AddPiece(p);
				}
			} else {
				((Add)PieceA).AddPiece(PieceB);
			}
			toReturn = (Add) PieceA;
		}
		else if(PieceB.isAdd()) {
			((Add)PieceB).AddPiece(PieceA);
			toReturn = (Add) PieceB;
		}
		else {
			toReturn = new Add(PieceA, PieceB);
		}
		return toReturn;
	}
	
	//Calls FB.add after multiplying the Subtrahend by -1
	public static Piece subtract(Piece Minuend, Piece Subtrahend) {
		if(Subtrahend.isAdd()) {
			Add toAdd = new Add();
			for(Piece p:((MultiVarOp)Subtrahend).Pieces()) {
				toAdd.AddPiece(FB.multiply(p, FB.num(-1)));
			}
			return FB.add(Minuend, toAdd);
		} else {
			return FB.add(Minuend, FB.multiply(Subtrahend, FB.num(-1)));
		}
	}
	
	public static Piece multiply(Piece PieceA, Piece PieceB) {
		Multiply toReturn;
		if(PieceA.isMultiply()) {
			if(PieceB.isMultiply()) { //Both are Multiplys already
				for(Piece p:((MultiVarOp)PieceB).Pieces()) {
					((Multiply)PieceA).AddPiece(p);
				}
			} else {
				((Multiply)PieceA).AddPiece(PieceB);
			}
			toReturn = (Multiply) PieceA;
		}
		else if(PieceB.isMultiply()) {
			((Multiply)PieceB).AddPiece(PieceA);
			toReturn = (Multiply) PieceB;
		}
		else {
			toReturn = new Multiply(PieceA, PieceB);
		}
		return toReturn;
	}
	
	public static Piece pow(Piece base, Piece exponent) {
		return new Power(base, exponent);
	}
	public static Piece powE(Piece exponent) {
		return FB.pow(FB.num(Math.E), exponent);
	}
	public static Piece divide(Piece dividend, Piece divisor) {
		return new Divide(dividend, divisor);
	}
	
	public static Piece sin(Piece toSine) {
		return new Sine(toSine);
	}
	public static Piece cos(Piece toCosine) {
		return new Cosine(toCosine);
	}
	public static Piece tan(Piece toTan) {
		return new Tangent(toTan);
	}
	public static Piece asin(Piece toArcsine) {
		return new Arcsine(toArcsine);
	}
	public static Piece acos(Piece toArccos) {
		return new Arccos(toArccos);
	}
	public static Piece atan(Piece toArctan) {
		return new Arctan(toArctan);
	}
	public static Piece ln(Piece toLog) {
		return new NaturalLog(toLog);
	}
	public static Piece log(Piece base, Piece toLog) {
		return new Log(base, toLog);
	}
	public static Piece sqrt(Piece toSquare) {
		return new Root(toSquare);
	}
	public static Piece num(double num) {
		return new DefinedConstant(num);
	}
	public static Piece num(int num) {
		return new DefinedConstant((double)num);
	}
	public static Piece var(String name) {
		return new Variable(name);
	}
	public static Piece x() {
		
		return new Variable("x");
	}
}
