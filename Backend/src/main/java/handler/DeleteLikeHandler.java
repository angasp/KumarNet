package handler;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.eclipse.jetty.http.HttpStatus;
import JWT.JWT;
import repository.Repository.LikeRepository;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class DeleteLikeHandler implements Handler {
    LikeRepository repository;
    JWT jwt = new JWT();

    public DeleteLikeHandler(LikeRepository likeRepository){
        this.repository = likeRepository;
    }

    @Override
    public void handle(@NotNull Context context) throws Exception {


        String token = context.req.getHeader("X-auth-Token");
        HashMap<String, Integer> res = new HashMap<>();

        try {
            if(token == null){
                throw new Exception("No token, authorization denied");
            }

            jwt.verifyJWT(token);
            int postId = Integer.parseInt(context.req.getParameter("postId"));
            int userId = jwt.getUserId(token);
            repository.delete(postId, userId);
            res.put("postId", postId);
            res.put("userId", userId);
            context.res.setStatus(HttpStatus.OK_200);
            System.out.println("removed like from post with postId " + postId);
            context.res.setHeader("X-auth-Token", token);
            context.json(res);

        }catch(Exception e){
            if(e.getMessage().contains("Token is not valid")){
                context.res.setStatus(HttpStatus.FORBIDDEN_403);
                context.result(e.getMessage());
                context.res.getOutputStream().print(e.getMessage());
                context.res.getOutputStream().close();
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
}
