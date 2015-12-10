package app.ecosense.models;

import java.io.Serializable;

public class Comment implements Serializable{

    private String name;
    private String date;
    private String content;

    public Comment(String name, String date, String content) {
        this.name = name;
        this.date = date;
        this.content = content;
    }

    public String getContent() {
        return content;
    }
    public String getDate() {
        return date;
    }
    public String getName() {return name; }
}
