package SentencesContainsLinks;

public class Main {
	private static String WIKI_FILES_FOLDER="/home/rtue/workspace/Wikipedia/src/main/resources/wikiFiles";
	private static int NUMBER_OF_THREADS = 1;

	public static void main(String[] args) 
	{
		//SentencesWithLink sentences = new SentencesWithLink(args[0],args[1]);
		System.out.println("a[0]= "+args[0]);
		System.out.println("a[1]= "+args[1]);
		SentencesWithLink sentences = new SentencesWithLink(NUMBER_OF_THREADS,WIKI_FILES_FOLDER);
		sentences.checkWikiPages();
		
	}

}
