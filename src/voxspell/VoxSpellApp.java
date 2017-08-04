package voxspell;

import java.awt.CardLayout;
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import voxspell.newquiz.QuizScreen;
import voxspell.scoreboard.ScoreboardScreen;
import voxspell.wordlist.entirelist.ChangeEntireWordListScreen;
import voxspell.wordlist.partiallist.ChangePartialWordListScreen;

/**
 * This is the main class where the application is initialized.
 * This class is responsible for owning the interchangeable screens and controlling the screen's interchange.
 * 
 * Author: echa232
 */
@SuppressWarnings("serial")
public class VoxSpellApp extends JPanel {

	/**
	 * Enum class consists of elements that represent the names of the interchangeable screens.
	 * @author echa232
	 *
	 */
	public enum AppScreen {
		MAIN_MENU("Main Menu"), NEW_SPELLING_QUIZ("New Spelling Quiz"),
		SCOREBOARD("Scoreboard"), CHANGE_ENTIRE_WORD_LIST("Change Entire Word List"),
		CHANGE_PARTIAL_WORD_LIST("Change Partial Word List"), HELP("HELP");

		String _screenName;

		private AppScreen(String screenName) {
			_screenName = screenName;
		}

		public String toString() {
			return _screenName;
		}
	}

	private CardLayout _cardLayout;

	/*
	 * The interchangeable screens
	 */
	private MainMenuScreen _mainMenuScreen;
	private QuizScreen _newQuizScreen;
	private ScoreboardScreen _scoreboardScreen;
	private ChangeEntireWordListScreen _changeEntireWordListScreen;
	private ChangePartialWordListScreen _changePartialWordListScreen;
	private HelpScreen _helpScreen;

	public VoxSpellApp() {
		buildGUI();
		_cardLayout.show(this, "Main Menu"); //begin with showing the Main Menu screen		
	}

	/**
	 * Build up the composition of GUI components that allows VoxSpellApp be in control for the screens' interchange.
	 */
	private void buildGUI() {
		//Using card layout to accommodate for interchangeable screens
		_cardLayout = new CardLayout();
		this.setLayout(_cardLayout);

		//Instantiate the screens and 
		//setup the association between VoxSpellApp with the screens it owns.
		//This is so that each screen is aware of which JPanel object is its owner.
		_mainMenuScreen = new MainMenuScreen(this);
		_newQuizScreen = new QuizScreen(this);
		_scoreboardScreen = new ScoreboardScreen(this);
		_changeEntireWordListScreen = new ChangeEntireWordListScreen(this);
		_changePartialWordListScreen = new ChangePartialWordListScreen(this);
		_helpScreen = new HelpScreen(this);

		//Add the screens to the application
		this.add(_mainMenuScreen.getPanel(), AppScreen.MAIN_MENU.toString());
		this.add(_newQuizScreen.getPanel(), AppScreen.NEW_SPELLING_QUIZ.toString());
		this.add(_scoreboardScreen.getPanel(), AppScreen.SCOREBOARD.toString());
		this.add(_changeEntireWordListScreen.getPanel(), AppScreen.CHANGE_ENTIRE_WORD_LIST.toString());
		this.add(_changePartialWordListScreen.getPanel(), AppScreen.CHANGE_PARTIAL_WORD_LIST.toString());
		this.add(_helpScreen.getPanel(), AppScreen.HELP.toString());		
	}

	private static void createAndShowGUI() {
		JFrame frame = new JFrame("VOXSPELL");
		JComponent contentPane = new VoxSpellApp();
		frame.setContentPane(contentPane);
		frame.setSize(new Dimension(800, 600));
		frame.setLocationRelativeTo(null); //puts window at the center of screen
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true); //Display the window		
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				createAndShowGUI();
			}
		});
	}

	/**
	 * The screens which VoxSpellApp owns may request the VoxSpellApp object to change from the current screen to another screen
	 * by calling this method.
	 * @param screen
	 */
	public void changeScreen(AppScreen screen) {
		switch(screen) {
		case CHANGE_ENTIRE_WORD_LIST:
			_cardLayout.show(this, AppScreen.CHANGE_ENTIRE_WORD_LIST.toString());
			_changeEntireWordListScreen.start();
			break;
		case CHANGE_PARTIAL_WORD_LIST:
			_cardLayout.show(this, AppScreen.CHANGE_PARTIAL_WORD_LIST.toString());
			break;
		case HELP:
			_cardLayout.show(this, AppScreen.HELP.toString());
			break;
		case MAIN_MENU:
			_cardLayout.show(this, AppScreen.MAIN_MENU.toString());
			break;
		case NEW_SPELLING_QUIZ:
			_cardLayout.show(this, AppScreen.NEW_SPELLING_QUIZ.toString());
			_newQuizScreen.start();
			break;
		case SCOREBOARD:
			_cardLayout.show(this, AppScreen.SCOREBOARD.toString());
			_scoreboardScreen.updateScreen();
			break;
		}
	}

}