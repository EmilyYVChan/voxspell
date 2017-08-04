package voxspell.wordlist.partiallist;

import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

/**
 * A class representing the table model used for when the user wants to change some parts of the current word list.
 * This table model is useful for recording and storing the words that the user has input in a JTable.
 * It is also useful for checking the validity of the user's input. (e.g. at most one apostrophe, no invalid characters such as %$# etc)
 * @author echa232
 *
 */
@SuppressWarnings("serial")
public class ChangePartialWordListTableModel extends AbstractTableModel {

	/*
	 * Every model in this class is fixed to a 12-by-1 table model
	 */
	private static final int COLUMN_COUNT = 1;
	private static final int ROW_COUNT = 12;

	protected ArrayList<String> _wordsOfTable;

	/**
	 * Creating a new instance of this object means to create a table model with blank data
	 * So the resetTable() method is reused for this purpose.
	 */
	public ChangePartialWordListTableModel() {
		resetTable();
	}

	/**
	 * This method is called whenever the user has given an input in a JTable
	 * This method involves checking the validity of the user input.
	 * The user's input is invalid 
	 * if there are non-alphabetical characters (other than the apostrophe)
	 * if there are more than one apostrophe characters
	 * if there is a whitespace character (e.g. the user has input more than one word per cell)
	 * 
	 * This method also involves storing the user's input when the input is valid.
	 * 
	 */
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex)
	{
		String word = aValue.toString().toLowerCase(); //store word in lower case
		int apostropheCount = 0;

		//Check if word is a valid input
		char[] wordAsCharArray = word.toCharArray();
		
		//Invalid input if word contains more than one apostrophe
		for (int i = 0; i < wordAsCharArray.length; i++) {
			if (wordAsCharArray[i] == '\'') {
				apostropheCount++;
				if(apostropheCount > 1) {
					JOptionPane.showMessageDialog(null, "Error: Invalid character(s) detected.\n"
							+ "Please make sure that the word only contains at most one apostrophe.", 
							"Error: Invalid characters detected", JOptionPane.WARNING_MESSAGE);
					apostropheCount = 0;
					return;
				}
			}
		}

		//Invalid input if word contains non-alphabetical characters
		for (int i = 0; i < wordAsCharArray.length; i++) {
			if (!(Character.isLetter(wordAsCharArray[i]))) {
				if (wordAsCharArray[i] == ' ') {
					JOptionPane.showMessageDialog(null, "Error: Invalid character(s) detected.\n"
							+ "Please make sure there are no whitespace characters and\n"
							+ "that there is only one word (or none) on each line.", 
							"Error: Invalid characters detected", JOptionPane.WARNING_MESSAGE);
					return;
				}
				else {
					JOptionPane.showMessageDialog(null, "Error: Invalid character(s) detected.\n"
							+ "The word can only consist of alphabets and at most one apostrophe \n"
							+ "at the appropriate place.\n"
							+ "The invalid character was " + wordAsCharArray[i]
							+ "\nPlease try adding the word again.",
							"Error: Invalid characters detected", JOptionPane.WARNING_MESSAGE);
					return;
				}
			}

		}
		//If reached this point, then it means the word is valid. Hence, store the word
		_wordsOfTable.set(rowIndex, word);
		return;

	}

	@Override
	public int getColumnCount() {
		return COLUMN_COUNT;
	}

	@Override
	public int getRowCount() {
		return ROW_COUNT;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return _wordsOfTable.get(rowIndex);
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		return true;
	}

	/**
	 * Resets the table data to contain only blank values.
	 */
	protected void resetTable() {
		_wordsOfTable = new ArrayList<String>();
		//Initially all words in _wordsOfTable are empty words
		for (int i = 0; i < ROW_COUNT; i++) {
			_wordsOfTable.add("");
		}
	}
}
