package com.aaronncfca.jgrapherandroid.exceptions;

public class CalculateException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CalculateException() {
	}

	public CalculateException(String message) {
		super(message);
	}

	public CalculateException(Throwable cause) {
		super(cause);
	}

	public CalculateException(String message, Throwable cause) {
		super(message, cause);
	}

	public CalculateException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
