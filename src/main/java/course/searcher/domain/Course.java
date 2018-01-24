package course.searcher.domain;

public class Course {

    private String source;
    private String title;
    private String price;
    private String author;
    private String length;
    private String rating;

    public Course() {

    }

    public Course(String source, String title, String price, String author, String length, String rating) {
        super();
        this.source = source;
        this.title = title;
        this.price = price;
        this.author = author;
        this.length = length;
        this.rating = rating;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Course [source=" + source + ", title=" + title + ", price=" + price + ", author=" + author + ", length="
                + length + "]";
    }

}
