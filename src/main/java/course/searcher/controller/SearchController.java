package course.searcher.controller;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import course.searcher.domain.Course;
import course.searcher.service.SearchService;

@Controller
public class SearchController {

    @Autowired
    private SearchService searchService;

    @RequestMapping("/search")
    public String search(@RequestParam("searchQuery") String searchQuery,
            @RequestParam(value = "isFree", required = false) String isFree, @RequestParam("source") String source,
            Model model) throws IOException, ParseException {

        List<Course> loadCoursesFromFiles = searchService.search(searchQuery, isFree, source);

        model.addAttribute("list", loadCoursesFromFiles);
        return "search-result";
    }

}
