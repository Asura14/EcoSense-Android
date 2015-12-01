package app.ecosense.models;

import java.util.Date;

public class Post {
    private String title;
    private String teaser;
    private String description;
    private String author;
    private Date postDate;

    public Post() {}
    public Post(String title, String teaser, String description, String author, Date postDate) {
        this.title = title;
        this.teaser = teaser;
        this.description = description;
        this.author = author;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public void setTeaser(String teaser) {
        this.teaser = teaser;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
}
