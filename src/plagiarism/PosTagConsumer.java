package plagiarism;

import java.util.concurrent.TransferQueue;

import org.maltparser.MaltParserService;
import org.maltparser.core.exception.MaltChainedException;

import models.POSFile;

public class PosTagConsumer implements Runnable {

	private final TransferQueue<POSFile> queue;
	private MaltParserService maltService;
	private String outDir;
	
	public PosTagConsumer(TransferQueue<POSFile> queue, String maltParams, String outDir) {
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
		String[] parsedTokens = maltService.parseTokens(posfile.getLines());
		Utils.writeToFile(outDir+posfile.getRelPath(), parsedTokens);
	}

}
