package course.searcher.utils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
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
    
    private static int i = 0;
    private static PrintWriter printWriter;
    private static Set<String> uniqueWords = new HashSet<String>();

    public static void main(String[] args) throws IOException {
        File dirToStoreIndex = new File("src/main/resources/IndexData");

        deleteOlderIndex(dirToStoreIndex);

        Directory directory = FSDirectory.open(dirToStoreIndex.toPath());

        Analyzer analyzer = new StandardAnalyzer();

        IndexWriterConfig config = new IndexWriterConfig(analyzer);

        IndexWriter indexWriter = new IndexWriter(directory, config);
        
        File startFolder = new File("src/main/resources/DataToIndex");
        File dicFile = new File("src/main/resources/dictionary/dictionary.txt");
        dicFile.delete();
        printWriter = new PrintWriter("src/main/resources/dictionary/dictionary.txt");
        
        File dirToBeIndexed = new File("src/main/resources/DataToIndex");
        indexFiles(dirToBeIndexed, indexWriter);
        indexWriter.close();
        printWriter.close();
        System.out.println(i);
    }

    private static void indexFiles(File file, IndexWriter indexWriter) throws IOException {
        File[] files = file.listFiles();
        for (File f : files) {
            if (!f.getName().contains("DS_Store")) {
                if (f.isDirectory()) {
                    indexFiles(f, indexWriter);
                } else {
                    //System.out.println("Indexing file " + f.getCanonicalPath());
                    i++;
                    Map<String, String> attributes = new HashMap<String, String>();
                    try (Stream<String> stream = Files.lines(Paths.get(file + "/" + f.getName()))) {
                        stream.limit(16).forEach(item -> {
                            if (!item.isEmpty() && (!item.contains("Additional description"))) {

//                                System.out.println(item.split(":")[0].trim().toLowerCase());
//                                System.out.println(item.split(":")[1].trim().toLowerCase());
//                                System.out.println(item.indexOf(":"));
                                //System.out.println(item.substring(0, item.indexOf(":")));
                                //System.out.println(item.substring(item.indexOf(":") + 2));
                                attributes.put(item.substring(0, item.indexOf(":")).toLowerCase(), item.substring(item.indexOf(":") + 2));
                                
                            }
                            
                        });

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Document document = new Document();

                    Field title = new StoredField("fileName", f.getName());
                    document.add(title);

                    Field source = new TextField("provider", attributes.get("provider"), Store.YES);
                    document.add(source);

                    Field language = new TextField("language", attributes.get("language"), Store.YES);
                    document.add(language);

                    Field body = new TextField("body", attributes.get("title"), Store.YES);
                    document.add(body);
                    
                    

                    if (attributes.get("price").toLowerCase().equals("free")) {
                        attributes.put("price", "$0.0");
                    }
                    Field price = new DoublePoint("price", Double.valueOf(attributes.get("price").substring(1)));
                    document.add(price);

                    Field rating = new DoublePoint("rating", Double.valueOf(attributes.get("rating")));
                    document.add(rating);
                    //System.out.println(attributes.get("title"));
                    if (attributes.get("title").contains(" ")) {
                        String[] fileWords = attributes.get("title").split("[\\s]+");
                        for (int i = 0; i < fileWords.length; i++) {
                            if (uniqueWords.add(fileWords[i])) {
                                printWriter.println(fileWords[i]);
                            }
                        }
                    } else {
                        if (uniqueWords.add(attributes.get("title"))) {
                            printWriter.println(attributes.get("title"));
                        }
                    }

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
