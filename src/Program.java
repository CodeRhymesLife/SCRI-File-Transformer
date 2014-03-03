import java.awt.Frame;

import javax.swing.JFrame;


public class Program {

	public static void main(String[] args) {
		// Create our frame and set defaults
		MainJFrame frame = new MainJFrame();
		frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Show frame
		frame.setVisible(true);
	}

}
