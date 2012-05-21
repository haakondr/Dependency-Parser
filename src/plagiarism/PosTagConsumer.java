package plagiarism;

import java.util.concurrent.TransferQueue;

import org.maltparser.MaltParserService;
import org.maltparser.core.exception.MaltChainedException;

import models.POSFile;

public class PosTagConsumer implements Runnable {

	private final TransferQueue<POSFile> queue;
	private MaltParserService maltService;
	
	public PosTagConsumer(TransferQueue<POSFile> queue, String maltParams) {
		this.queue = queue;
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
	public void consume(POSFile tfile) throws MaltChainedException {
		maltService.parseTokens(tfile.getLines());
	}

}
