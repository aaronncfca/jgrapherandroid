package com.aaronncfca.jgrapherandroid.pieces;

import java.util.*;

import com.aaronncfca.jgrapherandroid.exceptions.DeriveException;
import com.aaronncfca.jgrapherandroid.function.FB;
import com.aaronncfca.jgrapherandroid.function.FunctionBuilder;

public class Add extends MultiVarOp {

	public Add() {
		super();
	}
	
	public Add(Piece... pieces) {
		super(pieces);
	}

	public Add(List<Piece> pieces) {
		super(pieces);
	}

	@Override
	public double Calculate() {
		double toReturn = 0;
		for(Piece p : pieces) {
			toReturn += p.Calculate();
		}
		return toReturn;
	}

	//Returns an Add Piece with all components added
	@Override
	public Piece Derive(String var) throws DeriveException {
		
		FunctionBuilder toReturn = FB.from(new Add());
		for(Piece p : pieces) {
			toReturn.plus(p.Derive(var));
		}
		return toReturn.get();
	}
	
	@Override
	public Piece Simplify() {
		super.Simplify();
		List<Piece> constants = new ArrayList<Piece>();
		ListIterator<Piece> i = pieces.listIterator();
		for(Piece p; i.hasNext();) {
			p = i.next();
			if(p.isDefinedConstant()) {
				//Remove addends equal to 0
				if(p.Calculate() == 0) {
					i.remove();
				} else {
					constants.add(p);
				}
			} else if (p.isAdd()) {
				i.remove(); //must be called before i.add
				//Merge Add's
				for(Piece subPiece : p.getPieces()) {
					i.add(subPiece);
				}
			}
		}
		//Consolidate defined constants
		if(constants.size() > 1) {
			double total = 0;
			i = constants.listIterator();
			for(Piece p; i.hasNext();) {
				p = i.next();
				total += p.Calculate();
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
		String toReturn = "(";
		for(int i = 0; i < pieces.size(); i++) {
			if(i != 0) {
				toReturn += " + ";
			}
			toReturn += pieces.get(i).toString();
			//TODO: fix for brackets
		}
		return toReturn + ")";
	}
	
	@Override
	public Boolean isAdd() { return true; }
	
	
}
