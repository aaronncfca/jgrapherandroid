package com.aaronncfca.jgrapherandroid;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.aaronncfca.jgrapherandroid.function.SingleVarFunction;
import com.aaronncfca.jgrapherandroid.ui.FunctionInfo;
import com.aaronncfca.jgrapherandroid.ui.Point;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.FillType;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class GraphPanel extends View {

	private GestureDetectorCompat mDetector;
	
	public GraphPanel(Context context) {
		super(context);
		init();
	}

	public GraphPanel(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public GraphPanel(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}
	private float mouseLastX = 0;
	private float mouseLastY = 0;
	private boolean pauseInfo;
	
	private void init() {
		mDetector = new GestureDetectorCompat(this, new MyGestureListener());
		initPaint();
		offsetX = (getWidth() + deltaOffsetX) / 2;
		offsetY = (getHeight() + deltaOffsetY) / 2;
		zoom = (double) getWidth() / 100 * deltaZoom;
	}
	
	class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
		
		@Override
		public boolean onDown(MotionEvent event) {
			return true;
		}
		
		@Override
		public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
			return true;
		}
	}
	
	
	@Override
	public boolean onTouchEvent(MotionEvent e) {
		
		switch(e.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mouseLastX = e.getX();
			mouseLastY = e.getY();
			break;
		case MotionEvent.ACTION_UP:
			break;
		case MotionEvent.ACTION_MOVE:
			AdjustOffest((int)(e.getX() - mouseLastX), (int)(e.getY() - mouseLastY));
			mouseLastX = e.getX();
			mouseLastY = e.getY();
			break;
		}
		return true;
	}
	
	// Adds deltaX and deltaY to the current offset
		public void AdjustOffest(int deltaX, int deltaY) {
			deltaOffsetX += deltaX;
			deltaOffsetY += deltaY;
			offsetX = getWidth() / 2 + deltaOffsetX;
			offsetY = getHeight() / 2 + deltaOffsetY;
			invalidate();
		}
		
		public void SetOffset(int newOffsetX, int newOffsetY) {
			deltaOffsetX = newOffsetX;
			deltaOffsetY = newOffsetY;
			offsetX = getWidth() / 2 + deltaOffsetX;
			offsetY = getHeight() / 2 + deltaOffsetY;
			invalidate();
		}
		
		public void SetZoom(double newzoom) {
			deltaZoom = newzoom;
			zoom = (double) getWidth() / 100 * deltaZoom;
			invalidate();
		}
		
		public void AdjustZoom(double offsetZoom) {
			//deltaZoom = 2;
			deltaZoom = deltaZoom * Math.pow(1.2, offsetZoom); //Not working
			invalidate();
		}
		
		public void SetMouseLocation(int x) {
			infoX = convertFromX(x);
			invalidate();
		}
		
		@Override
		protected void onDraw(Canvas c) {
			super.onDraw(c);
			if(zoom == 0) {
				init();
			}
			c.drawRect(0, 0, getWidth(), getHeight(), paintBg);
			for(Map.Entry<Integer, FunctionInfo> fn
					: functions.entrySet()) {
				path.rewind();
				fn.getValue().Calculate(getWidth(), getHeight(),
						offsetX, offsetY,
						zoom);
				if(fn.getValue().arrayLength>0) {
					path.moveTo(fn.getValue().xArray[0], fn.getValue().yArray[0]);
					for(int i = 1; i < fn.getValue().arrayLength; i++) {
						path.lineTo(fn.getValue().xArray[i], fn.getValue().yArray[i]);
					}
				}
				c.drawPath(path, paintPath);
			}
		}
		
		public void AddFunction(int id, SingleVarFunction function) {
			functions.put(id, new FunctionInfo(function));
			invalidate();
		}
		
		public void RemoveLines(int graphId) {
			if(lines.containsKey(graphId)) {
				lines.remove(graphId);
			}
			invalidate();
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
		
		private void initPaint() {
			paintBg.setColor(0xFFCCCCCC);
			paintPath.setStrokeWidth(2);
			paintPath.setStyle(Style.STROKE);
			paintPath.setColor(0xFF2255F0);
			paintPath.setAntiAlias(true);
		}
		
		
		private static HashMap<Integer, LinkedList<List<Point>>> lines
			= new HashMap<Integer, LinkedList<List<Point>>>();
		
		private static HashMap<Integer, FunctionInfo> functions
			= new HashMap<Integer, FunctionInfo>();
		private double zoom = 0; //Initialized later
		private double offsetX = 0; 
		private double offsetY = 0;
		private double deltaOffsetX = 0; //Set by user and factored into offsetX/Y
		private double deltaOffsetY = 0;
		private double deltaZoom = 1;
		private double infoX = -1000;
		private boolean showAxes = true;
		private Paint paintBg = new Paint();
		private Paint paintPath = new Paint();
		private Path path = new Path();
}
