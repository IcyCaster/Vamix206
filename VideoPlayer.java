
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

public class VideoPlayer extends JPanel {

        private EmbeddedMediaPlayerComponent _mediaPlayer;
        private JButton _play;
        private JButton _fastForward;
        private JButton _fastBack;
        private String _mediaName; 
        private JProgressBar _seekBar;
        private EmbeddedMediaPlayer _video;
        private Timer _progress;
        
        public VideoPlayer(String VidName) {
        	
        	setLayout(new FlowLayout());
        	
        	_play=new JButton("Play/Pause");
       		_fastForward=new JButton("FastForward");
       		_fastBack=new JButton("FastBack");
        	_mediaName = VidName;
        	_seekBar= new JProgressBar();
        	
        	_mediaPlayer = new EmbeddedMediaPlayerComponent();
        	_mediaPlayer.setPreferredSize(new Dimension(600,325));
        	_seekBar.setPreferredSize(new Dimension(200,20));
        	_video = _mediaPlayer.getMediaPlayer();
        	
        	_play.addActionListener(new PauseHandler());
        	_seekBar.addMouseListener(new ForwardHandler());
        	
        	_progress= new Timer(200, new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					
					int i = (int)(_video.getPosition()*100);
					_seekBar.setValue(i);
				}
        		
        	});
        	
        	add(_seekBar);
            add(_mediaPlayer);
            add(_play);
          //  add(_fastForward);
       //     add(_fastBack);
            
       }
        
        

        public void playMedia(){
        	
        	_mediaPlayer.getMediaPlayer().playMedia(_mediaName);
        	_progress.start();
        }
        
        class PauseHandler implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				if(_video.getPosition()>=1){
					_video.playMedia(_mediaName);
				}
				else{
					_video.pause();
				}
			}
				
        }
        
        class ForwardHandler implements MouseListener{

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				
				float f = (float)(e.getX())/(float) (_seekBar.getWidth());
				_video.setPosition(f);
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
        	
        }
 }

