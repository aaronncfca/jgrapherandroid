package com.aaronncfca.jgrapherandroid.function;

import java.util.*;

import com.aaronncfca.jgrapherandroid.pieces.*;
import com.aaronncfca.jgrapherandroid.ui.Point;

public class SingleVarFunction {

	public SingleVarFunction(Piece piece, String independentVar) {
		this.piece = piece;
		Variable.setVariable(independentVar, 0);
		var = independentVar;
	}
	
	public void SetBounds(double min, double max, double increment) {
		this.min = min;
		this.max = max; 
		this.increment = increment;
	}
	
	public List<Point> GetPoints() {
		List<Point> points = new ArrayList<Point>();
		for(double pos = min; pos <= max; pos += increment) {
			Variable.setVariable(var, pos);
			points.add(new Point(pos, piece.Calculate()));
		}
		return points;
	}
	
	public double GetPoint(double x) {
		Variable.setVariable(var, x);
		return piece.Calculate();
	}
	
	public void ConsoleGraph(double xmin, double xmax, double xincrement, double ymin, double ymax, double yincrement) {
		int xrange = (int) Math.floor((xmax - xmin) / xincrement);
		int yrange = (int) Math.floor((ymax - ymin) / yincrement);
		double pos = xmin;
		double ynum = 0;
		String whitespace = " ";
		for(int i = 0; i < yrange; i++) {
			whitespace += " ";
		}
		
		for(int i = 0; i < xrange; i++) {
			StringBuilder ystring = new StringBuilder(whitespace);
			pos += xincrement;
			Variable.setVariable(var, pos);
			ynum = (piece.Calculate() - ymin) / yincrement;
			if(ynum > 0 && ynum < yrange) {
				ystring.setCharAt((int) Math.floor(ynum), '#');
			}
			System.out.println(ystring);
		}
	}
	
	private String var;
	private Piece piece;
	
	private double min;
	private double max;
	private double increment;
}
