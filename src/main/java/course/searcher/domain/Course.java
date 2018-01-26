package course.searcher.domain;

public class Course {

    private String provider;
    private String title;
    private String price;
    private String instructor;
    private String length;
    private String rating;
    private String language;
    private String description;
    private String pageUrl;
    private String imageUrl;

    public Course() {

    }

    public Course(String provider, String title, String price, String instructor, String length, String rating,
            String language, String description, String pageUrl, String url) {
        super();
        this.provider = provider;
        this.title = title;
        this.price = price;
        this.instructor = instructor;
        this.length = length;
        this.rating = rating;
        this.language = language;
        this.description = description;
        this.pageUrl = pageUrl;
        this.imageUrl = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
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

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "Course [source=" + provider + ", title=" + title + ", price=" + price + ", author=" + instructor
                + ", length=" + length + "]";
    }

}
