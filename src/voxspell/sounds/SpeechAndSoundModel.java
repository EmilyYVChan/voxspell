package voxspell.sounds;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * SpeechAndSoundModel is the class handles the code, logic, files and Bash commands
 * relating to making Festival calls and playing sound effects.
 * The Singleton design pattern is applied here to ensure that only one object is doing the bash commands.
 * 
 * Declaration: Majority of the code in this class is sourced from the group assignment 206 Assignment 3.
 * @author echa232
 */
public class SpeechAndSoundModel {

	/**
	 * Enum class representing the types of voices available for the Festival speech synthesiser
	 * @author echa232
	 *
	 */
	public enum Voice {
		AMERICAN, NEW_ZEALAND
	}

	private static Voice _currentVoice;

	private static SpeechAndSoundModel _festivalModel;

	private ExecutorService _executorService;

	/*
	 * File paths for Festival
	 */
	private static Path _americanVoiceFilePath;
	private static Path _newZealandVoiceFilePath;
	private static Path _slowPacedVoiceFilePath;	

	private static Path _masteredSpeechSCMFilePath;
	private static Path _failedSpeechSCMFilePath;

	/*
	 * File paths for sound effects
	 */
	private static Path _correctSoundFXFilePath;
	private static Path _wrongSoundFXFilePath;

	private SpeechAndSoundModel() {
		_currentVoice = Voice.AMERICAN; //default voice is the American voice
		_executorService = Executors.newSingleThreadExecutor();
		setupHiddenSoundFiles();
	}

	public static SpeechAndSoundModel getInstance() {
		if (_festivalModel == null) {
			_festivalModel = new SpeechAndSoundModel();
		}
		return _festivalModel;
	}

	public void speakCurrentWord(String currentWord) {
		String speechSCMCmd = "(SayText \"Please spell... ... " + currentWord + "\")";
		String tempFilePath = createAndGetTempSCMFile(speechSCMCmd).toString();
		String accentVoiceFilePath = getCurrentVoiceFilePath().toString();

		String festivalCommand = "festival -b " + accentVoiceFilePath + " " + tempFilePath;
		Runnable workerThread = new SoundModelWorker(festivalCommand);
		_executorService.execute(workerThread);
	}

	public void playCorrectSoundFX(){
		String soundFXCommand = "mpg123 " + _correctSoundFXFilePath;
		Runnable workerThread = new SoundModelWorker(soundFXCommand);
		_executorService.execute(workerThread);		
	}

	public void sayFaultedSpeech(String currentWord){
		//First play the sound effect, then play Festival's speech
		String soundFXCommand = "mpg123 " + _wrongSoundFXFilePath;
		Runnable workerThread = new SoundModelWorker(soundFXCommand);
		_executorService.execute(workerThread);


		String accentVoiceFilePath = getCurrentVoiceFilePath().toString();
		String festivalCommand = "festival -b " + accentVoiceFilePath + " ";

		//Depending on the current voice, minor adjustments to the speech is required for appropriate pauses.
		String speechSCMCmd = null;		
		switch (_currentVoice) {
		case AMERICAN:
			speechSCMCmd = "(SayText \"Try once more...... "+ currentWord + "...... " + currentWord + "\")";
			break;
		case NEW_ZEALAND:
			speechSCMCmd = "(SayText \"Try once more: "+ currentWord + ": " + currentWord + "\")";
			festivalCommand += _slowPacedVoiceFilePath.toString() + " ";
			break;
		}
		String tempFilePath = createAndGetTempSCMFile(speechSCMCmd).toString();
		festivalCommand += tempFilePath;

		workerThread = new SoundModelWorker(festivalCommand);
		_executorService.execute(workerThread);
	}

	public void playWrongSoundFX(){
		String soundFXCommand = "mpg123 " + _wrongSoundFXFilePath;
		Runnable workerThread = new SoundModelWorker(soundFXCommand);
		_executorService.execute(workerThread);		
	}

	public void relistenWord(String currentWord) {
		String speechSCMCmd = "(SayText \"" + currentWord + "\")";
		String tempFilePath = createAndGetTempSCMFile(speechSCMCmd).toString();
		String accentVoiceFilePath = getCurrentVoiceFilePath().toString();

		String festivalCommand = "festival -b " + accentVoiceFilePath + " " + _slowPacedVoiceFilePath.toString() + " " + tempFilePath;
		Runnable workerThread = new SoundModelWorker(festivalCommand);
		_executorService.execute(workerThread);		
	}

	/**
	 * Create the necessary hidden files/folders for Festival commands.
	 * These hidden files/folders are created in the same directory where the VoxSpellApp is executed.
	 * These hidden files/folders are only created if it doesn't already exists
	 * Assumption: if the folder already exists, then the files that the folder is supposed to contain already exist too.
	 * Files hierarchy:
	 * + festival
	 * |	|
	 * |	+ voice
	 * |		|
	 * |		slowPacedVoice.scm
	 * |		americanVoice.scm
	 * |		newZealandVoice.scm
	 * + speech
	 * 		|
	 * 		masteredSpeech.scm
	 * 		failedSpeech.scm 
	 * 
	 * Also set the file paths of the sound effects files.		
	 */
	private void setupHiddenSoundFiles() {
		//Sound effects files
		_correctSoundFXFilePath = Paths.get("./.soundFX/Quiz_SoundEffect_CorrectAnswer.mp3");
		_wrongSoundFXFilePath = Paths.get("./.soundFX/Quiz_SoundEffect_WrongAnswer.mp3");

		//Festival files
		try {
			Path _festivalFolderPath = Paths.get("./.festival");

			Path voiceFolderPath = Paths.get("./.festival/voice");
			_slowPacedVoiceFilePath = Paths.get("./.festival/voice/slowPacedVoice.scm");
			_americanVoiceFilePath = Paths.get("./.festival/voice/americanVoice.scm");
			_newZealandVoiceFilePath = Paths.get("./.festival/voice/newZealandVoice.scm");

			Path speechFolderPath = Paths.get("./.festival/speech");
			_masteredSpeechSCMFilePath = Paths.get("./.festival/masteredSpeech.scm");
			_failedSpeechSCMFilePath = Paths.get("./.festival/failedSpeech.scm");

			if (Files.notExists(_festivalFolderPath)) {
				Files.createDirectory(_festivalFolderPath);

				Files.createDirectory(voiceFolderPath);
				Files.createFile(_slowPacedVoiceFilePath);
				Files.createFile(_americanVoiceFilePath);
				Files.createFile(_newZealandVoiceFilePath);				
				writeSCMCodeToVoiceFiles();

				Files.createDirectory(speechFolderPath);
				Files.createFile(_masteredSpeechSCMFilePath);
				Files.createFile(_failedSpeechSCMFilePath);
				writeSCMCodeToTemplateSpeechFiles();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	/**
	 * This method will create a temporary SCM file to be passed as an argument to Festival commands.
	 * The contents of the file consist of the given speech argument.
	 * @param speech
	 * @return
	 */
	private Path createAndGetTempSCMFile(String speech) {
		try {
			//Create the temp SCM file in a temp folder in ./.festival folder			
			Path tempFolderPath = Paths.get("./.festival/temp");
			Files.createDirectories(tempFolderPath);
			Path tempSCMFilePath = Files.createTempFile(tempFolderPath, "tempSCM", ".scm");
			tempSCMFilePath.toFile().deleteOnExit(); //delete the temp file when application is closed.

			//Write the SCM code that makes Festival say the speech
			ArrayList<String> festivalSpeech = new ArrayList<String>();
			festivalSpeech.add(speech);
			Files.write(tempSCMFilePath, festivalSpeech, StandardCharsets.ISO_8859_1, StandardOpenOption.WRITE);			

			//return result for passing it as a file input for Festival
			return tempSCMFilePath;

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Write the SCM code to the voice files in ./.festival/voice folder
	 */
	private void writeSCMCodeToVoiceFiles() {
		try {
			//For slower-paced voice
			ArrayList<String> slowCommand = new ArrayList<String>();
			slowCommand.add("(Parameter.set 'Duration_Stretch 1.8)"); 
			Files.write(_slowPacedVoiceFilePath, slowCommand, StandardCharsets.ISO_8859_1, StandardOpenOption.APPEND);

			//For American voice
			ArrayList<String> americanVoiceCommand = new ArrayList<String>();
			americanVoiceCommand.add("(voice_kal_diphone)");
			Files.write(_americanVoiceFilePath, americanVoiceCommand, StandardCharsets.ISO_8859_1, StandardOpenOption.APPEND);

			//For NZ Voice
			ArrayList<String> newZealandVoiceCommand = new ArrayList<String>();
			newZealandVoiceCommand.add("(voice_akl_nz_jdt_diphone)");
			Files.write(_newZealandVoiceFilePath, newZealandVoiceCommand, StandardCharsets.ISO_8859_1, StandardOpenOption.APPEND);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Write the SCM code to the speech files (only those that are constant) in ./.festival/speech folder
	 */
	private void writeSCMCodeToTemplateSpeechFiles() {
		try {
			//For mastered
			String masteredSpeechSCMCmd = "(SayText \"Correct!\")";
			ArrayList<String> masteredSpeech = new ArrayList<String>();
			masteredSpeech.add(masteredSpeechSCMCmd);
			Files.write(_masteredSpeechSCMFilePath, masteredSpeech, StandardCharsets.ISO_8859_1, StandardOpenOption.WRITE);

			//For failed
			String failedSpeechSCMCmd = "(SayText \"Incorrect!\")";
			ArrayList<String> failedSpeech = new ArrayList<String>();
			failedSpeech.add(failedSpeechSCMCmd);
			Files.write(_failedSpeechSCMFilePath, failedSpeech, StandardCharsets.ISO_8859_1, StandardOpenOption.WRITE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Path getCurrentVoiceFilePath() {

		Path accentVoiceFilePath = null;

		switch(_currentVoice) {
		case AMERICAN:
			accentVoiceFilePath = _americanVoiceFilePath;
			break;
		case NEW_ZEALAND:
			accentVoiceFilePath = _newZealandVoiceFilePath;
			break;
		}

		return accentVoiceFilePath;
	}

	public void changeVoice(Voice newVoice) {
		_currentVoice = newVoice;		
	}

	/**
	 * A runnable object that ensures Festival speeches and sound effects do not overlap
	 */
	private class SoundModelWorker implements Runnable {

		String _festivalCommand;

		public SoundModelWorker(String festivalCommand) {
			_festivalCommand = festivalCommand;
		}

		@Override
		public void run() {
			ProcessBuilder pb = new ProcessBuilder("bash", "-c", _festivalCommand);
			Process process;
			try {
				process = pb.start();
				process.waitFor();
				process.destroy();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
