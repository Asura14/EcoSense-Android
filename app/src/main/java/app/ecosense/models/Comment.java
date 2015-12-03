package app.ecosense.models;

public class Comment {

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
    public String getName() {
        return name;
    }
}
