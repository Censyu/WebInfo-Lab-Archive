import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.queryparser.classic.Token;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.io.StringReader;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws Exception {
		// do something here

		// Build index - set dataPath & indexPath
//		IndexBuilder indexBuilder = new IndexBuilder(
//				"D:\\mylabs\\weblab\\lab1\\data\\test_docs.csv");
//		indexBuilder.BuildIndex();
//		indexBuilder.close();

		// Test searcher - set indexPath & keyword
//		Searcher searcher = new Searcher(
//				"D:\\mylabs\\weblab\\lab1\\SimpleSearcher\\lucene-index");
//		searcher.setfieldName("content");
//		searcher.Search("体育委员");

		// Get standard output for lab - set indexPath, queryPath, resultPath
//		Work();

		// Test
//		TestAnalyzer();
		TestSearch();

	}

	private static void TestAnalyzer() throws IOException {
		System.out.println("测试 IK Analyzer:");
		IKAnalyzer analyzer = new IKAnalyzer(true);
		Scanner in = new Scanner(System.in);
		String str = in.nextLine();
		while (!str.equals("OK")) {
			TokenStream tokenStream = analyzer.tokenStream("content", new StringReader(str));
			CharTermAttribute attr = tokenStream.addAttribute(CharTermAttribute.class);
			tokenStream.reset();
			while (tokenStream.incrementToken()) {
				System.out.print(attr.toString() + "/");
			}
			System.out.println();
			tokenStream.close();
			str = in.nextLine();
		}
	}

	private static void TestSearch() {
		Searcher searcher = new Searcher(
				"D:\\mylabs\\weblab\\lab1\\SimpleSearcher\\lucene-index");
		searcher.setfieldName("content");
		Scanner in = new Scanner(System.in);
		String str = in.nextLine();
		while (!str.equals("OK")) {
			searcher.Search(str);
			str = in.nextLine();
		}
	}

	private static void Work() {
		Searcher searcher = new Searcher(
				"D:\\mylabs\\weblab\\lab1\\SimpleSearcher\\lucene-index");
		searcher.setfieldName("content");
		searcher.Work("D:\\mylabs\\weblab\\lab1\\data\\test_querys2.csv"
				, "D:\\mylabs\\weblab\\lab1\\data\\output2.csv");
	}
}
