package voxspell.videoplayer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
import javax.swing.border.MatteBorder;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

/**
 * A video player that has an embedded media player using the vlcj external library
 * The video player plays a custom made video.
 * The custom made video is a slide show with a song playing in the background.
 * Each slide in the slide show is a single word that appears to be as though it's 'one line below the previous' word.
 * 
 * Code adapted from assignment 3 and Nasser Giacaman's ACP exercise
 * @author echa232
 *
 */
public class VideoPlayer {

	private EmbeddedMediaPlayerComponent _mediaPlayerComponent;
	private EmbeddedMediaPlayer _video;

	private JFrame _videoFrame;
	
	private JDialog _loadingMsgDialog;

	private JPanel _vlcButtons;
	private JButton _pauseBtn;
	private JButton _btnMute;
	private JButton _btnSkip;
	private JButton _btnSkipBack;

	private VideoWorker _worker; //A background thread for creating the video without freezing the GUI
	private ArrayList<String> _videoContent; //A list of words that will appear as the content of the video

	String outputVideo = "./.video/outputVideo.mp4"; //The path where the custom video will be at
	ArrayList<Path> slideFilePaths; //A list of paths. Each path leads to a single slide to be put into the slide show video

	public VideoPlayer(ArrayList<String> contentForVideo) {
		_videoContent = contentForVideo;
		setupMediaPlayer();
		setupVideoGUI();	
	}

	private void setupMediaPlayer() {
		NativeLibrary.addSearchPath(
				RuntimeUtil.getLibVlcLibraryName(), "/usr/lib/" 
				);
		Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);


		_mediaPlayerComponent = new EmbeddedMediaPlayerComponent();

		_video = _mediaPlayerComponent.getMediaPlayer();

		_video.addMediaPlayerEventListener(

				new MediaPlayerEventAdapter() {
					@Override
					public void finished (MediaPlayer mediaPlayer) {
						deleteVideoFiles();
						_videoFrame.dispose();
					}
				});
	}

	private void setupVideoGUI() {
		_videoFrame = new JFrame("End of Quiz Video");

		_videoFrame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				_video.stop();
				deleteVideoFiles();
				_videoFrame.dispose();
			}
		});

		_videoFrame.setLayout(new BorderLayout());

		_videoFrame.setContentPane(_mediaPlayerComponent);

		FlowLayout flowLayout = new FlowLayout();
		flowLayout.setAlignment(FlowLayout.CENTER); 

		_vlcButtons = new JPanel();
		_vlcButtons.setLayout(flowLayout);
		_vlcButtons.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

		setupBtnsGUIAndFunctions();

		//add vlcButtons panel to frame
		_videoFrame.add(_vlcButtons,BorderLayout.NORTH);
		_videoFrame.setSize(560,469);
		_videoFrame.setContentPane(_mediaPlayerComponent);
		_videoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		_videoFrame.setLocationRelativeTo(null);
		_videoFrame.setVisible(true);
	}

	private void setupBtnsGUIAndFunctions() {
		// add pause button
		_pauseBtn = new JButton("Pause");
		_pauseBtn.setForeground(Color.BLACK);
		_pauseBtn.setFont(new Font("Garuda", Font.BOLD | Font.ITALIC, 14));
		_pauseBtn.setBorder(new MatteBorder(1, 5, 1, 1, (Color) new Color(0, 102, 0)));
		_pauseBtn.setBackground(Color.WHITE);
		_pauseBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				_video.pause();
				if (_pauseBtn.getText().equals("Pause")) {
					_pauseBtn.setText("Play");
				} else {
					_pauseBtn.setText("Pause");
				}
			}
		});
		_vlcButtons.add(_pauseBtn);

		//add mute button
		_btnMute = new JButton("Mute");
		_btnMute.setForeground(Color.BLACK);
		_btnMute.setFont(new Font("Garuda", Font.BOLD | Font.ITALIC, 14));
		_btnMute.setBorder(new MatteBorder(1, 5, 1, 1, (Color) new Color(0, 102, 0)));
		_btnMute.setBackground(Color.WHITE);
		_btnMute.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_video.mute();
			}
		});
		_vlcButtons.add(_btnMute);

		// add skip forward button
		_btnSkip = new JButton("Skip Forward");
		_btnSkip.setForeground(Color.BLACK);
		_btnSkip.setFont(new Font("Garuda", Font.BOLD | Font.ITALIC, 14));
		_btnSkip.setBorder(new MatteBorder(1, 5, 1, 1, (Color) new Color(0, 102, 0)));
		_btnSkip.setBackground(Color.WHITE);
		_btnSkip.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_video.skip(5000);
			}
		});
		_vlcButtons.add(_btnSkip);

		//add skip backward button
		_btnSkipBack = new JButton("Skip Backward");
		_btnSkipBack.setForeground(Color.BLACK);
		_btnSkipBack.setFont(new Font("Garuda", Font.BOLD | Font.ITALIC, 14));
		_btnSkipBack.setBorder(new MatteBorder(1, 5, 1, 1, (Color) new Color(0, 102, 0)));
		_btnSkipBack.setBackground(Color.WHITE);
		_btnSkipBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_video.skip(-5000);
			}
		});
		_vlcButtons.add(_btnSkipBack);
	}

	/**
	 * Plays the video with a given file name
	 * @param filename
	 */
	public void playVideo(){
		_worker = new VideoWorker();
		_worker.execute();
	}

	/**
	 * Deletes the temporary video files made.
	 */
	private void deleteVideoFiles() {
		try {
			Files.deleteIfExists(Paths.get(outputVideo)); //deleting the video

			for (int i = 0; i < slideFilePaths.size(); i++) {
				Files.deleteIfExists(slideFilePaths.get(i)); //deleting the text slides files
				Files.deleteIfExists(Paths.get(slideFilePaths.get(i) + ".png")); //deleting the images slides files
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Swing Worker class that creates the video in the background so that the GUI does not freeze during the process
	 */
	private class VideoWorker extends SwingWorker<Void, Void>{

		@Override
		protected Void doInBackground() {
			//a pop up pane  that tells the user the video is under process, expect the user to wait until process is finished
			_loadingMsgDialog = new JDialog();
			_loadingMsgDialog.setTitle("Loading");
			_loadingMsgDialog.setResizable(false);

			JOptionPane optionPane = new JOptionPane("Loading video... \nPlease wait.", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
			_loadingMsgDialog.add(optionPane, BorderLayout.NORTH);

			JProgressBar progressBar = new JProgressBar();
			progressBar.setIndeterminate(true);
			_loadingMsgDialog.add(progressBar, BorderLayout.SOUTH);

			_loadingMsgDialog.pack();
			_loadingMsgDialog.setLocationRelativeTo(null);
			_loadingMsgDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			_loadingMsgDialog.setVisible(true);

			createWordsAsSlides(_videoContent);
			createVideoFile();
			return null;
		}

		@Override
		protected void done() {
			//close dialog
			_loadingMsgDialog.dispose();
			//play the manipulated video
			_video.playMedia(outputVideo);
		}

		/**
		 * Given a list of words, this method creates a .png image slide for each word that will contribute to the video later.
		 * @param contentForVideo
		 */
		private void createWordsAsSlides(ArrayList<String> contentForVideo) {

			try {
				//following the arranged order of the array list, create a slide containing the string content
				slideFilePaths = new ArrayList<Path>();
				int yPositionOffsetOfSlideText = 50; //y position offset of slide text begins at 50pt 
				for (int i = 0; i < contentForVideo.size(); i++) {
					int slideCount = i;
					Path filePath = Paths.get("./.video/slide" + slideCount);
					slideFilePaths.add(filePath);
					Files.createFile(filePath);

					String fileContent = "text 50," + yPositionOffsetOfSlideText + "\"" + contentForVideo.get(i) + "\"";
					Files.write(filePath, fileContent.getBytes(), StandardOpenOption.APPEND);

					String command = "convert -size 600x460 xc:white -font \"FreeMono\" -pointsize 36 -fill black -draw @" 
							+ filePath + " " + filePath + ".png";

					ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
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

					//increment y position offset so that next slide's text appears as though it's 'one line below' the previous slide's text
					yPositionOffsetOfSlideText += 36; 
				}


			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

		/**
		 * After creating a list of image slides, create the video as a slide show with a song playing in the background.
		 */
		private void createVideoFile() {
			String command = "ffmpeg -framerate 1/3 -i ./.video/slide%d.png -i ./.video/bgMusic.mp3 -c:v libx264 -c:a copy -vf fps=25 -pix_fmt yuv420p " + outputVideo;

			ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
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
