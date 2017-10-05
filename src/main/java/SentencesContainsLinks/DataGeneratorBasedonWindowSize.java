package SentencesContainsLinks;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;

public class DataGeneratorBasedonWindowSize {

	private static final Logger LOG  = Logger.getLogger(PrepareFileForWindowSize.class);
	private int entitySize;
	private int wordSize;
	private String file;


	private Pattern patternTag, patternLink, patternUri;
	private Matcher matcherTag, matcherLink, matcherUri;

	private static final String HTML_A_TAG_PATTERN = "(?i)<a([^>]+)>(.+?)</a>";
	private static final String HTML_A_HREF_TAG_PATTERN =
			"\\s*(?i)href\\s*=\\s*(\"([^\"]*\")|'[^']*'|([^'\">\\s]+))";

	public DataGeneratorBasedonWindowSize()
	{
		patternTag = Pattern.compile(HTML_A_TAG_PATTERN);
		patternLink = Pattern.compile(HTML_A_HREF_TAG_PATTERN);

	}
	public DataGeneratorBasedonWindowSize(int entitySize, int wordSize, String fileName){
		this.entitySize= entitySize;
		this.wordSize= wordSize;
		patternTag = Pattern.compile(HTML_A_TAG_PATTERN);
		patternLink = Pattern.compile(HTML_A_HREF_TAG_PATTERN);
		file = fileName;
	}


	public  void getOnlyEntities(String fileWikiSentencesWithLinks) {
		//fileWikiSentencesWithLinks ="/home/rtue/workspace/Word2VecJava/src/main/resources/temp_WikiSentencesLinks";

		try {

			final long now = System.currentTimeMillis();
			int lineCounter = 0;
			int sentenceCounter = 0;
			try {
				final BufferedReader br = new BufferedReader(new FileReader(fileWikiSentencesWithLinks));
				String line;
				List<String> sentences = new ArrayList<String>();
				
				//StringBuilder strbulilder = new StringBuilder();
				while ((line = br.readLine()) != null) 
				{
					//System.out.println(line);
					lineCounter++;
					StringBuilder sbuilder=new StringBuilder();

					List<String> tokens = new ArrayList<String>();

					PTBTokenizer<CoreLabel> ptbt = new PTBTokenizer<>(new StringReader(line),
							new CoreLabelTokenFactory(), "");

					int wordCount = wordSize; 
					while (ptbt.hasNext()) {

						String token = ptbt.next().toString();

						matcherLink = patternLink.matcher(token);
						//						if (token.contains("<a href=") || token.contains("<a href="))
						//						{  

						if (matcherLink.find()) {
							String link ="dbr:"+matcherLink.group(0).replaceAll("\"", "").replaceAll("href=", "");
							//System.out.println(link);
							try 
							{
								link = java.net.URLDecoder.decode(link);
								if (link.contains("\n")) 
								{
									break;
								}
								else
								{
									link = link.replaceAll("\"", "").toLowerCase();
									String[] hrefSplit = link.split(" ");
									
									
									if (hrefSplit.length>1) 
									{
										StringBuilder lbuilder = new StringBuilder();
										for (int i = 0; i < hrefSplit.length; i++) 
										{
											lbuilder.append( hrefSplit[i]+"_");

										}
										if (!lbuilder.toString().isEmpty()) {
											lbuilder=lbuilder.replace(lbuilder.length()-1, lbuilder.length(), "");	
											
										}
										sbuilder.append(lbuilder.toString()+" "); 
									}
									
									else
									{
										sbuilder.append(link+" ");
									}
									
									wordCount = wordSize;
								}
							} 

							catch (Exception e) 
							{
								break;
							}

						}


						//						}
						else if (wordCount>0 && !token.contains("</a>") && !token.contains("<a href=")&& 
								token.length()> 1 && !token.contains("`")&& !token.contains("'")) 
						{
							sbuilder.append(token.toLowerCase().replace(" ", "")+" ");
							wordCount--;
						}
					}
					if (sbuilder.length()>1) {
						if (sbuilder.substring(sbuilder.length()-1, sbuilder.length()).equals(" "))
						{
							sentences.add(sbuilder.toString().substring(0,sbuilder.length()-1));
						}
						else
						
							sentences.add(sbuilder.toString());
					}
					
					
					//System.out.println(sbuilder.toString());



					//					while (matcherTag.find()) //you find the all the tags in a sentence 
					//					{  
					//						String href = matcherTag.group(1); // entitiy, href
					//						//String linkText = matcherTag.group(2); // link text, anchor 
					//						//System.out.println(href);
					//						
					//						matcherLink = patternLink.matcher(href);
					//						String entity=null;
					//						while(matcherLink.find()) //href buldun(href partini atiyorsun)
					//						{
					//							String link = matcherLink.group(1); 
					//							//String anchor = matcherTag.group(2); // anchor
					//							//System.out.println(link);
					//							if (!link.equals("")&&!link.equals(" ")) 
					//							{
					//								try 
					//								{
					//									link = java.net.URLDecoder.decode(link);
					//									if (link.contains("\n")) 
					//									{
					//										break;
					//									}
					//								} 
					//								
					//								catch (Exception e) 
					//								{
					//									break;
					//								}
					//								
					//							}
					//							else
					//								break;
					//							
					//							entity = link.replaceAll("\"", "").toLowerCase(); //
					//							
					//							String[] hrefSplit = entity.split(" ");
					//
					//							StringBuilder entityBuilder = new StringBuilder();
					//
					//							for (int i = 0; i < hrefSplit.length; i++) 
					//							{
					//								entityBuilder.append( hrefSplit[i]+"_");
					//
					//							}
					//							
					//							
					//						}
					//
					//					}
				}
				System.out.println("Writing to log file");
				for (String str: sentences) 
				{
					LOG.info(str.toLowerCase());
					//System.out.println(str.toLowerCase());
				}
				br.close();					
			} catch (IOException e) {

				e.printStackTrace();

			}
			System.out.println("sentenceCount "+ sentenceCounter+ " lineCount "+ lineCounter);
			System.err.println(TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis()-now));

		} catch (final Exception exception) {
			exception.printStackTrace();
		}
	}

	public  void generateTrainSet2L2E(String fileWikiSentencesWithLinks) {

		final long now = System.currentTimeMillis();
		int lineCounter = 0;
		int sentenceCounter = 0;
		try {
			final BufferedReader br = new BufferedReader(new FileReader(fileWikiSentencesWithLinks));
			String line;

			StringBuilder strbulilder = new StringBuilder();
			while ((line = br.readLine()) != null) 
			{
				lineCounter++;

				matcherTag = patternTag.matcher(line);//get all the xml tag 

				while (matcherTag.find()) //you find the all the tags in a sentence 
				{  
					String href = matcherTag.group(1); // entitiy, href
					String anchor = matcherTag.group(2); // link text, anchor 
					//System.out.println(href);

					matcherLink = patternLink.matcher(href);
					String entity=null;
					while(matcherLink.find()) //href buldun(href partini atiyorsun)
					{
						String link = matcherLink.group(1); 
						anchor = matcherTag.group(2); // anchor
						//System.out.println(link);
						if (!link.equals("")&&!link.equals(" ")&& !anchor.equals("")&&!anchor.equals(" ")) 
						{
							try 
							{
								link = java.net.URLDecoder.decode(link);
								if (link.contains("\n")) 
								{
									break;
								}
							} 

							catch (Exception e) 
							{
								break;
							}

						}
						else
							break;

						entity = link.replaceAll("\"", "").toLowerCase(); //
						anchor=anchor.replaceAll("\"", "").replaceAll("\\(", "").replaceAll("\\)", "").replaceAll(",", "").toLowerCase();

						String[] hrefSplit = entity.split(" ");
						String[] anchorSplit= anchor.split(" ");

						String finalAnchor=""; 
						String finalEntity="dbr:"; 

						StringBuilder anchorBuilder= new StringBuilder();
						for (int i = 0; i <anchorSplit.length; i++) 
						{
							//anchorBuilder.append( anchorSplit[i]+"_");
							finalAnchor=finalAnchor.concat(anchorSplit[i]+"_");

						}


						StringBuilder entityBuilder = new StringBuilder();

						for (int i = 0; i < hrefSplit.length; i++) 
						{
							//entityBuilder.append( hrefSplit[i]+"_");
							finalEntity=finalEntity.concat(hrefSplit[i]+"_");

						}

						if (finalAnchor.length()>1) {
							if (finalAnchor.substring(finalAnchor.length()-1, finalAnchor.length()).equals("_"))
								finalAnchor=finalAnchor.substring(0,finalAnchor.length()-1);
						}

						if (finalEntity.length()>1) {

							if (finalEntity.substring(finalEntity.length()-1, finalEntity.length()).equals("_"))
								finalEntity=finalEntity.substring(0,finalEntity.length()-1);
						}


						//entityBuilder=entityBuilder.replace(entityBuilder.length()-1, entityBuilder.length(), "");	
						//anchorBuilder=anchorBuilder.replace(anchorBuilder.length()-1, anchorBuilder.length(), "");
						strbulilder.append(finalEntity+" "+finalAnchor+" ");
					}

				}
			}
			//System.out.println(strbulilder.toString());
			LOG.info(strbulilder.toString());
			br.close();					
		} catch (IOException e) {

			e.printStackTrace();

		}
		System.out.println("sentenceCount "+ sentenceCounter+ " lineCount "+ lineCounter);
		System.err.println(TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis()-now));
	}

	public void prepareFiles()
	{
		final long now = System.currentTimeMillis();
		int lineCounter = 0;
		int sentenceCounter = 0;
		try {
			final BufferedReader br = new BufferedReader(new FileReader(file));
			String line;

			StringBuilder strBuilder = new StringBuilder();
			int splitCount =0;
			while ((line = br.readLine()) != null) 
			{
				if (line.substring(line.length()-1, line.length()).equals(".")) 
				{
					line= line.substring(0, line.length()-1);
				}
				lineCounter++;
				String[] lineSplit= line.split(" ");
				for (int i = 0; i < lineSplit.length; i++) 
				{
					if (lineSplit[i].contains(Global.entityAnchorSeparator)&&lineSplit[i].contains("dbr:")) 
					{
						String[] entAnchorSplit = lineSplit[i].split(Global.entityAnchorSeparator);

						//System.out.println(lineSplit[i]);
						//							try {
						if (entAnchorSplit.length==2) 
						{
							splitCount++;
							strBuilder.append(entAnchorSplit[0].replaceAll("\"", "").replaceAll("\\(", "").toLowerCase()+" "+entAnchorSplit[1].replaceAll("\"", "").replaceAll("\\)", "").toLowerCase()+" ");
						}


						//							} catch (Exception e) {
						//								System.out.println(lineSplit[i]+" "+line);
						//								System.exit(0);
						//							}

					}
				}
				//					System.out.println(strBuilder);




				//
				//					String toReplace = entityBuilder +" - "+anchorBuilder; 
				//					resultSentence = resultSentence.replace(strTobereplaced, toReplace);
				//
				//
				//					if (!resultSentence.equals(line)) 
				//					{
				//						sentenceCounter++;
				//						LOG.info(resultSentence);
				//					}

			}
			LOG.info(strBuilder);

			br.close();					
			System.out.println();
			System.out.println("sentenceCount "+ sentenceCounter+ " lineCount "+ lineCounter);
			System.err.println(TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis()-now));

		} 
		catch (final Exception exception) {
			exception.printStackTrace();
		}

	}
}
