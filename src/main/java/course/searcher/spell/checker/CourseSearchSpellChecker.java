package course.searcher.spell.checker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.swabunga.spell.engine.SpellDictionaryHashMap;
import com.swabunga.spell.engine.Word;
import com.swabunga.spell.event.SpellCheckEvent;
import com.swabunga.spell.event.SpellCheckListener;
import com.swabunga.spell.event.SpellChecker;
import com.swabunga.spell.event.StringWordTokenizer;
import com.swabunga.spell.event.TeXWordFinder;

public class CourseSearchSpellChecker implements SpellCheckListener {

    private SpellChecker spellChecker;
    private List<String> misspelledWords;
    private static SpellDictionaryHashMap dictionaryHashMap;

    public CourseSearchSpellChecker() {
        misspelledWords = new ArrayList<String>();
        initialize();
    }

    private void initialize() {
        spellChecker = new SpellChecker(dictionaryHashMap);
        spellChecker.addSpellCheckListener(this);
    }

    static {

        File dict = new File("dictionary/dictionary.txt");
        try {
            dictionaryHashMap = new SpellDictionaryHashMap(dict);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getMisspelledWords(String text) {
        StringWordTokenizer texTok = new StringWordTokenizer(text, new TeXWordFinder());
        spellChecker.checkSpelling(texTok);
        return misspelledWords;
    }

    public String getCorrectedLine(String line) {
        List<String> misSpelledWords = getMisspelledWords(line);

        for (String misSpelledWord : misSpelledWords) {
            List<String> suggestions = getSuggestions(misSpelledWord);
            if (suggestions.size() == 0)
                continue;
            String bestSuggestion = suggestions.get(0);
            line = line.replace(misSpelledWord, bestSuggestion);
        }
        return line;
    }

    public List<String> getSuggestions(String misspelledWord) {
        @SuppressWarnings("unchecked")
        List<Word> suggestions = spellChecker.getSuggestions(misspelledWord, 0);
        List<String> stringSuggestions = new ArrayList<String>();
        for (Word suggestion : suggestions) {
            stringSuggestions.add(suggestion.getWord());
        }

        return stringSuggestions;
    }

    @Override
    public void spellingError(SpellCheckEvent event) {
        event.ignoreWord(true);
        misspelledWords.add(event.getInvalidWord());
    }

    public static void main(String[] args) {
        CourseSearchSpellChecker spellChecker = new CourseSearchSpellChecker();
        String result = spellChecker.getCorrectedLine("jva");
        System.out.println(result);
    }
}