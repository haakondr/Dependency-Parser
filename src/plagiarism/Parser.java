package plagiarism;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.maltparser.MaltParserService;
import org.maltparser.core.exception.MaltChainedException;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;


public class Parser {

	private MaltParserService maltService;
	private MaxentTagger tagger;
	public Parser(String maltparams, String stanfordPosModel) {
		try {
			maltService =  new MaltParserService();
			maltService.initializeParserModel(maltparams);
			tagger = new MaxentTagger(stanfordPosModel);
		} catch (MaltChainedException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String[] tagFile(String filename) {
		//TODO: write test verifying equal line numbers for input/output?
		//TODO: introduce concurrency? processFile and Utils.writeToFile should run in separate threads.
		List<String> taggedSentences = new ArrayList<String>();
		
		try {
			List<List<HasWord>> sentences = MaxentTagger.tokenizeText(new BufferedReader(new FileReader(filename)));
			
			
			for (List<HasWord> sentence : sentences) {
				ArrayList<TaggedWord> taggedSentence = tagger.tagSentence(sentence);
				
				int i = 1;
				for (TaggedWord token : taggedSentence) {
					taggedSentences.add(i+"\t"+token.word()+"\t"+"_"+"\t"+token.tag()+"\t"+token.tag()+"\t"+"_");
					i++;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return taggedSentences.toArray(new String[0]);
	}
	
	public String[] processFile(String filename) {
		String[] taggedLines = tagFile(filename);
		
		try {
			return maltService.parseTokens(taggedLines);
		} catch (MaltChainedException e) {
			e.printStackTrace();
		}
		
		return null;
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
