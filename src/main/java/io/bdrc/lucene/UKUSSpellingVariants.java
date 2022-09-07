package io.bdrc.lucene;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.lucene.analysis.synonym.SynonymMap;
import org.apache.lucene.util.CharsRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class UKUSSpellingVariants {
    
    public static final String baseDir = "src/main/resources/";
    static final Logger logger = LoggerFactory.getLogger(UKUSSpellingVariants.class);
    static final SynonymMap smap = getSynonyms();
    
    public static InputStream getResourceOrFile(final String baseName) {
        InputStream stream = null;
        stream = UKUSSpellingVariants.class.getClassLoader().getResourceAsStream("/"+baseName);
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
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getResourceOrFile("uk-us-spellings.csv")))) {
            // it is a bit dirty to parse csv like that but we fully control the input
            // and that makes one less dependency
            while (reader.ready()) {
                String line = reader.readLine();
                if (line.length() > 1) {
                    String[] row = line.split(",");
                    builder.add(new CharsRef(row[0]), new CharsRef(row[1]), false);
                }
            }
        } catch (IOException e) {
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
