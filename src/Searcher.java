import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class Searcher {
    IndexSearcher indexSearcher;
    QueryParser queryParser;
    Query query;

    public Searcher(String indexDirectoryPath) throws IOException {
        Directory indexDirectory = FSDirectory.open(new File(indexDirectoryPath));
        indexSearcher = new IndexSearcher(indexDirectory);
        indexSearcher.setSimilarity(similarity);
        queryParser = new QueryParser(Version.LUCENE_36, Constants.CONTENTS, new StandardAnalyzer(Version.LUCENE_36));
    }

    public TopDocs search(String searchQuery) throws IOException, ParseException {
        System.out.println("Searching for " + searchQuery);
        query = queryParser.parse(searchQuery);
        System.out.println("Type of query: " + query.getClass().getSimpleName());
        System.out.println("Query: " + query.toString());

        return indexSearcher.search(query, Constants.MAX_SEARCH);
    }

    public Document getDocument(ScoreDoc scoreDoc) throws IOException, ParseException {
        return indexSearcher.doc(scoreDoc.doc);
    }

    public void close() throws IOException {
        indexSearcher.close();
    }
    /**
     * TO-DO: We need to implement Euclidean Distance for scoring.
     * Link may help:
     * - https://lucene.apache.org/core/3_6_2/api/core/org/apache/lucene/search/Similarity.html
     * - https://stackoverflow.com/questions/41090904/lucene-scoring-get-cosine-similarity-as-scores
     * - http://www.lucenetutorial.com/advanced-topics/scoring.html
     * - Lecture 6 slides
     *
     * We can compute TF-IDF weighted vector space model by using the IndexReader.getTermFreqVector() and Searcher.docFreq()
     **/
    Similarity similarity = new DefaultSimilarity() {
        public float idf(int i, int il) {
            return 1;
        }
    };
}
