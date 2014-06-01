package com.atrakeur.quadripod.frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
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
	
	private JMenuItem itmQuit;
	private JMenuItem itmSerial;
	
	public MainFrame(Application app) {
		this.app = app;
		
		createComponents();
		placeComponents();
		createController();
		
		pack();
	}

	private void createComponents() {
		robotDisplay = new JPanel();
		robotDisplay.setPreferredSize(new Dimension(200, 300));
		
		flArm = new JPanel();
		frArm = new JPanel();
		blArm = new JPanel();
		brArm = new JPanel();
		
		JMenuBar menuBar = new JMenuBar(); {
			JMenu menu = new JMenu("File"); {
				itmQuit = new JMenuItem("Quit");
				menu.add(itmQuit);
			} menuBar.add(menu);
			
			menu = new JMenu("Parameters"); {
				itmSerial = new JMenuItem("Serial");
				menu.add(itmSerial);
			} menuBar.add(menu);
		} this.setJMenuBar(menuBar);
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
		add(p);
	}
	
	private void createController() {
		itmSerial.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (!app.hasSubFrameType(SerialFrame.class)) {
					SerialFrame newFrame = new SerialFrame(app);
					app.registerSubFrame(newFrame);
					newFrame.setVisible(true);
				} else {
					app.getFrame(SerialFrame.class).toFront();
					app.getFrame(SerialFrame.class).requestFocus();
				}
			}
		});
	}

}

