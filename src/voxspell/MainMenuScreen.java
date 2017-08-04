package voxspell;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.border.MatteBorder;

import voxspell.VoxSpellApp.AppScreen;
import voxspell.wordlist.ChangeWordListModel;

/**
 * MainMenuScreen is the first screen that the user sees when running VoxSpellApp.
 * @author echa232
 */
@SuppressWarnings("serial")
public class MainMenuScreen extends Screen {
	
	/*
	 * Buttons visible on the screen
	 */
	JButton _btnNewSpellingQuiz;
	JButton _btnChangeWordList;
	JButton _btnScoreboard;
	JButton _btnHelp;
		
	public MainMenuScreen(VoxSpellApp screenOwner) {
		super(screenOwner);
	}

	@Override
	protected void buildScreenGUI() {	
		
		/*
		 * build GUI section that has the title logo and welcome message
		 */
		JLabel lblTitleLogo = new JLabel();
		lblTitleLogo.setIcon(new ImageIcon("./.img/main_menu/Title_Logo.png"));
		lblTitleLogo.setBounds(50, 50, 222, 222);
		_screenPanel.add(lblTitleLogo);
		
		JLabel lblTitleWelcome = new JLabel();
		lblTitleWelcome.setIcon(new ImageIcon("./.img/main_menu/Title_Welcome.png"));
		lblTitleWelcome.setBounds(280, 50, 480, 222);
		_screenPanel.add(lblTitleWelcome);
		
		/*
		 * build GUI section that has the menu option buttons and labels
		 */
		//New Spelling Quiz
		JLabel lblNewSpellingQuiz = new JLabel();
		lblNewSpellingQuiz.setToolTipText("Start a new spelling quiz!");
		lblNewSpellingQuiz.setIcon(new ImageIcon("./.img/main_menu/Icon_NewSpellingQuiz_100px_Colour.png"));
		lblNewSpellingQuiz.setBounds(25, 320, 111, 111);
		_screenPanel.add(lblNewSpellingQuiz);

		_btnNewSpellingQuiz = new JButton("New Spelling Quiz");
		_btnNewSpellingQuiz.setToolTipText("Start a new spelling quiz!");		
		_btnNewSpellingQuiz.setBorder(new MatteBorder(1, 5, 1, 1, (Color) new Color(0, 102, 0)));
		_btnNewSpellingQuiz.setForeground(Color.BLACK);
		_btnNewSpellingQuiz.setMargin(new Insets(2, 4, 2, 8));
		_btnNewSpellingQuiz.setBackground(Color.WHITE);
		_btnNewSpellingQuiz.setFont(new Font("Garuda", Font.BOLD | Font.ITALIC, 24));
		_btnNewSpellingQuiz.setBounds(150, 350, 220, 50);
		
		lblNewSpellingQuiz.setLabelFor(_btnNewSpellingQuiz);
		_screenPanel.add(_btnNewSpellingQuiz);
		
		//Score board
		JLabel lblScoreboard = new JLabel();
		lblScoreboard.setToolTipText("See your latest scores!");
		lblScoreboard.setIcon(new ImageIcon("./.img/main_menu/Icon_Scoreboard_100px_Colour.png"));
		lblScoreboard.setBounds(410, 320, 111, 111);
		_screenPanel.add(lblScoreboard);
		
		_btnScoreboard = new JButton("Scoreboard");
		_btnScoreboard.setToolTipText("See your latest scores!");
		_btnScoreboard.setBackground(Color.WHITE);
		_btnScoreboard.setForeground(Color.BLACK);
		_btnScoreboard.setFont(new Font("Garuda", Font.BOLD | Font.ITALIC, 24));
		_btnScoreboard.setBorder(new MatteBorder(1, 5, 1, 1, (Color) new Color(0, 153, 255)));
		_btnScoreboard.setBounds(540, 350, 220, 50);
		_screenPanel.add(_btnScoreboard);
		
		//Change word list
		JLabel lblChangeWordList = new JLabel();
		lblChangeWordList.setToolTipText("Change the current word list!");
		lblChangeWordList.setIcon(new ImageIcon("./.img/main_menu/Icon_ChangeWordList_100px_Colour.png"));
		lblChangeWordList.setBounds(25, 440, 111, 111);
		_screenPanel.add(lblChangeWordList);
		
		_btnChangeWordList = new JButton("Change Word List");
		_btnChangeWordList.setToolTipText("Change the current word list");
		_btnChangeWordList.setForeground(Color.BLACK);
		_btnChangeWordList.setFont(new Font("Garuda", Font.BOLD | Font.ITALIC, 24));
		_btnChangeWordList.setBorder(new MatteBorder(1, 5, 1, 1, (Color) Color.ORANGE));
		_btnChangeWordList.setBackground(Color.WHITE);
		_btnChangeWordList.setBounds(150, 465, 220, 50);

		lblChangeWordList.setLabelFor(_btnChangeWordList);
		_screenPanel.add(_btnChangeWordList);
		
		//Help
		JLabel lblHelp = new JLabel();
		lblHelp.setToolTipText("Need more help?");
		lblHelp.setIcon(new ImageIcon("./.img/main_menu/Icon_Help_100px_Colour.png"));
		lblHelp.setBounds(410, 440, 111, 111);
		_screenPanel.add(lblHelp);
		
		_btnHelp = new JButton("Help");
		_btnHelp.setToolTipText("Need more help?");
		_btnHelp.setForeground(new Color(0, 0, 0));
		_btnHelp.setFont(new Font("Garuda", Font.BOLD | Font.ITALIC, 24));
		_btnHelp.setBorder(new MatteBorder(1, 5, 1, 1, (Color) new Color(139, 0, 0)));
		_btnHelp.setBackground(Color.WHITE);
		_btnHelp.setBounds(540, 465, 220, 50);
		_screenPanel.add(_btnHelp);
		
	}	
	
	/**
	 * Implementation of Screen's abstract method.
	 * Adds listeners to each button on the screen so that each button will 
	 * send a request to the screen's owner (e.g. VoxSpellApp) to change screens accordingly.
	 */
	@Override
	protected void setupGUIListeners() {
		_btnNewSpellingQuiz.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_owner.changeScreen(AppScreen.NEW_SPELLING_QUIZ);
			}			
		});
		
		_btnScoreboard.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_owner.changeScreen(AppScreen.SCOREBOARD);
			}			
		});
		
		_btnChangeWordList.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ChangeWordListModel model = new ChangeWordListModel();
				model.start(_owner);
			}			
		});
		
		_btnHelp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_owner.changeScreen(AppScreen.HELP);
			}			
		});		
	}
}

