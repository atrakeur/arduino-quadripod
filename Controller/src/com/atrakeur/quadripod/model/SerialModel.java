package com.atrakeur.quadripod.model;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;

public class SerialModel {
	
	private String portName;
	private int bauds;
	
	private SerialPort port;
	
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
	}
	
	public SerialModel(String portName, int bauds) {
		this.portName = portName;
		this.bauds = bauds;
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
	}
	
	public void disconnect() throws SerialPortException {
		if (!isConnected()) {
			throw new SerialPortException(port.getPortName(), "disconnect", "SerialCom not connected");
		}
		
		port.closePort();
		port = null;
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

}
