package course.searcher.utils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class Indexer {

    public static void main(String[] args) throws IOException {
        // Where to store indexes
        File dirToStoreIndex = new File("src/main/resources/IndexData");

        for (File file : dirToStoreIndex.listFiles()) {
            file.delete();
        }

        Directory directory = FSDirectory.open(dirToStoreIndex.toPath());

        Analyzer analyzer = new StandardAnalyzer();

        IndexWriterConfig config = new IndexWriterConfig(analyzer);

        IndexWriter indexWriter = new IndexWriter(directory, config);

        File dirToBeIndexed = new File("src/main/resources/DataToIndex");

        File[] files = dirToBeIndexed.listFiles();

        for (File f : files) {
            System.out.println("Indexing file " + f.getCanonicalPath());
            Document document = new Document();

            Field title = new StoredField("fileName", f.getName());
            document.add(title);

            Field location = new StoredField("location", f.getCanonicalPath());
            System.out.println(f.getPath());
            document.add(location);

            Field body = new TextField("body", new FileReader(f));
            document.add(body);

            indexWriter.addDocument(document);
        }

        indexWriter.close();

    }

}
