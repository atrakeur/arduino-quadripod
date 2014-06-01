package com.atrakeur.quadripod.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;

public class SerialModel {
	
	private String portName;
	private int bauds;
	
	private SerialPort port;
	
	private PropertyChangeSupport pcs;
	
	public SerialModel() {
		this(9600);
	}
	
	public SerialModel(int bauds) {
		this.bauds = bauds;
		
		String[] ports = availablePorts();
		if (ports.length >= 1) {
			portName = ports[0];
		} else {
			portName = "";
		}
		
		this.pcs = new PropertyChangeSupport(this);
	}
	
	public SerialModel(String portName, int bauds) {
		this.portName = portName;
		this.bauds = bauds;
		
		this.pcs = new PropertyChangeSupport(this);
	}
	
	public String[] availablePorts() {
		return SerialPortList.getPortNames();
	}
	
	public boolean isConnected() {
		return port != null;
	}

	public void connect() throws SerialPortException {
		if (isConnected()) {
			throw new SerialPortException(portName, "connect", "SerialCom allready connected");
		}
		
		port = new SerialPort(portName);
		port.openPort();//Open serial port
		port.setParams(SerialPort.BAUDRATE_9600, 
                                 SerialPort.DATABITS_8,
                                 SerialPort.STOPBITS_1,
                                 SerialPort.PARITY_NONE);
		
		pcs.firePropertyChange("connected", false, true);
	}
	
	public void disconnect() throws SerialPortException {
		if (!isConnected()) {
			throw new SerialPortException(port.getPortName(), "disconnect", "SerialCom not connected");
		}
		
		port.closePort();
		port = null;
		
		pcs.firePropertyChange("connected", true, false);
	}
	
	public void write(String str) throws SerialPortException {
		if (!isConnected()) {
			throw new SerialPortException(port.getPortName(), "write", "SerialCom not connected");
		}
		
		port.writeString(str);
	}
	
	public String read() throws SerialPortException {
		if (!isConnected()) {
			throw new SerialPortException(port.getPortName(), "write", "SerialCom not connected");
		}
		
		return port.readString();
	}
	
	public String read(int timeout) throws SerialPortException, InterruptedException {
		if (!isConnected()) {
			throw new SerialPortException(port.getPortName(), "write", "SerialCom not connected");
		}
		
		if (timeout < 10) {
			return read();
		}
		
		String buff;
		StringBuilder retVal = new StringBuilder();
		long end = System.currentTimeMillis() + timeout;
		
		while (System.currentTimeMillis() < end) {
			buff = read();
			
			if (buff != null) {
				retVal.append(buff);
			}
			
			Thread.sleep(1);
		}
		
		if (retVal.length() > 0) {
			return retVal.toString();
		}
		
		return null;
	}

	public String getPortName() {
		return portName;
	}

	public void setPortName(String portName) {
		String oldValue = this.portName;
		
		System.out.println(portName);
		this.portName = portName;
		
		pcs.firePropertyChange("portName", oldValue, portName);
	}

	public int getBauds() {
		return bauds;
	}

	public void setBauds(int bauds) {
		int oldValue = bauds;
		
		System.out.println(bauds);
		
		this.bauds = bauds;
		
		pcs.firePropertyChange("bauds", oldValue, bauds);
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(listener);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		pcs.removePropertyChangeListener(listener);
	}

}
