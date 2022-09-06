package io.bdrc.lucene;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.lucene.analysis.synonym.SynonymMap;
import org.apache.lucene.util.CharsRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;


public class SpellingVariantFilter {
    
    public static final String baseDir = "src/main/resources/";
    static final Logger logger = LoggerFactory.getLogger(SpellingVariantFilter.class);
    static final SynonymMap smap = getSynonyms();
    
    public static InputStream getResourceOrFile(final String baseName) {
        InputStream stream = null;
        stream = SpellingVariantFilter.class.getClassLoader().getResourceAsStream("/"+baseName);
        if (stream != null) {
            logger.info("found resource /{} through regular classloader", baseName);
            return stream;
        }
        stream = Thread.currentThread().getContextClassLoader().getResourceAsStream("/"+baseName);
        if (stream != null) {
            logger.info("found resource /{} through thread context classloader", baseName);
            return stream;
        }
        final String fileBaseName = baseDir+baseName;
        try {
            stream = new FileInputStream(fileBaseName);
            logger.info("found file {}", fileBaseName);
            return stream;
        } catch (FileNotFoundException e) {
            logger.info("could not find file {}", fileBaseName);
            return null;
        }  
    }
    
    private static SynonymMap getSynonyms() {
        SynonymMap.Builder builder = new SynonymMap.Builder(false);
        try (CSVReader reader = new CSVReader(new InputStreamReader(getResourceOrFile("uk-us-spellings.csv")))) {
            String[] line = reader.readNext();
            while (line != null) {
                builder.add(new CharsRef(line[0]), new CharsRef(line[1]), false);
                line = reader.readNext();
            }
        } catch (CsvValidationException | IOException e) {
            logger.error("can't read CSV file uk-us-spellings.csv", e);
        }
        try {
            return builder.build();
        } catch (IOException ex) {
            logger.error("can't build synonymMap", ex);
            return null;
        }
    }
}
