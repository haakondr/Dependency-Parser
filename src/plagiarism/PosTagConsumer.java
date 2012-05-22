package plagiarism;

import java.util.concurrent.BlockingQueue;

import org.maltparser.MaltParserService;
import org.maltparser.core.exception.MaltChainedException;

import models.POSFile;

public class PosTagConsumer implements Runnable {

	private final BlockingQueue<POSFile> queue;
	private MaltParserService maltService;
	private String outDir;
	
	public PosTagConsumer(BlockingQueue<POSFile> queue, String maltParams, String outDir) {
		this.queue = queue;
		this.outDir = outDir;
		try {
			this.maltService = new MaltParserService();
			maltService.initializeParserModel(maltParams);
		} catch (MaltChainedException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		while(true) {
			try {
				consume(queue.take());
			} catch (InterruptedException | MaltChainedException e) {
				e.printStackTrace();
			}
		}
		
	}
	public void consume(POSFile posfile) throws MaltChainedException {
		System.out.println("Consuming file "+posfile.getRelPath());
		System.out.println("Currently "+queue.size()+" files ready to be consumed");
		String[] parsedTokens = maltService.parseTokens(posfile.getLines());
		Utils.writeToFile(outDir+posfile.getRelPath(), parsedTokens);
		System.out.println("Done parsing file "+posfile.getRelPath());
	}

}
