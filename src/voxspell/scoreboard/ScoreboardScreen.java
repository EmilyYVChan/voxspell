package voxspell.scoreboard;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.MatteBorder;

import voxspell.Screen;
import voxspell.VoxSpellApp;
import voxspell.VoxSpellApp.AppScreen;
import voxspell.newquiz.HiddenQuizFilesModel;
import voxspell.newquiz.HiddenQuizFilesModel.QuizCategory;

@SuppressWarnings("serial")
public class ScoreboardScreen extends Screen {
	
	JComboBox<String> _categoryComboBox;
	JScrollPane _scoreScrollPane;
	JProgressBar _progressBar;
	
	JButton _btnClearScores;
	JButton _btnReturn;
	
	HiddenQuizFilesModel _filesModel; //model that is necessary for resetting the scores
	ScoreboardModel _currentScoreboardModel = new ScoreboardModel(QuizCategory.Adjectives); //Default StatsModel is of the adjectives category
	JTable _statsTable;
	
	public ScoreboardScreen(VoxSpellApp screenOwner) {
		super(screenOwner);
		_filesModel = HiddenQuizFilesModel.getInstance();
	}

	@Override
	protected void buildScreenGUI() {
		
		/*
		 * Build the GUI section for the screen's title heading
		 */
		JLabel lblScoreboard = new JLabel("Scoreboard");
		lblScoreboard.setBackground(Color.WHITE);
		lblScoreboard.setForeground(Color.BLACK);
		lblScoreboard.setFont(new Font("Garuda", Font.BOLD | Font.ITALIC, 32));
		lblScoreboard.setBounds(15, 15, 200, 50);
		_screenPanel.add(lblScoreboard);
		
		/*
		 * Build the GUI section that allows the user to choose which category of scores to see
		 */
		JLabel lblShowScoresFor = new JLabel("Show scores for : ");
		lblShowScoresFor.setToolTipText("Choose a category to see its scores!");
		lblShowScoresFor.setForeground(Color.BLACK);
		lblShowScoresFor.setBackground(Color.WHITE);
		lblShowScoresFor.setFont(new Font("Garuda", Font.BOLD | Font.ITALIC, 18));
		lblShowScoresFor.setBounds(220, 69, 210, 40);
		_screenPanel.add(lblShowScoresFor);
		
		String[] categories = {"Adjectives", "Adverbs", "Nouns", "Verbs"};
		_categoryComboBox = new JComboBox<String>(categories);
		_categoryComboBox.setToolTipText("Choose a category to see its scores!");
		_categoryComboBox.setFont(new Font("Garuda", Font.BOLD | Font.ITALIC, 18));
		_categoryComboBox.setBackground(Color.WHITE);
		_categoryComboBox.setBounds(430, 69, 150, 40);
		_screenPanel.add(_categoryComboBox);
		
		/*
		 * Build the GUI section that shows the user's overall progress in the selected category
		 */
		JLabel lblOverallProgress = new JLabel("Overall Progress :");
		lblOverallProgress.setToolTipText("This is your current overall progress in this category!");
		lblOverallProgress.setBackground(Color.WHITE);
		lblOverallProgress.setFont(new Font("Garuda", Font.BOLD | Font.ITALIC, 18));
		lblOverallProgress.setForeground(Color.BLACK);
		lblOverallProgress.setBounds(40, 120, 300, 40);
		_screenPanel.add(lblOverallProgress);
		
		_progressBar = new JProgressBar(0,100);
		_progressBar.setStringPainted(true);
		_progressBar.setFont(new Font("Garuda", Font.BOLD | Font.ITALIC, 14));
		_progressBar.setToolTipText("This is your current overall progress in this category!");
		_progressBar.setForeground(new Color(0, 153, 255));
		_progressBar.setBounds(40, 160, 700, 20);
		_screenPanel.add(_progressBar);
		
		/*
		 * Build the GUI section that shows the scrollable table of the user's score details of the selected category
		 */		
		_statsTable = new JTable(_currentScoreboardModel);
		_scoreScrollPane = new JScrollPane(_statsTable);	
		_scoreScrollPane.setBounds(20, 210, 750, 270);
		_screenPanel.add(_scoreScrollPane);

		updateScreen(); //updates both the progress bar and table with the correct values
		
		/*
		 * Build the GUI section that allows user to clear scores in current category or return to main menu
		 */		
		_btnClearScores = new JButton("Clear Scores");
		_btnClearScores.setToolTipText("Clears all current scores in this category!");
		_btnClearScores.setForeground(Color.BLACK);
		_btnClearScores.setFont(new Font("Garuda", Font.BOLD | Font.ITALIC, 18));
		_btnClearScores.setBorder(new MatteBorder(1, 5, 1, 1, (Color) new Color(0, 153, 255)));
		_btnClearScores.setBackground(Color.WHITE);
		_btnClearScores.setBounds(100, 510, 250, 40);
		_screenPanel.add(_btnClearScores);
		
		_btnReturn = new JButton("Return to Main Menu");
		_btnReturn.setToolTipText("Click this to return to the main menu!");
		_btnReturn.setForeground(Color.BLACK);
		_btnReturn.setFont(new Font("Garuda", Font.BOLD | Font.ITALIC, 18));
		_btnReturn.setBorder(new MatteBorder(1, 5, 1, 1, (Color) new Color(0, 153, 255)));
		_btnReturn.setBackground(Color.WHITE);
		_btnReturn.setBounds(450, 510, 250, 40);
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
		
		_btnClearScores.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int clear = JOptionPane.showConfirmDialog(null, "Are you sure you want to clear your scores?\nNote: Only scores of this category will be cleared.", "Confirm Clear Scores", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (clear == JOptionPane.YES_OPTION) {
					switch (_categoryComboBox.getSelectedItem().toString()) {
					case "Adjectives":
						_filesModel.reset(QuizCategory.Adjectives);
						_currentScoreboardModel = new ScoreboardModel(QuizCategory.Adjectives);
						break;
					case "Adverbs":
						_filesModel.reset(QuizCategory.Adverbs);
						_currentScoreboardModel = new ScoreboardModel(QuizCategory.Adverbs);
						break;
					case "Nouns":
						_filesModel.reset(QuizCategory.Nouns);
						_currentScoreboardModel = new ScoreboardModel(QuizCategory.Nouns);
						break;
					case "Verbs":
						_filesModel.reset(QuizCategory.Verbs);
						_currentScoreboardModel = new ScoreboardModel(QuizCategory.Verbs);
						break;
					}
					
					updateScreen();
					JOptionPane.showMessageDialog(null, "Scores cleared.");
					
				}
			}			
		});
		
		_categoryComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
					updateScreen();
			}			
		});
	}
	
	/**
	 * Updates the screen with the latest scores
	 */
	public void updateScreen() {
		switch (_categoryComboBox.getSelectedItem().toString()) {
		case "Adjectives":
			_currentScoreboardModel = new ScoreboardModel(QuizCategory.Adjectives);
			break;
		case "Adverbs":
			_currentScoreboardModel = new ScoreboardModel(QuizCategory.Adverbs);
			break;
		case "Nouns":
			_currentScoreboardModel = new ScoreboardModel(QuizCategory.Nouns);
			break;
		case "Verbs":
			_currentScoreboardModel = new ScoreboardModel(QuizCategory.Verbs);
			break;
		}
		
		//Refresh table and reformat it in desired style
		_statsTable = new JTable(_currentScoreboardModel);			
		formatTableStyle();
		
		_progressBar.setValue(_currentScoreboardModel.getCategoryOverallRate());
		
		if (_currentScoreboardModel.getCategoryOverallRate() == 100) {
			notifyCategoryComplete();
		}
	}
	
	/**
	 * Formatting the score table in the desired GUI visual design and layout
	 */
	private void formatTableStyle() {
		_statsTable.getColumnModel().getColumn(0).setPreferredWidth(100);
		_statsTable.getColumnModel().getColumn(1).setPreferredWidth(100);
		_statsTable.getColumnModel().getColumn(2).setPreferredWidth(100);
		_statsTable.getColumnModel().getColumn(3).setPreferredWidth(100);
		_statsTable.getColumnModel().getColumn(4).setPreferredWidth(100);
		_statsTable.getTableHeader().setFont(new Font("Garuda", Font.BOLD | Font.ITALIC, 16));
		_statsTable.getTableHeader().setBackground(new Color(0, 153, 255));
		_statsTable.setFont(new Font("Garuda", Font.PLAIN, 14));
		_statsTable.getTableHeader().setForeground(Color.WHITE);
		_statsTable.setBackground(Color.WHITE);
		_statsTable.setAutoscrolls(true);
		_statsTable.setFillsViewportHeight(true);
		_scoreScrollPane.setViewportView(_statsTable);
	}
	
	/**
	 * This method is called when the user has achieved 100% overall progress in the category
	 * Shows a congratulatory pop up message to the user.
	 */
	private void notifyCategoryComplete() {
		JOptionPane.showMessageDialog(null, "Congratulations!\n"
				+ "You have reached 100% for all words in this category!", 
				"Achievement: Category complete!", JOptionPane.INFORMATION_MESSAGE);
	}
}
