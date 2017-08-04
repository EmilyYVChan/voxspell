package voxspell.wordlist.partiallist;

import java.util.ArrayList;
import java.util.List;

import voxspell.newquiz.HiddenQuizFilesModel;
import voxspell.newquiz.HiddenQuizFilesModel.CategoryLevel;
import voxspell.newquiz.HiddenQuizFilesModel.QuizCategory;

/**
 * A class representing the model that handles the logic for when the user wants to change only some parts of the current word list.
 * Applies the MVC design pattern - where this class represents the 'Model' (an example of the View Controller is the ChangePartialWordListScreen)
 * @author echa232
 *
 */
public class ChangePartialWordListModel {

	HiddenQuizFilesModel _quizFilesModel;
	ArrayList<List<String>> _allWordsInCategory;

	ArrayList<String> _wordsToAdd;
	ArrayList<String> _wordsToRemove;
	QuizCategory _categoryToChange;

	/**
	 * Given the necessary input from the View-Controller, store the input and proceed to invoke methods to add and remove words from the current word list
	 * @param wordsToAdd
	 * @param wordsToRemove
	 * @param categoryToChange
	 */
	public ChangePartialWordListModel(ArrayList<String> wordsToAdd, ArrayList<String> wordsToRemove, String categoryToChange) {
		_quizFilesModel = HiddenQuizFilesModel.getInstance();

		_wordsToAdd = wordsToAdd;
		_wordsToRemove = wordsToRemove;

		for (QuizCategory c : QuizCategory.values()) {
			if (c.toString().equals(categoryToChange)) {
				_categoryToChange = c;
			}
		}

		addWordsToWordList();
		removeWordsFromWordList();
	}

	/**
	 * For each word to add, check if the word already exists in the current word list
	 * If it does, then ignore the word
	 * Else add the word to the word list
	 */
	private void addWordsToWordList() {
		
		_allWordsInCategory = _quizFilesModel.readCategoryLevelFilesWordsIntoArray(_categoryToChange);


		for (int i = 0; i < _wordsToAdd.size(); i++) {
			String wordToAdd = _wordsToAdd.get(i);
			if(checkIfWordAlreadyExists(wordToAdd)) {
				continue; //word already exists in current word list, so ignore word
			}
			else { //add new word to the current word list. As the word is new, add it to the lowest Category Level
				_quizFilesModel.addWordToLevelFile(_categoryToChange, CategoryLevel.ONE, wordToAdd);
			}

		}
	}

	/**
	 * For each word to remove, remove all occurrences of the word from all of the category's files (stats files and level files)
	 */
	private void removeWordsFromWordList() {
		for (int i = 0; i < _wordsToRemove.size(); i++) {
			String wordToRemove = _wordsToRemove.get(i);
			
			_quizFilesModel.removeWordFromAllStatsFile(_categoryToChange, wordToRemove);
			
			for (CategoryLevel level : CategoryLevel.values()) {
				_quizFilesModel.removeWordFromLevelFile(_categoryToChange, level, wordToRemove);
			}
		}
	}

	/**
	 * Checks if word already exists in the current word list
	 * @param word
	 * @return
	 */
	private boolean checkIfWordAlreadyExists(String word) {

		for (int j = 0; j < _allWordsInCategory.size(); j++) {
			for (int k = 0; k < _allWordsInCategory.get(j).size(); k++) {
				if (word.equals(_allWordsInCategory.get(j).get(k))) {
					return true;
				}
			}
		}
		
		return false;
	}


}
