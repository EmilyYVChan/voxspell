package voxspell.wordlist.partiallist;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;

import voxspell.Screen;
import voxspell.VoxSpellApp;
import voxspell.VoxSpellApp.AppScreen;

/**
 * This class represents the screen object that is shown when the user wants to change the parts of the current word list
 * Applies the MVC design pattern, where the screen acts as the View-Controller
 * Its corresponding models are:
 * 1. an instance of the ChangePartialWordListModel class
 * 2. Two instances of the ChangePartialWordListTableModel class, one for adding words, the other for removing words
 * @author echa232
 *
 */
@SuppressWarnings("serial")
public class ChangePartialWordListScreen extends Screen {

	JButton _btnConfirmChanges;
	JButton _btnCancelChanges;
	
	JComboBox<String> _categoryComboBox;

	private JTable _tableWordsToAdd;
	private JTable _tableWordsToRemove;

	private ChangePartialWordListTableModel _addWordsTableModel;
	private ChangePartialWordListTableModel _removeWordsTableModel;

	public ChangePartialWordListScreen(VoxSpellApp screenOwner) {
		super(screenOwner);
	}

	@Override
	protected void buildScreenGUI() {
		/*
		 * Build the GUI section that contains the title and sub-heading
		 */
		JLabel lblChangeSpellingList = new JLabel("Change Word List");
		lblChangeSpellingList.setForeground(Color.BLACK);
		lblChangeSpellingList.setFont(new Font("Garuda", Font.BOLD | Font.ITALIC, 32));
		lblChangeSpellingList.setBackground(Color.WHITE);
		lblChangeSpellingList.setBounds(15, 15, 350, 50);
		_screenPanel.add(lblChangeSpellingList);

		JLabel lblSubHeading = new JLabel("Changing some parts of the word list");
		lblSubHeading.setForeground(Color.BLACK);
		lblSubHeading.setFont(new Font("Garuda", Font.BOLD | Font.ITALIC, 24));
		lblSubHeading.setBackground(Color.WHITE);
		lblSubHeading.setBounds(15, 65, 600, 50);
		_screenPanel.add(lblSubHeading);

		/*
		 * Build the GUI section that allows the user to choose which category of words to modify
		 */
		JLabel lblChangeWordsIn = new JLabel("Change words in : ");
		lblChangeWordsIn.setToolTipText("Choose a category to change some of its words!");
		lblChangeWordsIn.setForeground(Color.BLACK);
		lblChangeWordsIn.setFont(new Font("Garuda", Font.BOLD | Font.ITALIC, 18));
		lblChangeWordsIn.setBackground(Color.WHITE);
		lblChangeWordsIn.setBounds(225, 127, 200, 40);
		_screenPanel.add(lblChangeWordsIn);

		String[] categories = {"Adjectives", "Adverbs", "Nouns", "Verbs"};
		_categoryComboBox = new JComboBox<String>(categories);
		_categoryComboBox.setToolTipText("Choose a category to change some of its words!");
		_categoryComboBox.setFont(new Font("Garuda", Font.BOLD | Font.ITALIC, 18));
		_categoryComboBox.setBackground(Color.WHITE);
		_categoryComboBox.setBounds(425, 127, 150, 40);
		_screenPanel.add(_categoryComboBox);

		/*
		 * Build the GUI section that allows the user to input words to add/remove
		 */
		JPanel panelAddWordsTable = new JPanel();
		panelAddWordsTable.setBackground(Color.WHITE);
		panelAddWordsTable.setBorder(new TitledBorder(null, "Add", TitledBorder.LEADING, TitledBorder.TOP, new Font("Garuda", Font.BOLD | Font.ITALIC, 24), new Color(0, 153, 51)));
		panelAddWordsTable.setBounds(20, 180, 360, 300);
		_screenPanel.add(panelAddWordsTable);
		panelAddWordsTable.setLayout(null);

		_addWordsTableModel = new ChangePartialWordListTableModel();
		_tableWordsToAdd = new JTable(_addWordsTableModel);
		_tableWordsToAdd.setBorder(null);
		_tableWordsToAdd.setAutoscrolls(false);
		_tableWordsToAdd.setBounds(15, 50, 330, 240);
		panelAddWordsTable.add(_tableWordsToAdd);
		_tableWordsToAdd.setForeground(new Color(0, 0, 0));
		_tableWordsToAdd.setFont(new Font("Garuda", Font.BOLD | Font.ITALIC, 18));
		_tableWordsToAdd.setRowHeight(20);
		_tableWordsToAdd.getColumnModel().getColumn(0).setPreferredWidth(300);
		_tableWordsToAdd.setToolTipText("These words will be ADDED to the current word list!");
		_addWordsTableModel.addTableModelListener(_tableWordsToAdd);

		JPanel panelRemoveWordsTable = new JPanel();
		panelRemoveWordsTable.setLayout(null);
		panelRemoveWordsTable.setBorder(new TitledBorder(null, "Remove", TitledBorder.LEADING, TitledBorder.TOP, new Font("Garuda", Font.BOLD | Font.ITALIC, 24), new Color(153, 0, 0)));
		panelRemoveWordsTable.setBackground(Color.WHITE);
		panelRemoveWordsTable.setBounds(400, 180, 360, 300);
		_screenPanel.add(panelRemoveWordsTable);

		_removeWordsTableModel = new ChangePartialWordListTableModel();
		_tableWordsToRemove = new JTable(_removeWordsTableModel);
		_tableWordsToRemove.getColumnModel().getColumn(0).setPreferredWidth(300);
		_tableWordsToRemove.setToolTipText("These words will be REMOVED from the current word list!");
		_tableWordsToRemove.setRowHeight(20);
		_tableWordsToRemove.setForeground(Color.BLACK);
		_tableWordsToRemove.setFont(new Font("Garuda", Font.BOLD | Font.ITALIC, 18));
		_tableWordsToRemove.setBorder(null);
		_tableWordsToRemove.setAutoscrolls(false);
		_tableWordsToRemove.setBounds(15, 50, 330, 240);
		_removeWordsTableModel.addTableModelListener(_tableWordsToRemove);
		panelRemoveWordsTable.add(_tableWordsToRemove);

		/*
		 * Build the GUI section that allows the user to confirm/cancel inputs
		 */
		_btnConfirmChanges = new JButton("Confirm Changes");
		_btnConfirmChanges.setToolTipText("Confirm and save changes!");
		_btnConfirmChanges.setForeground(Color.BLACK);
		_btnConfirmChanges.setFont(new Font("Garuda", Font.BOLD | Font.ITALIC, 18));
		_btnConfirmChanges.setBorder(new MatteBorder(1, 5, 1, 1, (Color) new Color(255, 204, 0)));
		_btnConfirmChanges.setBackground(Color.WHITE);
		_btnConfirmChanges.setBounds(100, 510, 250, 40);
		_screenPanel.add(_btnConfirmChanges);

		_btnCancelChanges = new JButton("Cancel Changes");
		_btnCancelChanges.setToolTipText("Cancel unsaved changes!");
		_btnCancelChanges.setForeground(Color.BLACK);
		_btnCancelChanges.setFont(new Font("Garuda", Font.BOLD | Font.ITALIC, 18));
		_btnCancelChanges.setBorder(new MatteBorder(1, 5, 1, 1, (Color) new Color(255, 204, 0)));
		_btnCancelChanges.setBackground(Color.WHITE);
		_btnCancelChanges.setBounds(450, 510, 250, 40);
		_screenPanel.add(_btnCancelChanges);
	}

	@Override
	protected void setupGUIListeners() {		
		_btnConfirmChanges.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ArrayList<String> wordsToAdd = _addWordsTableModel._wordsOfTable;
				ArrayList<String> wordsToRemove = _removeWordsTableModel._wordsOfTable;
				String categoryToChange = (String) _categoryComboBox.getSelectedItem();
				
				new ChangePartialWordListModel(wordsToAdd, wordsToRemove, categoryToChange);
				
				//Changes completed. Reset table and ask user whether he/she wants to make more changes or go back to main menu
				_addWordsTableModel.resetTable();
				_removeWordsTableModel.resetTable();
				
				//Refreshes table view
				_addWordsTableModel.fireTableChanged(null);
				_removeWordsTableModel.fireTableChanged(null);
				
				int option = JOptionPane.showConfirmDialog(null, "Completed changes to word list! \n"
						+ "Would you like to make more changes?", "Completed", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				
				switch (option) {
				case JOptionPane.YES_OPTION:
					//do nothing (i.e. stay on the same screen)
					break;
				case JOptionPane.NO_OPTION:
					_owner.changeScreen(AppScreen.MAIN_MENU);
					break;
				}
			}

		});

		_btnCancelChanges.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				int wordsToAddCount = 0;
				int wordsToRemoveCount = 0;

				int wordsOfTableSize = _addWordsTableModel._wordsOfTable.size(); //size is same for removeWordsTableModel too

				//Count number of words user had given to add to/remove from word list
				//Assumption: the size of the _wordsOfTable for both the add/removeWordsTableModel are the same
				for (int i = 0 ; i < wordsOfTableSize; i++) {
					if (!(_addWordsTableModel._wordsOfTable.get(i).equals(""))) { //If there is a word in the add table
						wordsToAddCount++;
					}
					
					if (!(_removeWordsTableModel._wordsOfTable.get(i).equals(""))) { //If there is a word in the remove table
						wordsToRemoveCount++;
					}
				}

				//If user had already given some input words to add/remove without confirming those words
				//Then ask user to confirm the cancellation.
				if ((wordsToAddCount != 0) || (wordsToRemoveCount != 0)) {
					int option = JOptionPane.showConfirmDialog(null, "Are you sure you want to cancel?\n"
							+ "The changes you have made will not be saved.", "Are you sure?", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

					switch (option) {
					case JOptionPane.YES_OPTION:
						_addWordsTableModel.resetTable();
						_removeWordsTableModel.resetTable();
						_owner.changeScreen(AppScreen.MAIN_MENU);
						break;
					case JOptionPane.NO_OPTION:
						//Do nothing (i.e. stay on the same screen)
						break;
					}
				}
				else { //User did not try to make any changes, so can just return to main menu
					_owner.changeScreen(AppScreen.MAIN_MENU);
				}
			}

		});
	}

}
