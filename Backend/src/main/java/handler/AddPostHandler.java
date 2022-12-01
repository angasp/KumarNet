package handler;

import io.javalin.core.validation.JavalinValidation;
import io.javalin.core.validation.Validator;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.UploadedFile;
import io.javalin.plugin.json.JavalinJackson;
import org.eclipse.jetty.http.HttpStatus;
import JWT.JWT;
import model.Post;
import repository.AwsUploadRepository;
import repository.Repository.PostRepository;
import request.GetPostsRequest;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public class AddPostHandler implements Handler {
    PostRepository repository;
    JWT jwt = new JWT();

    public AddPostHandler(PostRepository repository) {
        this.repository = repository;
    }


    @Override
    public void handle(@NotNull Context context) throws Exception {



        String token = context.req.getHeader("X-auth-Token");


        try {
            if(token == null){
                throw new Exception("No token, authorization denied");
            }

            jwt.verifyJWT(token);


            Post post = generatePost(new JavalinJackson(), context);
            GetPostsRequest getPostsRequest;

            post.setUserId(jwt.getUserId(token));


            repository.add(post);
            getPostsRequest = repository.selectPostById(post.getId());
            getPostsRequest.setUserId(jwt.getUserId(token));
            System.out.println("Added post with id " + post.getId());
            context.res.setStatus(HttpStatus.OK_200);
            context.res.setHeader("X-auth-Token", token);
            context.result("OK");
            context.json(getPostsRequest);
        } catch (SQLException e) {
            context.res.setStatus(HttpStatus.BAD_GATEWAY_502);
            context.res.getOutputStream().print("Database error happened");
            context.res.getOutputStream().close();
        } catch (Exception e) {
            if (e.getMessage().contains("Email is not valid")) {
                context.res.setStatus(HttpStatus.NOT_IMPLEMENTED_501);
                context.result("Email is not valid");
                context.res.getOutputStream().print("Email is not valid");
            } else if(e.getMessage().contains("No token, authorization denied")){
                context.res.setStatus(HttpStatus.FORBIDDEN_403);
                context.result("No token, authorization denied");
                context.res.getOutputStream().print("No token, authorization denied");
                context.res.getOutputStream().close();
            } else if(e.getMessage().contains("Token is not valid")){
                context.res.setStatus(HttpStatus.FORBIDDEN_403);
                context.result("Token is not valid");
                context.res.getOutputStream().print("Token is not valid");
                context.res.getOutputStream().close();
                System.out.println(e.getMessage());
            }else {
                context.res.setStatus(HttpStatus.FORBIDDEN_403);
                context.result(e.getMessage());
                context.res.getOutputStream().print(e.getMessage());
                context.res.getOutputStream().close();
                System.out.println(e.getMessage());
            }
            e.printStackTrace();

        }
    }

    private Post generatePost(JavalinJackson jackson, Context context) throws Exception {
        if (jackson == null) {
            throw new Exception("Invalid jackson");
        }
        if (context == null) {
            throw new Exception("Invalid context");
        }

        String url;

        AwsUploadRepository awsUploadRepository = new AwsUploadRepository();
        UploadedFile uploadedFile = context.uploadedFile("img");

        if (uploadedFile != null) {
            url = awsUploadRepository.uploadImgToAws(uploadedFile);
        } else {
            url = null;
        }

        JavalinValidation.register(Post.class, s -> jackson.fromJsonString(s, Post.class));
        Validator<Post> validator = context.formParamAsClass("body", Post.class);
        Post request = validator.get();

        String text = request.getText();
        System.out.println(text);
        return new Post(text, url);

    }
}
