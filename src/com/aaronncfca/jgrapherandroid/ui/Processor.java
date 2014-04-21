package com.aaronncfca.jgrapherandroid.ui;

import java.util.ArrayList;
import java.util.List;

import com.aaronncfca.jgrapherandroid.exceptions.InputException;
import com.aaronncfca.jgrapherandroid.exceptions.UndefinedVariableException;
import com.aaronncfca.jgrapherandroid.function.FB;
import com.aaronncfca.jgrapherandroid.pieces.*;

public class Processor {

	public static Piece ProcessInput(String input) throws InputException {

		List<InputNode> inputNodes = new ArrayList<InputNode>();
		while(input != "") {
			input = InputNode.GetNextNode(input, inputNodes);
		}
		return buildPiece(0, inputNodes.size() - 1, inputNodes);
	
	}
	
	private static Piece buildPiece(int begin, int end, List<InputNode> nodes) throws InputException, UndefinedVariableException {
		for(int pass = 1; pass <= 5; pass++) {
		for(int pos = begin; pos <= end; pos++) {
			switch(nodes.get(pos).GetType()) {
			case OpenParenth:
				int closeIndex = matchingCloseParenth(nodes, pos + 1, end);
				if(closeIndex == -1) {
					throw new InputException("Unclosed parenthesis.");
				}
				if(closeIndex == pos + 1) {
					throw new InputException("Empty parentheses.");
				}
				buildPiece(pos + 1, closeIndex - 1, nodes);
				//Remove parentheses and update end
				nodes.remove(pos + 2);
				nodes.remove(pos);
				end -= (closeIndex - pos);
				break;
			case Function:
				end -= processFunction(nodes, pos, end);
				break;
			case Number:
				double num;
				//Now is the only time we can check for this error.
				if(pos < end && nodes.get(pos + 1).GetType() == InputNode.type.Number) {
					throw new InputException("Invalid number format. Check for whitespace between numbers.");
				}
				try {
					num = Double.parseDouble(nodes.get(pos).GetText());
				} catch (NumberFormatException e) {
					throw new InputException("Number format error: " + e.getMessage());
				}
				end -= replaceNodes(nodes, new DefinedConstant(num), pos, pos);
				break;
			case Variable:
				
				end -= replaceNodes(nodes, new Variable(nodes.get(pos).GetText()), pos, pos);
				break;
			case Symbol:
				//Pass 1: ignore all symbols
				if(pass == 1) break;
				char symbol = nodes.get(pos).GetText().charAt(0);
				
				//Pass 2: catch malformed operator errors. Process '-' as a negative sign (e.g. -1).
				if(pass == 2) {
					if(pos == begin || nodes.get(pos - 1).GetType() != InputNode.type.ProcessedNode) {
						if(symbol == '-') {
							if(pos == end || nodes.get(pos + 1).GetType() != InputNode.type.ProcessedNode) {
								throw new InputException("Operator error with " + symbol + ": no right-hand operand.");
							}
							Piece newPiece = FB.multiply(FB.num(-1), nodes.get(pos + 1).GetPiece());
							end -= replaceNodes(nodes, newPiece, pos, pos + 1);
						} else {
							throw new InputException("Operator error with " + symbol + ": no left-hand operand.");
						}
					}
				}
				//Pass 3: catch more operator errors. Process ^
				else if(pass == 3) {
					if(pos == end || nodes.get(pos + 1).GetType() != InputNode.type.ProcessedNode){
						throw new InputException("Operator error with " + symbol + ": no right-hand operand.");
					}
					if(symbol == '^') {
						Piece newPiece = new Power(nodes.get(pos - 1).GetPiece(), nodes.get(pos + 1).GetPiece());
						end -= replaceNodes(nodes, newPiece, pos - 1, pos + 1);
						//Here and elsewhere decrementing pos to make up
						//for node at pos + 1 being deleted
						pos--;
					}
				}
				//Pass 4: * or /
				else if(pass == 4) {
					if(symbol == '*') {
						Piece newPiece = FB.multiply(nodes.get(pos - 1).GetPiece(), nodes.get(pos + 1).GetPiece());
						end -= replaceNodes(nodes, newPiece, pos - 1, pos + 1);
						pos--;
					}
					else if(symbol == '/') {
						Piece newPiece = FB.divide(nodes.get(pos - 1).GetPiece(), nodes.get(pos + 1).GetPiece());
						end -= replaceNodes(nodes, newPiece, pos - 1, pos + 1);
						pos--;
					}
				}
				//Pass 5: + or -
				else if(pass == 5) {
					if(symbol == '+') {
						Piece newPiece = FB.add(nodes.get(pos - 1).GetPiece(), nodes.get(pos + 1).GetPiece());
						end -= replaceNodes(nodes, newPiece, pos - 1, pos + 1);
						pos--;
					}
					else if(symbol == '-') {
						Piece newPiece = FB.add(nodes.get(pos - 1).GetPiece(),
								FB.multiply(FB.num(-1), nodes.get(pos + 1).GetPiece()));
						end -= replaceNodes(nodes, newPiece, pos - 1, pos + 1);
						pos--;
					}
				}
				
				break;
			case ProcessedNode:
				//Assume two processed Pieces side-by-side should be multiplied together
				while (pass > 1
						&& pos < end
						&& nodes.get(pos+1).GetType() == InputNode.type.ProcessedNode) {
					Piece newPiece = FB.multiply(nodes.get(pos).GetPiece(), nodes.get(pos + 1).GetPiece());
					end -= replaceNodes(nodes, newPiece, pos, pos + 1);
				}
				break;
			case CloseParenth:
			case Unknown:
			case Comma:
				throw new InputException("Misplaced or unrecognized character: " + nodes.get(pos).GetText());
			default:
				break;
			}
		}
		}
		if(end != begin || nodes.get(begin).GetType() != InputNode.type.ProcessedNode) {
			throw new InputException("Unknown error.");
		}
		return nodes.get(begin).GetPiece();
	}
	
	/**
	 * Removes all nodes at or between begin and end; places replacement
	 * in a new ProcessedNode in their place
	 * @return Returns the difference in nodes' length after replacement
	 */
	private static int replaceNodes(List<InputNode> nodes, Piece replacement, int begin, int end) {
		//Reverse for loop 
		for(int i = end; i >= begin; i--) {
	    	nodes.remove(i);
	    }
		nodes.add(begin, new ProcessedNode(replacement));
		return end - begin;
	}
	
	/**
	 * Finds the next InputType of type toFind at or after begin. Returns -1 if not found.
	 */
	private static int nextNodeOfType(List<InputNode> nodes, InputNode.type toFind, int begin) {
		int index = begin;
		while(nodes.size() > index) {
			if(nodes.get(index).GetType() != toFind) {
				index++;
			} else {
				return index;
			}
		}
		return -1;
	}
	
	/**
	 * Finds the next closing parenth at or after begin, ignoring matching sets.
	 * Returns -1 if not found.
	 */
	private static int matchingCloseParenth(List<InputNode> nodes, int begin, int end) {
		int index = begin;
		//Start with one parenth to close
		int parenthIndex = 1;
		while(nodes.size() > index && end >= index) {
			if(nodes.get(index).GetType() == InputNode.type.CloseParenth) {
				parenthIndex--;
				if(parenthIndex == 0) {
					return index;
				}
			} else if (nodes.get(index).GetType() == InputNode.type.OpenParenth) {
				parenthIndex++;
			}
			index++;
		}
		return -1;
	}
	
	/**
	 * Processes the function at functionPos and replaces all
	 * corresponding InputNodes with a ProcessedNode of the function
	 * @param end Represents the logical upper border of nodes, which
	 * should be smaller than or equal to the actual size of nodes
	 * @return Returns the number of InputNodes removed from nodes
	 */
	private static int processFunction(List<InputNode> nodes, int functionPos, int end) throws InputException, UndefinedVariableException {
		int beginLength = nodes.size();
		String functionName = nodes.get(functionPos).GetText().toLowerCase();
		Piece toReturn = null;
		List<Piece> args = new ArrayList<Piece>();
		int closeIndex = matchingCloseParenth(nodes, functionPos + 2, end);
		if(closeIndex == -1) {
			throw new InputException("Missing closing parenthesis on function " + functionName);
		}
		int nextComma = functionPos + 1;
		int lastComma = nextComma;
		while (true) {
			nextComma = nextNodeOfType(nodes, InputNode.type.Comma, lastComma + 1);
			if(nextComma == -1 || nextComma >= closeIndex) {
				break;
			}
			args.add(buildPiece(lastComma + 1, nextComma - 1, nodes));
			lastComma += 2;
			//Compensate for nodes removed by buildPiece
			closeIndex -= (nextComma - lastComma + 2);
		}
		if(closeIndex > functionPos + 2) {
			args.add(buildPiece(lastComma + 1, closeIndex - 1, nodes));
			//Compensate for nodes removed by buildPiece
			closeIndex -= (closeIndex - lastComma - 2);
		}
		switch(functionName)
		{
		case "sin":
		case "sine":
			if(args.size() != 1) {
				throw new InputException("Wrong argument count in function " + functionName);
			}
			toReturn = new Sine(args.get(0));
			break;
		case "cos":
		case "cosine":
			if(args.size() != 1) {
				throw new InputException("Wrong argument count in function " + functionName);
			}
			toReturn = new Cosine(args.get(0));
			break;
		case "tan":
		case "tangent":
			if(args.size() != 1) {
				throw new InputException("Wrong argument count in function " + functionName);
			}
			toReturn = new Tangent(args.get(0));
			break;
		case "arcsine":
		case "asin":
		case "asine":
			if(args.size() != 1) {
				throw new InputException("Wrong argument count in function " + functionName);
			}
			toReturn = new Arcsine(args.get(0));
			break;
		case "arccos":
		case "acos":
		case "acosine":
			if(args.size() != 1) {
				throw new InputException("Wrong argument count in function " + functionName);
			}
			toReturn = new Arccos(args.get(0));
			break;
		case "arctan":
		case "atan":
		case "atangent":
			if(args.size() != 1) {
				throw new InputException("Wrong argument count in function " + functionName);
			}
			toReturn = new Arctan(args.get(0));
			break;
		case "log":
			if(args.size() == 1) {
				toReturn = new Log(new DefinedConstant(10), args.get(0));
			} else if (args.size() == 2) {
				toReturn = new Log(args.get(0), args.get(1));
			} else {
				throw new InputException("Wrong argument count in function " + functionName);
			}
			break;
		case "ln":
			if(args.size() != 1) {
				throw new InputException("Wrong argument count in function " + functionName);
			}
			toReturn = new NaturalLog(args.get(0));
			break;
		case "sqrt":
			if(args.size() != 1) {
				throw new InputException("Wrong argument count in function " + functionName);
			}
			toReturn = new Power(args.get(0), new DefinedConstant(0.5));
			break;
		case "root":
			if(args.size() == 1) {
				toReturn = new Power(args.get(0), new DefinedConstant(0.5));
			} else if (args.size() == 2) {
				toReturn = new Root(args.get(0), args.get(1));
			} else {
				throw new InputException("Wrong argument count in function " + functionName);
			}
			break;
		}
		replaceNodes(nodes, toReturn, functionPos, closeIndex);
		return beginLength - nodes.size();
	}
	

}
