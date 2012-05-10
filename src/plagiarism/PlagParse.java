package plagiarism;


public class PlagParse {
	  
    public static void main(String[] args) {
		Parser parser = new Parser("-c engmalt.linear-1.7.mco -m parse -w . -lfi parser.log");
		parser.processFiles(args[0], args[1]);
		System.out.println("parsing done");
	}    
}
