package models;

import java.util.ArrayList;
import java.util.List;


public class POSFile {
	private String relPath;
	private List<String[]> sentences;
	
	public POSFile(PlagFile file) {
		this.relPath = file.getRelPath();
		this.sentences = new ArrayList<>();
	}
	
	public void addSentence(String[] tokens) {
		sentences.add(tokens);
	}

	public List<String[]> getSentences() {
		return sentences;
	}

	public String getRelPath() {
		return relPath;
	}
}
