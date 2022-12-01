package request;

import java.util.List;

public class GetPostsWithPaginationRequest {

    private List<GetPostsRequest> posts;
    private boolean hasNext;

    public List<GetPostsRequest> getPosts() {
        return posts;
    }

    public void setPosts(List<GetPostsRequest> posts) {
        this.posts = posts;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }
}
