package Vamix206;


import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.ImageIcon;


public class VideoEditPane extends Panel{

	JButton _stripAudio;
	File _playingFile;
	JTextField _output;
	JLabel _loading;
	
	public VideoEditPane(){

		ImageIcon animation =  new ImageIcon("/home/frankie/ajax-loader.gif");
		
		_stripAudio = new JButton ("Start Extract");
		_output = new JTextField("Enter output filename");
		_loading = new JLabel(animation);
		_loading.setVisible(false);
		
		_output.setPreferredSize(new Dimension(230,30));
		_stripAudio.addActionListener(new extractHandler());
		
		add(_stripAudio);
		add(_output);
		add(_loading);
	}
	
	public void update(File playingFile){
		_playingFile=playingFile;
	}
	
	class extractHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				//Check if output file already exists, if yes then it asks whether or not the
				//user wants to overwrite
				Process oProcess=runBashCommand("[ -e "+"\""+_output.getText()+"\""+" ]");
				oProcess.waitFor();
				if (oProcess.exitValue()!=0){
					if(_playingFile!=null){
						_stripAudio.setEnabled(false);
						_output.setEnabled(false);
						exWorker ex=new exWorker(false);
						ex.execute();
					}
				}
				//Performs the overwrite functionality
				else if(oProcess.exitValue()==0 && _output.getText()!=null) {
					Object[] options = {"Override", "Cancel"};
					
					int selection = JOptionPane.showOptionDialog(_output,
					"File with this name already exists","Error ecountered",
					JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE,null,options,options[0]);
					if(selection == JOptionPane.YES_OPTION){
						_stripAudio.setEnabled(false);
						exWorker ex=new exWorker(true);
						ex.execute();
					}
				}
			} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
	
	class exWorker extends SwingWorker<Void,Integer>{
		boolean _overwrite;
		Process _toDoProcess;
		 
		public exWorker(boolean over){
			_overwrite = over;
		}
		
		@Override
		protected Void doInBackground()  {
			try {
				//Performs the extract action
				if(!_overwrite){

				_loading.setVisible(true);
				_toDoProcess = runBashCommand("avconv -i "+ "/"+_playingFile.getPath()+ " "+ _output.getText()+".mp3");
				_toDoProcess.waitFor();
				}
				//Performs the overwriting action
				else{

				_loading.setVisible(true);
				_toDoProcess = runBashCommand("avconv -y -i "+ "/"+_playingFile.getPath()+ " "+ _output.getText()+".mp3");
				_toDoProcess.waitFor();
				}
			} catch (Exception e) {
				e.printStackTrace();
				}
			
			return null;
		}
		
		protected void done(){
			_loading.setVisible(false);
			_stripAudio.setEnabled(true);
			_output.setEnabled(true);
			if(_toDoProcess.exitValue()==0){
				JOptionPane.showMessageDialog(_stripAudio, "Extract successful");
				}
			else {JOptionPane.showMessageDialog(_stripAudio, "Extract failed");}
			}
		}

}


// When stripping audio: _video.getAudioTrackCount()
// To check that the value is greater than 0. As you should throw an error "ERROR: No Audio Signal!" if there is no audio.