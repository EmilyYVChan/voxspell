package voxspell.newquiz;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import voxspell.newquiz.HiddenQuizFilesModel.LevelShiftDirection;
import voxspell.newquiz.HiddenQuizFilesModel.QuizCategory;
import voxspell.newquiz.HiddenQuizFilesModel.WordMastery;
import voxspell.sounds.SpeechAndSoundModel;

public class QuizModel {

	//Models
	private SpeechAndSoundModel _speechAndSoundModel;
	private HiddenQuizFilesModel _filesModel;

	// List of QuizModelListeners
	private List<QuizModelListener> _listeners;

	//Fields for storing useful state information about the current quiz
	ArrayList<String> _gameWords;
	QuizCategory _category;
	String _currentWord;
	private String _userSpeltWord;
	public int _currentWordCount;
	private int _attemptsCount;
	public int _numOfWordsMastered;
	int _numOfWordsInCategory;
	
	public QuizModel() {		
		//Establish the associations with other models necessary for a quiz
		_speechAndSoundModel = SpeechAndSoundModel.getInstance();
		_filesModel = HiddenQuizFilesModel.getInstance();
		_listeners = new ArrayList<QuizModelListener>();
	}

	/**
	 * Calling this method will initialise the states necessary for a new spelling quiz
	 * @param category: category of words to be quizzed on
	 */
	void startNewQuiz(QuizCategory category) {
		_category = category;
		_currentWordCount = 0;
		_attemptsCount = 1;
		_numOfWordsMastered = 0;
		generateGameWords(category);		
	}

	/**
	 * Given the selected category, generates a list of quiz words for the game.
	 * Concept used: spaced repetition of quiz words (simple brute-force adaptation)
	 * Overall goal: user is quizzed on more unfamiliar words than familiar/very familiar words.
	 * The aim is to quiz the user on 5 unfamiliar words, 3 familiar words and 2 very familiar words.
	 * If there are insufficient number of words at a particular familiarity level, 
	 * then priority is given for the extra room of quiz words to be filled with words at a lower familiarity level.
	 * If the user is familiar with most of the words in the category, then gives a reminder to the user to add some new words to the category. 
	 * If there aren't enough words in the category, the game will not begin.
	 */
	void generateGameWords(QuizCategory category) {
		//Get the 2D array of all quiz words (columns = level of familiarity, rows = words in level) of selected category
		ArrayList<List<String>> allWordsInCategory = _filesModel.readCategoryLevelFilesWordsIntoArray(category);

		//If there aren't enough words in the category, quiz game cannot begin.
		//Ensures that there are at least 10 words to be quizzed on
		_numOfWordsInCategory = 0;
		for (int i = 0; i < allWordsInCategory.size(); i++) {
			int numOfWordsInLevel = allWordsInCategory.get(i).size();
			_numOfWordsInCategory += numOfWordsInLevel;
		}
		if (_numOfWordsInCategory < 10) {
			fire(QuizModelEvent.EventType.NotEnoughWordsInWordList);
			return;
		}

		//Note: File must end at the end of the last word, not on a new line!

		//All list of words are shuffled first to ensure they're in random order
		//Words in level 1 & 2 are words that the user are unfamiliar/less familiar with.
		ArrayList<String> listOfUnfamiliarWords = new ArrayList<String>();
		listOfUnfamiliarWords.addAll(allWordsInCategory.get(0));
		listOfUnfamiliarWords.addAll(allWordsInCategory.get(1));
		Collections.shuffle(listOfUnfamiliarWords);
		int numOfUnfamiliarWords = listOfUnfamiliarWords.size();

		//Words in level 3 & 4 are words that the user are somewhat-familiar with.
		ArrayList<String> listOfFamiliarWords = new ArrayList<String>();
		listOfFamiliarWords.addAll(allWordsInCategory.get(2));
		listOfFamiliarWords.addAll(allWordsInCategory.get(3));
		Collections.shuffle(listOfFamiliarWords);
		int numOfFamiliarWords = listOfFamiliarWords.size();

		//Words in level 5 are words that the user are very familiar with.
		ArrayList<String> listOfVeryFamiliarWords = new ArrayList<String>();
		listOfVeryFamiliarWords.addAll(allWordsInCategory.get(4));
		Collections.shuffle(listOfVeryFamiliarWords);
		int numOfVeryFamiliarWords = listOfVeryFamiliarWords.size();

		_gameWords = new ArrayList<String>();

		if (numOfUnfamiliarWords < 5) { //there are more familiar/very familiar words than unfamiliar words
			//send reminder to user
			fire(QuizModelEvent.EventType.LowNumberOfUnfamiliarWords);

			for (int i = 0; i < numOfUnfamiliarWords; i++) { //add all unfamiliar words (ie add all words at level 1 and 2)
				String word = listOfUnfamiliarWords.get(i);
				_gameWords.add(word);
			}

			//first consider number of very familiar words and add words accordingly
			if (numOfVeryFamiliarWords < 2) { //not enough 'very familiar' words to add to game words. This means most of game words will be at 'familiar' level
				//add all 'very familiar' words
				for (int i = 0; i < numOfVeryFamiliarWords; i++) {
					String word = listOfVeryFamiliarWords.get(i);
					_gameWords.add(word);
				}
				//fill the rest of the 10 game words with 'familiar' words
				int numOfEmptySlotsToFill = 10 - _gameWords.size();
				for (int i = 0; i < numOfEmptySlotsToFill; i++) {
					String word = listOfFamiliarWords.get(i);
					_gameWords.add(word);
				}
			} else { //there are enough 'very familiar' words to add to game words. But prioritise adding as much words at lower familiarity (ie 'familiar') as possible
				if (numOfFamiliarWords < 3) { //not enough 'familiar' words to add to game words. This means most of game words will be at 'very familiar' level
					//add all 'familiar' words
					for (int i = 0; i < numOfFamiliarWords; i++) {
						String word = listOfFamiliarWords.get(i);
						_gameWords.add(word);
					}
					//fill the rest of the 10 game words with 'very familiar' words
					int numOfEmptySlotsToFill = 10 - _gameWords.size();
					for (int i = 0; i < numOfEmptySlotsToFill; i++) {
						String word = listOfVeryFamiliarWords.get(i);
						_gameWords.add(word);
					}
				} else { //there are at least 3 familiar words. but need to check if there are enough familiar words to accommodate for empty slots caused by lack of unfamiliar words
					if (numOfFamiliarWords >= (3 + (5-numOfUnfamiliarWords))) {
						for (int i = 0; i < (3 + (5-numOfUnfamiliarWords)); i++) {
							String word = listOfFamiliarWords.get(i);
							_gameWords.add(word);
						}
						//add the 2 'very familiar' words to complete the game words set
						int numOfEmptySlotsToFill = 10 - _gameWords.size();
						for (int i = 0; i < numOfEmptySlotsToFill; i++) {
							String word = listOfVeryFamiliarWords.get(i);
							_gameWords.add(word);
						}
					} else { //not enough familiar words to accommodate for all empty slots caused by lack of unfamiliar words
						//add all familiar words
						for (int i = 0; i < numOfFamiliarWords; i++) {
							String word = listOfFamiliarWords.get(i);
							_gameWords.add(word);
						}
						//fill the rest of the 10 game words with 'very familiar' words
						int numOfEmptySlotsToFill = 10 - _gameWords.size();
						for (int i = 0; i < numOfEmptySlotsToFill; i++) {
							String word = listOfVeryFamiliarWords.get(i);
							_gameWords.add(word);
						}
					}
				}
			}
		} else { //there are more unfamiliar words than familiar/very familiar words
			if (numOfVeryFamiliarWords < 2) {
				//not enough very familiar words for the usual quiz allocation. so add them all
				for (int i = 0; i < numOfVeryFamiliarWords; i++) {
					String word = listOfVeryFamiliarWords.get(i);
					_gameWords.add(word);
				}

				if (numOfFamiliarWords < 3) { //not enough familiar words for the usual quiz allocation. so add them all
					for (int i = 0; i < numOfFamiliarWords; i++) {
						String word = listOfFamiliarWords.get(i);
						_gameWords.add(word);
					}
					//fill the rest of the 10 game words with 'unfamiliar' words
					int numOfEmptySlotsToFill = 10 - _gameWords.size();
					for (int i = 0; i < numOfEmptySlotsToFill; i++) {
						String word = listOfUnfamiliarWords.get(i);
						_gameWords.add(word);
					}
				}
				else { //there are at least 3 familiar words. but need to check if there are enough unfamiliar words to accommodate for empty slots caused by lack of very familiar words
					if (numOfUnfamiliarWords >= (5+(2-numOfVeryFamiliarWords))) { //there are enough unfamiliar words
						//add the unfamiliar words
						for (int i = 0; i < (5+(2-numOfVeryFamiliarWords)); i++) {
							String word = listOfUnfamiliarWords.get(i);
							_gameWords.add(word);
						}
						//add 3 familiar words as per the normal quiz allocation
						for (int i = 0; i < 3; i++) {
							String word = listOfFamiliarWords.get(i);
							_gameWords.add(word);
						}
					}
					else { //not enough unfamiliar words to accommodate
						//add all unfamiliar words
						for (int i = 0; i < numOfUnfamiliarWords; i++) {
							String word = listOfUnfamiliarWords.get(i);
							_gameWords.add(word);
						}
						//fill the rest of the 10 game words with 'familiar' words
						int numOfEmptySlotsToFill = 10 - _gameWords.size();
						for (int i = 0; i < numOfEmptySlotsToFill; i++) {
							String word = listOfFamiliarWords.get(i);
							_gameWords.add(word);
						} 
					}
				}				
			} else { //there are at least two of the necessary 2 'very familiar' words
				if (numOfFamiliarWords < 3) {
					//add all familiar words
					for (int i = 0; i < numOfFamiliarWords; i++) {
						String word = listOfFamiliarWords.get(i);
						_gameWords.add(word);
					}
					//if numOfUnfamiliarWords can accommodate for the 5 required words AND the empty slots caused by lack of familiar words
					//add the appropriate number of unfamiliar words and add the 2 required very familiar words
					if (numOfUnfamiliarWords >= (5+(3-numOfFamiliarWords))) {
						for (int i = 0; i < (5+(3-numOfFamiliarWords)); i++) {
							String word = listOfUnfamiliarWords.get(i);
							_gameWords.add(word);
						}

						for (int i = 0; i < 2; i++) {
							String word = listOfVeryFamiliarWords.get(i);
							_gameWords.add(word);
						} 
					} else { //not enough unfamiliar words to accommodate. add all unfamiliar words and fill the rest of the empty slots with very familiar words
						for (int i = 0; i < numOfUnfamiliarWords; i++) {
							String word = listOfUnfamiliarWords.get(i);
							_gameWords.add(word);
						}
						//fill the rest of the 10 game words with 'very familiar' words
						int numOfEmptySlotsToFill = 10 - _gameWords.size();
						for (int i = 0; i < numOfEmptySlotsToFill; i++) {
							String word = listOfVeryFamiliarWords.get(i);
							_gameWords.add(word);
						} 
					}
				}
				else { //there are enough unfamiliar, familiar and very familiar words for the normal quiz allocation
					for (int i = 0; i < 5; i++) {
						String word = listOfUnfamiliarWords.get(i);
						_gameWords.add(word);
					}

					for (int i = 0; i < 3; i++) {
						String word = listOfFamiliarWords.get(i);
						_gameWords.add(word);
					}

					for (int i = 0; i < 2; i++) {
						String word = listOfVeryFamiliarWords.get(i);
						_gameWords.add(word);
					}

				}
			}	
		}

//		//Debugging purposes only
//		for (int i = 0; i < _gameWords.size(); i++) {
//			System.out.println(_gameWords.get(i));
//		}
		
		spellWordsPrompt();
	}


	/**
	 * Checks user spelt word validity.
	 * If current word contains an apostrophe, does not detect an apostrophe character input as invalid.
	 * If word does not contain an apostrophe and user gives an apostrophe or any non-alphabetical character in the input, then it is an invalid input
	 * 
	 * Declaration: code derived from assignment 3
	 */
	protected boolean isValidUserInput(){

		char[] inputChars = _userSpeltWord.toCharArray();
		char[] answerChars = _currentWord.toCharArray();

		ArrayList<Character> invalidChars = new ArrayList<Character>();

		for (int i = 0; i < _currentWord.length(); i ++){
			//see if the correct word has any non letter characters and add to list (avoid duplicates)
			if (! Character.isLetter(answerChars[i])){
				if (! invalidChars.contains(answerChars[i])){
					invalidChars.add(answerChars[i]);
				}
			}
		}

		for (int a = 0 ; a < inputChars.length; a ++){
			if(! Character.isLetter(inputChars[a])) {
				//if encounter a potential invalid character, check if invalidChars contains it
				if (! invalidChars.contains(inputChars[a])){
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Checks user's attempted spelling input against the quizzed word.
	 * If the user spelt the word correctly on the first try, the word 'goes up one level of familiarity'
	 * If the user faulted the word, the word remains on the same level.
	 * If the user failed the word, the word 'goes down one level of familiarity'.
	 */
	void checkSpelling(String userSpeltWord) {
		//if blank input, ignore
		if (userSpeltWord == null || userSpeltWord.length() == 0) {
			return;
		}

		//for case-insensitive processing
		_userSpeltWord = userSpeltWord.toLowerCase(); 
		_currentWord = _currentWord.toLowerCase();

		//Checks that user's spelt word contains valid characters only
		if (!isValidUserInput()){ 
			fire(QuizModelEvent.EventType.InvalidUserInput);
			_speechAndSoundModel.speakCurrentWord(_currentWord); //repeats current word for user to try spelling again
			return;
		}

		//check spelling
		//Only if user mastered the word would the label update to a check mark
		//Otherwise (i.e. faulted or failed), the label updates to a cross mark.
		if (_userSpeltWord.equals(_currentWord)) { //if user spelt word correctly
			_speechAndSoundModel.playCorrectSoundFX();
			if (_attemptsCount == 1) { //Mastered word
				_numOfWordsMastered++;
				storeWordInStatsFile(WordMastery.MASTERED);//store word in stats file
				_filesModel.shiftWordByALevel(_category, LevelShiftDirection.UP, _currentWord);// move word up one level
				fire(QuizModelEvent.EventType.SpeltWordCorrectly);
			}
			else { //Faulted word
				storeWordInStatsFile(WordMastery.FAULTED); //store word in stats file
			}

			_attemptsCount = 1; //reset attempts for the next word
			spellWordsPrompt(); //move onto the next word
		}
		else { //user spelt word incorrectly
			if (_attemptsCount == 1) { //Faulted word
				_attemptsCount++;
				_speechAndSoundModel.sayFaultedSpeech(_currentWord);
				fire(QuizModelEvent.EventType.SpeltWordWrongly);
			}
			else { //Failed word
				_speechAndSoundModel.playWrongSoundFX();
				storeWordInStatsFile(WordMastery.FAILED);//store word in stats file
				_filesModel.shiftWordByALevel(_category, LevelShiftDirection.DOWN, _currentWord);//move word down one level
				fire(QuizModelEvent.EventType.FailedWord);
				//move onto next word
				_attemptsCount = 1;
				spellWordsPrompt();
			}
		}		
	}

	/**
	 * While there are still more words left to quiz, quiz them.
	 * Otherwise, fire event that the quiz has ended.
	 */
	private void spellWordsPrompt() {
		_currentWordCount++;
		if (_currentWordCount <= 10) { //while there are still more words to spell
			_currentWord = _gameWords.get(_currentWordCount-1);
			_speechAndSoundModel.speakCurrentWord(_currentWord);
			fire(QuizModelEvent.EventType.NewWordInQuiz);
		}
		else {
			fire(QuizModelEvent.EventType.EndOfQuiz);
			_currentWordCount = 0; //reset word count for next new spelling quiz
			return;
		}
	}

	void relistenWord() {
		_speechAndSoundModel.relistenWord(_currentWord);
	}

	/**
	 * Depending on whether the user mastered/faulted/failed spelling of word, store in the appropriate file.
	 * @param masteryLevel
	 */
	private void storeWordInStatsFile(WordMastery masteryLevel) {
		_filesModel.addWordToStatsFile(_category, masteryLevel, _currentWord);
	}

	/**
	 * Registers a QuizModelListener on this QuizModel object.
	 */
	public void addQuizModelListener(QuizModelListener listener) {
		_listeners.add(listener);
	}

	/**
	 * Deregisters a QuizModelListener from this QuizModel object.
	 */
	public void removeQuizModelListener(QuizModelListener listener) {
		_listeners.remove(listener);
	}

	/**
	 * Iterates through registered QuizModelListeners and fires a 
	 * QuizModelEvent to each in turn.
	 */
	private void fire(QuizModelEvent.EventType eventType) {
		for(QuizModelListener listener : _listeners) {
			listener.updateView(eventType);
		}
	}
}
