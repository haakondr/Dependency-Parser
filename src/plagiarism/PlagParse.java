package plagiarism;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

import models.PlagFile;
import models.POSFile;


public class PlagParse {
	  
    public static void main(String[] args) throws ClassNotFoundException, IOException {
		
    	BlockingQueue<POSFile> queue = new LinkedBlockingQueue<POSFile>();
    	PlagFile[] files = Utils.getTaskList(args[0]);
    	
    	
    	
    	PosTagConsumer consumer  = new PosTagConsumer(queue, "-c engmalt.linear-1.7.mco -m parse -w . -lfi parser.log", args[1]);
    	
    	int cpuCount = Runtime.getRuntime().availableProcessors();
    	int threadCount = (cpuCount < 5) ? 2 : cpuCount - 3;
    	PlagFile[][] chunks = Utils.getChunks(files, threadCount);
    	System.out.println("thread count: "+threadCount+" chunks: "+chunks.length);
//    	MaxentTagger tagger = new MaxentTagger("wsj-0-18-bidirectional-distsim.tagger");
    	
    	for (int i = 0; i < threadCount; i++) {
    		PosTagProducer producer = new PosTagProducer(queue, chunks[i], "wsj-0-18-bidirectional-distsim.tagger");
    		new Thread(producer, "PosTagProducer: "+i).start();
		}
    	
    	new Thread(consumer, "maltparserConsumer").start();
    	
    	System.out.println("running with "+queue.size()+" threads");
    
	}
}