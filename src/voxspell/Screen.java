package voxspell;

import java.awt.Color;

import javax.swing.JPanel;

/**
 * An abstract class that defines the general Screen.
 * A screen is a JPanel that has a ScreensOwner.
 * Having a ScreensOwner means that there are other screens that is interchangeable with this screen.
 * The interchangeable property is achieved by means of a CardLayout.
 */
@SuppressWarnings("serial")
public abstract class Screen extends JPanel {
	
	protected JPanel _screenPanel;
	public VoxSpellApp _owner;
	
	public Screen(VoxSpellApp screenOwner) {
		_screenPanel = new JPanel();
		_owner = screenOwner;
		
		/*
		 * Setting up the necessary initial GUI of the panel
		 * All subclasses of Screen has a white background and uses an absolute layout.
		 */
		_screenPanel.setBackground(Color.WHITE);
		_screenPanel.setLayout(null);
		
		buildScreenGUI();
		setupGUIListeners();		
	}

	/**
	 * A getter method that allows for the screen's interchangeability. 
	 * To be used for adding the Screen to a CardLayout
	 * @return
	 */
	protected JPanel getPanel() {
		return _screenPanel;
	}
	
	protected abstract void buildScreenGUI();
	
	protected abstract void setupGUIListeners();
}
