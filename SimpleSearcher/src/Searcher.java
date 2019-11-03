import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.nio.file.Paths;

public class Searcher {

	private int maxSearchNum = 20;
	private String indexPath = "lucene-index";
	private Directory dir;
	private Analyzer analyzer;
	private IndexReader reader;
	private IndexSearcher searcher;
	private QueryParser parser;
	private String fieldName = "title";
	private Query query;

	TopDocs topDocs;
	ScoreDoc[] scoreDocs;

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
		} catch (IOException e) {
			System.out.println("Cannot open index files at: \"" + indexPath + "\"");
			e.printStackTrace();
		}
	}

	public void Query(String keyword) {
		try {
			System.out.println("Searching: \"" + keyword + "\"");
			query = parser.parse(keyword);
			topDocs = searcher.search(query, maxSearchNum);
			System.out.println("Find: " + topDocs.totalHits + " results!");
			scoreDocs = topDocs.scoreDocs;

			// TODO: set output here
			PrintResults();

		} catch (ParseException e) {
			System.out.println("Parse error with keyword: \"" + keyword + "\"");
			e.printStackTrace();
		} catch (IOException e_io) {
			System.out.println("Search error with keyword: \"" + keyword + "\"");
			e_io.printStackTrace();
		}
	}

	private void PrintResults() {
		try{
			int count = 0;
			for (ScoreDoc result : scoreDocs) {
				Document targetDoc = searcher.doc(result.doc);
				System.out.println("--------------------------------------------");
				System.out.println("NO.    : " + ++count);
				System.out.println("id     : " + targetDoc.get("id"));
				System.out.println("url    : " + targetDoc.get("url"));
				System.out.println("title  : " + targetDoc.get("title"));
//				System.out.println("content: " + targetDoc.get("content"));
				System.out.println("score* : " + result.score);
			}
		} catch (IOException e) {
			System.out.println("Failed to get results!");
			e.printStackTrace();
		}

	}

	private void SaveResults() {
		// TODO: save to file
	}
}
