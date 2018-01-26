package course.searcher.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoublePoint;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.stereotype.Component;

import course.searcher.domain.Course;

@Component
public class SearchService {

    public List<Course> search(String searchQuery, String isFreeSelected, String[] sources, String priceRange,
            String highRating) throws IOException, ParseException {

        List<String> documentNames = new ArrayList<String>();
        File dirToStoreIndex = new File(getClass().getResource("/IndexData").getFile());
        Directory directory = FSDirectory.open(dirToStoreIndex.toPath());

        IndexReader indexReader = DirectoryReader.open(directory);

        IndexSearcher searcher = new IndexSearcher(indexReader);

        Query query = generateQuery(searchQuery, isFreeSelected, sources, priceRange, highRating);

        TopDocs topDocs = searcher.search(query, 1000);

        ScoreDoc[] hits = topDocs.scoreDocs;
        for (int i = 0; i < hits.length; i++) {
            int docId = hits[i].doc;
            Document document = searcher.doc(docId);
            String fileName = document.get("fileName");
            documentNames.add(fileName);
        }

        return loadCoursesFromFiles(documentNames);
    }

    public List<Course> loadCoursesFromFiles(List<String> documents) {

        List<Course> courses = new ArrayList<Course>();

        File dataDirectory = new File(getClass().getResource("/DataToIndex").getFile());

        for (String document : documents) {

            Map<String, String> attributes = new HashMap<String, String>();

            try (Stream<String> stream = Files.lines(Paths.get(dataDirectory + "/" + document))) {
                stream.forEach(item -> {
                    attributes.put(item.split(":")[0].trim().toLowerCase(), item.split(":")[1].trim());
                });

                Course course = new Course(attributes.get("source"), attributes.get("title"), attributes.get("price"),
                        attributes.get("author"), attributes.get("length"), attributes.get("rating"));
                courses.add(course);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return courses;
    }

    private BooleanQuery generateQuery(String searchQuery, String isFree, String[] sources, String priceRange,
            String highRating) {
        TermQuery searchQueryTerm = new TermQuery(new Term("body", searchQuery.toLowerCase()));

        BooleanQuery.Builder booleanQuieryBuilder = new BooleanQuery.Builder();
        booleanQuieryBuilder.add(new BooleanClause(searchQueryTerm, Occur.MUST));

        if (isFree != null) {
            TermQuery termQuery = new TermQuery(new Term("body", "free"));
            booleanQuieryBuilder.add(new BooleanClause(termQuery, Occur.MUST));
        }
        
        if (highRating != null) {
            booleanQuieryBuilder.add(getDoublePointQuery("rating", 4.0, 6), Occur.MUST);
        }

        Set<String> sourceSet = new HashSet<String>(Arrays.asList(sources));
        if (!sourceSet.contains("All") && sourceSet.size() != 3) {
            booleanQuieryBuilder.add(getSourceQuery(sourceSet), Occur.MUST);
        }

        int priceRangeInt = Integer.valueOf(priceRange);
        switch (priceRangeInt) {
        case 0:
            break;
        case 1:
            booleanQuieryBuilder.add(getDoublePointQuery("price", 0.0, 49.9), Occur.MUST);
            break;
        case 2:
            booleanQuieryBuilder.add(getDoublePointQuery("price", 50.0, 99.9), Occur.MUST);
            break;
        case 3:
            booleanQuieryBuilder.add(getDoublePointQuery("price", 100.0, 9999.9), Occur.MUST);
            break;
        default:
            break;
        }

        return booleanQuieryBuilder.build();
    }

    private Query getDoublePointQuery(String field, double lowerValue, double maxValue) {
        return DoublePoint.newRangeQuery(field, lowerValue, maxValue);
    }

    private Query getSourceQuery(Set<String> sources) {
        BooleanQuery.Builder sourceBooleanQueryBuilder = new BooleanQuery.Builder();
        for (String source : sources) {
            TermQuery sourceTermQuery = new TermQuery(new Term("source", source.toLowerCase()));
            sourceBooleanQueryBuilder.add(new BooleanClause(sourceTermQuery, Occur.SHOULD));
        }
        return sourceBooleanQueryBuilder.build();
    }

}
