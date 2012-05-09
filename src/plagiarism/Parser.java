package plagiarism;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.List;

import org.maltparser.MaltParserService;
import org.maltparser.core.exception.MaltChainedException;


public class Parser {

	private MaltParserService maltService;

	public Parser(String maltparams) {
		try {
			maltService =  new MaltParserService();
			maltService.initializeParserModel(maltparams);
		} catch (MaltChainedException e) {
			e.printStackTrace();
		}
	}
	
	
	public String[] processFile(String filename) {
		String[] tokens = null;
		try {
			List<String> lines = Files.readAllLines(FileSystems.getDefault().getPath(filename), StandardCharsets.UTF_8);
			tokens = maltService.parseTokens((lines.toArray(new String[0])));
		} catch (IOException | MaltChainedException e) {
			e.printStackTrace();
		}
		
		return tokens;
	}

	public void processFiles(String dir, String baseDir) {
		File[] files = Utils.getFiles(dir);

		for (File file : files) {
			String relativePath = new File(baseDir).toURI().relativize(file.toURI()).getPath();
			if(file.isFile() && file.getName().endsWith(".txt")) {
				System.out.println("Parsing file: "+file.getPath());
				
				String[] parseData = processFile(file.getPath());

				Utils.writeToFile("data/out/"+relativePath, parseData);
			}else if(file.isDirectory()) {
				processFiles(file.getPath(), baseDir);
			}
		}
	}

	public void processFiles(String dir) {
		processFiles(dir, dir);
	}
}
