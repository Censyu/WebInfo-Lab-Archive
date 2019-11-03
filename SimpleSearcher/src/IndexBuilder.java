import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class IndexBuilder {
	private String indexPath = "lucene-index";
	private String dataPath = "data";
	private Directory directory;    // FIXME:
	private Directory dir;
	private Analyzer analyzer;
	private IndexWriterConfig config;
	private IndexWriter indexWriter;
	private File[] files;

	public IndexBuilder() throws IOException {
		this.init();
	}

	public IndexBuilder(String path) throws IOException {
		this.indexPath = path;
		init();
	}

	public void setIndexPath(String path){
		this.indexPath = path;
	}

	public void setDataPath(String path) {
		this.dataPath = path;
	}

	public void init() throws IOException {
		// FIXME: choose RAM directory or DISK directory
//		directory = new RAMDirectory();
		dir = FSDirectory.open(Paths.get(indexPath));

		analyzer = new StandardAnalyzer();
		config = new IndexWriterConfig(analyzer);
		indexWriter = new IndexWriter(directory, config);

		config.setOpenMode(OpenMode.CREATE);
	}

	public void ParseData() throws IOException {
		// TODO: parse data from .csv
	}

	public void AddDocument() throws IOException {
		Document doc = new Document();
		doc.add(new StoredField("id", ""));
		doc.add(new StoredField("url", ""));
		doc.add(new TextField("title", "",Field.Store.YES));
		doc.add(new TextField("content", "",Field.Store.YES));

		indexWriter.addDocument(doc);
	}

	public void BuildIndex() throws IOException {
		// TODO: save to index file
		indexWriter.commit();
//		indexWriter.close();
	}

	public static void main(String[] args){
		// test this class here
	}
}
