package handler;

import java.sql.SQLException;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.plugin.json.JavalinJackson;
import org.eclipse.jetty.http.HttpStatus;
import JWT.JWT;
import model.User;
import repository.Repository.UserRepository;
import request.AuthRequest;
import request.ErrorRequest;
import org.jetbrains.annotations.NotNull;
import org.mindrot.jbcrypt.BCrypt;


public class AddUserHandler implements Handler {
    UserRepository repository;
    JWT jwt = new JWT();

    public AddUserHandler(UserRepository repository) {
        this.repository = repository;
    }


    @Override
    public void handle(@NotNull Context context) throws Exception {
        User user = generateUser(new JavalinJackson(), context);

        AuthRequest authRequest = new AuthRequest();
        ErrorRequest errorRequest = new ErrorRequest();

        try {
            if (user != null) {
                repository.add(user);
            } else {
                throw new Exception("Empty request sent");
            }
            authRequest.setToken(jwt.generateJWT(String.valueOf(user.getId()), user.getUsername(), 14444444));
            context.res.setStatus(200);
            context.res.setHeader("X-auth-Token", authRequest.getToken());
            context.json(authRequest);
        } catch (SQLException e) {
            if (e.getMessage().contains("ERROR: duplicate key value violates unique constraint \"username_unique\"\n")) {
                context.res.setStatus(HttpStatus.FORBIDDEN_403);
                errorRequest.setError("User with this username already exist");
                context.res.getOutputStream().print("User with this username already exist");
                context.json(errorRequest);
            } else {
                System.out.println(e.getMessage());
                context.res.setStatus(HttpStatus.BAD_GATEWAY_502);
                context.res.getOutputStream().print("Database error happened");

            }
            context.res.getOutputStream().close();
        } catch (Exception e) {
            if (e.getMessage().contains("Email is not valid")) {
                context.res.setStatus(HttpStatus.NOT_ACCEPTABLE_406);
                errorRequest.setError("Email is not valid");
                context.res.getOutputStream().print("Email is not valid");
            } else {
                context.res.setStatus(HttpStatus.NOT_IMPLEMENTED_501);
                context.res.getOutputStream().print("Request is not valid");
            }
            context.res.getOutputStream().close();
        }
    }

    private String passwordToHash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    private User generateUser(JavalinJackson jackson, Context context) throws Exception {

        if (jackson == null) {
            throw new Exception("Invalid jackson");
        }
        if (context == null) {
            throw new Exception("Invalid context");
        }
        try {

            User request = jackson.fromJsonString(context.body(), User.class);

            //BODY
            String name = request.getName();
            String surname = request.getSurname();
            String username = request.getUsername();
            String email = request.getEmail();
            String gender = request.getGender();
            String passwordHash = request.getPassword();
            String bio = request.getBio();
            String birthDate = request.getBirthDate();
            String profession = request.getProfession();


            return new User(name, surname, username, User.GENDER.valueOf(gender), email, passwordToHash(passwordHash), bio, birthDate, profession);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}


