package handler;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.eclipse.jetty.http.HttpStatus;
import JWT.JWT;
import repository.Repository.CommentRepository;
import org.jetbrains.annotations.NotNull;

public class DeleteCommentHandler implements Handler {
    CommentRepository repository;
    JWT jwt = new JWT();
    public DeleteCommentHandler(CommentRepository commentRepository) {
        this.repository = commentRepository;
    }


    @Override
    public void handle(@NotNull Context context) throws Exception {


        String token = context.req.getHeader("X-auth-Token");

        try {
            if(token == null){
                throw new Exception("No token, authorization denied");
            }

            jwt.verifyJWT(token);
            int id = Integer.parseInt(context.req.getParameter("id"));
            repository.delete(id, jwt.getUserId(token));
            context.res.setStatus(HttpStatus.OK_200);
            context.res.setHeader("X-auth-Token", token);
            context.res.getOutputStream().print(id);
        } catch (Exception e) {
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
