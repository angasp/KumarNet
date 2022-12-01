package model;

public class Post {
    private int id;
    private String text;
    private String img_url;
    private int user_id;
    private String date;  //date


    public Post( String text, String img_url, int user_id, String date) {
        this.text = text;
        this.img_url = img_url;
        this.user_id = user_id;
        this.date = date;
    }

    public Post() {

    }

    public Post(String text, String img_url) {
        this.img_url = img_url;
        this.text = text;

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImgUrl() {
        return img_url;
    }

    public void setImgUrl(String img_url) {
        this.img_url = img_url;
    }

    public int getUserId() {
        return user_id;
    }

    public void setUserId(int user_id) {
        this.user_id = user_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
