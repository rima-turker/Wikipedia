package SentencesContainsLinks;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

public class PrepareFileForWindowSize 
{
	private static final Logger LOG  = Logger.getLogger(PrepareFileForWindowSize.class);
	private int entitySize;
	private int wordSize;
	private String file;
	public PrepareFileForWindowSize(int entitySize, int wordSize, String fileName){
		this.entitySize= entitySize;
		this.wordSize= wordSize;
		file = fileName;
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
					//System.out.println(line);
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
