package com.aaronncfca.jgrapherandroid.ui;

import java.util.*;

import com.aaronncfca.jgrapherandroid.function.*;

public class FunctionInfo {

	public FunctionInfo(SingleVarFunction fn) {
		function = fn;
	}
	
	//Updates the member lists x and y
	public void Calculate(int width, int height, double offsetX, double offsetY, double zoom) {
		double increment = 1 / zoom;
		double xmin = 0 - offsetX * increment;
		double xmax = (width - offsetX) * increment;
		if(x == null								// if uninitiated...
				|| increment * 2 < this.increment	// or too much change in zoom...
				|| increment / 2 > this.increment
				|| xmin > this.xmin + width / 4		// or too many extra points...
				|| xmax < this.xmax + width / 4) {
			recalculate(increment, xmin, xmax);		// do a full recalculation
		}
		else {
			if(xmin < this.xmin) {
				for(double pos = xmin; pos < this.xmin; pos += this.increment) {
					addPoint(x, y, pos);
				}
			}
			if(xmax > this.xmax) {
				for(double pos = this.xmax + this.increment;
						pos <= xmax; pos += this.increment) {
					addPoint(x, y, pos);
				}
			}
		}
		arrayLength = x.size();
		int xNew[] = new int[arrayLength];
		int yNew[] = new int[arrayLength];
		for(int i = 0; i < arrayLength; i++) {
			xNew[i] = (int) (x.get(i) * zoom + offsetX);
			yNew[i] = (int) (y.get(i) * zoom * -1 + offsetY);
		}
		xArray = xNew;
		yArray = yNew;
	}
//	
//	public int[] GetX(double zoom, int offsetX, int offsetY) {
//		return null;
//	}

	public double GetPoint(double x) {
		return function.GetPoint(x);
	}
	
	public int[] xArray;
	public int[] yArray;
	public int arrayLength;
	
	
	private void recalculate(double increment, double xmin, double xmax) {
		x = new ArrayList<Double>();
		y = new ArrayList<Double>();
		this.increment = increment;
		this.xmin = xmin;
		this.xmax = xmax;
		for(double pos = xmin; pos <= xmax; pos += increment) {
			addPoint(x, y, pos);
		}
	}
	
	private void addPoint(List<Double> x, List<Double> y, double pos) {
		double yPos = function.GetPoint(pos);
		if(Double.isNaN(yPos)) return;
		x.add(pos);
		y.add(yPos);
	}
	
	private SingleVarFunction function;
	private double increment;
	private double xmin;
	private double xmax;
	// x and y should always be the same length and should have
	// parallel values.
	private List<Double> x; 
	private List<Double> y;

}
