package Vamix206;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

public class VideoPlayer extends JPanel {

	private EmbeddedMediaPlayerComponent _videoPlayer; 
	private EmbeddedMediaPlayer _video;

	private JProgressBar _videoPlayerBar;
	private JPanel _videoPlayerFunctions;

	private JButton _fullScreenButton;
	private RoundButton _rewindButton; 
	private RoundButton _playPauseButton; 
	private RoundButton _fastforwardButton; 
	private JButton _muteButton; 
	private JSlider _volumeControl;

	private String _videoName; 

	private Timer _progress;

	public VideoPlayer(String videoName) {

		ImageIcon _circleButtonIcon =  new ImageIcon("/home/chester/Desktop/circle.png"); // Test Circle image.



		_videoName = videoName;
		setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

		// Adds the media player component.
		_videoPlayer = new EmbeddedMediaPlayerComponent();
		_videoPlayer.setPreferredSize(new Dimension(640,360));
		add(_videoPlayer, BorderLayout.NORTH);
		_video = _videoPlayer.getMediaPlayer();

		// Adds the video time bar.
		_videoPlayerBar = new JProgressBar();
		_videoPlayerBar.setPreferredSize(new Dimension(640,30));
		add(_videoPlayerBar);

		// Adds the panel which holds all the buttons.
		_videoPlayerFunctions = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		_videoPlayerFunctions.setPreferredSize(new Dimension(640,70));
		_videoPlayerFunctions.setBackground(Color.CYAN);
		add(_videoPlayerFunctions, BorderLayout.SOUTH);

		// Clicking on the progress bar.
		_progress = new Timer(200, new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				int i = (int)(_video.getPosition()*100);
				_videoPlayerBar.setValue(i);
			}
		});

		// Creating the buttons.
		_fullScreenButton = new JButton("FullScreen");
		_rewindButton = new RoundButton(_circleButtonIcon);
		_playPauseButton = new RoundButton(_circleButtonIcon);
		_fastforwardButton = new RoundButton(_circleButtonIcon);
		_muteButton = new JButton("Mute");
		_volumeControl = new JSlider(0,200);

		// Adding the buttons.
		_videoPlayerFunctions.add(_fullScreenButton);
		_videoPlayerFunctions.add(_rewindButton);
		_videoPlayerFunctions.add(_playPauseButton);
		_videoPlayerFunctions.add(_fastforwardButton);
		_videoPlayerFunctions.add(_muteButton);
		_videoPlayerFunctions.add(_volumeControl);

		// Adding the Listeners to the buttons.
		_playPauseButton.addActionListener(new PauseHandler());
		_videoPlayerBar.addMouseListener(new ForwardHandler());
		_muteButton.addActionListener(new MuteHandler());
		_volumeControl.addChangeListener(new VolumeHandler());
		_volumeControl.addMouseListener(new VolumeSeekHandler());

		_rewindButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				_video.skip(-7000);
			}
		});

		_fastforwardButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				_video.skip(7000);
			}
		});













	}

	// Method which plays the selected media.
	public void playMedia(){
		_videoPlayer.getMediaPlayer().playMedia(_videoName);
		_progress.start();
	}

	// Pause/Plays the video.
	class PauseHandler implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {

			if(_video.getPosition() >= 1){
				_video.playMedia(_videoName);
			}
			else{
				_video.pause();
			}
		}
	}

	// Fast forwards the video.
	class ForwardHandler extends MouseAdapter{
		@Override
		public void mousePressed(MouseEvent e) {

			float f = (float)(e.getX())/(float) (_videoPlayerBar.getWidth());
			_video.setPosition(f);
		}
	}

	// Mutes the video.
	class MuteHandler implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {
			_video.mute();
		}
	}

	// Sets the volume.
	class VolumeSeekHandler extends MouseAdapter{
		public void mousePressed(MouseEvent e) {
			_volumeControl.setValue(e.getX());
		}
	}
	class VolumeHandler implements ChangeListener{
		@Override
		public void stateChanged(ChangeEvent e) {
			JSlider source = (JSlider)e.getSource();
			_video.setVolume(source.getValue());

		}
	}
}
