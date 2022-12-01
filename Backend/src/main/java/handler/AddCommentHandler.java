package handler;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.plugin.json.JavalinJackson;
import org.eclipse.jetty.http.HttpStatus;
import JWT.JWT;
import model.Comment;
import repository.Repository.CommentRepository;
import request.GetCommentsRequest;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public class AddCommentHandler implements Handler {
    CommentRepository repository;
    JWT jwt = new JWT();

    public AddCommentHandler(CommentRepository repository) {
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
            GetCommentsRequest getCommentsRequest;

            Comment comment = generateComment(new JavalinJackson(), context);
            comment.setUserId(jwt.getUserId(token));
            repository.add(comment);
            getCommentsRequest = repository.selectCommentById(comment.getId());

            System.out.println("Added comment with id " + comment.getId());
            context.res.setStatus(200);
            context.res.setHeader("X-auth-Token", token);
            context.json(getCommentsRequest);
        } catch (SQLException e) {
            if (e.getMessage().contains("ERROR: insert or update on table \"comment\" violates foreign key constraint \"fk_comment_post\"")) {
                context.res.setStatus(500);
                System.out.println(e.getMessage());
                context.res.getOutputStream().print("The post with this id doesn't exist");
            } else if (e.getMessage().contains("insert or update on table \"comment\" violates foreign key constraint \"fk_comment_user\"")) {
                context.res.setStatus(500);
                System.out.println(e.getMessage());
                context.res.getOutputStream().print("The user with this id doesn't exist");
            } else {
                context.res.setStatus(500);
                System.out.println(e.getMessage());
                context.res.getOutputStream().print("Database error happened");
            }
            context.res.getOutputStream().close();

        } catch (Exception e) {
            if(e.getMessage().contains("Token is not valid")){
                context.res.setStatus(HttpStatus.FORBIDDEN_403);
                context.result(e.getMessage());
                context.res.getOutputStream().print("Token is not valid");
                context.res.getOutputStream().close();
                System.out.println(e.getMessage());
            } else if(e.getMessage().contains("No token, authorization denied")){
                context.res.setStatus(HttpStatus.FORBIDDEN_403);
                context.result("No token, authorization denied");
                context.res.getOutputStream().print("No token, authorization denied");
                context.res.getOutputStream().close();
            } else {
                context.res.setStatus(HttpStatus.INTERNAL_SERVER_ERROR_500);
                context.res.getOutputStream().print("Server Error happened");
                System.out.println(e.getMessage());
            }
        }
    }


    private Comment generateComment(JavalinJackson javalinJackson, Context context) throws Exception {
        if (javalinJackson == null) {
            throw new Exception("Invalid jackson");
        }
        if (context == null) {
            throw new Exception("Invalid context");
        }
        Comment request = javalinJackson.fromJsonString(context.body(), Comment.class);
        String text = request.getText();
        int user_id = request.getUserId();
        int post_id = Integer.parseInt(context.req.getParameter("postId"));
        return new Comment(text, user_id, post_id);

    }
}
