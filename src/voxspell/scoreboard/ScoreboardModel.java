package voxspell.scoreboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import voxspell.newquiz.HiddenQuizFilesModel;
import voxspell.newquiz.HiddenQuizFilesModel.QuizCategory;

/**
 * This class represents the 'model' part in the MVC design pattern relating to scoreboards.
 * This model extends the AbstractTableModel so that the scores can be shown in a JTable.
 * @author echa232
 *
 */
@SuppressWarnings("serial")
public class ScoreboardModel extends AbstractTableModel {

	private HiddenQuizFilesModel _filesModel; //the files model is useful for getting the necessary info to show scores
	private ArrayList<ArrayList<String>> _database = new ArrayList<ArrayList<String>>();
	private ArrayList<List<String>> _listsOfWordsForStats = new ArrayList<List<String>>(); //contains lists of lists of strings. List 1 is mastered words, list 2 is faulted words, list 3 is failed words
	private ArrayList<List<String>> _listsOfAllWordsByLevels = new ArrayList<List<String>>();
	
	private int _categoryOverallRate;
	
	protected String[] _databaseHeaderNames = { "Word", "Mastered", "Faulted", "Failed", "Accuracy %" };

	protected ScoreboardModel(QuizCategory category) {
		_filesModel = HiddenQuizFilesModel.getInstance();
		_listsOfWordsForStats = _filesModel.readCategoryStatsFilesWordsIntoArray(category);
		_listsOfAllWordsByLevels = _filesModel.readCategoryLevelFilesWordsIntoArray(category);
		createDatabase();
		_categoryOverallRate = calcCategoryOverallRate();
	}	

	/**
	 * For each word in category,
	 * calculate number of times mastered, faulted and failed
	 * calculate accuracy rate of word.
	 * Then, add the word and the calculated values to the database 
	 */
	private void createDatabase() {
		ArrayList<String> allWordsInCategory = groupAllWordsInCategoryIntoOneList();
		
		for (int i = 0; i < allWordsInCategory.size(); i++) {
			ArrayList<String> wordStats = new ArrayList<String>();
			
			String word = allWordsInCategory.get(i);
			wordStats.add(word);
			
			for (int j = 0; j < _listsOfWordsForStats.size(); j++) {
				Integer count = Collections.frequency(_listsOfWordsForStats.get(j), word);
				wordStats.add(count.toString());
			}
			
			//accuracy rate is calculated by (mastered)/(mastered+faulted+failed) * 100
			Integer countMastered = Integer.parseInt(wordStats.get(1));
			Integer countFaulted = Integer.parseInt(wordStats.get(2));
			Integer countFailed = Integer.parseInt(wordStats.get(3));
			int accuracyRate = (int) Math.round((countMastered*1.0 / (countMastered + countFaulted + countFailed)) * 100);
			
			wordStats.add(accuracyRate + "%");
			
			_database.add(wordStats);
		}
		
		sortDatabase();
	}

	private ArrayList<String> groupAllWordsInCategoryIntoOneList() {
		ArrayList<String> allWords = new ArrayList<String>();
		for (int i = 0; i < _listsOfAllWordsByLevels.size(); i++) {
			for (int j = 0; j < _listsOfAllWordsByLevels.get(i).size(); j++) {
				allWords.add(_listsOfAllWordsByLevels.get(i).get(j));
			}
		}
		
		return allWords;
	}

	/**
	 * First sort by the scores by decreasing accuracy rates, then by ascending alphabetical order
	 */
	private void sortDatabase() {
		Collections.sort(_database, new Comparator<ArrayList<String>>() {

			@Override
			public int compare(ArrayList<String> o1, ArrayList<String> o2) {
				
				//Compare objects as numbers, not as strings. 
				int o1Int = Integer.parseInt(o1.get(4).split("%")[0]);
				int o2Int = Integer.parseInt(o2.get(4).split("%")[0]);
				
				if (o2Int == o1Int) {
					return (o1.get(0).compareTo(o2.get(0)));
				} else {
					if (o2Int < o1Int) {
						return -1;
					}
					else {
						return 1;
					}
				}
			}
			
		});
	}
	
	private int calcCategoryOverallRate() {
		int overallCategoryRate = 0;
		
		Integer totalRatesOfWords = 0;
		
		for (int i = 0; i < _database.size(); i++) { //for each word, add up the % and divide that by (total # of words * 100%)
			//get the int value of the percentage
			String rateOfWord = _database.get(i).get(4);
			String[] tempStringList = rateOfWord.split("%");
			totalRatesOfWords += Integer.parseInt(tempStringList[0]);
		}
		
		overallCategoryRate = (int)Math.round((totalRatesOfWords.intValue())/(_database.size() * 100.0) * 100);
		
		return overallCategoryRate;
	}
	
	public int getCategoryOverallRate() {
		return _categoryOverallRate;
	}
	
	@Override
	public String getColumnName(int col) {
		return _databaseHeaderNames[col];
	}
	
	@Override
	public int getColumnCount() {
		return _databaseHeaderNames.length;
	}

	@Override
	public int getRowCount() {
		return _database.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		ArrayList<String> rowData = _database.get(rowIndex);
		String value = rowData.get(columnIndex);
		return value;
	}
}
