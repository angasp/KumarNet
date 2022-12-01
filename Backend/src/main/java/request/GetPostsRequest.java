package request;

import java.util.List;

public class GetPostsRequest {
    private int id;
    private int userId;
    private String avatar_url;
    private String username;
    private String img_url;
    private String text;
    private String date;


    private List<GetCommentsRequest> comments;
    private List<GetLikesRequest> likes;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<GetCommentsRequest> getComments() {
        return comments;
    }

    public void setComments(List<GetCommentsRequest> comments) {
        this.comments = comments;
    }

    public List<GetLikesRequest> getLikes() {
        return likes;
    }

    public void setLikes(List<GetLikesRequest> likes) {
        this.likes = likes;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }



    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


}
