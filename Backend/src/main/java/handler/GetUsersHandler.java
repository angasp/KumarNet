package handler;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.eclipse.jetty.http.HttpStatus;
import JWT.JWT;
import repository.Repository.UserRepository;
import request.GetUsersRequest;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class GetUsersHandler implements Handler {

    UserRepository repository;
    JWT jwt = new JWT();

    public GetUsersHandler(UserRepository repository) {
        this.repository = repository;
    }


    List<GetUsersRequest> users = new ArrayList<>();
    @Override
    public void handle(@NotNull Context context) throws IOException {


        try {
            String token = context.req.getHeader("X-auth-Token");
            if(token == null){
                throw new Exception("No token, authorization denied");
            }
            jwt.verifyJWT(token);
            String username = context.req.getParameter("username");
            if (username != null) {
                if(username.equals("undefined")){
                    users = repository.selectAll();
                } else {
                    users = repository.selectByUserName(username);
                }
            }else {
                users = repository.selectAll();
            }
            context.res.setStatus(HttpStatus.OK_200);
            context.res.setHeader("X-auth-Token", token);
            context.json(users);
        } catch(Exception e){
            if(e.getMessage().contains("No token, authorization denied")){
                context.res.setStatus(HttpStatus.FORBIDDEN_403);
                context.result("No token, authorization denied");
                context.res.getOutputStream().print("No token, authorization denied");
                context.res.getOutputStream().close();
            } else {
                context.res.setStatus(HttpStatus.FORBIDDEN_403);
                context.result("Token is not valid");
                context.res.getOutputStream().print("Token is not valid");
                context.res.getOutputStream().close();
            }
        }
    }



}


