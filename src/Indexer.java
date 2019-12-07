import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

public class Indexer {
    private IndexWriter writer;

    public Indexer(String indexDirectoryPath) throws IOException {
        // directory to store the indexes
        Directory indexDirectory = FSDirectory.open(new File(indexDirectoryPath));

        // indexer
        writer = new IndexWriter(indexDirectory, new StandardAnalyzer(Version.LUCENE_36), true, IndexWriter.MaxFieldLength.UNLIMITED);
    }

    public void close() throws CorruptIndexException, IOException {
        writer.close();
    }

    private Document getDocument(File file) throws IOException {
        Document document = new Document();

        // index file contents
        // add JSoup to strip body from html file and remove tags to get text from file.
        // TO-DO: Later, we can parse html files into more meaningful sections (meta-data, links, body, ...).
        org.jsoup.nodes.Document htmlDoc = Jsoup.parse(file, "utf-8");
        String bodyText = htmlDoc.body().text();

        document.add(new Field(Constants.CONTENTS, bodyText, Field.Store.YES, Field.Index.ANALYZED));
        document.add(new Field(Constants.FILE_NAME, file.getName(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        document.add(new Field(Constants.FILE_PATH, file.getCanonicalPath(), Field.Store.YES, Field.Index.NOT_ANALYZED));

        return document;
    }

    private void indexFile(File file) throws IOException {
        System.out.println("Indexing " + file.getCanonicalPath());
        Document document = getDocument(file);
        writer.addDocument(document);
    }

    public int createIndex(String dataDirPath, FileFilter filter) throws IOException {
        // get all the files in the directory
        File[] list = new File(dataDirPath).listFiles();

        for (File file: list) {
            if (file.isDirectory()) {
                createIndex(file.getAbsolutePath(), new HtmlFileFilter());
            }
            if (!file.isDirectory() && !file.isHidden() && file.exists() && file.canRead() && filter.accept(file)) {
                indexFile(file);
            }
        }

        return writer.numDocs();
    }
}
