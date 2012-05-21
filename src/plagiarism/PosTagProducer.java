package plagiarism;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import models.PlagFile;
import models.POSFile;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class PosTagProducer implements Runnable{

	private final BlockingQueue<POSFile> queue;
	private PlagFile[] files;
	private MaxentTagger tagger;

	public PosTagProducer(BlockingQueue<POSFile> queue, PlagFile[] files, String taggerParams){
		this.queue = queue;
		this.files = files;
		try {
			this.tagger = new MaxentTagger(taggerParams);
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		for (PlagFile file : files) {
			String[] taggedFile = tagFile(file.getPath());

			try {
				queue.put(new POSFile(file.getRelPath(), taggedFile));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}


	public String[] tagFile(String filename) {
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

}
