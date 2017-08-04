package voxspell.newquiz;

/**
 * A class that contains the enum class representing all possible types of QuizModel events that will be
 * of interest to QuizModel Listeners.
 * @author echa232
 *
 */
public class QuizModelEvent {
	
	// Set of event types.
	public enum EventType {
		NotEnoughWordsInWordList,
		LowNumberOfUnfamiliarWords,
		InvalidUserInput,
		NewWordInQuiz,
		EndOfQuiz,
		SpeltWordCorrectly,
		SpeltWordWrongly,
		FailedWord
	};
}
