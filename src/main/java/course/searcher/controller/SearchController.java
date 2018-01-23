package course.searcher.controller;

import java.util.ArrayList;
import java.util.List;

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
    public String search(@RequestParam("searchQuery") String searchQuery, Model model) {
        model.addAttribute("searchedQuery", searchQuery);

        List<Course> loadCoursesFromFiles = searchService.loadCoursesFromFiles(new ArrayList<String>());

        model.addAttribute("list", loadCoursesFromFiles);
        return "search-result";
    }

}
