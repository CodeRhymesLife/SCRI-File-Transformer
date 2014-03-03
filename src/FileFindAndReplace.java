import java.beans.DesignMode;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Finds and replaces values in files
 * @author Administrator
 *
 */
public class FileFindAndReplace {
	public static final Charset s_Charset = StandardCharsets.UTF_8;
	
	/**
	 * Iterates through the list of files and finds and replaces values
	 * 
	 * @param list - files to iterate through
	 * @param arrayList - values to find and replace
	 * @throws IOException - Exception can be thrown when reading and writing files
	 */
	public static ArrayList<File> Run(List<File> list, ArrayList<FindReplacePair> findReplacePairs, String destinationFolder) throws IOException
	{
		if(list == null)
			throw new NullPointerException("files");
		
		if(findReplacePairs == null)
			throw new NullPointerException("findReplacePairs");
		
		if(destinationFolder == null)
			throw new NullPointerException("destinationFolder");
		
		// Get destination folder
		File destinationFolderFile = new File(destinationFolder);
		
		// If the destination folder doesnt exist and we're unable to create it,
		// throw an exception
		if(!destinationFolderFile.exists() && !destinationFolderFile.mkdir())
			throw new IOException(String.format("Unable to create directory %s", destinationFolder));
		
		ArrayList<File> newFiles = new ArrayList<File>();
		
		// Iterate over each file and find and replace the values
		for(int fileIndex = 0; fileIndex < list.size(); fileIndex++)
		{
			// Get the file
			File file = list.get(fileIndex);
			
			if(file == null || file.isDirectory())
				continue;
			
			// Read the file contents
			String content = new String(Files.readAllBytes(file.toPath()), s_Charset);
			
			// Replace values in string
			content = ReplaceValuesInString(content, findReplacePairs);
			
			// Get the destination path
			Path destinationFilePath = Paths.get(destinationFolder, file.getName());
			
			// Save the file
			Path newFilePath = Files.write(destinationFilePath, content.getBytes(s_Charset));
			
			// Get the new file
			File newFile = new File(newFilePath.toString());
			
			// Add the new file to the list of files we will return
			newFiles.add(newFile);
		}
		
		return newFiles;
	}
	
	/**
	 * Replaces values in the string based on the find replace pairs
	 * @param content - content to search and replace
	 * @param findReplacePairs - find and replace pairs used to replace values in content
	 * @return string with values replaced based on the given find replace pairs
	 */
	private static String ReplaceValuesInString(String content, ArrayList<FindReplacePair> findReplacePairs)
	{
		if(content == null || content.equals(""))
			return "";
		
		if(findReplacePairs == null)
			throw new NullPointerException("findReplacePairs");
		
		// Find and replace values in this file
		for(int findReplaceIndex = 0; findReplaceIndex < findReplacePairs.size(); findReplaceIndex++)
		{
			// Get the find replace pair index
			FindReplacePair findReplacePair = findReplacePairs.get(findReplaceIndex);
			
			// Replace the content
			content = content.replaceAll(findReplacePair.Find, findReplacePair.Replace);
		}
		
		return content;
	}
}


