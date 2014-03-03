

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JLabel;

public class MainJFrame extends JFrame {
	// Title for frame
	private final static String Title = "SCRI File Transformer";
		
	public MainJFrame() {
		// Set the title
		super(Title);
		
		final JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		final ArrayList<FindReplacePair> findReplacePairs = new ArrayList<FindReplacePair>();
		findReplacePairs.add(new FindReplacePair("13835-S", "13835_CS_S"));
		
		JButton btnSelectFilesTo = new JButton("Click Me To Transform Files");
		getContentPane().add(btnSelectFilesTo, BorderLayout.CENTER);
		btnSelectFilesTo.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int returnVal = fileChooser.showDialog(MainJFrame.this, "Select The Folder Containing Files To Transform");
				
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File directory = fileChooser.getSelectedFile();
					
					Path outputDir = Paths.get(directory.getAbsolutePath(), "TransformedFiles");
					
					try {
						FileFindAndReplace.Run(Arrays.asList(directory.listFiles()),
								findReplacePairs,
								outputDir.toString());
						
						// If Desktop is supported open the output dir
						if (Desktop.isDesktopSupported()) {
							File outputDirFile = new File(outputDir.toString());
						    Desktop.getDesktop().open(outputDirFile);
						}
						// Otherwise, show a dialog
						else
						{
							JOptionPane.showMessageDialog(null, "Files replaced successfully. New files are located in " + outputDir.toString(), "Success", JOptionPane.INFORMATION_MESSAGE);
						}
					} catch (IOException e) {
						JOptionPane.showMessageDialog(null, "Oops. Something bad happened. Please contact rcjames1004@gmail.com for assistance.", "Ugh Oh", JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
		});
		
		JLabel lblThisApplicationSearches = new JLabel("This application transforms datastat files into a format usable by SCRI. Currently it only updates participant ids. For example, \"13835-S01\" will become \"13835_CS_S01\" ");
		getContentPane().add(lblThisApplicationSearches, BorderLayout.NORTH);
	}
}
