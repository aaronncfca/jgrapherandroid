package com.aaronncfca.jgrapherandroid.pieces;

public class SpecificConstant extends DefinedConstant {

	public SpecificConstant(Constants c) {
		super(c.Value());
	}
	
	public Boolean IsE() {
		return (constant == Constants.E);
	}
	
	public Boolean IsPI() {
		return (constant == Constants.PI);
	}
	
	private Constants constant;
}
