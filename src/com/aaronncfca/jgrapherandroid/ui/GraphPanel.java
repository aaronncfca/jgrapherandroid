package com.aaronncfca.jgrapherandroid.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import java.util.*;

import javax.swing.JPanel;

import com.aaronncfca.jgrapherandroid.function.SingleVarFunction;

public class GraphPanel extends JPanel {

	public GraphPanel() {
	}
	
	// Adds deltaX and deltaY to the current offset
	public void AdjustOffest(int deltaX, int deltaY) {
		deltaOffsetX += deltaX;
		deltaOffsetY += deltaY;
		repaint();
	}
	
	public void SetOffset(int offsetX, int offsetY) {
		deltaOffsetX = offsetX;
		deltaOffsetY = offsetY;
		repaint();
	}
	
	public void SetZoom(double newzoom) {
		deltaZoom = newzoom;
		repaint();
	}
	
	public void AdjustZoom(double offsetZoom) {
		//deltaZoom = 2;
		deltaZoom = deltaZoom * Math.pow(1.2, offsetZoom); //Not working
		repaint();
	}
	
	public void SetMouseLocation(int x) {
		infoX = convertFromX(x);
		repaint();
	}

	public GraphPanel(LayoutManager arg0) {
		super(arg0);
	}

	public GraphPanel(boolean arg0) {
		super(arg0);
	}

	public GraphPanel(LayoutManager arg0, boolean arg1) {
		super(arg0, arg1);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(new Color(.1F, .1F, .1F));
		g2.fillRect(0, 0, getWidth(), getHeight());
		zoom = (double) getWidth() / 100 * deltaZoom;
		offsetX = (getWidth() + deltaOffsetX) / 2;
		offsetY = (getHeight() + deltaOffsetY) / 2;
		g2.setColor(new Color(.2F, .2F, .2F));
		drawGrid(g2);
		g2.setColor(new Color(.7F, .7F, .7F));
		if(showAxes) {
			drawLine(g2, convertX(0), false);
			drawLine(g2, convertY(0), true);
		}
		g2.setColor(new Color(.3F, .5F, .3F));
		showInfo(g2);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(new Color(.3F, .4F, .9F));
		for(Map.Entry<Integer, FunctionInfo> fn
				: functions.entrySet()) {
			fn.getValue().Calculate(getWidth(), getHeight(),
					offsetX, offsetY,
					zoom);
			g2.drawPolyline(fn.getValue().xArray, fn.getValue().yArray, fn.getValue().arrayLength);	
		}
		
	}
	
	public void AddFunction(int id, SingleVarFunction function) {
		functions.put(id, new FunctionInfo(function));
		repaint();
	}
	
	public void RemoveLines(int graphId) {
		if(lines.containsKey(graphId)) {
			lines.remove(graphId);
		}
		repaint();
	}
	
	private void drawGrid(Graphics2D g2) {
		int levels = (zoom >= 1) ?
				1 + (int) (Math.log10(zoom)) :
				(int) (Math.log10(zoom));
		double unitsPer = Math.pow(10, -1 * levels) * 100;
		double pixelsPer = unitsPer * zoom;
		double firstX = (0 + offsetX) % pixelsPer;
		double firstY = (0 + offsetY) % pixelsPer;
		for (double x = firstX; x <= getWidth(); x += pixelsPer)
		{
			drawLine(g2, (int) x, false);
		}
		for (double y = firstY; y <= getHeight(); y += pixelsPer)
		{
			drawLine(g2, (int) y, true);
		}
	}
	
	private void showInfo(Graphics2D g2) {
		int x = convertX(infoX);
		if(x > 0 && x < getWidth()) {
			double yVal;
			int y;
			for(Map.Entry<Integer, FunctionInfo> fn
					: functions.entrySet()) {
				yVal = fn.getValue().GetPoint(infoX);
				y = convertY(yVal);
				int textY;
				if(y > 0 && y < getHeight()) {
					drawLine(g2, y, true);
					textY = y - 3;
				} else {
					textY = (y > 0) ? getHeight() - 5 : 15;
				}
				g2.drawString(String.format("%.4f: %.4f", infoX, yVal), x + 3, textY);
			}
			drawLine(g2, x, false);
		}
	}
	
	private void drawLine(Graphics2D g2, int loc, boolean horizontal) {
		if(horizontal) {
			g2.drawLine(0, loc, getWidth(), loc);
		} else {
			g2.drawLine(loc, 0, loc, getHeight());
		}
	}
	
	private int convertX(double x) {
		return (int) (x * zoom + offsetX);
	}
	private int convertY(double y) {
		return (int) (y * zoom * -1 + offsetY);
	}
	
	private double convertFromX(double x) {
		return (double) (x - offsetX) / zoom;
	}
	private double convertFromY(double y) {
		return (double) (y - offsetY) / zoom * -1;
	}
	
	private static HashMap<Integer, LinkedList<List<Point>>> lines
		= new HashMap<Integer, LinkedList<List<Point>>>();
	
	private static HashMap<Integer, FunctionInfo> functions
		= new HashMap<Integer, FunctionInfo>();
	private double zoom = 1.0;
	private double offsetX = 0; //Set anew each time paintComponent is called
	private double offsetY = 0;
	private double deltaOffsetX = 0; //Set by user and factored into offsetX/Y
	private double deltaOffsetY = 0;
	private double deltaZoom = 1;
	private double infoX = -1000;
	private boolean showAxes = true;
}
