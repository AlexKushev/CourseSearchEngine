package course.searcher.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @RequestMapping("/search")
    public String search(@RequestParam("searchQuery") String searchQuery, 
            Model model) {

        System.out.println(searchQuery);

        return "search-result";
    }

}
