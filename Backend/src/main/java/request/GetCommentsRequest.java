package request;

import java.util.List;

public class GetCommentsRequest {

    private int id;
    private int userId;
    private String avatar_url;
    private String username;
    private String text;
    private int postId;
    private int countLike;
    private List<GetLikesRequest> likes;

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }


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

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatarUrl(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getCountLike() {
        return countLike;
    }

    public void setCountLike(int countLike) {
        this.countLike = countLike;
    }

    public List<GetLikesRequest> getLikes() {
        return likes;
    }

    public void setLikes(List<GetLikesRequest> likes) {
        this.likes = likes;
    }
}



