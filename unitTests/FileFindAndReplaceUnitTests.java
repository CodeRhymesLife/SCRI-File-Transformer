import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;


public class FileFindAndReplaceUnitTests {
	@Rule
	public TemporaryFolder temp = new TemporaryFolder();

	
	@Test (expected = NullPointerException.class)
	public void Run_NullFiles_VerifyNullPointerException() throws IOException {
		FileFindAndReplace.Run(null, new ArrayList<FindReplacePair>(), "");
	}

	@Test (expected = NullPointerException.class)
	public void Run_NullFindReplacePairs_VerifyNullPointerException() throws IOException {
		FileFindAndReplace.Run(new ArrayList<File>(), null, "");
	}
	
	@Test (expected = NullPointerException.class)
	public void Run_NullDestination_VerifyNullPointerException() throws IOException {
		FileFindAndReplace.Run(new ArrayList<File>(), new ArrayList<FindReplacePair>(), null);
	}
	
	@Test
	public void Run_EmptyFilesArray_VerifyEmptyFilesArrayReturned() throws IOException {
		ArrayList<File> newFiles = FileFindAndReplace.Run(new ArrayList<File>(), new ArrayList<FindReplacePair>(), temp.newFolder().getAbsolutePath());
		
		assertEquals("Verify no new files were created",
				0,
				newFiles.size());
	}
	
	@Test
	public void Run_PassInOneFile_VerifyOneNewFileCreated() throws IOException {
		File testFile = temp.newFile("testFile.txt");
		
		ArrayList<File> files = new ArrayList<File>();
		files.add(testFile);
		
		String destinationFolder = Paths.get(testFile.getParent(), "temp").toString();
		ArrayList<File> newFiles = FileFindAndReplace.Run(files, new ArrayList<FindReplacePair>(), destinationFolder);
		
		assertEquals("Verify 1 new file created",
				1,
				newFiles.size());
	}
	
	@Test
	public void Run_PassInOneFile_VerifyOneNewFileCreatedInDestination() throws IOException {
		File testFile = temp.newFile("testFile.txt");
		
		ArrayList<File> files = new ArrayList<File>();
		files.add(testFile);
		
		String destinationFolder = Paths.get(testFile.getParent(), "temp").toString();
		ArrayList<File> newFiles = FileFindAndReplace.Run(files, new ArrayList<FindReplacePair>(), destinationFolder);
		
		File newFile = newFiles.get(0);
		assertEquals("Verify new file path is correct",
				Paths.get(destinationFolder, testFile.getName()).toString(),
				newFile.getPath());
	}
	
	@Test
	public void Run_FileWithoutReplaceValues_VerifyContentTheSame() throws IOException {
		File testFile = temp.newFile("testFile.txt");
		
		String fileContent = "Some replace text that does not contain values we're looking for";
		WriteToFile(testFile, fileContent);
		
		ArrayList<File> files = new ArrayList<File>();
		files.add(testFile);
		
		ArrayList<FindReplacePair> findReplacePairs = new ArrayList<FindReplacePair>();
		findReplacePairs.add(new FindReplacePair("valueNotInFileContent", "doesnt matter"));
		
		String destinationFolder = Paths.get(testFile.getParent(), "temp").toString();
		ArrayList<File> newFiles = FileFindAndReplace.Run(files, findReplacePairs, destinationFolder);
		
		File newFile = newFiles.get(0);
		assertTrue("Verify the content of the file didnt change",
				ContentMatchesFile(newFile, fileContent));
	}
	
	@Test
	public void Run_FileWithReplaceValues_VerifyContentReplacedCorrectly() throws IOException {
		File testFile = temp.newFile("testFile.txt");
		
		String fileContent = "This value does get replaced because does is in replace pair below. does";
		WriteToFile(testFile, fileContent);
		
		ArrayList<File> files = new ArrayList<File>();
		files.add(testFile);
		
		// Replace does with REPLACED
		ArrayList<FindReplacePair> findReplacePairs = new ArrayList<FindReplacePair>();
		findReplacePairs.add(new FindReplacePair("does", "REPLACED"));
		
		String destinationFolder = Paths.get(testFile.getParent(), "temp").toString();
		ArrayList<File> newFiles = FileFindAndReplace.Run(files, findReplacePairs, destinationFolder);
		
		File newFile = newFiles.get(0);
		
		String expectedString = "This value REPLACED get replaced because REPLACED is in replace pair below. REPLACED";
		assertTrue("Verify the content of the file didnt change",
				ContentMatchesFile(newFile, expectedString));
	}
	
	private static void WriteToFile(File file, String text) throws IOException {
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file.getAbsolutePath(), true)));
		out.print(text);
		out.close();
	}
	
	private static boolean ContentMatchesFile(File file, String content) throws IOException{
		String fileContent = new String(Files.readAllBytes(file.toPath()), FileFindAndReplace.s_Charset);
		return content.equals(fileContent);		
	}
}
