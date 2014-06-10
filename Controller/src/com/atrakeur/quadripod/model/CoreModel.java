package com.atrakeur.quadripod.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;

import javax.swing.SwingUtilities;

import jssc.SerialPortException;

import com.atrakeur.quadripod.Application;

public class CoreModel {
	
	public enum ArmPosition {
		FLARM,
		FRARM,
		BLARM,
		BRARM
	}
	
	private Application app;
	private final PropertyChangeSupport pcs;
	
	private HashMap<String, String> lastCommands;
	private SerialCommandThread thread;
	
	private ArmModel[] arms;
	
	public CoreModel(Application app) {
		this.app = app;
		this.pcs = new PropertyChangeSupport(this);
		
		this.lastCommands = new HashMap<String, String>();
		this.thread       = new SerialCommandThread(app.getSerialModel(), this);
		
		this.arms         = new ArmModel[ArmPosition.values().length];
		for (int i = 0; i < ArmPosition.values().length; i++) {
			arms[i] = new ArmModel(this, ArmPosition.values()[i]);
		}
		
		bindThreadToSerial();
	}
	
	public ArmModel getArm(int index) {
		return arms[index];
	}
	
	public ArmModel getArm(ArmPosition pos) {
		return getArm(pos.ordinal());
	}
	
	public synchronized void sendCommand(String command) throws SerialPortException {
		app.getSerialModel().write(command);
	}
	
	/**
	 * Parse then add a command to command lists (threadsafe)
	 * @param data
	 */
	public synchronized void addCommand(String data) {
		String[] cmds = data.split("\n");
		
		for(String cmd : cmds) {
			String[] items = data.split(":");
			
			if (items.length == 2) {
				addCommand(items[0], items[1]);
			}
		}
	}
	
	/**
	 * Add command and value (threadsafe)
	 * @param key
	 * @param value
	 */
	public synchronized void addCommand(final String key, String value) {
		value = value.trim();
		
		final String oldValue = this.lastCommands.get(key);
		final String newValue = value;
		
		this.lastCommands.put(key, value);
		
		/* Invoke app model inside SwingThread */
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				pcs.firePropertyChange(key, oldValue, newValue);
			}
		});
	}
	
	/**
	 * Get the value of the given command (threadsafe)
	 * @param key
	 * @return
	 */
	public synchronized String getCommand(String key) {
		return this.lastCommands.get(key);
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		pcs.removePropertyChangeListener(listener);
	}
	
	/**
	 * Bind thread start/stop to serial connection/disconnection
	 */
	private void bindThreadToSerial() {
		app.getSerialModel().addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent arg0) {
				if (arg0.getPropertyName().equals("connected")) {
					if (arg0.getOldValue().equals(Boolean.FALSE) && arg0.getNewValue().equals(Boolean.TRUE)) {
						thread.start();
					}
					if (arg0.getOldValue().equals(Boolean.TRUE) && arg0.getNewValue().equals(Boolean.FALSE)) {
						thread.run = false;
					}
				}
			}
		});
	}
	
	/**
	 * Internal thread class that listen Serial port and push commands to parent CoreModel
	 */
	private class SerialCommandThread extends Thread {
		
		public volatile boolean run;
		private final SerialModel serial;
		private final CoreModel core;
		
		public SerialCommandThread(SerialModel serial, CoreModel core) {
			this.run    = true;
			this.serial = serial;
			this.core   = core;
		}
		
		public void run() {
			System.out.println("Run!");
			StringBuilder sb = new StringBuilder();
			try {
				while (run) {
					
					String data = serial.read(100);
					
					if (data == null) {
						continue;
					}
					
					sb.append(data);
					int endCmd;
					while ((endCmd = sb.indexOf("\n")) > 0) {
						addCommand(sb.substring(0, endCmd));
						sb.delete(0, endCmd + 1);
					}
				}
			} catch (SerialPortException e) {
				e.printStackTrace();
			}
			System.out.println("Stop!");
		}
		
	}

}
