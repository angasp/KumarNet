package handler;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.eclipse.jetty.http.HttpStatus;
import JWT.JWT;
import repository.Repository.PostRepository;
import request.GetPostsRequest;
import request.GetPostsWithPaginationRequest;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class GetAllPostsHandler implements Handler {

    PostRepository repository;
    JWT jwt = new JWT();

    public GetAllPostsHandler(PostRepository postRepository) {
        this.repository = postRepository;
    }


    List<GetPostsRequest> posts = new ArrayList<>();
    GetPostsWithPaginationRequest getPosts = new GetPostsWithPaginationRequest();

    @Override
    public void handle(@NotNull Context context) throws IOException {


        String token = context.req.getHeader("X-auth-Token");

        try {
            if (token == null) {
                throw new Exception("No token, authorization denied");
            }

            jwt.verifyJWT(token);
            int per_page = Integer.parseInt(context.req.getParameter("per_page"));
            int page = Integer.parseInt(context.req.getParameter("page"));
            int currPage;
            if (page == 0) {
                currPage = 0;
            } else {
                currPage = page * per_page;
            }

            int countAll = repository.getPostCount();
            int allPages = (int) Math.ceil(countAll / 10);
            boolean hasNext = (allPages - page) > 0;


            posts = repository.selectAll(per_page, currPage);
            getPosts.setPosts(posts);
            getPosts.setHasNext(hasNext);
            context.res.setStatus(HttpStatus.OK_200);
            context.res.setHeader("X-auth-Token", token);
            context.result("OK");
            context.json(getPosts);
        } catch (Exception e) {
            if (e.getMessage().contains("Token is not valid")) {
                context.res.setStatus(HttpStatus.FORBIDDEN_403);
                context.result(e.getMessage());
                context.res.getOutputStream().print("Token is not valid");
                context.res.getOutputStream().close();
            } else if (e.getMessage().contains("No token, authorization denied")) {
                context.res.setStatus(HttpStatus.FORBIDDEN_403);
                context.result("No token, authorization denied");
                context.res.getOutputStream().print("No token, authorization denied");
                context.res.getOutputStream().close();
            } else {
                context.res.setStatus(HttpStatus.INTERNAL_SERVER_ERROR_500);
                context.res.getOutputStream().print("Server Error happened");
                System.out.println("Get Post" + e.getMessage());
            }
        }

    }


}


