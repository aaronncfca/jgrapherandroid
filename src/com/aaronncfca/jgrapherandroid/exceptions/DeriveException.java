package com.aaronncfca.jgrapherandroid.exceptions;

public class DeriveException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DeriveException() {
	}

	public DeriveException(String message) {
		super(message);
	}

	public DeriveException(Throwable cause) {
		super(cause);
	}

	public DeriveException(String message, Throwable cause) {
		super(message, cause);
	}

	public DeriveException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
