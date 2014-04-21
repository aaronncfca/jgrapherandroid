package com.aaronncfca.jgrapherandroid.exceptions;

public class UndefinedVariableException extends RuntimeException {
	public UndefinedVariableException(String string) {
		varName = string;
	}
	public String VariableName() {
		return varName;
	}
	private String varName;

	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
}
