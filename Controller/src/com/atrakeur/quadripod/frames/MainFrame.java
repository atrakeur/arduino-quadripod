package com.atrakeur.quadripod.frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.atrakeur.quadripod.Application;
import com.atrakeur.quadripod.utils.GBCWrapper;


public class MainFrame extends JFrame {
	
	Application app;
	
	private JPanel robotDisplay;
	private JPanel flArm;
	private JPanel frArm;
	private JPanel blArm;
	private JPanel brArm;
	
	public MainFrame(Application app) {
		this.app = app;
		
		createComponents();
		placeComponents();
		createController();
		
		pack();
	}

	private void createComponents() {
		robotDisplay = new JPanel();
		
		flArm = new JPanel();
		
		frArm = new JPanel();
		
		blArm = new JPanel();
		
		brArm = new JPanel();
	}

	private void placeComponents() {
		setLayout(new BorderLayout());
		JPanel p = new JPanel(new GridBagLayout()); {
			p.add(robotDisplay, new GBCWrapper(1, 0).size(1, 2).fill(GridBagConstraints.BOTH).weight(2, 2));
			
			p.add(flArm, new GBCWrapper(0, 0).fill(GridBagConstraints.BOTH).weight(1, 1));
			p.add(frArm, new GBCWrapper(2, 0).fill(GridBagConstraints.BOTH).weight(1, 1));
			p.add(blArm, new GBCWrapper(0, 1).fill(GridBagConstraints.BOTH).weight(1, 1));
			p.add(brArm, new GBCWrapper(2, 1).fill(GridBagConstraints.BOTH).weight(1, 1));
		}
		p.setBackground(Color.black);
		add(p);
	}
	
	private void createController() {
		// TODO Auto-generated method stub
		
	}

}

