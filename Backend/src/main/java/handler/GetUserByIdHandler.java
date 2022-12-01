package handler;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.eclipse.jetty.http.HttpStatus;
import JWT.JWT;
import model.User;
import repository.Repository.PostRepository;
import repository.Repository.UserRepository;
import request.GetPostsRequest;
import request.GetUserByIdRequest;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class GetUserByIdHandler implements Handler {

    UserRepository repository;
    PostRepository postRepository;
    JWT jwt = new JWT();

    public GetUserByIdHandler(UserRepository userRepository, PostRepository postRepository) {
        this.repository = userRepository;
        this.postRepository = postRepository;
    }


    User user = new User();
    List<GetPostsRequest> posts = new ArrayList<>();
    GetUserByIdRequest getUserById = new GetUserByIdRequest();
    @Override
    public void handle(@NotNull Context context) throws IOException {


        String token = context.req.getHeader("X-auth-Token");
        try {
            if(token == null){
                throw new Exception("No token, authorization denied");
            }

            jwt.verifyJWT(token);
            int id;
            if (context.req.getParameter("id") != null) {
                id = Integer.parseInt(context.req.getParameter("id"));
            } else {
                id = jwt.getUserId(token);
            }
            user = repository.selectById(id);
            posts = postRepository.selectAllByUserID(id);
            getUserById.setUser(user);
            getUserById.setPosts(posts);
            if (user == null) {
                context.res.setStatus(HttpStatus.NOT_FOUND_404);
                context.res.getOutputStream().print("User with this id doesn't exist");
                context.res.getOutputStream().close();
            } else {
                context.res.setStatus(HttpStatus.OK_200);
                context.res.setHeader("X-auth-Token", token);
                context.json(getUserById);
            }
        }catch (Exception e){
            if(e.getMessage().contains("No token, authorization denied")){
                context.res.setStatus(HttpStatus.FORBIDDEN_403);
                context.result("No token, authorization denied");
                context.res.getOutputStream().print("No token, authorization denied");
                context.res.getOutputStream().close();
                System.out.println("User " + e.getMessage());
            } else {
                context.res.setStatus(HttpStatus.FORBIDDEN_403);
                context.result("Token is not valid");
                context.res.getOutputStream().print("Token is not valid");
                context.res.getOutputStream().close();
                System.out.println("User" + e.getMessage());
            }
        }


    }



}


