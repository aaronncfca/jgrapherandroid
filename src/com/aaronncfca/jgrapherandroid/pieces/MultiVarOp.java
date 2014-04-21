package com.aaronncfca.jgrapherandroid.pieces;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public abstract class MultiVarOp extends Operator {

	public MultiVarOp() {
		pieces = new ArrayList<Piece>();
	}
	
	public MultiVarOp(Piece... Pieces) {
		pieces = new ArrayList<Piece>();
		for(Piece p : Pieces){
			pieces.add(p);
		}
	}

	public MultiVarOp(List<Piece> Pieces) {
		pieces = Pieces;
	}
	
	@Override
	public Piece Simplify() {
		ListIterator<Piece> i = pieces.listIterator();
		for(Piece p; i.hasNext();) {
			p = i.next();
			i.set(p.Simplify());
		}
		return this;
	}

	@Override
	protected Piece[] getPieces() {
		return pieces.toArray(new Piece[pieces.size()]);
	}

	public List<Piece> Pieces() {
		return pieces;
	}
	
	@Override
	public Boolean isMultiVarOp() { return true; }
	
	protected List<Piece> pieces;
	
	public void AddPiece(Piece piece) {
		pieces.add(piece);
	}

}
