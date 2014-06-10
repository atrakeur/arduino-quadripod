package com.atrakeur.quadripod.frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import jssc.SerialPortException;

import com.atrakeur.quadripod.Application;
import com.atrakeur.quadripod.model.SerialModel;
import com.atrakeur.quadripod.utils.GBCWrapper;

public class SerialFrame extends JFrame implements PropertyChangeListener {
	
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
		updateProperties();
		createController();
		
		pack();
	}

	private void createComponents() {
		lblStatus = new JLabel(LBLSTATUS_NO_CONNECTION);
		btnConnect = new JButton(BTNCONNECT_CONNECT);
		
		lblPort = new JLabel("Port: ");
		cmdPort = new JComboBox<>();
		
		lblBauds = new JLabel("Bauds: ");
		cmdBauds = new JComboBox<>();
		
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
		this.addComponentListener(new ComponentAdapter() {
			public void componentShown(ComponentEvent e) {
				model.addPropertyChangeListener(SerialFrame.this);
			}
			
			public void componentHidden(ComponentEvent e) 
			{
				model.removePropertyChangeListener(SerialFrame.this);
			}
		});
		
		cmdClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SerialFrame.this.setVisible(false);
			}
		});
		
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					if (model.isConnected()) {
							model.disconnect();
					} else {
						model.connect();
					}
				} catch (SerialPortException e) {
					JOptionPane.showMessageDialog(SerialFrame.this, e.toString(), 
								"Exception Occurred", JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
				}
			}
		});
		
		cmdPort.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.setPortName(cmdPort.getItemAt(cmdPort.getSelectedIndex()));
			}
		});
		
		cmdBauds.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (cmdBauds.getItemAt(cmdBauds.getSelectedIndex()) != null) {
					model.setBauds(cmdBauds.getItemAt(cmdBauds.getSelectedIndex()));
				}
			}
		});
	}
	
	public void updateProperties() {
		if (model.isConnected()) {
			lblStatus.setText(LBLSTATUS_CONNECTED);
			btnConnect.setText(BTNCONNECT_DISCONNECT);
		} else {
			lblStatus.setText(LBLSTATUS_NO_CONNECTION);
			btnConnect.setText(BTNCONNECT_CONNECT);
		}
		
		//cmdPort.removeAllItems();
		for (String port: model.availablePorts()) {
			cmdPort.addItem(port);
			if (port.equals(model.getPortName())) {
				cmdPort.setSelectedItem(port);
			}
		}
		
		cmdBauds.removeAllItems();
		for (Integer baud: CMDBAUDS_VALUES) {
			cmdBauds.addItem(baud);
			if (baud == model.getBauds()) {
				System.out.println("Eq for "+baud+" "+model.getBauds());
				cmdBauds.setSelectedItem(baud);
			}
		}
	}

	public void propertyChange(PropertyChangeEvent evt) {
		updateProperties();
	}

}
