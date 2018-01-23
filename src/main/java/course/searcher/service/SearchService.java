package course.searcher.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import course.searcher.domain.Course;

@Component
public class SearchService {

    public List<Course> loadCoursesFromFiles(List<String> documents) {

        List<Course> courses = new ArrayList<Course>();

        File dataDirectory = new File(getClass().getResource("/DataToIndex").getFile());

        for (String document : documents) {

            List<String> attributes = new ArrayList<String>();

            try (Stream<String> stream = Files.lines(Paths.get(dataDirectory + "/" + document))) {

                stream.forEach(item -> {
                    attributes.add(item.split(":")[1].trim());
                });

                Course course = new Course(attributes.get(0), attributes.get(1), attributes.get(2), attributes.get(3),
                        attributes.get(4));
                courses.add(course);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return courses;
    }

}
