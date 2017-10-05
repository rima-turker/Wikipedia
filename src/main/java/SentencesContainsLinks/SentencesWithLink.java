package SentencesContainsLinks;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;



public class SentencesWithLink {
	
	private static final Logger LOG  = Logger.getLogger(SentencesWithLink.class);
	private static String WIKI_FILES_FOLDER;
	private static int NUMBER_OF_THREADS;
	
	private static ExecutorService executor;

	public SentencesWithLink(int thread, String folders)
	{
		NUMBER_OF_THREADS= thread;
		
		executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
		WIKI_FILES_FOLDER= folders;
		
	}

	public  void checkWikiPages() {
		try {
			final File[] listOfFolders = new File(WIKI_FILES_FOLDER).listFiles();
			Arrays.sort(listOfFolders);
			final long now = System.currentTimeMillis();
			for (int i = 0; i < listOfFolders.length; i++) {
				final String subFolder = listOfFolders[i].getName();
				final File[] listOfFiles = new File(WIKI_FILES_FOLDER + File.separator + subFolder + File.separator).listFiles();
				Arrays.sort(listOfFiles);
				for (int j = 0; j < listOfFiles.length; j++) {
					final String file = listOfFiles[j].getName();
					executor.execute(handle(WIKI_FILES_FOLDER + File.separator + subFolder + File.separator+File.separator+file));
				}
			}
			executor.shutdown();
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
			System.err.println(TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis()-now));

		} catch (final Exception exception) {
			exception.printStackTrace();
		}
	}
	
	public  void checkWikiPagesWOThread() {
		try {
			final File[] listOfFolders = new File(WIKI_FILES_FOLDER).listFiles();
			Arrays.sort(listOfFolders);
			final long now = System.currentTimeMillis();
			for (int i = 0; i < listOfFolders.length; i++) {
				final String subFolder = listOfFolders[i].getName();
				final File[] listOfFiles = new File(WIKI_FILES_FOLDER + File.separator + subFolder + File.separator).listFiles();
				Arrays.sort(listOfFiles);
				for (int j = 0; j < listOfFiles.length; j++) {
					final String file = listOfFiles[j].getName();
					startGetSentencesWithLinksWOThread(WIKI_FILES_FOLDER + File.separator + subFolder + File.separator+file);
				}
			}
			System.err.println(TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis()-now));

		} catch (final Exception exception) {
			exception.printStackTrace();
		}
	}
	
	private void startGetSentencesWithLinksWOThread(String pathToSubFolder) 
	{
		int lineCounter = 0;
		try {
			final BufferedReader br = new BufferedReader(new FileReader(pathToSubFolder));
			String line;
			
			while ((line = br.readLine()) != null) {
		

				if (!line.contains("<")|| line.contains("<doc id=")||line.contains("</doc>")) {
					continue;
				}
				
				final HTMLLinkExtractor htmlLinkExtractor = new HTMLLinkExtractor();
				final LinkedList<String> linkSentences = new LinkedList<String>(htmlLinkExtractor.grabOnlySentencesHTMLLinks((line)));
				
				for (int i = 0; i < linkSentences.size(); i++) {
					LOG.info(linkSentences.get(i));
					lineCounter++;
		        }
			}
			br.close();					
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//System.out.println("Count of sentences with links "+ lineCounter);
	}
	private static Runnable handle(String pathToSubFolder) {
		final Runnable r = new Runnable() {
			@Override
			public void run() {
				try {
					final BufferedReader br = new BufferedReader(new FileReader(pathToSubFolder));
					String line;
					int lineCounter = 0;
					while ((line = br.readLine()) != null) {
				

						if (!line.contains("<")|| line.contains("<doc id=")||line.contains("</doc>")) {
							continue;
						}
						
						final HTMLLinkExtractor htmlLinkExtractor = new HTMLLinkExtractor();
						
//						final Vector<String> links = htmlLinkExtractor.grabOnlySentencesHTMLLinks(line);
//						for (Iterator<?> iterator = links.iterator(); iterator.hasNext();) {
//							final String htmlLink = (String) iterator.next();
//							LOG.info(htmlLink);
//						}
					}
					
					br.close();					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		return r;
	}
}
