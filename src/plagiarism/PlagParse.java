package plagiarism;

import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TransferQueue;

import models.PlagFile;
import models.POSFile;


public class PlagParse {
	  
    public static void main(String[] args) {
		
    	TransferQueue<POSFile> queue = new LinkedTransferQueue<>();
    	PlagFile[] files = Utils.getTaskList(args[0]);
    	
    	
    	
    	PosTagConsumer consumer  = new PosTagConsumer(queue, "-c engmalt.linear-1.7.mco -m parse -w . -lfi parser.log", args[1]);
    	
    	int cpuCount = Runtime.getRuntime().availableProcessors();
    	int threadCount = (cpuCount < 4) ? 2 : cpuCount / 2;
    	PlagFile[][] chunks = Utils.getChunks(files, threadCount);
    	System.out.println("thread count: "+threadCount+" chunks: "+chunks.length);

    	for (int i = 0; i < threadCount; i++) {
    		PosTagProducer producer = new PosTagProducer(queue, chunks[i], "wsj-0-18-bidirectional-distsim.tagger");
    		new Thread(producer, "PosTagProducer: "+i).start();
		}
    	
    	new Thread(consumer, "maltparserConsumer").start();
    	
    	System.out.println("running with "+queue.size()+" threads");
   
    
		System.out.println("parsing done");
	}
}