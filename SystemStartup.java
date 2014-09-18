package Vamix206;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class SystemStartup {

	public static void main(String[] args) {

		try {
			// Sets the look-and feel of the system.
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");

		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {

			// Catches all the stack traces.
			e.printStackTrace();
		}

		// Creates the new mainframe.
		new MainFrame();

	}
}
