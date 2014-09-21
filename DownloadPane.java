package Vamix206;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingWorker;


public class DownloadPane extends Panel{
	
	JTextField _url;
	JCheckBox _openSource;
	JButton _dlButton;
	JButton _cancelButton;
	JProgressBar _bar;
	InnerWorker _inner;
	
	
	public DownloadPane(){
		
		_inner = null;
		_url = new JTextField();
		_openSource = new JCheckBox("Is this open source?");
		_dlButton = new JButton("Download");
		_cancelButton = new JButton("Cancel");
		_bar = new JProgressBar(0,100);
		_bar.setValue(0);
		_bar.setStringPainted(true);
		_dlButton.setEnabled(false);
		
		setLayout(new FlowLayout());
		_url.setPreferredSize(new Dimension(250,30));
		_dlButton.addActionListener(new dlButtonListener());
		_bar.setPreferredSize(new Dimension(250,20));
		_openSource.addActionListener(new boxHandler());
		_cancelButton.addActionListener(new cancelHandler());
		
		add(_url);
		add(_openSource);
		add(_dlButton);
		add(_cancelButton);
		add(_bar);
		
	}
	
	class dlButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			
			try {
				//Checks whether or not the file already exists
				String url=_url.getText();
				Process sProcess=runBashCommand("basename "+"\""+url+"\"");
				sProcess.waitFor();
				InputStream stdout = sProcess.getInputStream();
				BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));
				String name = stdoutBuffered.readLine();
				
				sProcess=runBashCommand("[ -e "+"\""+name+"\""+" ]");
				sProcess.waitFor();
				
				//Performs download when the file doesn't already exist
				if(sProcess.exitValue()!=0){
					
					_inner=new InnerWorker(new ProcessBuilder("wget","--progress=dot",url));
					_inner.execute();
					_dlButton.setEnabled(false);
					_openSource.setEnabled(false);
				}
				
				//Implementation for when file already exists
				else{
					//Create JOptionPane to ask for user decision
					Object[] options = {"Overwrite","Continue","Cancel"};
					 int output = JOptionPane.showOptionDialog(_url, "Please pick an option"
				               ,"File already exists" ,JOptionPane.YES_NO_CANCEL_OPTION,
				               JOptionPane.QUESTION_MESSAGE,null,options,options[2]);

				            if(output == JOptionPane.YES_OPTION){
					            //Implementation for overwrite
								Process temppro=runBashCommand("rm "+"\""+name+"\"");
								temppro.waitFor();
					            _inner=new InnerWorker(new ProcessBuilder("wget","--progress=dot",url));
					            _inner.execute();
					            _dlButton.setEnabled(false);
								_openSource.setEnabled(false);
							
				            }else if(output == JOptionPane.NO_OPTION){
					            //Implementation for continue	
					            _inner=new InnerWorker(new ProcessBuilder("wget","--progress=dot","-c",url));
							    _inner.execute();
							    _dlButton.setEnabled(false);
								_openSource.setEnabled(false);
				            }
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			};
		}
	}
	
	class cancelHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			_inner.cancelWork();
			
		}

	}
	class boxHandler implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (_dlButton.isEnabled()) {
				_dlButton.setEnabled(false); 
			}
			else {
				_dlButton.setEnabled(true);
			}
		}
	}
	
	
	 class InnerWorker extends SwingWorker<Void,String>{
		 
		 Process sProcess;
		 ProcessBuilder _psb;
		 
			@Override
			protected Void doInBackground() throws Exception {
				//Starts the download and reads the output, calling find Percentage to publish it
				sProcess = _psb.start();
				InputStream stderr = sProcess.getErrorStream();
				BufferedReader stderrRead = new BufferedReader(new InputStreamReader(stderr));
				String line=null;
				
				while ((line = stderrRead.readLine()) != null ) {
					findPercentage(line);
				}
		
				return null;
			}
			
			//Formats the output string into numbers only and publishes them
			public void findPercentage(String line){
				Pattern p= Pattern.compile("\\d{1,}%");
				Matcher m= p.matcher(line);
				if(m.find()){
				String percent=m.group(0).replace("%", "");
				publish(percent);
				}
			}
			
			protected void done(){
			//Shows appropriate message depending on whether the download was successful or not
			//and sets the buttons to be active again
			if (sProcess.exitValue()==0){
				JOptionPane.showMessageDialog(_url, "Download Successful");
				_dlButton.setEnabled(true);
				_openSource.setEnabled(true);
				_bar.setValue(0);
				
			}
			else {
				//If a problem occurred, send out an appropriate message and set the buttons to active
				JOptionPane.showMessageDialog(_url, "Error occured");
				_dlButton.setEnabled(true);
				_openSource.setEnabled(true);	
			}
		}
		
		protected void process(List<String> chunks){
			for (int i=0;i<chunks.size();i++){
				String s=chunks.get(i);
				_bar.setValue(Integer.parseInt(s));
			}
		}
			
		public InnerWorker( ProcessBuilder psb){
			sProcess=null;
			_psb=psb;
		}
		
		public void cancelWork(){
			//Destroys the process and sets the buttons to be active again
			if (sProcess!=null){
				_bar.setValue(0);
				sProcess.destroy();
				_dlButton.setEnabled(true);
				_openSource.setEnabled(true);
			}
		}
	 }
}





	
