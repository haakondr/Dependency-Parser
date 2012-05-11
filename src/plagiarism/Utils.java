package plagiarism;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class Utils {

	public static void writeToFile(String filename, String[] lines) {
		File f = new File(filename);
		File parent = f.getParentFile();

		if(!parent.exists()) {
			parent.mkdirs();
		}
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(filename));

			for (String line : lines) {
				writer.write(line);
				writer.newLine();

			}
			writer.close();
		}catch ( IOException ioe ) {
			ioe.printStackTrace();
		}
	}

	public static File[] getFiles(String directory) {
		return new File(directory).listFiles();
	}

	public static List<String> readAllLines(String filename) {
		List<String> out = new ArrayList<String>();
		try {
			FileInputStream fstream = new FileInputStream(filename);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String line = null;
			 while ((line = br.readLine()) != null)   {
			 	out.add(line);
			 }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return out;

	}
}
