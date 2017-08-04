package voxspell;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.border.MatteBorder;

import voxspell.VoxSpellApp.AppScreen;

/**
 * This class represents the 'Help' screen in the VoxSpellApp.
 * @author echa232
 *
 */
@SuppressWarnings("serial")
public class HelpScreen extends Screen {
	
	JButton _btnReturn;
	
	//The text detailing help/instructions/tips that will be shown on the screen
	private static final String HELPTEXT =
			"<html>"
			+ "<p>◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆</p>"
			
			+ "<p style = \"text-align:center;\"><font face=\"Garuda\" size=\"6\"><b><i><u>"
				+ "Welcome to VOXSPELL!"
			+ "</u></i></b></font></p>"

			+ "<font face=\"Garuda\" size=\"5\"><b><i>"
				+ "In the main menu, there are several options to choose from:"
				+ "<ul>"
					+ "<li>New Spelling Quiz"
						+ "<ul>"
							+ "<li>Starts a new spelling quiz</li>"
							+ "<li>Before a new spelling quiz can start, pick a category of words (Adjectives, Adverbs, Nouns or Verbs) to be quizzed on.</li>"
							+ "<li>Each quiz consists of 10 quiz words.</li>"
							+ "<li>After hearing “Please spell [quiz word]”, type the quiz word into the text box next to “Spell word 1 of 10” and press the Enter keyboard key / click the “Check Spelling” button.</li>"
							+ "<li>You could change the voice’s accent in the drop-down menu next to “Current Speech Voice”. The accents currently available are ‘American’ and ‘New Zealand’ accents.</li>"
							+ "<li>You could relisten to the quiz word again for any number of times by clicking the “Relisten to Word” button.</li>"
							+ "<li>After spelling 10 words, you could choose to play a ‘quiz review’ video. The video shows the correct spelling for each word in the quiz.</li>"
						+ "</ul>"
					+ "</li>"
					+ "<li>Scoreboard"
						+ "<ul>"
							+ "<li>Shows your latest scores for each category of words.</li>"
							+ "<li>To see the scores for a particular category of words, choose the desired category from the drop-down menu next to “Show scores for”.</li>"
							+ "<li>The progress bar shows your overall progress of getting all the words in the category to 100%.</li>"
							+ "<li>The table shows for each word in the category:"
								+ "<ul>"
									+ "<li>the number of times you have mastered the word(i.e. spelt it correctly on the first try during a quiz).</li>"
									+ "<li>the number of times you have faulted the word (spelt it wrongly on the first try during a quiz).</li>"
									+ "<li>the number of times you have failed the word (spelt it wrongly after faulted).</li>"
									+ "<li>the accuracy % (overall score of the word)</li>"
								+ "</ul>"
							+ "<li>You could clear all the scores <u>in the currently selected category</u> by clicking the Clear Scores button.</li>"
						+ "</ul>"
					+ "</li>"
					+ "<li>Change Word List"
						+ "<ul>"
							+ "<li>Make changes to the word list.</li>"
							+ "<li>Firstly, a pop up will be shown to ask you whether you want to change the entire word list or just some words in the current word list.</li>"
							+ "<li>If you want to change the <u>entire</u> word list:"
								+ "<ul>"
									+ "<li>Pick the file containing the words that will be the new spelling list (for details on how the file should be formatted, please read the User Manual).</li>"
									+ "<li>Note: changing the entire word list means all previous words, progress and scores will be removed.</li>"
									+ "<li>The screen will show a progress bar running while the new word list is being processed.</li>"
									+ "<li>Once the word list has finished processing and the current word list is successfully updated, the ‘Progress Details’ will show what words have been added to each category.</li>"
									+ "<li>If an error occurred during processing, the ‘Progress Details’ will show what the error was and some helpful guidance to deal with the error.</li>"
								+ "</ul>"
							+ "</li>"
							+ "<li>If you want to change only <u>some</u> words in the current word list:"
								+ "<ul>"
									+ "<li>Pick a category of words to change.</li>"
									+ "<li>In the ‘Add’ table, type in a word to add to the category in any line.</li>"
									+ "<li>In the ‘Remove’ table, type in a word to remove from the category in any line.</li>"
									+ "<li>In each table, type in <u>at most one word per line</u>.</li>"
									+ "<li>The maximum number of words you can add or remove at one time is 12.</li>"
									+ "<li>If the word already exists in the category, then the word is not added.</li>"
									+ "<li>If the word doesn’t exist in the category, then the word is not removed.</li>"
									+ "<li>To make the changes effective, click the ‘Confirm Changes’ button.</li>"
									+ "<li>To discard any unconfirmed changes, click the ‘Cancel Changes’ button.</li>"
								+ "</ul>"
							+ "</li>"
						+ "</ul>"
					+ "</li>"
				+ "</ul>"
			+ "</i></b></font>"	
			+ "<p>◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆ ◆</p>"
			+ "</html>";
	
	
	public HelpScreen(VoxSpellApp screenOwner) {
		super(screenOwner);
	}

	@Override
	protected void buildScreenGUI() {
		
		/*
		 * Build the GUI section that contains the Help title
		 */
		JLabel lblHelp = new JLabel("Help");
		lblHelp.setForeground(Color.BLACK);
		lblHelp.setFont(new Font("Garuda", Font.BOLD | Font.ITALIC, 32));
		lblHelp.setBackground(Color.WHITE);
		lblHelp.setBounds(15, 15, 200, 50);
		_screenPanel.add(lblHelp);
		
		/*
		 * Build the GUI section that contains the help/instructions
		 */
		JEditorPane paneHelp = new JEditorPane();
		paneHelp.setContentType("text/html");
		paneHelp.setText(HELPTEXT);
		paneHelp.setEditable(false);
		paneHelp.setCaretPosition(0); //This is necessary to show the user the beginning of the text rather than the end of it.
		
		JScrollPane helpTxtScrollPane = new JScrollPane();
		helpTxtScrollPane.setBounds(25, 75, 750, 405);		
		helpTxtScrollPane.setViewportView(paneHelp);
		_screenPanel.add(helpTxtScrollPane);
		
		/*
		 * Build the GUI section that allows user to return to main menu
		 */
		_btnReturn = new JButton("Return to Main Menu");
		_btnReturn.setToolTipText("Click this to return to the main menu!");
		_btnReturn.setForeground(Color.BLACK);
		_btnReturn.setFont(new Font("Garuda", Font.BOLD | Font.ITALIC, 18));
		_btnReturn.setBorder(new MatteBorder(1, 5, 1, 1, (Color) new Color(139, 0, 0)));
		_btnReturn.setBackground(Color.WHITE);
		_btnReturn.setBounds(280, 510, 250, 40);
		_screenPanel.add(_btnReturn);
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

}
