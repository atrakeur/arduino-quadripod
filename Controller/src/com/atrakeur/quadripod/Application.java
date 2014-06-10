package com.atrakeur.quadripod;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.atrakeur.quadripod.frames.MainFrame;
import com.atrakeur.quadripod.frames.SerialFrame;
import com.atrakeur.quadripod.model.CoreModel;
import com.atrakeur.quadripod.model.SerialModel;

public class Application {
	
	private final MainFrame mainFrame;
	private final HashMap<Class<? extends JFrame>, Object> subFrames;

	private final CoreModel coreModel;
	private final SerialModel serialModel;
	
	public Application() {
		serialModel = new SerialModel();
		coreModel = new CoreModel(this);

		
		mainFrame = new MainFrame(this);
		subFrames = new HashMap<>();
		
		mainFrame.setVisible(true);
	}
	
	public <T extends JFrame> boolean hasSubFrameType(Class<T> type) {
		return subFrames.containsKey(type);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends JFrame> T getFrame(Class<T> type) {
		if (!hasSubFrameType(type)) {
			return null;
		}
		
		return (T)subFrames.get(type);
	}
	
	public <T extends JFrame> boolean registerSubFrame(final T frame) {
		if (hasSubFrameType(frame.getClass())) {
			return false;
		}
		
		subFrames.put(frame.getClass(), frame);
		//listener to auto unregister frame on setVisible(false)
		frame.addComponentListener(new ComponentAdapter() {
			public void componentHidden(ComponentEvent e) 
			{
			    unRegisterSubFrame(frame);
			}
		});
		
		return true;
	}
	
	public <T extends JFrame> boolean unRegisterSubFrame(T frame) {
		if (!hasSubFrameType(frame.getClass())) {
			return false;
		}
		
		subFrames.remove(frame.getClass());
		
		return true;
	}
	
	public SerialModel getSerialModel() {
		return serialModel;
	}
	
	public CoreModel getCoreModel() {
		return coreModel;
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Application();
			}
		});
	}

}
