package app.ecosense.models;

import android.text.format.DateUtils;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Post implements Serializable {
    private String title;
    private String teaser;
    private String description;
    private String author;
    private String postDate;
    private ArrayList<Comment> comments;
    private String image;
    private int id;

    public Post() {}
    public Post(String title, String teaser, String description, String author, String postDate) {
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
    public void setPostDate(String postDate) {
        postDate = postDate.replaceAll("T" , " ");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-DD hh:mm:ss");
        try {
            Date date = sdf.parse(postDate);
            this.postDate = DateUtils.getRelativeTimeSpanString(date.getTime(), System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }
    public void setImage(String image) {
        if(image.equals("")) {
            this.image = "http://s18.postimg.org/49u5y8oyh/logo_Eco_Sense1.png";
        }else
            this.image = image;
    }
    public void setID(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public String getTeaser() {
        return teaser;
    }
    public String getDescription() {
        return description;
    }
    public String getAuthor() {
        return author;
    }
    public String getPostDate() {
        return postDate;
    }
    public ArrayList<Comment> getComments() {
        return comments;
    }
    public String getImage() {
        return image;
    }
    public int getID() { return this.id; }
}
