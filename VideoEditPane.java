package Vamix206;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingWorker;


public class VideoEditPane extends Panel{

	JButton _stripAudio;
	File _playingFile;
	JTextField _output;
	
	
	public VideoEditPane(File playing){
		
		_playingFile= playing;
		_stripAudio = new JButton ("Start Extract");
		_output = new JTextField("Enter output filename");
		
		
	}

}
