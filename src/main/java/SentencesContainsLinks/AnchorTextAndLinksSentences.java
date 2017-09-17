package SentencesContainsLinks;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import SentencesContainsLinks.HTMLLinkExtractor.HtmlLink;

public class AnchorTextAndLinksSentences {
	private static final Logger LOG  = Logger.getLogger(AnchorTextAndLinksSentences.class);
	private static String WIKI_FILES_FOLDER="/home/rtue/workspace/Wikipedia/src/main/resources/wikiFiles";
	private static int NUMBER_OF_THREADS = 1;



	private static ExecutorService executor;

	
	public static void main(String[] args) {
		

//		NUMBER_OF_THREADS = Integer.parseInt(args[0]);
//		WIKI_FILES_FOLDER = args[1];
//
		executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
		checkWikiPages();

	}

	private static void checkWikiPages() {
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
						
						final Vector<HtmlLink> links = htmlLinkExtractor.grabHTMLLinks(line);
						for (Iterator<?> iterator = links.iterator(); iterator.hasNext();) {
							final HtmlLink htmlLink = (HtmlLink) iterator.next();
							LOG.info(htmlLink);
//							System.out.println(htmlLink);
						}
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
