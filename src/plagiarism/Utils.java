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
		File f = new File(filename);
		File parent = f.getParentFile();

		if(!parent.exists()) {
			parent.mkdirs();
		}
		try {
			BufferedWriter writer = 
					Files.newBufferedWriter(
							FileSystems.getDefault().getPath(".", filename), 
							StandardCharsets.UTF_8, 
							StandardOpenOption.CREATE);
			for (String line : lines) {
				writer.write(line+"\n");

				writer.flush();
			}
		}catch ( IOException ioe ) {
			ioe.printStackTrace();
		}
	}

	public static File[] getFiles(String directory) {
		return new File(directory).listFiles();
	}
}
