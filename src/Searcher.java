/**
 * References for SearchQuery
 * 1. https://www.avajava.com/tutorials/lessons/how-do-i-combine-queries-with-a-boolean-query.html
 * 2. https://www.tutorialspoint.com/lucene/lucene_booleanquery.htm
 * */

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
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
}
