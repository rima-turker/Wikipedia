package SentencesContainsLinks;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sound.sampled.Line;

import org.apache.log4j.Logger;
import org.apache.poi.sl.usermodel.StrokeStyle.LineCompound;

import SentencesContainsLinks.HTMLLinkExtractor.HtmlLink;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.LexedTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;

public class PreprocessingWord2Vec 
{
	private static final Logger LOG  = Logger.getLogger(PreprocessingWord2Vec.class);
	private static String fileWikiSentencesWithLinks;

	private static ExecutorService executor;
	private Pattern patternTag, patternLink, patternUri;
	private Matcher matcherTag, matcherLink, matcherUri;

	private static final String HTML_A_TAG_PATTERN = "(?i)<a([^>]+)>(.+?)</a>";
	private static final String HTML_A_HREF_TAG_PATTERN =
			"\\s*(?i)href\\s*=\\s*(\"([^\"]*\")|'[^']*'|([^'\">\\s]+))";
	private static final String URI_ENCODE = "%[0-9a-f]{2}";

	public PreprocessingWord2Vec(String file)
	{
		fileWikiSentencesWithLinks= file;
		patternTag = Pattern.compile(HTML_A_TAG_PATTERN);
		patternLink = Pattern.compile(HTML_A_HREF_TAG_PATTERN);
		patternUri = Pattern.compile(URI_ENCODE);
	}

	public  void readFile() {


		try {

			final long now = System.currentTimeMillis();
			int lineCounter = 0;
			try {
				final BufferedReader br = new BufferedReader(new FileReader(fileWikiSentencesWithLinks));
				String line;

				StringBuilder strbulilder = new StringBuilder();


				while ((line = br.readLine()) != null) 
				{
					String resultSentence=line;


					//System.out.println(line);

					matcherTag = patternTag.matcher(line);

					while (matcherTag.find()) 
					{  //you find the all the tags

						String hrefOriginal = matcherTag.group(1); // entitiy
						String href="";

						matcherUri= patternUri.matcher(hrefOriginal);
						if (matcherUri.find()) 
						{
							href = java.net.URLDecoder.decode(hrefOriginal);
						}
						else
						{

							href = hrefOriginal;
						}

						//						try {
						//							
						//						} catch (Exception e) {
						//							System.out.println("In exception "+line+ " "+href);
						//						}


						matcherLink = patternLink.matcher(href);
						String entity=null;
						if (matcherLink.find()) 
						{

							entity = matcherLink.group(1).replaceAll("\"", ""); //
							String anchor = matcherTag.group(2); // anchor
							
							if (entity.equals(" ")  || anchor.equals(" ")) 
							{
								resultSentence=line;
								break;
							}
							String strTobereplaced = "<a"+ hrefOriginal+">"+anchor+"</a>";
							//System.out.println("strTobereplaced " + strTobereplaced );
							String[] anchorSplit= anchor.split(" ");

							String[] hrefSplit = entity.split(" ");

							StringBuilder anchorBuilder = new StringBuilder();
							StringBuilder entityBuilder = new StringBuilder("dbr:");


							for (int i = 0; i <anchorSplit.length; i++) 
							{
								anchorBuilder.append( anchorSplit[i]+"_");

							}

							try 
							{
								anchorBuilder=anchorBuilder.replace(anchorBuilder.length()-1, anchorBuilder.length(), "");
							} 
							catch (Exception e) {
								System.out.println("Exception "+anchorBuilder.toString()+" "+ anchorBuilder.length()+"line "+ line);
								System.exit(0);
							}


							for (int i = 0; i < hrefSplit.length; i++) 
							{
								entityBuilder.append( hrefSplit[i]+"_");

							}

							entityBuilder=entityBuilder.replace(entityBuilder.length()-1, entityBuilder.length(), "");	



							String toReplace = entityBuilder +" "+anchorBuilder; 
							resultSentence = resultSentence.replace(strTobereplaced, toReplace);
						}



						//						System.out.println("href "+ href +" linkText "+ anchor+ " link "+ matcherTag.matches());
						//						matcherLink = patternLink.matcher(href);
						//
						//						while (matcherLink.find()) {
						//
						//							String link = matcherLink.group(1); // link
						//							System.out.println("href "+ href +" linkText "+ anchor+ " link "+ link);

					}
					//					String[] tokenizer = line.split(" ");
					//					
					//					for (String token: tokenizer)
					//					{
					//						System.out.println(token);
					//						matcherTag = patternTag.matcher(token);
					//						if (matcherTag.find()) //you found a tag
					//						{
					//							
					//							String href = matcherTag.group(1); // href
					//							String linkText = matcherTag.group(2); // link text
					//							matcherLink = patternLink.matcher(href);
					//							String link = matcherLink.group(1);
					//							
					//							System.out.println("href "+ href +" linkText "+ linkText+ " link "+ link);
					//						}
					//
					//
					//	

					//	System.out.println(line+"\n"+ resultSentence);	
					if (!resultSentence.equals(line)) 
					{
						LOG.info(resultSentence);
					}
					
				}

				br.close();					
			} catch (IOException e) {

				e.printStackTrace();

			}
			
			System.err.println(TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis()-now));

		} catch (final Exception exception) {
			exception.printStackTrace();
		}
	}
}
