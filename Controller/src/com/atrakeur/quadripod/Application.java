package com.atrakeur.quadripod;

import javax.swing.SwingUtilities;

import com.atrakeur.quadripod.frames.MainFrame;

public class Application {
	
	public Application() {
		//TODO create models?
		
		new MainFrame(this).setVisible(true);
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Application();
			}
		});
	}

}
