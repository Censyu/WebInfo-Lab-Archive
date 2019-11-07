public class Main {
	public static void main(String[] args) throws Exception{
		// do something here

		// Build index - set dataPath & indexPath
//		IndexBuilder indexBuilder = new IndexBuilder(
//				"D:\\mylabs\\weblab\\lab1\\data\\data100.csv");
//		indexBuilder.BuildIndex();
//		indexBuilder.close();

		// Test searcher - set indexPath & keyword
//		Searcher searcher = new Searcher(
//				"D:\\mylabs\\weblab\\lab1\\SimpleSearcher\\lucene-index");
//		searcher.setfieldName("content");
//		searcher.Search("体育委员");

		// Get standard output for lab - set indexPath, queryPath, resultPath
		Searcher searcher = new Searcher(
				"D:\\mylabs\\weblab\\lab1\\SimpleSearcher\\lucene-index");
		searcher.Work("D:\\mylabs\\weblab\\lab1\\data\\test_querys.csv"
				, "result.csv");

	}
}
