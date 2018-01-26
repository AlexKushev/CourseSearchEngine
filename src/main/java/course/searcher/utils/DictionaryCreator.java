package course.searcher.utils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class DictionaryCreator {

    private static Set<String> uniqueWords = new HashSet<String>();

    public static void main(String[] args) throws IOException {
        File startFolder = new File("src/main/resources/DataToIndex");
        File dicFile = new File("src/main/resources/dictionary/dictionary.txt");
        dicFile.delete();
        PrintWriter printWriter = new PrintWriter(dicFile);
        extractWords(startFolder, printWriter);
        printWriter.close();
        System.out.println("Dictionary done");
    }

    private static void extractWords(File file, PrintWriter printWriter) throws IOException {
        File[] files = file.listFiles();
        for (File f : files) {
            if (!f.getName().contains("DS_STORE")) {
                if (f.isDirectory()) {
                    extractWords(file, printWriter);
                } else {
                    Map<String, String> attributes = new HashMap<String, String>();
                    try (Stream<String> stream = Files.lines(Paths.get(file + "/" + f.getName()))) {

                        stream.forEach(item -> {
                            attributes.put(item.split(":")[0].trim().toLowerCase(),
                                    item.split(":")[1].trim().toLowerCase());
                        });

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String[] fileWords = attributes.get("title").split("[\\s]+");
                    for (int i = 0; i < fileWords.length; i++) {
                        if (uniqueWords.add(fileWords[i])) {
                            printWriter.println(fileWords[i]);
                        }
                    }

                }
            }
        }
    }
}
