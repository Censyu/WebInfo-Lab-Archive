import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.*;
import java.nio.file.Paths;

public class Searcher {

	private int maxSearchNum = 20;
	private boolean showContent = false;
	private String indexPath = "lucene-index";
	private Directory dir;
	private Analyzer analyzer;
	private IndexReader reader;
	private IndexSearcher searcher;
	private QueryParser parser;
	private MultiFieldQueryParser multiParser;
	private String fieldName = "";
	private Query query;

	private BufferedReader buffReader;
	private BufferedWriter buffWriter;

	private static String lineseperator = System.getProperty("line.separator");


	public Searcher() {
		init();
	}

	public Searcher(String indexPath) {
		this.indexPath = indexPath;
		init();
	}

	public void setIndexPath(String path) {
		this.indexPath = path;
	}

	public void setfieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	private void init() {
		try {
			dir = FSDirectory.open(Paths.get(indexPath));
			analyzer = new IKAnalyzer(true);
			reader = DirectoryReader.open(dir);
			searcher = new IndexSearcher(reader);
			parser = new QueryParser(fieldName, analyzer);
			parser.setDefaultOperator(QueryParser.AND_OPERATOR);
			multiParser = new MultiFieldQueryParser(new String[]{"title", "content"}, analyzer);
			multiParser.setDefaultOperator(MultiFieldQueryParser.AND_OPERATOR);
		} catch (IOException e) {
			System.out.println("Cannot open index files at: \"" + indexPath + "\"");
			e.printStackTrace();
		}
	}

	public void Search(String keyword) {
			long start = System.currentTimeMillis();
			TopDocs topDocs = SearchDocs(keyword);
			long end = System.currentTimeMillis();
			System.out.println("Find: " + topDocs.totalHits + " results! Using " +
					(end - start) + " ms");

			// Set output here
			PrintResults(topDocs);
	}

	private void PrintResults(TopDocs topDocs) {
		try {
			ScoreDoc [] scoreDocs = topDocs.scoreDocs;
			int count = 0;

			for (ScoreDoc result : scoreDocs) {
				Document targetDoc = searcher.doc(result.doc);
				System.out.println("--------------------------------------------");
				System.out.println("NO.    : " + ++count);
				System.out.println("id     : " + targetDoc.get("id"));
				System.out.println("url    : " + targetDoc.get("url"));
				System.out.println("title  : " + targetDoc.get("title"));

				if (showContent) {
					System.out.println("content: " + targetDoc.get("content"));
				}

				System.out.println("score* : " + result.score);
			}
		} catch (IOException e) {
			System.out.println("Failed to get results!");
			e.printStackTrace();
		}
	}

	// Work on standard query file for lab1
	// FIXME: test on this method
	public void Work(String queryPath, String resultPath) {
		try {
			int count = 0;
			String[] dataline;
			buffReader = new BufferedReader(new FileReader(queryPath));
			buffWriter = new BufferedWriter(new FileWriter(resultPath));
			TopDocs searchResults;
			ScoreDoc[] scoreDocs;

			// discard title
			buffReader.readLine();
			// write title
			buffWriter.write("query_id,doc_id\n");

			// read all queries in file
			// | query | query_id |
			while ((dataline = ParseDataLine()) != null) {
				count++;
				searchResults = SearchDocs(dataline[0]);
				scoreDocs = searchResults.scoreDocs;

				System.out.println("Get: " + searchResults.totalHits + "Hits!");

				for (ScoreDoc scoreDoc : scoreDocs) {
					int id = scoreDoc.doc;
					// write one line
					// | query_id | doc_id |
					buffWriter.write(dataline[1] + "," +
							searcher.doc(id).get("id") + "\n");
				}
			}
			System.out.println("Finished! Total count:" + count);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String[] ParseDataLine() throws IOException {
		String line = buffReader.readLine();
		if (line == null || line.trim().isEmpty()) {
			return null;
		} else {
			return line.split(",", 2);
		}
	}

	private TopDocs SearchDocs(String keyword) {
		System.out.println("Searching: \"" + keyword + "\"");
		try {
			query = multiParser.parse(keyword);
			return searcher.search(query, maxSearchNum);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
