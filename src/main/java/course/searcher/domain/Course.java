package course.searcher.domain;

public class Course {

    private String source;
    private String title;
    private String price;
    private String length;
    private String description;
    private String link;

    public Course(String source, String title, String price, String length, String description, String link) {
        super();
        this.source = source;
        this.title = title;
        this.price = price;
        this.length = length;
        this.description = description;
        this.link = link;
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

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

}
