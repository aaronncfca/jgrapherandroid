package com.aaronncfca.jgrapherandroid.pieces;

import java.util.ArrayList;
import java.util.List;

import com.aaronncfca.jgrapherandroid.exceptions.DeriveException;

public abstract class Piece {
	public abstract double Calculate();
	public abstract Piece Derive(String var) throws DeriveException;
	protected abstract Piece[] getPieces();
	//Simplifies contents and returns itself as a Piece
	public abstract Piece Simplify();

	public Boolean isConstant() { return false; }
	/**
	 * Returns true if this piece is a Constant or a Variable with
	 * a String representation that does not match var
	 */
	public Boolean isConstant(String var) { return true; }
	public Boolean isOperator() { return false; }
	public Boolean isSingleVarOp() { return false; }
	public Boolean isMultiVarOp() { return false; }
	public Boolean isTwoVarOp() { return false; }
	public Boolean isAdd() { return false; }
	public Boolean isMultiply() { return false; }
	public Boolean isSine() { return false; }
	public Boolean isCosine() { return false; }
	public Boolean isTangent() { return false; }
	public Boolean isArcsine() { return false; }
	public Boolean isArccos() { return false; }
	public Boolean isArctan() { return false; }
	public Boolean isDivide() { return false; }
	public Boolean isPower() { return false; }
	public Boolean isNaturalLog() { return false; }
	public Boolean isLog() { return false; }
	public Boolean isDefinedConstant() { return false; }
	
	public boolean Contains(Piece toFind) {
		Piece[] pieces = getPieces();
		List<Piece> que = new ArrayList<Piece>();
		
		while (pieces.length > 0) {
			for(int i = 0; i < pieces.length; i++) {
				
				//DO STUFF HERE to pieces[i]
				if(toFind.equals(pieces[i])) {
					return true;
				}
				
				if(pieces[i].getPieces().length > 0) {
					//Restart loop on this piece; add other pieces
					//to the que
					for(; i < pieces.length; i++) {
						que.add(pieces[i]);
					}
					pieces = pieces[i].getPieces();
					i = 0;
				}
			}
			pieces = que.toArray(new Piece[que.size()]);
			que.clear();
		}
		
		return false;
	}

}
