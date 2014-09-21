package Vamix206;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class TextEditPane extends Panel {
	
	
	JComboBox _openingOrClosing = null;
	JTextField _timePeriodOfText = null;
	JComboBox _fontSelection = null;
	JComboBox _sizeSelection = null;

	
	public TextEditPane() {
		
		// JLabel blankLabel = new JLabel();
		
		GridLayout textEditLayout = new GridLayout(2,8);
		setLayout(textEditLayout);
		
		// Option to add to opening or closing scene.
		add(new JLabel("Add text to the:"));
		String[] sceneOptions = new String[3];
		sceneOptions[0] = ("-Select-");
		sceneOptions[1] = ("Opening Scene");
		sceneOptions[2] = ("Closing Scene");
		_openingOrClosing = new JComboBox(sceneOptions);
		_openingOrClosing.setPreferredSize(new Dimension(20,100));
		add(_openingOrClosing);
		
		// Time length of draw text.
		add(new JLabel("Length of time to display text: (seconds)"));
		_timePeriodOfText = new JTextField(5);
		add(_timePeriodOfText);
		
		// Select the font you would like.
		add(new JLabel("Select a font:"));
		String[] fontOptions = new String[7];
		fontOptions[0] = ("Default");
		fontOptions[1] = ("Mono (Regular)");
		fontOptions[2] = ("Mono (Bold)");
		fontOptions[3] = ("Sans (Regular)");
		fontOptions[4] = ("Sans (Bold)");
		fontOptions[5] = ("Serif (Regular)");
		fontOptions[6] = ("Serif (Bold)");
		_fontSelection = new JComboBox(fontOptions);
		add(_fontSelection);
		
		add(new JLabel("Select a size:"));
		String[] sizeOptions = new String[65];
		for(int i = 8; i < 73; i++) {
			sizeOptions[i-8] = Integer.toString(i);
		}
		_sizeSelection = new JComboBox(sizeOptions);
		add(_sizeSelection);
		
		
		
		
		
		// CHECK ON THE LAB IF THESE WORK! THESE ARE FROM MY COMPUTER SO I AM NOT SURE!
		
		
		
		
		// All one command. Don't know how to add it to a certain time period.
		//avconv -i test.mp4 -vf "drawtext=fontfile='/usr/share/fonts/truetype/freefont/FreeSans.ttf':
		//text='test if this does it for a period':fontsize=60:fontcolor=white" -strict experimental out.mp4

		
	}
	
}
