package handler;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.plugin.json.JavalinJackson;
import org.eclipse.jetty.http.HttpStatus;
import JWT.JWT;
import repository.Repository.UserRepository;
import request.AuthRequest;
import request.SignInRequest;
import org.jetbrains.annotations.NotNull;
import org.mindrot.jbcrypt.BCrypt;

public class SignInHandler implements Handler {
    UserRepository repository;
    JWT jwt = new JWT();

    public SignInHandler(UserRepository userRepository){
        this.repository = userRepository;
    }


    @Override
    public void handle(@NotNull Context context) throws Exception {
        try{
            AuthRequest authRequest = new AuthRequest();


            JavalinJackson jackson = new JavalinJackson();
            SignInRequest request = jackson.fromJsonString(context.body(), SignInRequest.class);
            SignInRequest user = repository.selectByUsernameSignIn(request.getUsername());

            if(!verifyPassword(request.getPassword(), user.getPassword())){
                throw new Exception("The password is wrong");
            } else{
                authRequest.setToken(jwt.generateJWT(String.valueOf(user.getId()), user.getUsername(), 14444444));
                context.res.setStatus(HttpStatus.OK_200);
                context.res.setHeader("X-auth-Token", authRequest.getToken());
                context.json(authRequest);
            }
        }catch(Exception e) {
            context.res.setStatus(HttpStatus.UNAUTHORIZED_401);
            System.out.println(e.getMessage());
            context.res.getOutputStream().print("Password or username is wrong");
            context.result("Password or username is wrong");
            context.res.getOutputStream().close();
        }
    }


    private boolean verifyPassword(String password, String hashPassword) {
        return BCrypt.checkpw(password, hashPassword);
    }
}

