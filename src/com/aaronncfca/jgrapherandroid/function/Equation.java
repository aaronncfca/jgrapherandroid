package com.aaronncfca.jgrapherandroid.function;

import java.util.List;

import com.aaronncfca.jgrapherandroid.pieces.Add;
import com.aaronncfca.jgrapherandroid.pieces.MultiVarOp;
import com.aaronncfca.jgrapherandroid.pieces.Multiply;
import com.aaronncfca.jgrapherandroid.pieces.Piece;
import com.aaronncfca.jgrapherandroid.pieces.SingleVarOp;
import com.aaronncfca.jgrapherandroid.pieces.TwoVarOp;

public class Equation {
	protected enum InequalityType	{
		Equals,
		GreaterThan,
		LessThan,
		GreaterOrEqual,
		LessOrEqual;
	}
	
	public Equation() {
		this(FB.num(0), FB.num(0), InequalityType.Equals);
	}
	
	public Equation(Piece p1, Piece p2, InequalityType it) {
		piece1 = p1;
		piece2 = p2;
		IT = it;
	}
	
	public void Simplify() {
		simplify();
	}
	
	private void simplify() {
	}
	
	public int SolveFor(Piece toFind) {
		if (!piece1.Contains(toFind) && !piece2.Contains(toFind)) {
			return -1;
		}
		else if(piece1.Contains(toFind) && !piece2.Contains(toFind)) {
			while(!piece1.equals(toFind) && piece1.Contains(toFind)) {
				if(!unwrap(piece1, piece2, toFind, true)) {
					break; //TODO: try something else.
				}
			}
		}
		return -1;
	}
	
	public void move(Piece from, Piece to) {
		//TODO: do this after adding arcsin, arccos etc.
		//and after creating SingleVarOp.Inverse() (Maybe)
	}
	

	public boolean unwrap(Piece toUnwrap, Piece toWrap, Piece solveFor, boolean toPiece1) {
		Piece wrapped = null;
		Piece unwrapped = null;
		if(toUnwrap.isSingleVarOp()) {
			SingleVarOp svo = (SingleVarOp) toUnwrap;
			unwrapped = svo.innerPiece;
			if(svo.isSine()) {
				wrapped = FB.asin(toWrap);
			}
			else if(svo.isCosine()) {
				wrapped = FB.acos(toWrap);
			}
			else if(svo.isTangent()) {
				wrapped = FB.atan(toWrap);
			}
			else if(svo.isArcsine()) {
				wrapped = FB.sin(toWrap);
			}
			else if(svo.isArccos()) {
				wrapped = FB.cos(toWrap);
			}
			else if(svo.isArctan()) {
				wrapped = FB.tan(toWrap);
			}
			else if(svo.isNaturalLog()) {
				wrapped = FB.powE(toWrap); 
			}
			else {
				//Correct our assumption in setting unwrapped
				//to toReverse.innerPiece
				unwrapped = null; 
			}
		}
		else if(toUnwrap.isTwoVarOp()) {
			TwoVarOp tvo = (TwoVarOp) toUnwrap;
			if(tvo.isPower()) {
				if(tvo.PieceB().Contains(solveFor)) {
					wrapped = FB.log(toWrap, tvo.PieceA());
					unwrapped = tvo.PieceA();
				}
				if(tvo.PieceA().Contains(solveFor)) {
					wrapped = FB.log(tvo.PieceA(), toWrap);
					unwrapped = tvo.PieceB();
				}
			}
			else if(tvo.isLog()) {
				if(tvo.PieceB().Contains(solveFor)) {
					wrapped = FB.pow(toWrap, tvo.PieceB());
					unwrapped = tvo.PieceA();
				}
				if(tvo.PieceA().Contains(solveFor)) {
					wrapped = FB.pow(tvo.PieceA(), toWrap);
					unwrapped = tvo.PieceB();
				}
			}
			else if(tvo.isDivide()) {
				if(tvo.PieceB().Contains(solveFor)) {
					wrapped = FB.multiply(toWrap, tvo.PieceB());
					unwrapped = tvo.PieceA();
				}
				if(tvo.PieceA().Contains(solveFor)) {
					wrapped = FB.multiply(tvo.PieceA(), toWrap);
					unwrapped = tvo.PieceB();
				}
			}
		}
		else if(toUnwrap.isMultiVarOp()) {
			MultiVarOp mvo = (MultiVarOp) toUnwrap;
			List<Piece> piecesWith = mvo.Pieces();
			List<Piece> piecesWithout = mvo.Pieces();
			for(int i = 0; i < piecesWith.size(); i++) {
				if(!piecesWith.get(i).Contains(solveFor)) {
					piecesWithout.add(piecesWith.get(i));
					piecesWith.remove(i);
					i--;
				}
			}
			if(piecesWithout.size() == 0) { return false; } //Can't simplify
			
			if (mvo.isMultiply()) {
				if(piecesWithout.size() == 1) {
					wrapped = FB.divide(toWrap, piecesWithout.get(0));
				} else {
					wrapped = FB.divide(toWrap, new Multiply(piecesWithout));
				}
				if (piecesWithout.size() > 0) {
					FunctionBuilder fb = FB.from(piecesWith.get(0));
					for (int i = 1; i < piecesWithout.size(); i++) {
						fb.times(piecesWith.get(0));
					}
					unwrapped = fb.get();
				}
			} else if (mvo.isAdd()) {
				if(piecesWithout.size() == 1) {
					wrapped = FB.subtract(toWrap, piecesWithout.get(0));
				} else {
					wrapped = FB.subtract(toWrap, new Add(piecesWithout));
				}
				if (piecesWithout.size() > 0) {
					FunctionBuilder fb = FB.from(piecesWith.get(0));
					for (int i = 1; i < piecesWithout.size(); i++) {
						fb.plus(piecesWith.get(0));
					}
					unwrapped = fb.get();
				} else {
					
				}
			}
			
		}
		if(unwrapped == null || wrapped == null) {
			return false;
		}
		if(toPiece1) {
			piece1 = unwrapped;
			piece2 = wrapped;
		} else {
			piece2 = unwrapped;
			piece1 = wrapped;
		}
		return true;
	}
	
	private Piece piece1;
	private Piece piece2;
	
	public InequalityType IT;

}
