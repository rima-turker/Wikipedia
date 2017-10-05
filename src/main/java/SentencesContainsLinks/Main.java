package SentencesContainsLinks;

import org.apache.log4j.Logger;

public class Main {
	
	private static final Logger LOG  = Logger.getLogger(Main.class);
	public static void main(String[] args) 
	{
		DataGeneratorBasedonWindowSize d =new DataGeneratorBasedonWindowSize(1,4,"");
		d.getOnlyEntities(args[0]);
		//d.getOnlyEntities("");
	
//		PrepareFileForWindowSize windowSize = new PrepareFileForWindowSize();
//		System.out.println(args[0]);
//		System.out.println("Hello");
		//LOG.info("HEllo");
//		windowSize.generateTrainSet2L2E(args[0]);
//		windowSize.generateTrainSet2L2E("/home/rtue/workspace/Wikipedia/src/main/resources/temp_WikiSentencesLinks");
//		System.out.println(args[0]);
//		PreprocessingWord2Vec pre = new PreprocessingWord2Vec(args[0]);
//		PreprocessingWord2Vec pre = new PreprocessingWord2Vec("/home/rtue/workspace/Wikipedia/src/main/resources/temp_WikiSentencesLinks");
//		pre.readFile();
		
//		PrepareFileForWindowSize windowSize = new PrepareFileForWindowSize(2, 2, "/home/rtue/workspace/Wikipedia/src/main/resources/temp_firstPreProcessing");
//		PrepareFileForWindowSize windowSize = new PrepareFileForWindowSize(2, 2,args[0]);
//		PrepareFileForWindowSize windowSize = new PrepareFileForWindowSize(2,2,"/home/rtue/workspace/Wikipedia/src/main/resources/temp_windowSize");
//		windowSize.prepareFiles();
		
//		SentencesWithLink sentences = new SentencesWithLink(1,args[0]);
//		System.out.println("a[0]= "+ args[0]);
//		System.out.println("a[1]= "+args[1]);
//		sentences.checkWikiPagesWOThread();
	}

}
