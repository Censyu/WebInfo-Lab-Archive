public class Main {
	public static void main(String[] args) throws Exception{
		// do something here
//		IndexBuilder indexBuilder = new IndexBuilder(
//				"D:\\mylabs\\weblab\\lab1\\data\\data50.csv");
//		indexBuilder.BuildIndex();
//		indexBuilder.close();

		Searcher searcher = new Searcher(
				"D:\\mylabs\\weblab\\lab1\\SimpleSearcher\\lucene-index");
		searcher.Query("豆丁网");
	}
}
