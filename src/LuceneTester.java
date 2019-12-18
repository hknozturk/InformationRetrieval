import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import EuclideanDistance.EuclideanDistance;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermFreqVector;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 * '+' indicates that a term is a MUST
 * '-' indicates that a term is a MUST_NOT
 * "" indicates that a term is a SHOULD
 *
 * We can use QueryParser to parse BooleanQueries. In addition, we can also use wildcard terms in queries.
 * Example queries: '(mushroom steak) AND (bacon eggs)'
 *                  '(mushroom steak) OR (bacon eggs)'
 *                  'mushroom steaks'
 *                  '(mushroom OR steak)' AND (ste?k)'
 *                  '+contents:mushrooms +contents:steak'
 *                  '+contents:mushrooms -contents:steak'
 *                  '+contents:mushrooms contents:steak'
 */

public class LuceneTester {
    String indexDir = "/home/hawken/InformationRetrieval/Index";
    String dataDir = "/home/hawken/InformationRetrieval/articles";
    Indexer indexer;
    Searcher searcher;

    public static void main(String[] args) throws IOException, ParseException {
        LuceneTester tester;
        tester = new LuceneTester();

//        tester.creteIndex();

        System.out.println("Welcome to Wikipedia Search Engine");
        System.out.println("Enter search query:");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String searchQuery = reader.readLine();

//        tester.sortUsingIndex(searchQuery);
        tester.sortUsingRelevance(searchQuery);
//        tester.search(searchQuery);
    }

    private void creteIndex() throws IOException {
        indexer = new Indexer(indexDir);
        int numIndexed;
        long startTime = System.currentTimeMillis();
        numIndexed = indexer.createIndex(dataDir, new HtmlFileFilter());
        long endTime = System.currentTimeMillis();
        indexer.close();
        System.out.println(numIndexed + " File indexed, time taken: " + (endTime - startTime) + " ms");
    }

    private void search(String searchQuery) throws IOException, ParseException {
        Directory indexDirectory = FSDirectory.open(new File(indexDir));
        IndexReader indexReader = IndexReader.open(indexDirectory);
        searcher = new Searcher(indexDir);
        long startTime = System.currentTimeMillis();
        TopDocs hits = searcher.search(searchQuery);
        long endTime = System.currentTimeMillis();

        System.out.println(hits.totalHits + " documents found.\nTime: " + (endTime - startTime) + " ms");
        for (ScoreDoc scoreDoc : hits.scoreDocs) {
            int docId = scoreDoc.doc;
            Document doc = searcher.getDocument(scoreDoc);
            TermFreqVector termFreqVector = indexReader.getTermFreqVector(docId, Constants.CONTENTS);
            System.out.print("Score: "+ scoreDoc.score + " ");
            System.out.println("File: " + doc.get(Constants.FILE_PATH));
            System.out.println("Document Term Freq: " + termFreqVector);
        }

        searcher.close();
    }

    private void sortUsingRelevance(String searchQuery) throws IOException, ParseException {
        Directory indexDirectory = FSDirectory.open(new File(indexDir));
        IndexReader indexReader = IndexReader.open(indexDirectory);
        searcher = new Searcher(indexDir);
        long startTime = System.currentTimeMillis();
        searcher.setDefaultFieldSortScoring(true, false);
        TopDocs hits = searcher.search(searchQuery, Sort.RELEVANCE);
        long endTime = System.currentTimeMillis();

        System.out.println(hits.totalHits +
                " documents found. Time :" + (endTime - startTime) + "ms");
        for(ScoreDoc scoreDoc : hits.scoreDocs) {
            int docId = scoreDoc.doc;
            Document doc = searcher.getDocument(scoreDoc);
            TermFreqVector termFreqVector = indexReader.getTermFreqVector(docId, Constants.CONTENTS);
            System.out.print("Score: "+ scoreDoc.score + " ");
            System.out.println("File: "+ doc.get(Constants.FILE_PATH));
//            System.out.println("Document Term Freq: " + termFreqVector);
        }
        searcher.close();
    }

    private void sortUsingIndex(String searchQuery) throws IOException, ParseException {
        Directory indexDirectory = FSDirectory.open(new File(indexDir));
        IndexReader indexReader = IndexReader.open(indexDirectory);
        searcher = new Searcher(indexDir);
        long startTime = System.currentTimeMillis();

        searcher.setDefaultFieldSortScoring(true, false);
        TopDocs hits = searcher.search(searchQuery, Sort.INDEXORDER);
        long endTime = System.currentTimeMillis();

        System.out.println(hits.totalHits +
                " documents found. Time :" + (endTime - startTime) + "ms");
        for(ScoreDoc scoreDoc : hits.scoreDocs) {
            int docId = scoreDoc.doc;
            Document doc = searcher.getDocument(scoreDoc);
            TermFreqVector termFreqVector = indexReader.getTermFreqVector(docId, Constants.CONTENTS);
            System.out.print("Score: "+ scoreDoc.score + " ");
            System.out.println("File: "+ doc.get(Constants.FILE_PATH));
            System.out.println("Document Term Freq: " + termFreqVector);
        }
        searcher.close();
    }
}
