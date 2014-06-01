package com.atrakeur.quadripod.utils;

import java.awt.GridBagConstraints;

public class GBCWrapper extends GridBagConstraints {
	
	public GBCWrapper(int x, int y) {
		this.gridx = x;
		this.gridy = y;
	}
	
	public GBCWrapper weight(double x, double y) {
		this.weightx = x;
		this.weighty = y;
		return this;
	}
	
	public GBCWrapper size(int width, int height) {
		this.gridwidth = width;
		this.gridheight = height;
		return this;
	}
	
	public GBCWrapper fill(int val) {
		this.fill = val;
		return this;
	}

}
