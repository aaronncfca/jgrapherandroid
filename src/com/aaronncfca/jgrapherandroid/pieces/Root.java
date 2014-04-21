package com.aaronncfca.jgrapherandroid.pieces;

import com.aaronncfca.jgrapherandroid.function.FB;

public class Root extends Power {

	public Root(Piece base, Piece exponent) {
		super(base, FB.divide(FB.num(1), exponent));
	}
	
	public Root(Piece base, double exponent) {
		super(base, FB.num(1/exponent));
	}
	
	public Root(Piece base) {
		super(base, FB.num(1/2));
	}

}
