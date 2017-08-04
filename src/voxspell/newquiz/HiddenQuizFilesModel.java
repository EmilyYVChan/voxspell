package voxspell.newquiz;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 * A Singleton class that manages reading/writing actions to the hidden files
 * necessary for a new quiz and for statistics when running the VoxSpellApp.
 * Declaration: most of the code in this class is derived from my assignment 3.
 * @author echa232
 */
public class HiddenQuizFilesModel {

	public enum LevelShiftDirection {
		UP, DOWN
	}

	public enum CategoryLevel {
		ONE(1), TWO(2), THREE(3),FOUR(4),FIVE(5);

		public final int _value;
		private CategoryLevel(int level) {
			_value = level;
		}

		public static CategoryLevel getCategoryLevelGivenInt(int intLevel) {
			for (CategoryLevel categoryLevel : CategoryLevel.values()) {
				if (categoryLevel._value == intLevel) {
					return categoryLevel;
				}
			}
			throw new ClassCastException("No such level!");
		}
	}

	public enum WordMastery {
		MASTERED, FAULTED, FAILED
	}

	public enum QuizCategory {
		Adjectives("adjectives"), Adverbs("adverbs"), Verbs("verbs"), Nouns("nouns");

		public Path _categoryFolder;
		public ArrayList<Path> _categoryLevelFilePaths;
		public ArrayList<Path> _categoryStatsFilePaths;

		private QuizCategory(String categoryName) {
			//Initialise fields
			_categoryLevelFilePaths = new ArrayList<Path>();
			_categoryStatsFilePaths = new ArrayList<Path>();

			String pathName;
			//create category's parent folder
			pathName = "./.quiz/" + categoryName;
			_categoryFolder = Paths.get(pathName);

			//create category's files for levels 1-5
			for (int i = 1; i <= 5; i++) {
				pathName = "./.quiz/" + categoryName + "/Level" + i;
				_categoryLevelFilePaths.add(Paths.get(pathName));
			}

			//create category's files for stats
			String[] statsLevels = {"Mastered", "Faulted", "Failed"};
			for (int i = 0; i < statsLevels.length; i++) {
				pathName = "./.quiz/" + categoryName + "/" + statsLevels[i].toString();
				_categoryStatsFilePaths.add(Paths.get(pathName));
			}
		}
	}

	private static HiddenQuizFilesModel _hiddenQuizFilesModel;

	private HiddenQuizFilesModel()
	{
		setUpHiddenFiles();
	}

	public static synchronized HiddenQuizFilesModel getInstance()
	{
		if (_hiddenQuizFilesModel == null){
			_hiddenQuizFilesModel = new HiddenQuizFilesModel();
		}		
		return _hiddenQuizFilesModel;
	}

	/**
	 * Create the necessary hidden files/folders for compiling quiz word lists according to the user's ability 
	 * and storing statistics when running VoxSpellApp
	 * These hidden files/folders are created in the same directory where the VoxSpellApp is executed.
	 * These hidden files/folders are only created if it doesn't already exists
	 */
	public void setUpHiddenFiles() {
		/*
		 * Create quiz folder and files
		 * First there is the quiz folder.
		 * Then in the quiz folder, there are 4 folders: adjectives, adverbs, verbs and nouns
		 * In each of those 4 folders, there are 8 files: levels 1 to 5, and mastered/faulted/failed
		 * The level files are used to compile the word list according to the category and the user's familiarity (rated level 1 to 5) with the words
		 * The mastered/fauled/failed files are used for statistical purposes
		 */
		try {
			/*
			 * For each category, create the files for all the file paths in
			 * the category's categoryLevelFilePaths and categoryStatsFilePaths 
			 */
			for (QuizCategory category : QuizCategory.values()) {
				//Create the parent directories first to prevent IO exception being thrown
				if (Files.notExists(category._categoryFolder)) {
					Files.createDirectories(category._categoryFolder);
				}
				for (int i = 0; i < category._categoryLevelFilePaths.size(); i++) {
					Path filePath = category._categoryLevelFilePaths.get(i);
					if (Files.notExists(filePath)) {
						Files.createFile(filePath);
					}
				}

				for (int i = 0; i < category._categoryStatsFilePaths.size(); i++) {
					Path filePath = category._categoryStatsFilePaths.get(i);
					if (Files.notExists(filePath)) {
						Files.createFile(filePath);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Given a category, this method returns a 2D array.
	 * The 2D array contains a list of lists of quiz words of the category.
	 * The position of the list of quiz words in the list corresponds to the user's level of familiarity with the quiz words
	 * 
	 * Declaration: code reused from assignment 3
	 */
	public ArrayList<List<String>> readCategoryLevelFilesWordsIntoArray(QuizCategory category) {		
		//Instantiate the 2D array
		ArrayList<List<String>> allWordsInCategory = new ArrayList<List<String>>();

		//Given the category, read the level files into the array
		ArrayList<Path> categoryLevelFilePaths = null;
		try {
			switch (category) {
			case Adjectives:
				categoryLevelFilePaths = QuizCategory.Adjectives._categoryLevelFilePaths;
				break;
			case Adverbs:
				categoryLevelFilePaths = QuizCategory.Adverbs._categoryLevelFilePaths;
				break;
			case Nouns:
				categoryLevelFilePaths = QuizCategory.Nouns._categoryLevelFilePaths;
				break;
			case Verbs:
				categoryLevelFilePaths = QuizCategory.Verbs._categoryLevelFilePaths;
				break;
			}

			for (int i = 0; i < categoryLevelFilePaths.size(); i++) {
				List<String> wordsAtLevel = Files.readAllLines(categoryLevelFilePaths.get(i), StandardCharsets.ISO_8859_1);
				allWordsInCategory.add(wordsAtLevel);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return allWordsInCategory;
	}

	public ArrayList<List<String>> readCategoryStatsFilesWordsIntoArray(QuizCategory category) {
		//Instantiate the 2D array
		ArrayList<List<String>> allWordsInCategory = new ArrayList<List<String>>();

		//Given the category, read the level files into the array
		ArrayList<Path> categoryStatsFilePaths = null;
		try {
			switch (category) {
			case Adjectives:
				categoryStatsFilePaths = QuizCategory.Adjectives._categoryStatsFilePaths;
				break;
			case Adverbs:
				categoryStatsFilePaths = QuizCategory.Adverbs._categoryStatsFilePaths;
				break;
			case Nouns:
				categoryStatsFilePaths = QuizCategory.Nouns._categoryStatsFilePaths;
				break;
			case Verbs:
				categoryStatsFilePaths = QuizCategory.Verbs._categoryStatsFilePaths;
				break;
			}

			for (int i = 0; i < categoryStatsFilePaths.size(); i++) {
				List<String> wordsAtStats = Files.readAllLines(categoryStatsFilePaths.get(i), StandardCharsets.ISO_8859_1);
				allWordsInCategory.add(wordsAtStats);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return allWordsInCategory;
	}

	/**
	 * Given a word, append it as a new line in the specified stats file
	 */
	protected void addWordToStatsFile(QuizCategory category, WordMastery statsFile, String word) {
		String wordOnNewLine = word + "\n";
		try {
			switch (statsFile) {
			case MASTERED:
				Files.write(category._categoryStatsFilePaths.get(0), wordOnNewLine.getBytes(), StandardOpenOption.APPEND);
				break;
			case FAULTED:
				Files.write(category._categoryStatsFilePaths.get(1), wordOnNewLine.getBytes(), StandardOpenOption.APPEND);
				break;
			case FAILED:
				Files.write(category._categoryStatsFilePaths.get(2), wordOnNewLine.getBytes(), StandardOpenOption.APPEND);
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addWordToLevelFile(QuizCategory category, CategoryLevel level, String word) {
		try {
			String wordOnNewLine = word + "\n";
			Path levelFilePath = category._categoryLevelFilePaths.get(level._value - 1);
			Files.write(levelFilePath, wordOnNewLine.getBytes(), StandardOpenOption.APPEND);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void removeWordFromLevelFile(QuizCategory category, CategoryLevel level, String word) {

		Path levelFilePath = category._categoryLevelFilePaths.get(level._value - 1); //which Level file in category to remove the word from
		removeWordFromFile(word, levelFilePath);
		
	}
	
	/**
	 * Removes all occurrences of the given word from all stats files in the given category
	 */
	public void removeWordFromAllStatsFile(QuizCategory category, String word) {
		
		for (int i = 0; i < category._categoryStatsFilePaths.size(); i++) {
			Path statsFilePath = category._categoryStatsFilePaths.get(i);
			removeWordFromFile(word, statsFilePath);						
		}
	}
	
	private void removeWordFromFile(String word, Path filePath) {
		try {
			List<String> wordsInFile = Files.readAllLines(filePath, StandardCharsets.ISO_8859_1);//First read in all words from file
			List<String> tempList = new ArrayList<String>();
			tempList.add(word); 
			wordsInFile.removeAll(tempList);//Then remove all occurrences of the given word
			
			//Recreate a blank file in the same path as the file and rewrite words into the blank file.
			Files.delete(filePath);
			Files.createFile(filePath);
			Files.write(filePath, wordsInFile, StandardCharsets.ISO_8859_1, StandardOpenOption.APPEND);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Reset stats in category and assumes user's familiarity with all words in the category reverts back to level 1. 
	 */
	public void reset(QuizCategory category) {
		try {

			//Reset stats: 
			//delete mastered/faulted/failed files in category and recreate them as blank files
			//Reset quiz words in category: 
			//first read all the words in all 5 levels and store in a variable
			//Then, delete the 5 level files and the folder and recreate them as blank files. 
			//Finally rewrite the words into the level 1 file of the category
			ArrayList<List<String>> allWordsInCategory = readCategoryLevelFilesWordsIntoArray(category);

			for (int i = 0; i < category._categoryStatsFilePaths.size(); i++) {
				Files.deleteIfExists(category._categoryStatsFilePaths.get(i));
			} 

			for (int i = 0; i < category._categoryLevelFilePaths.size(); i++) {
				Files.deleteIfExists(category._categoryLevelFilePaths.get(i));
			}

			setUpHiddenFiles();

			for (int i = 0; i < allWordsInCategory.size(); i++) {
				for (int j = 0; j < allWordsInCategory.get(i).size(); j++) {
					addWordToLevelFile(category, CategoryLevel.ONE, allWordsInCategory.get(i).get(j));
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	/**
	 * Given the category, direction and word to move, moves the word up/down one level
	 * First it looks for which level the word is in, and then moves the word according to the direction
	 */
	void shiftWordByALevel(QuizCategory category, LevelShiftDirection direction, String word) {		
		//Look for which level the word is in
		ArrayList<List<String>> allWordsInCategory = readCategoryLevelFilesWordsIntoArray(category);
		CategoryLevel currentLevelOfWord = null;
		for (int i = 0; i < allWordsInCategory.size(); i++) {
			List<String> currentListBeingExamined = allWordsInCategory.get(i);
			if (currentListBeingExamined.contains(word)) { //Assumption: once the word has been found at the current list being examined, the word is assumed to be non-existent at higher levels
				currentLevelOfWord = CategoryLevel.getCategoryLevelGivenInt(i+1);
				break;
			}
		}

		//Check if current level of word is at the lowest/highest level possible with the desired direction of shift
		//Cannot shift word lower than its lowest level and vice versa.
		if ((currentLevelOfWord._value == 1) && (direction == LevelShiftDirection.DOWN)) {
			return;
		}
		else if ((currentLevelOfWord._value == category._categoryLevelFilePaths.size()) && (direction == LevelShiftDirection.UP)) {
			return;
		}

		//Determine the new level to put the word in (in terms of CategoryLevel)
		CategoryLevel newLevelOfWord = null;
		switch (direction) {
		case DOWN:
			newLevelOfWord = CategoryLevel.getCategoryLevelGivenInt(currentLevelOfWord._value - 1);
			break;
		case UP:
			newLevelOfWord = CategoryLevel.getCategoryLevelGivenInt(currentLevelOfWord._value + 1);
			break;
		}

		//remove word at current level
		removeWordFromLevelFile(category, currentLevelOfWord, word);

		//add word at new level
		addWordToLevelFile(category, newLevelOfWord, word);
	}

}