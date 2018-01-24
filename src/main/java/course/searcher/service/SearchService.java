package course.searcher.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.QueryBuilder;
import org.springframework.stereotype.Component;

import course.searcher.domain.Course;

@Component
public class SearchService {

    public List<Course> search(String searchQuery, String isFreeSelected, String source)
            throws IOException, ParseException {

        List<String> documentNames = new ArrayList<String>();
        System.out.println(getClass().getResource("/IndexData") == null);
        File dirToStoreIndex = new File(getClass().getResource("/IndexData").getFile());
        Directory directory = FSDirectory.open(dirToStoreIndex.toPath());

        // start searching
        IndexReader indexReader = DirectoryReader.open(directory);

        IndexSearcher searcher = new IndexSearcher(indexReader);

        Query query = generateQuery(searchQuery, isFreeSelected, source);

        TopDocs topDocs = searcher.search(query, 10);

        ScoreDoc[] hits = topDocs.scoreDocs;
        for (int i = 0; i < hits.length; i++) {
            int docId = hits[i].doc;
            Document d = searcher.doc(docId);
            String fileName = d.get("fileName");
            documentNames.add(fileName);
        }

        return loadCoursesFromFiles(documentNames);
    }

    public List<Course> loadCoursesFromFiles(List<String> documents) {

        List<Course> courses = new ArrayList<Course>();

        File dataDirectory = new File(getClass().getResource("/DataToIndex").getFile());

        for (String document : documents) {

            List<String> attributes = new ArrayList<String>();

            try (Stream<String> stream = Files.lines(Paths.get(dataDirectory + "/" + document))) {

                stream.forEach(item -> {
                    attributes.add(item.split(":")[1].trim());
                });

                Course course = new Course(attributes.get(0), attributes.get(1), attributes.get(2), attributes.get(3),
                        attributes.get(4));
                courses.add(course);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return courses;
    }

    private BooleanQuery generateQuery(String searchQuery, String isFree, String source) {
        TermQuery searchQueryTerm = new TermQuery(new Term("body", searchQuery.toLowerCase()));

        BooleanQuery.Builder booleanQuieryBuilder = new BooleanQuery.Builder();
        booleanQuieryBuilder.add(new BooleanClause(searchQueryTerm, Occur.MUST));

        if (isFree != null) {
            TermQuery termQuery = new TermQuery(new Term("body", "free"));
            booleanQuieryBuilder.add(new BooleanClause(termQuery, Occur.MUST));
        }
        if (!source.toLowerCase().equals("all")) {
            TermQuery termQuery = new TermQuery(new Term("body", source.toLowerCase()));
            booleanQuieryBuilder.add(new BooleanClause(termQuery, Occur.MUST));
        }
        return booleanQuieryBuilder.build();

    }
}
