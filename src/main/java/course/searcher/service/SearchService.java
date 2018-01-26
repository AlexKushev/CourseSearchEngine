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
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import course.searcher.domain.Course;
import course.searcher.utils.Utils;

@Component
public class SearchService {

    @Autowired
    private SpellCheckService spellCheckerService;
    private List<String> documentNames;

    public List<Course> search(String searchQuery, String isFreeSelected, String[] sources, String priceRange,
            String highRating) throws IOException, ParseException {

        documentNames = new ArrayList<String>();

        File dirToStoreIndex = new File(getClass().getResource("/IndexData").getFile());
        Directory directory = FSDirectory.open(dirToStoreIndex.toPath());

        IndexReader indexReader = DirectoryReader.open(directory);

        IndexSearcher searcher = new IndexSearcher(indexReader);

        generateQuery(searchQuery, searcher, isFreeSelected, sources, priceRange, highRating, false);

        // TopDocs topDocs = searcher.search(query, 1000);
        //
        // ScoreDoc[] hits = topDocs.scoreDocs;
        // for (int i = 0; i < hits.length; i++) {
        // int docId = hits[i].doc;
        // Document document = searcher.doc(docId);
        // String fileName = document.get("fileName");
        // documentNames.add(fileName);
        // }

        return loadCoursesFromFiles(documentNames);
    }

    private int getHits(Query query, IndexSearcher searcher) throws IOException {
        TopDocs topDocs = searcher.search(query, 1000);

        ScoreDoc[] hits = topDocs.scoreDocs;
        for (int i = 0; i < hits.length; i++) {
            int docId = hits[i].doc;
            Document document = searcher.doc(docId);
            String fileName = document.get("fileName");
            documentNames.add(fileName);
        }
        return hits.length;
    }

    public List<Course> loadCoursesFromFiles(List<String> documents) {

        List<Course> courses = new ArrayList<Course>();

        File dataDirectory = new File(getClass().getResource("/DataToIndex").getFile());

        for (String document : documents) {

            Map<String, String> attributes = new HashMap<String, String>();

            try (Stream<String> stream = Files.lines(Paths.get(dataDirectory + "/" + document))) {
                stream.limit(16).forEach(item -> {
                    if (!item.isEmpty()
                            && (!item.contains("Additional description") && (!item.contains("Description")))) {

                        // System.out.println(item.split(":")[0].trim().toLowerCase());
                        // System.out.println(item.split(":")[1].trim().toLowerCase());
                        // System.out.println(item.indexOf(":"));
                        // System.out.println(item.substring(0,
                        // item.indexOf(":")));
                        // System.out.println(item.substring(item.indexOf(":") +
                        // 2));
                        attributes.put(item.substring(0, item.indexOf(":")).toLowerCase(),
                                item.substring(item.indexOf(":") + 2));

                    }

                });
                Course course = new Course(attributes.get("provider"), attributes.get("title"), attributes.get("price"),
                        attributes.get("instructor"), attributes.get("length"), attributes.get("rating"),
                        attributes.get("language"), attributes.get("description"), attributes.get("url"),
                        attributes.get("image"));
                courses.add(course);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return courses;
    }

    private void generateQuery(String searchQuery, IndexSearcher searcher, String isFree, String[] sources,
            String priceRange, String highRating, boolean test) throws IOException {

        BooleanQuery.Builder booleanQuieryBuilder = new BooleanQuery.Builder();

        // booleanQuieryBuilder.add(new
        // BooleanClause(getSearchQueryQuery(searchQuery), Occur.MUST));

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

        String[] queryWords = searchQuery.split("[\\s]+");
        List<String> queryWordsList = Arrays.asList(queryWords);
        if (queryWordsList.size() == 1) {
            booleanQuieryBuilder.add(singleWordSearchQueryGenerator(searchQuery), Occur.MUST);
            getHits(booleanQuieryBuilder.build(), searcher);
        } else {
            if (!test) {
                booleanQuieryBuilder.add(multipleWordSearchQueryGenerator(queryWordsList), Occur.MUST);
                if (getHits(booleanQuieryBuilder.build(), searcher) == 0) {
                    generateQuery(searchQuery, searcher, isFree, sources, priceRange, highRating, true);
                } else {
                    System.out.println("noo need word separation");
                }
            } else {
                System.out.println("word separation");
                booleanQuieryBuilder.add(multipleWordSearchQueryBySeparatedWordsGenerator(queryWordsList), Occur.MUST);
                getHits(booleanQuieryBuilder.build(), searcher);
            }
        }

    }

    private Query getDoublePointQuery(String field, double lowerValue, double maxValue) {
        return DoublePoint.newRangeQuery(field, lowerValue, maxValue);
    }

    private Query getSourceQuery(Set<String> sources) {
        BooleanQuery.Builder sourceBooleanQueryBuilder = new BooleanQuery.Builder();
        for (String source : sources) {
            TermQuery sourceTermQuery = new TermQuery(new Term("provider", source.toLowerCase()));
            sourceBooleanQueryBuilder.add(new BooleanClause(sourceTermQuery, Occur.SHOULD));
        }
        return sourceBooleanQueryBuilder.build();
    }

    private Query getSearchQueryQuery(String searchQuery) {
        String[] queryWords = searchQuery.split("[\\s]+");
        List<String> queryWordsList = Arrays.asList(queryWords);
        if (queryWordsList.size() == 1) {
            return singleWordSearchQueryGenerator(searchQuery);
        } else {
            return multipleWordSearchQueryGenerator(queryWordsList);
        }
    }

    private Query singleWordSearchQueryGenerator(String searchQuery) {
        System.out.println(searchQuery);
        System.out.println(spellCheckerService.getFixedQueryWord(searchQuery.toLowerCase()));
        return new TermQuery(new Term("body", spellCheckerService.getFixedQueryWord(searchQuery.toLowerCase())));
    }

    private Query multipleWordSearchQueryGenerator(List<String> queryWordList) {
        PhraseQuery.Builder phraseQueryBuilder = new PhraseQuery.Builder();
        for (String word : queryWordList) {
            if (!Utils.stopWords.contains(word)) {
                phraseQueryBuilder.add(new Term("body", spellCheckerService.getFixedQueryWord(word)));
            }
        }

        phraseQueryBuilder.setSlop(5);
        return phraseQueryBuilder.build();
    }

    private Query multipleWordSearchQueryBySeparatedWordsGenerator(List<String> queryWordLis) {
        BooleanQuery.Builder booleanBuilder = new BooleanQuery.Builder();
        for (String word : queryWordLis) {
            if (!Utils.stopWords.contains(word)) {
                TermQuery wordTermQuery = new TermQuery(new Term("body", spellCheckerService.getFixedQueryWord(word)));
                booleanBuilder.add(new BooleanClause(wordTermQuery, Occur.SHOULD));
            }
        }

        return booleanBuilder.build();
    }

}
