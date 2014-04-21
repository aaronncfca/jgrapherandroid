package com.aaronncfca.jgrapherandroid.pieces;

import com.aaronncfca.jgrapherandroid.exceptions.DeriveException;
import com.aaronncfca.jgrapherandroid.function.FB;

public class Log extends TwoVarOp {

	public Log(Piece base, Piece toLog) {
		super(base, toLog);
	}

	@Override
	public double Calculate() {
		return Math.log(pieceB.Calculate()) / Math.log(pieceB.Calculate());
	}

	@Override
	public Piece Derive(String var) throws DeriveException {
		return FB.divide(FB.ln(pieceB), FB.ln(pieceA)).Derive(var);
	}

	@Override
	public Boolean isLog() {
		return true;
	}
}
