package request;

import model.User;

import java.util.List;

public class GetUserByIdRequest {
    private User user;
    private int count;
    private List<GetPostsRequest> posts;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<GetPostsRequest> getPosts() {
        return posts;
    }

    public void setPosts(List<GetPostsRequest> posts) {
        this.posts = posts;
    }
}
