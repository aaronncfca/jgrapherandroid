package com.aaronncfca.jgrapherandroid.ui;

import java.util.List;

import com.aaronncfca.jgrapherandroid.pieces.Piece;

public class InputNode {

	public enum type {
		Symbol,
		Function,
		Number,
		Variable,
		OpenParenth,
		CloseParenth,
		Comma,
		Unknown,
		ProcessedNode
	}
	protected InputNode() {
		nodeType = type.Unknown;
		info = null;
	}
	public InputNode(type t, String text) {
		nodeType = t;
		info = text;
	}
	
	public type GetType() {
		return nodeType;
	}
	public String GetText() {
		return info;
	}
	
	/**
	 * Allows GetPiece to be called on ProcessedNodes.
	 * @return Returns null except on instances of ProcessedNode
	 */
	public Piece GetPiece() {
		return null;
	}
	
	/**Receives a string; finds the first node and appends it to nodeList
	 * 
	 * @param from String representing user input
	 * @param nodeList 
	 * @return Returns the same string minus processed input. Returns an empty string
	 * if the input string is empty or only whitespace.
	 */
	public static String GetNextNode(String from, List<InputNode> nodeList)
	{
		if(from.length() == 0) {
			return "";
		}
		type finalType;
		charType lastType = checkType(from.charAt(0));
		int i = 0;
		int whitespaceEnd = 0;
		boolean done = false;
		while(!done && whitespaceEnd < from.length()) {
			lastType = checkType(from.charAt(whitespaceEnd));
			if(lastType == charType.Space) {
				whitespaceEnd++;
			} else {
				i = whitespaceEnd + 1;
				done = true;
			}
		}
		if(lastType == charType.Space) {
			//String was full of spaces.
			return "";
		}
		switch(lastType) {
		case Letter:
		case Digit:
			done = false;
			charType currentType = null;
			for(; !done && i < from.length(); i++) {
				currentType = checkType(from.charAt(i));
				switch(currentType) {
				case Letter:
				case Digit:
					//Only continue if we have another matching char type
					if(lastType != currentType) {
						i--;
						done = true;
					}
					break;
				default:
					i--;
					done = true;
					break;
				}
			}
			if(lastType == charType.Digit) {
				finalType = type.Number;
			} else if (currentType == charType.OpenParenth) {
				finalType = type.Function;
			} else {
				finalType = type.Variable;
			}
			break;
		case OpenParenth:
			finalType = type.OpenParenth;
			break;
		case CloseParenth:
			finalType = type.CloseParenth;
			break;
		case Comma:
			finalType = type.Comma;
			break;
		case Symbol:
			finalType = type.Symbol;
			break;
		default:
			finalType = type.Unknown;
			break;
		}
		nodeList.add(new InputNode(finalType, from.substring(whitespaceEnd, i)));
		
		return from.substring(i);
	}
	
	private static charType checkType(char c) {
		charType toReturn;
		if (c == '(') {
			toReturn = charType.OpenParenth;
		} else if (c == ')') {
			toReturn = charType.CloseParenth;
		} else if (c == '+' || c == '-' || c == '/' || c == '*' || c == '^') {
			toReturn = charType.Symbol;
		} else if (c == ',') {
			toReturn = charType.Comma;
		} else if(Character.isLetter(c)) {
			toReturn = charType.Letter;
		} else if (Character.isDigit(c) || c == '.') {
			toReturn = charType.Digit;
		} else if (Character.isWhitespace(c)) {
			toReturn = charType.Space;
		} else toReturn = charType.Unknown;
		
		return toReturn;
	}
	protected type nodeType;
	private enum charType {
		Symbol,
		Letter,
		Digit,
		OpenParenth,
		CloseParenth,
		Comma,
		Space,
		Unknown
	}
	private String info;

}
