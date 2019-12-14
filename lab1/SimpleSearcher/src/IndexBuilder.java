import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.*;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

public class IndexBuilder {
	private String dataPath = "data";
	private String indexPath = "lucene-index";
	private Directory ramdir;
	private Analyzer analyzer;
	private IndexWriterConfig config;
	private IndexWriter ramwriter;
	//	private File[] files;
	private BufferedReader reader;
	private String[] dataline;


	public IndexBuilder() throws IOException {
		this.init();
	}

	public IndexBuilder(String dataPath) throws IOException {
		this.dataPath = dataPath;
		init();
	}

	public IndexBuilder(String dataPath, String indexPath) throws IOException {
		this.dataPath = dataPath;
		this.indexPath = indexPath;
		init();
	}

	public void setIndexPath(String path) {
		this.indexPath = path;
	}

	public void setDataPath(String path) {
		this.dataPath = path;
	}

	private void init() throws IOException {
		System.out.println("Init indexBuilder... ");
		System.out.println("Data Path: \"" + dataPath + "\"");
		System.out.println("Index Path: \"" + indexPath + "\"");
		// using ramDirectory, save to disk when builder closed
		ramdir = new RAMDirectory();
		// using IK analyzer
		analyzer = new IKAnalyzer(true);

		config = new IndexWriterConfig(analyzer);
		config.setOpenMode(OpenMode.CREATE_OR_APPEND);
		ramwriter = new IndexWriter(ramdir, config);

		reader = new BufferedReader(new FileReader(dataPath));
	}

	// Parse one line from data.csv
	private String[] ParseDataLine() throws IOException {
		String line = reader.readLine();
		if (line == null || line.trim().isEmpty()) {
			return null;
		} else {
			// FIXME: spilt facing too many commas
			return line.split(",", 4);
		}
	}

	private Boolean CreateDocument() throws IOException {
		dataline = ParseDataLine();
		if (dataline != null) {
			Document doc = new Document();
			doc.add(new StoredField("id", dataline[0]));
			doc.add(new StoredField("url", dataline[1]));
			doc.add(new TextField("title", dataline[2], Field.Store.YES));
			doc.add(new TextField("content", dataline[3], Field.Store.YES));

			// test here
//			if(dataline[0].equals("12")) {
//				System.out.println(dataline[3]);
//			}

			ramwriter.addDocument(doc);
			return true;
		} else {
			return false;
		}
	}

	public void BuildIndex() throws IOException {
		// Discard titles in first row
		ParseDataLine();
		int count = 0;
		while (CreateDocument()) {
			count++;
			if (count % 200 == 0) {
				System.out.println("Create " + count + " entries...");
			}
		}
		System.out.println("Build Index Done! Total count: " + count);
		ramwriter.commit();
	}

	// FIXME: test this method
	private void SaveIndex() throws IOException {
		Directory fsdir = FSDirectory.open(Paths.get(indexPath));
		IndexWriterConfig fsconfig = new IndexWriterConfig(analyzer);
		IndexWriter fswriter = new IndexWriter(fsdir, fsconfig);
		fswriter.addIndexes(ramdir);
		fswriter.close();
	}

	public void close() throws IOException {
		reader.close();
		ramwriter.close();
		SaveIndex();
	}

	public static void main(String[] args) throws IOException {
		// test this class here
		IndexBuilder indexBuilder = new IndexBuilder();
		indexBuilder.setDataPath("path");
		indexBuilder.setIndexPath("path");
		indexBuilder.BuildIndex();
		indexBuilder.close();
	}
}
