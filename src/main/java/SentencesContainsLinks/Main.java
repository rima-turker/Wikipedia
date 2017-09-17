package SentencesContainsLinks;

import org.apache.log4j.Logger;

public class Main {
	private static final Logger LOG  = Logger.getLogger(SentencesWithLink.class);
	public static void main(String[] args) 
	{
		SentencesWithLink sentences = new SentencesWithLink(Integer.parseInt(args[0]),args[1]);
		System.out.println("a[0]= "+Integer.parseInt(args[0]));
		System.out.println("a[1]= "+args[1]);
		sentences.checkWikiPages();
	}

}
