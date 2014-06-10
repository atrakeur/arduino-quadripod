package com.atrakeur.quadripod.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import jssc.SerialPortException;

import com.atrakeur.quadripod.Application;
import com.atrakeur.quadripod.model.CoreModel.ArmPosition;

public class ArmModel {
	
	public static final int POSITION_MIN =   0;
	public static final int POSITION_MAX = 100;
	
	public static final String VERTICAL_POSITION_COMMAND = "Controller@setVPosition:%d %d\n";
	
	private Application app;
	private ArmPosition pos;
	
	private int verticalPosition;
	private int horizontalPosition;
	
	private PropertyChangeSupport pcs;
	
	public ArmModel(Application app, ArmPosition pos) {
		this.app = app;
		this.pos = pos;
		
		pcs = new PropertyChangeSupport(this);
	}
	
	public int getVerticalPosition() {
		return verticalPosition;
	}

	public void setVerticalPosition(int verticalPosition) {
		try {
			int oldValue = this.verticalPosition;
			
			app.getSerialModel().write(String.format(VERTICAL_POSITION_COMMAND, pos.ordinal(), verticalPosition));
			
			this.verticalPosition = verticalPosition;
			
			pcs.firePropertyChange("verticalPosition", oldValue, verticalPosition);
		} catch (SerialPortException e) {
			e.printStackTrace();
		}
	}

	public int getHorizontalPosition() {
		return horizontalPosition;
	}

	public void setHorizontalPosition(int horizontalPosition) {
		int oldValue = this.horizontalPosition;
		
		this.horizontalPosition = horizontalPosition;
		
		pcs.firePropertyChange("horizontalValue", oldValue, horizontalPosition);
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		pcs.removePropertyChangeListener(listener);
	}

}
