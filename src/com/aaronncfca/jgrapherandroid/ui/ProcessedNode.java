package com.aaronncfca.jgrapherandroid.ui;

import com.aaronncfca.jgrapherandroid.pieces.Piece;

public class ProcessedNode extends InputNode {

	public ProcessedNode(Piece p) {
		super();
		nodeType = type.ProcessedNode;
		piece = p;
	}
	
	public Piece GetPiece() {
		return piece;
	}
	
	private Piece piece;

}
