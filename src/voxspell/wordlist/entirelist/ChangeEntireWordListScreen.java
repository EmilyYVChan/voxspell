package voxspell.wordlist.entirelist;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;

import voxspell.Screen;
import voxspell.VoxSpellApp;
import voxspell.VoxSpellApp.AppScreen;
import voxspell.newquiz.HiddenQuizFilesModel.QuizCategory;
import voxspell.wordlist.ChangeWordListException;

/**
 * This class represents the screen object that is shown when the user wants to change the entire word list
 * Applies the MVC design pattern, where the screen acts as the View-Controller
 * Its corresponding model is an instance of the ChangeEntireWordListModel class.
 * @author echa232
 *
 */
@SuppressWarnings("serial")
public class ChangeEntireWordListScreen extends Screen {

	JButton _btnReturn;
	JLabel _lblProgress;
	JProgressBar _progressBar;
	JTextArea _progressTxtArea;

	private ChangeEntireWordListModel _model;

	public ChangeEntireWordListScreen(VoxSpellApp screenOwner) {
		super(screenOwner);
	}

	@Override
	protected void buildScreenGUI() {
		/*
		 * Build the GUI section that contains the title and sub-heading
		 */
		JLabel lblChangeSpellingList = new JLabel("Change Word List");
		lblChangeSpellingList.setBounds(15, 15, 500, 50);
		lblChangeSpellingList.setForeground(Color.BLACK);
		lblChangeSpellingList.setFont(new Font("Garuda", Font.BOLD | Font.ITALIC, 32));
		lblChangeSpellingList.setBackground(Color.WHITE);
		_screenPanel.add(lblChangeSpellingList);
		
		JLabel lblSubHeading = new JLabel("Changing the entire word list");
		lblSubHeading.setForeground(Color.BLACK);
		lblSubHeading.setFont(new Font("Garuda", Font.BOLD | Font.ITALIC, 24));
		lblSubHeading.setBackground(Color.WHITE);
		lblSubHeading.setBounds(15, 65, 500, 50);
		_screenPanel.add(lblSubHeading);

		/*
		 * Build the GUI section that contains the progress bar and label
		 */
		_lblProgress = new JLabel("Processing new word list...");
		_lblProgress.setFont(new Font("Garuda", Font.BOLD | Font.ITALIC, 18));
		_lblProgress.setForeground(Color.BLACK);
		_lblProgress.setBackground(Color.WHITE);
		_lblProgress.setBounds(15, 130, 750, 40);
		_screenPanel.add(_lblProgress);
		
		_progressBar = new JProgressBar();
		_progressBar.setIndeterminate(true);
		_progressBar.setToolTipText("This is your current overall progress in this category!");
		_progressBar.setForeground(new Color(255, 204, 0));
		_progressBar.setFont(new Font("Garuda", Font.BOLD | Font.ITALIC, 14));
		_progressBar.setBounds(40, 170, 700, 20);
		_screenPanel.add(_progressBar);
		
		/*
		 * Build the GUI section that contains the help/instructions
		 */
		JScrollPane progressTxtScrollPane = new JScrollPane();
		progressTxtScrollPane.setBounds(20, 210, 750, 270);
		progressTxtScrollPane.setBorder(new TitledBorder(null, "Progress details", TitledBorder.LEADING, TitledBorder.TOP, new Font("Garuda", Font.BOLD | Font.ITALIC, 18), Color.BLACK));
		progressTxtScrollPane.setBackground(Color.WHITE);		
		_screenPanel.add(progressTxtScrollPane);
		
		_progressTxtArea = new JTextArea();
		_progressTxtArea.setEditable(false);
		_progressTxtArea.setForeground(Color.BLACK);
		_progressTxtArea.setToolTipText("Details about the progress of changing the entire word list!");
		_progressTxtArea.setLineWrap(true);
		_progressTxtArea.setWrapStyleWord(true);
		_progressTxtArea.setFont(new Font("Garuda", Font.PLAIN, 16));
		_progressTxtArea.setText("");
		progressTxtScrollPane.setViewportView(_progressTxtArea);
		
		/*
		 * Build the GUI section that allows user to return to main menu
		 */
		_btnReturn = new JButton("Return to Main Menu");
		_btnReturn.setToolTipText("Click this to return to the main menu!");
		_btnReturn.setForeground(Color.BLACK);
		_btnReturn.setFont(new Font("Garuda", Font.BOLD | Font.ITALIC, 18));
		_btnReturn.setBorder(new MatteBorder(1, 5, 1, 1, (Color) new Color(255, 204, 0)));
		_btnReturn.setBackground(Color.WHITE);
		_btnReturn.setBounds(280, 510, 250, 40);
		_screenPanel.add(_btnReturn);
		_btnReturn.setVisible(false); //User cannot return to main menu until file has finished processing.

	}

	@Override
	protected void setupGUIListeners() {
		_btnReturn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_owner.changeScreen(AppScreen.MAIN_MENU);
			}			
		});		
	}

	/**
	 * This method is called when the user wants to change the entire word list.
	 * A model is created and the model handles all of the logic relating to changing the word list.
	 * This method is responsible for catching any exceptions thrown by the model.
	 * Depending on the exception caught, the GUI view will change and show a slightly different view to to the user.
	 */
	public void start() {
		try {
			_model = new ChangeEntireWordListModel();
		} catch (ChangeWordListException e) {
			_progressTxtArea.setText("Sorry, an error occurred during processing. The word list remains unchanged.\n");
			
			switch(e._exceptionType) {
			case IncorrectlyFormattedFile:
				_progressTxtArea.append("Error: Selected file is not correctly formatted\n"
						+ "Please make sure there are list of words for the each of the categories "
						+ "(i.e. Adjectives, Adverbs, Nouns and Verbs). "
						+ "Please also make sure that the beginning of each category starts with a '%' sign before the category's name "
						+ "(e.g. %Adjectives or %Adverbs).");
				break;
			case InvalidCharacter:
				_progressTxtArea.append("Error: Invalid character(s) detected in a word in the selected file\n"
						+ "The word containing the invalid character(s) is: "
						+ e._errorWord
						+ "\nPlease remove the invalid character(s) in the file and try again.");
				break;
			}
			
			_progressBar.setIndeterminate(false);
			_lblProgress.setText("No progress available due to error");
			_btnReturn.setVisible(true);
			return;
		}
		
		//No exceptions caught so far, which means changing the entire word list is complete.
		_progressBar.setIndeterminate(false); 
		
		//Update progress panel
		_lblProgress.setText("Complete! Current word list has been successfully updated.");
		_progressBar.setValue(100);

		QuizCategory[] categories = QuizCategory.values();
		//Update JTextArea for successful transfer
		for (int i = 0; i < _model._allWords.size(); i++) {
			//First print out the category name, then print out all the new words that have been updated into the quiz files
			_progressTxtArea.append("\nThe following words have been added to the " + categories[i].toString() + " category: \n");
			for (int j = 0; j < _model._allWords.get(i).size(); j++) {
				_progressTxtArea.append(_model._allWords.get(i).get(j) + "\n");
			}
		}

		_progressTxtArea.append("\nNow you can enjoy new spelling quizzes with the new words!\n");
		_progressTxtArea.setCaretPosition(0);
		_btnReturn.setVisible(true);
	}
}
