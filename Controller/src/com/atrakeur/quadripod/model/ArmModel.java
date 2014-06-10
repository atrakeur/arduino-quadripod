package com.atrakeur.quadripod.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import jssc.SerialPortException;

import com.atrakeur.quadripod.Application;
import com.atrakeur.quadripod.model.CoreModel.ArmPosition;

public class ArmModel {
	
	public static final int POSITION_MIN =   0;
	public static final int POSITION_MAX = 100;
	
	public static final String VERTICAL_POSITION_COMMAND   = "Controller@setVPosition:%d %d\n";
	public static final String HORIZONTAL_POSITION_COMMAND = "Controller@setHPosition:%d %d\n";
	
	private CoreModel  core;
	private ArmPosition pos;
	
	private int verticalPosition;
	private int horizontalPosition;
	
	private PropertyChangeSupport pcs;
	
	public ArmModel(CoreModel core, ArmPosition pos) {
		this.core = core;
		this.pos  = pos;
		
		pcs = new PropertyChangeSupport(this);
		
		createCoreListener();
	}
	
	public int getVerticalPosition() {
		return verticalPosition;
	}

	public void setVerticalPosition(int verticalPosition) throws SerialPortException {
		int oldValue = this.verticalPosition;
		
		core.sendCommand(String.format(VERTICAL_POSITION_COMMAND, pos.ordinal(), verticalPosition));
		
		this.verticalPosition = verticalPosition;
		
		pcs.firePropertyChange("verticalPosition", oldValue, verticalPosition);
	}

	public int getHorizontalPosition() {
		return horizontalPosition;
	}

	public void setHorizontalPosition(int horizontalPosition) throws SerialPortException {
		int oldValue = this.horizontalPosition;
		
		core.sendCommand(String.format(HORIZONTAL_POSITION_COMMAND, pos.ordinal(), horizontalPosition));
		
		this.horizontalPosition = horizontalPosition;
		
		pcs.firePropertyChange("horizontalValue", oldValue, horizontalPosition);
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		pcs.removePropertyChangeListener(listener);
	}
	
	private void createCoreListener() {
		core.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals("Controller@setVPosition")) {
					//TODO check returned value?
				}
				if (evt.getPropertyName().equals("Controller@setHPosition")) {
					//TODO check returned value?
				}
			}
		});
	}

}
