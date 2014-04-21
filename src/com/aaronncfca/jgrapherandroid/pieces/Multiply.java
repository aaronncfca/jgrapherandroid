package com.aaronncfca.jgrapherandroid.pieces;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import com.aaronncfca.jgrapherandroid.exceptions.DeriveException;

public class Multiply extends MultiVarOp {

	public Multiply() {
		super();
	}

	public Multiply(Piece... pieces) {
		super(pieces);
	}
	
	public Multiply(List<Piece> pieces) {
		super(pieces);
	}

	@Override
	public double Calculate() {
		double toReturn = 1;
		for(Piece p : pieces) {
			toReturn *= p.Calculate();
		}
		return toReturn;
	}
	
	//d(a_0 * a_2 * ... * a_n) = d(a_0) * a_2 * ... * a_n + a_0 * d(a_2) * ...
	@Override
	public Piece Derive(String var) throws DeriveException {
		Add toReturn = new Add();
		for(int i1 = 0; i1 < pieces.size(); i1++) {
			Multiply toAdd = new Multiply();
			toAdd.AddPiece(pieces.get(i1).Derive(var));
			for(int i2 = 0; i2 < pieces.size(); i2++) {
				if(i1 != i2) {
					toAdd.AddPiece(pieces.get(i2));
				}
			}
			toReturn.AddPiece(toAdd);
		}
		return (Piece) toReturn;
	}
	
	@Override
	public Piece Simplify() {
		super.Simplify();
		List<Piece> constants = new ArrayList<Piece>();
		ListIterator<Piece> i = pieces.listIterator();
		for(Piece p; i.hasNext();) {
			p = i.next();
			if(p.isDefinedConstant()) {
				if (p.Calculate() == 0) {
					return new DefinedConstant(0); // 0 * x = 0
				} else if (p.Calculate() == 1) {
					//Doesn't effect multiplication
					i.remove();
				} else {
					constants.add(p);
				}
			} else if (p.isMultiply()) {
				//Merge Multiply's
				for(Piece subPiece : p.getPieces()) {
					i.add(subPiece);
				}
				i.remove();
			}
		}
		//Consolidate defined constants
		if(constants.size() > 1) {
			double total = 1;
			i = constants.listIterator();
			for(Piece p; i.hasNext();) {
				p = i.next();
				total *= p.Calculate();
				pieces.remove(p);
			}
			pieces.add(new DefinedConstant(total));
		}
		switch (pieces.size()) {
		case 0:
			//All sub-pieces must have been removed by above code
			return new DefinedConstant(0);
		case 1:
			return pieces.get(0);
		default:
			return this;
		}
	}
	
	@Override
	public String toString() {
		String toReturn = "";
		for(Piece p : pieces) {
			toReturn += p.toString() + " * ";
		}
		//Return without the last instance of " * "
		return toReturn.substring(0, toReturn.length() - 3);
	}
	

	@Override
	public Boolean isMultiply() { return true; }
	
}
