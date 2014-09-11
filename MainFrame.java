import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;



public class MainFrame extends JFrame{
	
	private JTabbedPane _tabbedpane;
	private VideoPlayer _player;
	
	public MainFrame(){
		
		setLayout(new BorderLayout());
        _player = new VideoPlayer("F:\\Music\\PV's\\Packaged-Irony.mp4");
 		//_player.setPreferredSize(new Dimension (850,350));
		
		_tabbedpane= new JTabbedPane();
		_tabbedpane.add(new JPanel());
		_tabbedpane.setPreferredSize(new Dimension(100,300));
		
		add(_player,BorderLayout.CENTER);
		add(_tabbedpane,BorderLayout.SOUTH);
		
		setPreferredSize(new Dimension(1000, 700));
		pack();
		
		this.setVisible(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		_player.playMedia();
		
	}
	
	public static void main(String[] args) {
		 NativeLibrary.addSearchPath(
                 RuntimeUtil.getLibVlcLibraryName(), "C:\\Program Files\\VideoLAN\\VLC"
             );
             Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
		MainFrame main= new MainFrame();

	}
	
	
}
