package course.searcher.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DataGenerator {

    public static void main(String[] args) throws FileNotFoundException {
        List<String> titles = new ArrayList<String>();
        titles.add("Introduction to Java");
        titles.add("Introduction to JavaScript");
        titles.add("Learn Math");
        titles.add("English Basics");
        titles.add("Progamming with Python");
        titles.add("JavaScript in Deep");

        List<String> prices = new ArrayList<String>();
        prices.add("99.99");
        prices.add("9.99");
        prices.add("14.99");
        prices.add("100.99");
        prices.add("149.99");

        List<String> providers = new ArrayList<String>();
        providers.add("Udemy");
        providers.add("Edx");
        providers.add("Coursera");

        List<String> authors = new ArrayList<String>();
        authors.add("Author1");
        authors.add("Author2");
        authors.add("Author3");

        List<String> lengths = new ArrayList<String>();
        lengths.add("4.5");
        lengths.add("37.4");
        lengths.add("50");
        lengths.add("120");
        
        List<String> ratings = new ArrayList<String>();
        ratings.add("5");
        ratings.add("2.3");
        ratings.add("4.2");
        ratings.add("2");

        Random random = new Random();
        
        File file = new File("src/main/resources/DataToIndex");
        for (File f : file.listFiles()) {
            f.delete();
        }

        for (int i = 1; i <= 10; i++) {
            String fileName = "course" + i + ".txt";
            PrintWriter printWriter = new PrintWriter("src/main/resources/DataToIndex/" + fileName);
            printWriter.println("Source : " + providers.get(random.nextInt(providers.size())));
            printWriter.println("Title : " + titles.get(random.nextInt(titles.size())));
            printWriter.println("Price : " + prices.get(random.nextInt(prices.size())));
            printWriter.println("Author : " + authors.get(random.nextInt(authors.size())));
            printWriter.println("Length : " + lengths.get(random.nextInt(lengths.size())));
            printWriter.println("Rating : " + ratings.get(random.nextInt(ratings.size())));
            printWriter.close();
        }

    }
    
    
}
