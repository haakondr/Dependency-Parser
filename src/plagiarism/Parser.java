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

	private MaltParserService service;

	public Parser(String params) {
		try {
			service =  new MaltParserService();
			service.initializeParserModel(params);
		} catch (MaltChainedException e) {
			e.printStackTrace();
			System.out.println("Maltparser exception: "+ e.getMessage());
		}
	}

	public String[] parseFile(String filename) {
		List<String> lines;
		try {
			lines = Files.readAllLines(FileSystems.getDefault().getPath(filename), StandardCharsets.UTF_8);
			return service.parseTokens(lines.toArray(new String[0]));

		} catch (IOException | MaltChainedException e) {
			e.printStackTrace();
		}

		return null;
	}

	public void parseFiles(String dir, String baseDir) {
		File[] files = Utils.getFiles(dir);
		
		for (File file : files) {
			String relativePath = new File(baseDir).toURI().relativize(file.toURI()).getPath();
			if(file.isFile() && file.getName().endsWith(".txt")) {
				System.out.println("Parsing file: "+file.getPath());
				String[] parseData = parseFile(file.getPath());
				
				System.out.println(relativePath);
				Utils.writeToFile("data/out/"+relativePath, parseData);
			}else if(file.isDirectory()) {
				parseFiles(file.getPath(), baseDir);
			}
		}
	}
	
	public void parseFiles(String dir) {
		parseFiles(dir, dir);
	}
}
