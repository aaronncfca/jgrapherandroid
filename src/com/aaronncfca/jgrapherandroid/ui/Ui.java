package com.aaronncfca.jgrapherandroid.ui;
import java.io.*;
import java.util.*;

import javax.swing.JFrame;

import android.webkit.WebView.FindListener;

import com.aaronncfca.jgrapherandroid.GraphPanel;
import com.aaronncfca.jgrapherandroid.R;
import com.aaronncfca.jgrapherandroid.exceptions.*;
import com.aaronncfca.jgrapherandroid.function.*;
import com.aaronncfca.jgrapherandroid.pieces.*;

public class Ui {

	
	
	public Ui() {
		inputDevice = new BufferedReader(new InputStreamReader(System.in));
	}
	public Ui(InputStreamReader inputStream) {
		inputDevice = new BufferedReader(inputStream);
	}
	public Piece getInput() throws InputException {
		Piece toReturn = null;
		String input = null;
		try {
			input = inputDevice.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		toReturn = Processor.ProcessInput(input);
		System.out.println(toReturn.toString());
		return toReturn;
	}
	
	public static Piece GetWindowInput() throws InputException {
		String input = "sin(x) + 5"; //window.GetInput();
		return Processor.ProcessInput(input);
	}
	
	public static void OpenWindow() {
		//window.Show();
	}
	
	public static void Graph2d(SingleVarFunction func) {
		//window.AddGraph(func.hashCode(), func);
	}
	
	private BufferedReader inputDevice;
	//private static MainWindow window = new MainWindow();

}

