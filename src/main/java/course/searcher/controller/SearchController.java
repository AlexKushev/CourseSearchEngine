package course.searcher.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchController {

    @RequestMapping("/search")
    public String search(@RequestParam("searchQuery") String searchQuery, Model model) {

        //File file1 = new File(getClass().getResource("/DataToIndex").getFile());

        List<String> list = new ArrayList<String>();

        model.addAttribute("searchedQuery", searchQuery);
        model.addAttribute("list", list);
        return "search-result";
    }

}
