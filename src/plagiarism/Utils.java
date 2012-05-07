package plagiarism;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;


public class Utils {

    public static void writeToFile(String filename, String[] lines) {
    	System.out.println("trying to write to:"+filename);
    	File f = new File(filename);
    	File parent = f.getParentFile();
    
    	if(!parent.exists()) {
    		parent.mkdir();
    	}
    	
        try {
            BufferedWriter writer = 
                Files.newBufferedWriter(
                        FileSystems.getDefault().getPath(".", filename), 
                        StandardCharsets.UTF_8, 
                        StandardOpenOption.CREATE);
            for (String line : lines) {
                writer.write(line+"\n");
			}
        }
        catch ( IOException ioe ) {
            ioe.printStackTrace();
        }
        System.out.println("Wrote to file: "+filename);
    }
    
	public static File[] getFiles(String directory) {
		return new File(directory).listFiles();
	}
}
