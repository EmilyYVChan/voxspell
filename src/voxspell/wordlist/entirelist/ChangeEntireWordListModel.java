package voxspell.wordlist.entirelist;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import voxspell.newquiz.HiddenQuizFilesModel;
import voxspell.newquiz.HiddenQuizFilesModel.CategoryLevel;
import voxspell.newquiz.HiddenQuizFilesModel.QuizCategory;
import voxspell.wordlist.ChangeWordListException;
import voxspell.wordlist.ChangeWordListException.ExceptionType;

public class ChangeEntireWordListModel {

	private static File _newListFile;
	private HiddenQuizFilesModel _filesModel;
	protected ArrayList<ArrayList<String>> _allWords;

	public ChangeEntireWordListModel() throws ChangeWordListException {
		readNewWordListIntoArray();
		clearOldWordList();
		updateWordList();		
	}

	/**
	 * Create 2D array of words
	 * Each row is a word in the category, each column represents a category
	 * Columns are assumed to be in this order: Adjectives, Adverbs, Nouns, Verbs
	 * This means it is assumed that the file paths follow this order too.
	 * Declaration: code is partly sourced from assignment 3
	 * @throws ChangeWordListException
	 */
	@SuppressWarnings("resource")
	private void readNewWordListIntoArray() throws ChangeWordListException {

		int columnIndex = -1; //keep tracks of which list to update in allWords
		_allWords = new ArrayList<ArrayList<String>>();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(_newListFile));
			
			//fills allWords
			String word = br.readLine();
			
			while (word != null) {
				word = word.trim().toLowerCase(); //trims all leading and trailing white spaces in a word and stores as lower case
				
				//Check if word is a blank line
				if (word.length() == 0) {
					word = br.readLine();
					continue;
				}
								
				if (Character.toString(word.charAt(0)).equals("%")) {
					columnIndex++;
					_allWords.add(new ArrayList<String>());
				}
				else {
					//Check if word is in the correct format (alphabetical and apostrophes only. Any other characters are invalid)
					if (!(word.matches("[a-zA-Z']+"))) {
						//Word contains invalid characters
						throw new ChangeWordListException(ExceptionType.InvalidCharacter, word);
					}
					
					//Check if the word had already been added to allWords before (to prevent repetitive words in quiz)
					if (_allWords.get(columnIndex).contains(word)) {
						word = br.readLine();
						continue;
					}
					
					_allWords.get(columnIndex).add(word);
				}
				word = br.readLine();
			}
			
			br.close();
			
			if (_allWords.size() != 4) { //more than the necessary categories were given in file
				throw new ChangeWordListException(ExceptionType.IncorrectlyFormattedFile);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Removes all the words from all files in the category
	 */
	private void clearOldWordList() {
		_filesModel = HiddenQuizFilesModel.getInstance();
		for (QuizCategory qc : QuizCategory.values()) { //remove all files and set them up again
			try {
				for (Path p : qc._categoryLevelFilePaths) {
					Files.deleteIfExists(p);
				} 
				
				for (Path p : qc._categoryStatsFilePaths) {
					Files.deleteIfExists(p);
				}
				
				Files.deleteIfExists(qc._categoryFolder);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		_filesModel.setUpHiddenFiles();

	}

	/**
	 * Adds the new words to the initial level in the category
	 */
	private void updateWordList() {
		//Write all the words of each category into the Level 1 file of that category
		for (int i = 0; i < _allWords.size(); i++) { //for each category of words
			for (int j = 0; j < _allWords.get(i).size(); j++) { //for each word in category
				//add word to level 1 file of the category
				switch(i) {
				case 0: //First column in allWords: Adjectives
					_filesModel.addWordToLevelFile(QuizCategory.Adjectives, CategoryLevel.ONE, _allWords.get(i).get(j));
					break;
				case 1: //Second column in allWords: Adverbs
					_filesModel.addWordToLevelFile(QuizCategory.Adverbs, CategoryLevel.ONE, _allWords.get(i).get(j));
					break;
				case 2: //Third column in allWords: Nouns
					_filesModel.addWordToLevelFile(QuizCategory.Nouns, CategoryLevel.ONE, _allWords.get(i).get(j));
					break;
				case 3: //Fourth column in allWords: Verbs
					_filesModel.addWordToLevelFile(QuizCategory.Verbs, CategoryLevel.ONE, _allWords.get(i).get(j));
					break;					
				}
			}
		}
	}

	public static void setNewFile(File file) {
		_newListFile = file;
	}
}
