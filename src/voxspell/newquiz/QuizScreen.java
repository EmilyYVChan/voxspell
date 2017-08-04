package voxspell.newquiz;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.MatteBorder;

import voxspell.Screen;
import voxspell.VoxSpellApp;
import voxspell.VoxSpellApp.AppScreen;
import voxspell.newquiz.HiddenQuizFilesModel.QuizCategory;
import voxspell.sounds.SpeechAndSoundModel;
import voxspell.sounds.SpeechAndSoundModel.Voice;
import voxspell.videoplayer.VideoPlayer;

/**
 * This class represents the screen object that is shown when the user wants to play a new spelling quiz
 * Applies the MVC design pattern, where the screen acts as the View-Controller
 * Its corresponding models are 
 * 1. an instance of the QuizModel class
 * 2. an instance of the SpeechAndSoundModel class 
 * @author echa232
 *
 */
@SuppressWarnings("serial")
public class QuizScreen extends Screen implements QuizModelListener {

	/*
	 * Models associated with this View-Controller
	 */
	private QuizModel _quizModel;
	private SpeechAndSoundModel _speechAndSoundModel;

	JButton _btnCheckSpelling;
	JButton _btnRelistenToWord;
	JButton _btnReturn;
	JTextField _spellWordInputField;
	JLabel _lblSpellWord;
	JComboBox<String> _voiceComboBox;
	JTextPane _msgTextPane;

	ArrayList<JLabel> _visualProgressLabels;

	ImageIcon _iconCheckMark;
	ImageIcon _iconCrossMark;
	ImageIcon _iconNeutral;

	public QuizScreen(VoxSpellApp screenOwner) {
		super(screenOwner);
		_quizModel = new QuizModel();
		_quizModel.addQuizModelListener(this);
		_speechAndSoundModel = SpeechAndSoundModel.getInstance();
	}

	@Override
	protected void buildScreenGUI() {

		/*
		 * Build the GUI section for the screen's title heading
		 */
		JLabel lblSpellingQuiz = new JLabel("Spelling Quiz");
		lblSpellingQuiz.setFont(new Font("Garuda", Font.BOLD | Font.ITALIC, 32));
		lblSpellingQuiz.setBounds(15, 15, 500, 50);
		_screenPanel.add(lblSpellingQuiz);

		/*
		 * Build the GUI section for visual overview of user's spelling game progress. Use lots of JLabels
		 */
		try {
			BufferedImage imageCheckMark = ImageIO.read(new File("./.img/new_spelling_quiz/Quiz_CheckMark_70px.png"));
			BufferedImage imageCrossMark = ImageIO.read(new File("./.img/new_spelling_quiz/Quiz_CrossMark_70px.png"));
			BufferedImage imageNeutral = ImageIO.read(new File("./.img/new_spelling_quiz/Quiz_Neutral_70px.png"));

			_iconCheckMark = new ImageIcon(imageCheckMark);
			_iconCrossMark = new ImageIcon(imageCrossMark);
			_iconNeutral = new ImageIcon(imageNeutral);
		} catch (IOException e) {
			e.printStackTrace();
		}

		_visualProgressLabels = new ArrayList<JLabel>(); //ArrayList storing all the references to the 10 JLabels

		JLabel lblQuiz_1 = new JLabel();
		lblQuiz_1.setIcon(_iconNeutral);
		lblQuiz_1.setBackground(Color.WHITE);
		lblQuiz_1.setBounds(40, 82, 70, 70);
		_screenPanel.add(lblQuiz_1);
		_visualProgressLabels.add(lblQuiz_1);

		JLabel lblQuiz_2 = new JLabel();
		lblQuiz_2.setIcon(_iconNeutral);
		lblQuiz_2.setBackground(Color.WHITE);
		lblQuiz_2.setBounds(110, 82, 70, 70);
		_screenPanel.add(lblQuiz_2);
		_visualProgressLabels.add(lblQuiz_2);

		JLabel lblQuiz_3 = new JLabel();
		lblQuiz_3.setIcon(_iconNeutral);
		lblQuiz_3.setBackground(Color.WHITE);
		lblQuiz_3.setBounds(180, 82, 70, 70);
		_screenPanel.add(lblQuiz_3);
		_visualProgressLabels.add(lblQuiz_3);

		JLabel lblQuiz_4 = new JLabel();
		lblQuiz_4.setIcon(_iconNeutral);
		lblQuiz_4.setBackground(Color.WHITE);
		lblQuiz_4.setBounds(250, 82, 70, 70);
		_screenPanel.add(lblQuiz_4);
		_visualProgressLabels.add(lblQuiz_4);

		JLabel lblQuiz_5 = new JLabel();
		lblQuiz_5.setIcon(_iconNeutral);
		lblQuiz_5.setBackground(Color.WHITE);
		lblQuiz_5.setBounds(320, 82, 70, 70);
		_screenPanel.add(lblQuiz_5);
		_visualProgressLabels.add(lblQuiz_5);

		JLabel lblQuiz_6 = new JLabel();
		lblQuiz_6.setIcon(_iconNeutral);
		lblQuiz_6.setBackground(Color.WHITE);
		lblQuiz_6.setBounds(390, 82, 70, 70);
		_screenPanel.add(lblQuiz_6);
		_visualProgressLabels.add(lblQuiz_6);

		JLabel lblQuiz_7 = new JLabel();
		lblQuiz_7.setIcon(_iconNeutral);
		lblQuiz_7.setBackground(Color.WHITE);
		lblQuiz_7.setBounds(460, 82, 70, 70);
		_screenPanel.add(lblQuiz_7);
		_visualProgressLabels.add(lblQuiz_7);

		JLabel lblQuiz_8 = new JLabel();
		lblQuiz_8.setIcon(_iconNeutral);
		lblQuiz_8.setBackground(Color.WHITE);
		lblQuiz_8.setBounds(530, 82, 70, 70);
		_screenPanel.add(lblQuiz_8);
		_visualProgressLabels.add(lblQuiz_8);

		JLabel lblQuiz_9 = new JLabel();
		lblQuiz_9.setIcon(_iconNeutral);
		lblQuiz_9.setBackground(Color.WHITE);
		lblQuiz_9.setBounds(600, 82, 70, 70);
		_screenPanel.add(lblQuiz_9);
		_visualProgressLabels.add(lblQuiz_9);

		JLabel lblQuiz_10 = new JLabel();
		lblQuiz_10.setIcon(_iconNeutral);
		lblQuiz_10.setBackground(Color.WHITE);
		lblQuiz_10.setBounds(670, 82, 70, 70);
		_screenPanel.add(lblQuiz_10);
		_visualProgressLabels.add(lblQuiz_10);

		/*
		 * Build the GUI section for printing output of useful respond messages to the user
		 */
		_msgTextPane = new JTextPane();
		_msgTextPane.setContentType("text/html");
		_msgTextPane.setToolTipText("A helpful hint may sometimes appear!");
		_msgTextPane.setForeground(Color.BLACK);
		_msgTextPane.setEditable(false);
		_msgTextPane.setBackground(Color.WHITE);
		_msgTextPane.setBounds(15, 173, 770, 80);
		_screenPanel.add(_msgTextPane);

		/*
		 * Build the GUI section for the functionality of spelling the word
		 */
		_lblSpellWord = new JLabel("Spell word 1 of 10 : ");
		_lblSpellWord.setToolTipText("Spell the word you heard here!");
		_lblSpellWord.setSize(new Dimension(200, 200));
		_lblSpellWord.setForeground(Color.BLACK);
		_lblSpellWord.setFont(new Font("Garuda", Font.BOLD | Font.ITALIC, 18));
		_lblSpellWord.setBackground(Color.WHITE);
		_lblSpellWord.setBounds(140, 270, 210, 40);
		_screenPanel.add(_lblSpellWord);

		_spellWordInputField = new JTextField();
		_spellWordInputField.setFont(new Font("Garuda", Font.BOLD | Font.ITALIC, 18));
		_spellWordInputField.setBounds(350, 270, 300, 40);
		_spellWordInputField.setColumns(10);
		_screenPanel.add(_spellWordInputField);

		/*
		 * Build the GUI section that contains functionality for user to relisten word & check spelling
		 */
		_btnRelistenToWord = new JButton("Relisten to Word");
		_btnRelistenToWord.setBorder(new MatteBorder(1, 5, 1, 1, (Color) new Color(0, 102, 0)));
		_btnRelistenToWord.setBackground(Color.WHITE);
		_btnRelistenToWord.setToolTipText("Click this to hear the word again!");
		_btnRelistenToWord.setForeground(Color.BLACK);
		_btnRelistenToWord.setFont(new Font("Garuda", Font.BOLD | Font.ITALIC, 18));
		_btnRelistenToWord.setBounds(140, 337, 190, 40);
		_screenPanel.add(_btnRelistenToWord);

		_btnCheckSpelling = new JButton("Check Spelling");
		_btnCheckSpelling.setToolTipText("Check to see if you've spelt the word correctly!");
		_btnCheckSpelling.setForeground(Color.BLACK);
		_btnCheckSpelling.setFont(new Font("Garuda", Font.BOLD | Font.ITALIC, 18));
		_btnCheckSpelling.setBorder(new MatteBorder(1, 5, 1, 1, (Color) new Color(0, 102, 0)));
		_btnCheckSpelling.setBackground(Color.WHITE);
		_btnCheckSpelling.setBounds(460, 337, 190, 40);
		_screenPanel.add(_btnCheckSpelling);

		/*
		 * Build the GUI section that allows the user to change Festival's voice
		 */
		JLabel lblCurrentSpeechVoice = new JLabel("Current Speech Voice: ");
		lblCurrentSpeechVoice.setSize(new Dimension(200, 200));
		lblCurrentSpeechVoice.setToolTipText("To change voices, pick a different voice from the drop down menu!");
		lblCurrentSpeechVoice.setForeground(Color.BLACK);
		lblCurrentSpeechVoice.setBackground(Color.WHITE);
		lblCurrentSpeechVoice.setFont(new Font("Garuda", Font.BOLD | Font.ITALIC, 18));
		lblCurrentSpeechVoice.setBounds(140, 437, 300, 40);
		_screenPanel.add(lblCurrentSpeechVoice);

		String[] voiceOptions = {"American", "New Zealand"};
		_voiceComboBox = new JComboBox<String>(voiceOptions);
		_voiceComboBox.setBackground(Color.WHITE);
		_voiceComboBox.setToolTipText("Choose a different voice!");
		_voiceComboBox.setForeground(Color.BLACK);
		_voiceComboBox.setFont(new Font("Garuda", Font.BOLD | Font.ITALIC, 18));
		_voiceComboBox.setBounds(430, 437, 220, 40);
		_screenPanel.add(_voiceComboBox);

		/*
		 * Build the GUI section that allows user to return to main menu
		 */
		_btnReturn = new JButton("Return to Main Menu");
		_btnReturn.setToolTipText("Click this to return to the main menu!");
		_btnReturn.setForeground(Color.BLACK);
		_btnReturn.setFont(new Font("Garuda", Font.BOLD | Font.ITALIC, 18));
		_btnReturn.setBorder(new MatteBorder(1, 5, 1, 1, (Color) new Color(0, 102, 0)));
		_btnReturn.setBackground(Color.WHITE);
		_btnReturn.setBounds(280, 510, 250, 40);
		_screenPanel.add(_btnReturn);

		/*
		 * Build the GUI section that is a separator line for aesthetic purposes
		 */
		JSeparator separator = new JSeparator();
		separator.setForeground(new Color(0, 102, 0));
		separator.setBounds(0, 412, 800, 2);
		_screenPanel.add(separator);
	}	

	protected void setupGUIListeners() {
		_btnCheckSpelling.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {				
				if (_spellWordInputField.getText() == null || _spellWordInputField.getText().length() == 0) {
					_msgTextPane.setText(
							"<html>"
									+ "<p style = \"text-align:center;\">"
									+ "<font face=\"Garuda\" size=\"5\">"
									+ "<b><i>"
									+ "Sorry, a blank answer is not accepted! Please enter a word."
									+ "</i></b>"
									+ "</font></p></html>");
					//won't be accepting a blank answer
				}							
				else {
					_msgTextPane.setText("");
					_quizModel.checkSpelling(_spellWordInputField.getText());
				}			
			}		
		});

		_spellWordInputField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (_spellWordInputField.getText() == null || _spellWordInputField.getText().length() == 0) {
					_msgTextPane.setText(
							"<html>"
									+ "<p style = \"text-align:center;\">"
									+ "<font face=\"Garuda\" size=\"5\">"
									+ "<b><i>"
									+ "Sorry, a blank answer is not accepted! Please enter a word."
									+ "</i></b>"
									+ "</font></p></html>");
					//won't be accepting a blank answer
				}							
				else {
					_msgTextPane.setText("");
					_quizModel.checkSpelling(_spellWordInputField.getText());
				}
			}		
		});

		_btnRelistenToWord.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_quizModel.relistenWord();			
			}		
		});

		_btnReturn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				returnToScreenOwnerMainScreen();
				resetForNextGame();
			}			
		});

		_voiceComboBox.addItemListener(new ItemListener() { //When user selects a different voice with the JComboBox
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getItemSelectable() instanceof JComboBox) {
					JComboBox<?> item = (JComboBox<?>) e.getItemSelectable();

					if (e.getStateChange() == ItemEvent.SELECTED) {
						if (item.getSelectedItem().equals("American")) {
							_speechAndSoundModel.changeVoice(Voice.AMERICAN);
						}
						else {
							_speechAndSoundModel.changeVoice(Voice.NEW_ZEALAND);
						}
					}
				}						
			}
		});

	}

	/**
	 * Starts a quiz session with prompting user to choose a category of quiz words
	 */
	public void start() {
		showChooseCategoryPopUp(); 
	}

	/**
	 * Prompts the user to choose a category of quiz words
	 */
	private void showChooseCategoryPopUp() {
		/*
		 * Create and show the pop up GUI
		 */
		JPanel popUpPanel = new JPanel();

		JLabel messageLbl = new JLabel("Choose a category:");
		String[] categories = {"Adjectives", "Adverbs", "Nouns", "Verbs"};
		JComboBox<String> categoryComboBox = new JComboBox<String>(categories);

		popUpPanel.add(messageLbl);
		popUpPanel.add(categoryComboBox);

		int option = JOptionPane.showConfirmDialog(null, popUpPanel, "Choose category", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

		/*
		 * Pass selected category to model
		 */
		if (option == JOptionPane.OK_OPTION) {
			String comboBoxSelection = categoryComboBox.getSelectedItem().toString();
			QuizCategory selectedCategory = null;

			switch (comboBoxSelection) {
			case "Adjectives":
				selectedCategory = QuizCategory.Adjectives;
				break;
			case "Adverbs":
				selectedCategory = QuizCategory.Adverbs;
				break;
			case "Nouns":
				selectedCategory = QuizCategory.Nouns;
				break;
			case "Verbs":
				selectedCategory = QuizCategory.Verbs;
				break;
			}

			_quizModel.startNewQuiz(selectedCategory);			
		}
		else { //User did not choose a category. Quiz does not begin. Returns to main menu
			returnToScreenOwnerMainScreen();
		}
	}

	/**
	 * Returns to the main menu
	 */
	private void returnToScreenOwnerMainScreen() {
		_owner.changeScreen(AppScreen.MAIN_MENU);
	}

	/**
	 * Updates the screen in a way that depends on the type of event fired by the QuizModel
	 */
	public void updateView(QuizModelEvent.EventType eventType) {
		switch(eventType) {
		/*
		 * User had given an invalid input
		 */
		case InvalidUserInput:
			_msgTextPane.setText(
					"<html>"
							+ "<p style = \"text-align:center;\">"
							+ "<font face=\"Garuda\" size=\"5\">"
							+ "<b><i>"
							+ "Invalid character(s) detected!\n"
							+ "Please enter alphabetical characters and at most one apostrophe character only."
							+ "</i></b>"
							+ "</font></p></html>");
			break;

			/*
			 * Show a reminder to the user to add some new words soon for a challenge
			 */
		case LowNumberOfUnfamiliarWords:
			_msgTextPane.setText(
					"<html>"
							+ "<p style = \"text-align:center;\">"
							+ "<font face=\"Garuda\" size=\"4.8\">"
							+ "<b><i>"
							+ "You're getting quite familiar with most of our words!<br>"
							+ "Perhaps try adding some new words soon for a more challenging quiz?"
							+ "</i></b>"
							+ "</font></p></html>");
			break;

			/*
			 * There aren't enough words in the current word list to start a quiz.
			 */
		case NotEnoughWordsInWordList:
			JOptionPane.showMessageDialog(null, "Sorry, there's not enough words to start a new quiz.\n"
					+ "Please add at least " + (10-_quizModel._numOfWordsInCategory) + " new quiz words to the " + _quizModel._category.toString() + " word list \n"
					+ "through the 'Change Word List' button in the main menu.", 
					"Error: Unable to start quiz", JOptionPane.ERROR_MESSAGE);
			returnToScreenOwnerMainScreen(); 
			break;

			/*
			 * One of 10 steps during a quiz game
			 */
		case NewWordInQuiz:
			_spellWordInputField.setText("");
			_lblSpellWord.setText("Spell word " + (_quizModel._currentWordCount) + " of 10 : ");
			break;

			/*
			 * The quiz game has ended. Proceed to show the quiz game's results and ask for user whether the user wants to play the end-of-game video
			 */
		case EndOfQuiz:
			StringBuffer message = new StringBuffer();
			int numOfWordsMasteredInGame = _quizModel._numOfWordsMastered;
			int numOfWordsQuizzed = (_quizModel._currentWordCount - 1);
			double overallQuizScore = numOfWordsMasteredInGame*1.0/numOfWordsQuizzed*1.0;
			if (overallQuizScore > 0.80) { //if user got a score of 80% or higher
				message.append("Well done!\n");
			}
			else if (overallQuizScore > 0.30) { //if user got a score of 30% or higher
				message.append("Good effort!\n");				
			}
			else { //user got < 30% score
				message.append("More practice needed!\n");
			}

			message.append("You spelt " + numOfWordsMasteredInGame + " out of " + numOfWordsQuizzed + " words correctly.\n");

			int option = JOptionPane.showConfirmDialog(null, message + "\nWould you like to play a quiz review video?", 
					"Game over", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
			if (option == JOptionPane.YES_OPTION) {
				//Plays video. Gives the list of game words to construct the video
				ArrayList<String> wordsForVideo = _quizModel._gameWords;
				VideoPlayer mediaPlayer = new VideoPlayer(wordsForVideo);
				mediaPlayer.playVideo();				
			}			

			returnToScreenOwnerMainScreen();
			resetForNextGame();
			break;

			/*
			 * User has spelt word correctly
			 */
		case SpeltWordCorrectly:
			//First get the current word count, as it corresponds to the label to update
			//As the user spelt the word correctly, update the label to show the check mark
			int labelNumToUpdateAsCorrect = _quizModel._currentWordCount - 1;
			JLabel labelToUpdateAsCorrect = _visualProgressLabels.get(labelNumToUpdateAsCorrect);
			labelToUpdateAsCorrect.setIcon(_iconCheckMark);
			break;

			/*
			 * User has spelt word correctly
			 */
		case SpeltWordWrongly:
			//First get the current word count, as it corresponds to the label to update
			//As the user spelt the word wrongly, update the label to show the cross mark
			int labelNumToUpdateAsWrong = _quizModel._currentWordCount - 1;
			JLabel labelToUpdateAsWrong = _visualProgressLabels.get(labelNumToUpdateAsWrong);
			labelToUpdateAsWrong.setIcon(_iconCrossMark);
			break;

			/*
			 * User had spelt word wrongly twice. Show the spelling of the word to the user.
			 */
		case FailedWord:
			//Show user the spelling of the word when the user has failed
			_msgTextPane.setText(
					"<html>"
							+ "<p style = \"text-align:center;\">"
							+ "<font face=\"Garuda\" size=\"5\">"
							+ "<b><i>"
							+ "The correct spelling of the word was: "
							+ "<u>" + _quizModel._currentWord + "</u>"
							+ "</i></b>"
							+ "</font></p></html>");
			break;

		}
	}

	/**
	 * Resets the necessary GUI components to be ready for a fresh new game next time.
	 */
	private void resetForNextGame() {
		//Reset input field and image labels for the next game.
		_spellWordInputField.setText("");

		for (int i = 0; i < _visualProgressLabels.size(); i++) {
			JLabel label = _visualProgressLabels.get(i);
			label.setIcon(_iconNeutral);
		}

		_msgTextPane.setText("");
	}
}
