package course.searcher.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import course.searcher.spell.checker.CourseSearchSpellChecker;

@Component
public class SpellCheckService {

    @Autowired
    private CourseSearchSpellChecker spellChecker;

    public String getFixedQueryWord(String word) {
        return spellChecker.getCorrectedLine(word);
    }

}
