package io.bdrc.lucene;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.junit.Test;

public class TestAnalyzer {

    static TokenStream tokenize(final Reader reader, final Tokenizer tokenizer) throws IOException {
        tokenizer.close();
        tokenizer.end();
        tokenizer.setReader(reader);
        tokenizer.reset();
        return tokenizer;
    }
    
    static private void assertTokenStream(final TokenStream tokenStream, final List<String> expected) {
        try {
            List<String> termList = new ArrayList<String>();
            CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
            while (tokenStream.incrementToken()) {
                termList.add(charTermAttribute.toString());
            }
            System.out.println("1 " + String.join(" ", expected));
            System.out.println("2 " + String.join(" ", termList) + "\n");
            assertThat(termList, is(expected));
        } catch (IOException e) {
            assertTrue(false);
        }
    }
    
    @Test
    public void testEnglishAnalyzer() throws Exception {
        String input = "realizes realisation Tashi's ཀིས་ཁ་རྒ་ང་ 一理 śā s\u0301a\u0304 qing";
        Reader reader = new StringReader(input);
        List<String> expected = Arrays.asList("realiz", "realiz", "tashi", "ཀིས", "ཁ", "རྒ", "ང", "一", "理", "sha", "sha", "qing");
        Analyzer englishAnalyzer = new BDRCEnglishAnalyzer();
        TokenStream indexTk = englishAnalyzer.tokenStream("", input);
        indexTk.reset();
        assertTokenStream(indexTk, expected);
        englishAnalyzer.close();
        reader.close();
    }
}
