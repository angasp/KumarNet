package handler;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.eclipse.jetty.http.HttpStatus;
import JWT.JWT;
import model.Like;
import repository.Repository.LikeRepository;
import request.GetLikesRequest;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public class AddLikeHandler implements Handler {
    LikeRepository repository;
    JWT jwt = new JWT();


    public AddLikeHandler(LikeRepository repository) {
        this.repository = repository;
    }


    @Override
    public void handle(@NotNull Context context) throws Exception {


        String token = context.req.getHeader("X-auth-Token");

        try {
            if (token == null) {
                throw new Exception("No token, authorization denied");
            }

            jwt.verifyJWT(token);

            int postId;
            Like like = new Like();
            postId = Integer.parseInt(context.req.getParameter("postId"));
            GetLikesRequest getLikesRequest;

            like.setPost_id(postId);
            like.setUserId(jwt.getUserId(token));
            repository.add(like);
            getLikesRequest = repository.selectLikeById(like.getId());

            System.out.println("Added like with id " + like.getId());
            context.res.setStatus(200);
            context.res.setHeader("X-auth-Token", "123");
            context.result("OK");
            context.json(getLikesRequest);
        } catch (SQLException e) {
            if (e.getMessage().contains("insert or update on table \"like\" violates foreign key constraint \"fk_like_user\"")) {
                context.res.getOutputStream().print("User with this id doesn't exist");
            } else {
                context.res.getOutputStream().print("Database error happened");
            }
            context.res.setStatus(HttpStatus.INTERNAL_SERVER_ERROR_500);
            System.out.println(e.getMessage());
            context.res.getOutputStream().close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (e.getMessage().contains("Token is not valid")) {
                context.res.setStatus(HttpStatus.FORBIDDEN_403);
                context.result(e.getMessage());
                context.res.getOutputStream().print("Token is not valid");
                context.res.getOutputStream().close();
                System.out.println(e.getMessage());
            } else if (e.getMessage().contains("No token, authorization denied")) {
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
}
