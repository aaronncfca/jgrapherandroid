package com.aaronncfca.jgrapherandroid.pieces;

import java.util.HashMap;

import com.aaronncfca.jgrapherandroid.exceptions.UndefinedVariableException;

public class Variable extends Piece {
	public Variable(String name) throws UndefinedVariableException {
		this.name = name;
	}

	@Override
	protected Piece[] getPieces() {
		return new Piece[] { };
	}
	
	@Override
	public double Calculate() {
		if(!values.containsKey(name)) {
			throw new UndefinedVariableException(name);
		}
		double v = values.get(name);
		return v;
	}

	@Override
	public Piece Derive(String var) {
		if (name.equals(var)) {
			return new DefinedConstant(1);
		} else {
			//Treat it as a constant
			return new DefinedConstant(0);
		}
	}
	
	@Override
	public Piece Simplify() {
		return this;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	/**
	 * Returns true only if var is not the string representation
	 * of this variable. 
	 */
	@Override
	public Boolean isConstant(String var) {
		if(var.equals(name)) {
			return false;
		} else return true;
	}
	
	public boolean equals(Object o) {
		if(o == null || this.getClass() != o.getClass()) {
			return false;
		}
		Variable var = (Variable) o;
		if(var.name.equals(this.name)) return true;
		else return false;
	}
	
	private String name;
	
	public static void setVariable(String var, double value) {
		values.put(var, value);
	}
	public static double getVariable(String var) {
		return values.get(var);
	}
	/**
	 * Removes var from the list of defined constants and variables
	 * @return Returns true if var was a member of the list; false
	 * otherwise
	 */
	public static boolean removeVariable(String var) {
		if(values.containsKey(var)) {
			values.remove(var);
			return true;
		}
		else return false;
	}
	
	private static HashMap<String, Double> values = new HashMap<String, Double>();
}
