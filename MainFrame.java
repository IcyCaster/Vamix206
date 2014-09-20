package Vamix206;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;



public class MainFrame extends JFrame{

	private JPanel _topFeatures;
	private JTabbedPane _tabbedpane;

	private JPanel _bigCircleButtons; 

//	private RoundButton _selectionButton; 
//	private RoundButton _downloadButton; 
//	private RoundButton _videoEditButton;
//	private RoundButton _textEditButton;
//	private RoundButton _saveButton; 
	private JButton _selectionButton; 
	private JButton _downloadButton; 
	private JButton _videoEditButton;
	private JButton _textEditButton;
	private JButton _saveButton; 

	//Index positions of all the panels in the tabbed pane
	private int _downloadIndex;
	private int _videoEditIndex;
	private int _textEditIndex;
	
	private JFileChooser _chooser = new JFileChooser();
	
	private VideoPlayer _completeVideoPlayer;

	private File _playingFile;
	// Images for the circle buttons
	private ImageIcon _circleImageOne =  new ImageIcon("/home/frankie/FastForward.png");
	private ImageIcon _circleImageTwo =  new ImageIcon();
	private ImageIcon _circleImageThree =  new ImageIcon();
	private ImageIcon _circleImageFour =  new ImageIcon();
	private ImageIcon _circleImageFive =  new ImageIcon();
	
	
	public MainFrame(){

		// A panel containing button panel on the left, and the complete video player on the right.
		_topFeatures = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		_topFeatures.setPreferredSize(new Dimension(100 + 640,100 + 360));
		add(_topFeatures, BorderLayout.NORTH);

		// A tabbed pane which consists of the Download, Edit Video and Edit Text tabs.
		_tabbedpane = new JTabbedPane();
		_tabbedpane.setPreferredSize(new Dimension(100 + 640,250));
		add(_tabbedpane, BorderLayout.SOUTH);
		
		//Initialise download pane
		final DownloadPane downloadPane = new DownloadPane();
		_tabbedpane.addTab("Download",downloadPane);
		_downloadIndex = _tabbedpane.indexOfTab("Download");
		
		final TextEditPane videoEditPane = new TextEditPane();
		_tabbedpane.addTab("Video Edit", videoEditPane);
		_videoEditIndex = _tabbedpane.indexOfTab("Video Edit");
		
		final TextEditPane textEditPane = new TextEditPane();
		_tabbedpane.addTab("Text Edit", textEditPane);
		_textEditIndex = _tabbedpane.indexOfTab("Text Edit");

		// A panel containing the five buttons: Select Media, Download Media, Edit Media, Edit Text, Save.
		_bigCircleButtons = new JPanel(new GridLayout(5,1,0,0));
		_bigCircleButtons.setPreferredSize(new Dimension(100,460));
		_bigCircleButtons.setBackground(Color.LIGHT_GRAY);
		_topFeatures.add(_bigCircleButtons, BorderLayout.WEST);

		// A panel containing the actual media player at the top, a progress bar in the middle and a series of buttons at the bottom.
		_completeVideoPlayer = new VideoPlayer();
		_completeVideoPlayer.setPreferredSize(new Dimension(640,460));
		_topFeatures.add(_completeVideoPlayer, BorderLayout.EAST);
		
		// Creating the circle buttons.
//		_selectionButton = new RoundButton(_circleImageOne);
//		_downloadButton = new RoundButton(_circleImageOne);
//		_videoEditButton = new RoundButton(_circleImageOne);
//		_textEditButton = new RoundButton(_circleImageOne);
//		_saveButton = new RoundButton(_circleImageOne);
		_selectionButton = new JButton("<html><center>Select<br>Media</center></html");
		_downloadButton = new JButton("<html><center>Download<br>Media</center></html");
		_videoEditButton = new JButton("<html><center>Edit the<br>Video</center></html");
		_textEditButton = new JButton("<html><center>Edit the<br>Text</center></html");
		_saveButton = new JButton("<html><center>Save<br>Media</center></html");
		

		_selectionButton.addActionListener(new chooserHandler());
		
		// Adding the circle buttons.
		_bigCircleButtons.add(_selectionButton, BorderLayout.CENTER);
		_bigCircleButtons.add(_downloadButton, BorderLayout.CENTER);
		_bigCircleButtons.add(_videoEditButton, BorderLayout.CENTER);
		_bigCircleButtons.add(_textEditButton, BorderLayout.CENTER);
		_bigCircleButtons.add(_saveButton, BorderLayout.CENTER);

		

		// Implementing the buttons functions.
//		_selectionButton.addActionListener(new ActionListener(){
//			@Override
//			public void actionPerformed(ActionEvent arg0) {
//				// TODO Auto-generated method stub
//
//			}
//		});

		_downloadButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				_tabbedpane.setSelectedIndex(_downloadIndex);

			}
		});

		_videoEditButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				_tabbedpane.setSelectedIndex(_videoEditIndex);

			}	
		});

		_textEditButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				_tabbedpane.setSelectedIndex(_textEditIndex);

			}
		});

		_saveButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub

			}	

    	});


		// Completing the Mainframe setup.
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//this.setResizable(false);
		this.setVisible(true);
		pack();


	}
	
	class chooserHandler implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
	
			int returnVal=_chooser.showOpenDialog(_completeVideoPlayer);
			
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				
				_playingFile = _chooser.getSelectedFile();
				try {
					//Check if file is valid or not, and if it isn't then complain
					Process checkFile= Panel.runBashCommand("file -ib "+"\""+_playingFile.getPath()+"\""+" | grep \"video\\|audio\"");
					checkFile.waitFor();
					if (checkFile.exitValue()!=0)
						 JOptionPane.showMessageDialog(_completeVideoPlayer, "Please select a media file");
					else{
						_completeVideoPlayer.playMedia(_playingFile.getPath());
					}
						
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
