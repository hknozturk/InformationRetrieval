package Soundex;

import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Set;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;

/**
 * Custom SoundexAnalyzer is implemented by following the lucene documentation
 * {https://lucene.apache.org/core/3_6_2/api/core/org/apache/lucene/analysis/package-summary.html}
 **/

public class SoundexAnalyzer extends ReusableAnalyzerBase {
    private Set<?> stopSet;
    private Version matchVersion;

    public SoundexAnalyzer(Version matchVersion) {
        this.matchVersion = matchVersion;
        stopSet = StopFilter.makeStopSet(matchVersion);
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
        StandardTokenizer tokenStream = new StandardTokenizer(matchVersion, reader);
        // Default maximum allowed token length: 255
        tokenStream.setMaxTokenLength(255);
        TokenStream result = new StandardFilter(matchVersion, tokenStream);
        result = new LowerCaseFilter(matchVersion, result);
        result = new StopFilter(matchVersion, result, stopSet);
        result = new SoundexFilter(result);

        return new TokenStreamComponents(tokenStream, result);
    }

    public static void main(String[] args) throws IOException {
        // text to tokenize
        final String text = "This is a demo of the TokenStream API";

        SoundexAnalyzer soundexAnalyzer = new SoundexAnalyzer(Version.LUCENE_36);
        TokenStream stream = soundexAnalyzer.tokenStream("field", new StringReader(text));

        // get the CharTermAttribute from the TokenStream
        CharTermAttribute termAtt = stream.addAttribute(CharTermAttribute.class);

        try {
            stream.reset();

            // print all tokens until stream is exhausted
            while (stream.incrementToken()) {
                System.out.println(termAtt.toString());
            }

            stream.end();
        } finally {
            stream.close();
        }
    }
}
