package com.atrakeur.quadripod.frames.panels;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.atrakeur.quadripod.Application;
import com.atrakeur.quadripod.model.ArmModel;
import com.atrakeur.quadripod.model.CoreModel.ArmPosition;
import com.atrakeur.quadripod.utils.GBCWrapper;

public class ArmPanel extends JPanel{
	
	private Application app;
	private ArmModel model;
	
	private JLabel lblVertical;
	private JLabel lblHorizontal;
	private JSlider verticalPosition;
	private JSlider horizontalPosition;
	
	public ArmPanel(Application app, ArmPosition armPos) {
		this.app = app;
		this.model = app.getCoreModel().getArm(armPos);
		
		createComponents();
		placeComponents();
		updateProperties();
		createController();
	}
	
	private void createComponents() {
		lblVertical = new JLabel("Vertical: 00");
		lblHorizontal = new JLabel("Horizontal: 00");
		
		verticalPosition = new JSlider(JSlider.VERTICAL, ArmModel.POSITION_MIN, ArmModel.POSITION_MAX, 0);
		horizontalPosition = new JSlider(JSlider.HORIZONTAL, ArmModel.POSITION_MIN, ArmModel.POSITION_MAX, 0);
	}
	
	private void placeComponents() {
		setLayout(new BorderLayout());
		JPanel p = new JPanel(new GridBagLayout()); {
			JPanel q = new JPanel(new GridLayout(0,1)); {
				q.add(lblVertical);
				q.add(lblHorizontal);
			} p.add(q, new GBCWrapper(0, 0));
			
			p.add(verticalPosition, new GBCWrapper(1, 0));
			p.add(horizontalPosition, new GBCWrapper(0, 1).size(2, 0));
		} add(p);
	}
	
	private void updateProperties() {
		lblVertical.setText("Vertical: "+model.getVerticalPosition());
		verticalPosition.setValue(model.getVerticalPosition());
		lblHorizontal.setText("Vertical: "+model.getHorizontalPosition());
		horizontalPosition.setValue(model.getHorizontalPosition());
	}
	
	private void createController() {
		model.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				updateProperties();
			}
		});
		
		verticalPosition.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				model.setVerticalPosition(verticalPosition.getValue());
			}
		});
		
		horizontalPosition.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				model.setHorizontalPosition(horizontalPosition.getValue());
			}
		});
	}

}
