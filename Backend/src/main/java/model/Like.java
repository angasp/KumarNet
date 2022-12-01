package model;

public class Like {
    private int id;
    private int userId;
    private int comment_id;
    private int post_id;

    public Like(){}

    public Like( int user_id, int comment_id, int post_id) {
        this.userId = user_id;
        this.comment_id = comment_id;
        this.post_id = post_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int user_id) {
        this.userId = user_id;
    }

    public int getComment_id() {
        return comment_id;
    }

    public void setComment_id(int comment_id) {
        this.comment_id = comment_id;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }
}
