package voxspell.wordlist;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import voxspell.VoxSpellApp;
import voxspell.VoxSpellApp.AppScreen;
import voxspell.wordlist.entirelist.ChangeEntireWordListModel;

/**
 * This class represents the main model that handles the logic for changing word lists.
 * The logic handled by this class is at a higher level of abstraction than the other models (ChangeEntireWordListModel and ChangePartialWordListModel)
 * The logic mainly manages which lower-level model to use given the user's input.
 * @author echa232
 *
 */
public class ChangeWordListModel {

	private VoxSpellApp _owner;

	/**
	 * Starts the change-word-list-process by asking what type of change the user wants to make first 
	 */
	public void start(VoxSpellApp screenOwner) {
		_owner = screenOwner;
		showChooseChangeMethodPopUp(); 
	}

	/**
	 * Prompts the user to choose what type of change the user wants to make (i.e. changing the entire list / part of the list)
	 */
	private void showChooseChangeMethodPopUp() {
		Object[] ChangeMethodOptions = {"Change the entire list", "Change some words only"};
		int changeOption = JOptionPane.showOptionDialog(null, "How would you like to change the word list?", 
				"Change word list", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, 
				null, ChangeMethodOptions, null);
		
		switch(changeOption) {	
		case JOptionPane.YES_OPTION:
			showChooseNewListDialog(); 
			break;			
		case JOptionPane.NO_OPTION:
			_owner.changeScreen(AppScreen.CHANGE_PARTIAL_WORD_LIST);
			break;
		default:
			_owner.changeScreen(AppScreen.MAIN_MENU);
			break;		
		}
	}
	
	/**
	 * If the user chooses to change the entire list, follow up with prompting the user to select the file that contains the new word list
	 * Also gives the user a warning that changing the entire list will remove all previous words, progress and scores.
	 */
	private void showChooseNewListDialog() {
		JDialog dialog = new JDialog();

		JEditorPane txtWarning = new JEditorPane();
		txtWarning.setContentType("text/html");
		txtWarning.setText("<html>"
				+ "<p><font face=\"Garuda\" size=\"6\" color=\"red\"><b><i><u>Warning!</u></i></b></font></p>"
				+ "<p><font face=\"Garuda\" size=\"5\"><b><i>"
				+ "Changing the entire word list means all previous words, progress and scores will be <u>removed</u>."
				+ "</i></b></font></p>"
				+ "</html>");

		JFileChooser fileChooser = new JFileChooser();

		fileChooser.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand() == JFileChooser.CANCEL_SELECTION) {
					dialog.dispose();
					_owner.changeScreen(AppScreen.MAIN_MENU);
				} 
				else if (e.getActionCommand() == JFileChooser.APPROVE_SELECTION) {
					File newListFile = fileChooser.getSelectedFile();
					dialog.dispose();
					
					ChangeEntireWordListModel.setNewFile(newListFile);
					_owner.changeScreen(AppScreen.CHANGE_ENTIRE_WORD_LIST);
				}
			}
		});

		dialog.add(txtWarning, BorderLayout.NORTH);
		dialog.add(fileChooser, BorderLayout.SOUTH);

		dialog.setBackground(Color.WHITE);
		
		//If user closes the dialog without choosing a file, then return to main menu
		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		dialog.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				dialog.dispose();
				_owner.changeScreen(AppScreen.MAIN_MENU);
			}
		});

		dialog.setTitle("Choose new word list file");
		dialog.pack();
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
	}
}
