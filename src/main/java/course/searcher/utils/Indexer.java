package course.searcher.utils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoublePoint;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class Indexer {

    public static void main(String[] args) throws IOException {
        File dirToStoreIndex = new File("src/main/resources/IndexData");

        deleteOlderIndex(dirToStoreIndex);

        Directory directory = FSDirectory.open(dirToStoreIndex.toPath());

        Analyzer analyzer = new StandardAnalyzer();

        IndexWriterConfig config = new IndexWriterConfig(analyzer);

        IndexWriter indexWriter = new IndexWriter(directory, config);

        File dirToBeIndexed = new File("src/main/resources/DataToIndex");
        indexFiles(dirToBeIndexed, indexWriter);
        indexWriter.close();
    }

    private static void indexFiles(File file, IndexWriter indexWriter) throws IOException {
        File[] files = file.listFiles();
        for (File f : files) {
            if (!f.getName().contains("DS_Store")) {
                if (f.isDirectory()) {
                    indexFiles(f, indexWriter);
                } else {
                    System.out.println("Indexing file " + f.getCanonicalPath());
                    Map<String, String> attributes = new HashMap<String, String>();
                    try (Stream<String> stream = Files.lines(Paths.get(file + "/" + f.getName()))) {

                        stream.forEach(item -> {
                            attributes.put(item.split(":")[0].trim().toLowerCase(), item.split(":")[1].trim());
                        });

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Document document = new Document();

                    Field title = new StoredField("fileName", f.getName());
                    document.add(title);

                    Field source = new TextField("source", attributes.get("source"), Store.YES);
                    document.add(source);

                    Field body = new TextField("body", new FileReader(f));
                    document.add(body);

                    if (attributes.get("price").toLowerCase().equals("free")) {
                        attributes.put("price", "0.0");
                    }
                    Field price = new DoublePoint("price", Double.valueOf(attributes.get("price")));
                    document.add(price);

                    Field rating = new DoublePoint("rating", Double.valueOf(attributes.get("rating")));
                    document.add(rating);

                    indexWriter.addDocument(document);
                }
            }
        }
    }

    private static void deleteOlderIndex(File dirToStoreIndex) {
        for (File file : dirToStoreIndex.listFiles()) {
            file.delete();
        }
    }
}
