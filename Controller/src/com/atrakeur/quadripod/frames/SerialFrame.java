package com.atrakeur.quadripod.frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.atrakeur.quadripod.Application;
import com.atrakeur.quadripod.model.SerialModel;
import com.atrakeur.quadripod.utils.GBCWrapper;

public class SerialFrame extends JFrame {
	
	public static final String LBLSTATUS_NO_CONNECTION = "Not connected";
	public static final String LBLSTATUS_CONNECTED = "Connected";
	
	public static final String BTNCONNECT_CONNECT = "Connect";
	public static final String BTNCONNECT_DISCONNECT = "Disconnect";
	
	public static final int CMDBAUDS_DEFAULT = 1;
	public static final Integer[] CMDBAUDS_VALUES = {4800, 9600};
	
	Application app;
	SerialModel model;
	
	JLabel lblStatus;
	JButton btnConnect;
	
	JLabel lblPort;
	JComboBox<String> cmdPort;
	
	JLabel lblBauds;
	JComboBox<Integer> cmdBauds;
	
	JButton cmdClose;
	
	public SerialFrame(Application app) {
		this.app = app;
		this.model = app.getSerialModel();
		
		createComponents();
		placeComponents();
		createController();
		
		pack();
	}

	private void createComponents() {
		lblStatus = new JLabel(LBLSTATUS_NO_CONNECTION);
		btnConnect = new JButton(BTNCONNECT_CONNECT);
		
		lblPort = new JLabel("Port: ");
		cmdPort = new JComboBox<>(model.availablePorts());
		
		lblBauds = new JLabel("Bauds: ");
		cmdBauds = new JComboBox<>(CMDBAUDS_VALUES);
		cmdBauds.setSelectedIndex(CMDBAUDS_DEFAULT);
		
		cmdClose = new JButton("Close");
	}

	private void placeComponents() {
		setLayout(new BorderLayout());
		JPanel p = new JPanel(new GridBagLayout()); {
			p.add(lblStatus, new GBCWrapper(0, 0).weight(1, 1));
			p.add(btnConnect, new GBCWrapper(1, 0).weight(1, 1));
			
			p.add(lblPort, new GBCWrapper(0, 1).fill(GridBagConstraints.BOTH).weight(2, 1));
			p.add(cmdPort, new GBCWrapper(1, 1).fill(GridBagConstraints.BOTH).weight(1, 1));
			
			p.add(lblBauds, new GBCWrapper(0, 2).fill(GridBagConstraints.BOTH).weight(2, 1));
			p.add(cmdBauds, new GBCWrapper(1, 2).fill(GridBagConstraints.BOTH).weight(1, 1));
			
			p.add(cmdClose, new GBCWrapper(0, 3).size(2, 1));
		}
		add(p);
	}
	
	private void createController() {
		cmdClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SerialFrame.this.setVisible(false);
			}
		});
	}

}
