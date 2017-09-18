package SentencesContainsLinks;


public class Main {
	
	public static void main(String[] args) 
	{
	
	
		
//		System.out.println(args[0]);
		PreprocessingWord2Vec pre = new PreprocessingWord2Vec(args[0]);
//		PreprocessingWord2Vec pre = new PreprocessingWord2Vec("/home/rtue/workspace/Wikipedia/src/main/resources/temp_WikiSentencesLinks");
		pre.readFile();
//		SentencesWithLink sentences = new SentencesWithLink(Integer.parseInt(args[0]),args[1]);
//		System.out.println("a[0]= "+Integer.parseInt(args[0]));
//		System.out.println("a[1]= "+args[1]);
//		sentences.checkWikiPages();
	}

}
