package voxspell.wordlist;

/**
 * An Exception class for models handling the logic of changing word lists.
 * It is useful to throw an instance of this Exception for handling checked errors caused by the user.
 * @author echa232
 *
 */
@SuppressWarnings("serial")
public class ChangeWordListException extends Exception {
	
	/**
	 * An enum class that consists of elements representing all possible types of 
	 * checked errors that could occur while changing word lists.
	 * @author echa232
	 *
	 */
	public enum ExceptionType {
		InvalidCharacter, IncorrectlyFormattedFile
	}
	
	public ExceptionType _exceptionType;
	public String _errorWord;
	
	/**
	 * A constructor class that simply specifies what type of checked error has occured.
	 * @param type
	 */
	public ChangeWordListException(ExceptionType type) {
		_exceptionType = type;
	}
	
	/**
	 * A constructor class that specifies what type of checked error has occured
	 * and also additional useful information to be used for debugging and/or 
	 * providing helpful resolution info to the user
	 * @param type
	 * @param wordWithErrorInIt
	 */
	public ChangeWordListException(ExceptionType type, String wordWithErrorInIt) {
		this(type);
		_errorWord = wordWithErrorInIt;
	}
}
