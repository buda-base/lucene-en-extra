package io.bdrc.lucene;

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.en.EnglishPossessiveFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.synonym.SynonymGraphFilter;

public class BDRCEnglishAnalyzer extends Analyzer {

    @Override
    protected Reader initReader(final String fieldName, Reader reader) {
        reader = new TibetanDTSFilter(reader);
        return super.initReader(fieldName, reader);
    }
    
    @Override
    protected TokenStreamComponents createComponents(final String fieldName) {
      final Tokenizer source = new StandardTokenizer();
      TokenStream result = new ASCIIFoldingFilter(source);
      result = new EnglishPossessiveFilter(result);
      result = new LowerCaseFilter(result);
      result = new StopFilter(result, EnglishAnalyzer.ENGLISH_STOP_WORDS_SET);
      result = new SynonymGraphFilter(result, SpellingVariantFilter.smap, false);
      result = new PorterStemFilter(result);
      return new TokenStreamComponents(source, result);
    }

    @Override
    protected TokenStream normalize(final String fieldName, final TokenStream in) {
      return new LowerCaseFilter(in);
    }
    
}
