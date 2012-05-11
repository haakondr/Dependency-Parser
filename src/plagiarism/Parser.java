package plagiarism;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
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
		//TODO: write test verifying equal line numbers for input/output?
		List<String> tokens = new ArrayList<String>();
		try {
			List<String> lines = Utils.readAllLines(filename);
			List<String> sentence = new ArrayList<String>();
			for (int i = 0; i < lines.size(); i++) {
				
				if(lines.get(i).startsWith("1\t")) {
					if(sentence.size()>0) {
						tokens.addAll(Arrays.asList(maltService.parseTokens(sentence.toArray(new String[0]))));
					}
					sentence.clear();
				} 
				sentence.add(lines.get(i));
				
				if(i == lines.size()-1) {
					tokens.addAll(Arrays.asList(maltService.parseTokens(sentence.toArray(new String[0]))));
				}
			}
		} catch(MaltChainedException e) {
			e.printStackTrace();
		}
		
		return tokens.toArray(new String[0]);
	}

	public void processFiles(String dir, String baseDir, String outdir) {
		File[] files = Utils.getFiles(dir);

		for (File file : files) {
			String relativePath = new File(baseDir).toURI().relativize(file.toURI()).getPath();
			if(file.isFile() && file.getName().endsWith(".txt")) {
				System.out.println("Parsing file: "+file.getPath());
				String[] parseData = processFile(file.getPath());
				Utils.writeToFile(outdir+relativePath, parseData);
			}else if(file.isDirectory()) {
				processFiles(file.getPath(), baseDir, outdir);
			}
		}
	}

	public void processFiles(String dir, String outdir) {
		processFiles(dir, dir, outdir);
	}
}
