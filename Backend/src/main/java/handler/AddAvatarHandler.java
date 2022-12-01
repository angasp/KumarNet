package handler;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.UploadedFile;
import org.eclipse.jetty.http.HttpStatus;
import JWT.JWT;
import repository.AwsUploadRepository;
import repository.Repository.PostRepository;
import repository.Repository.UserRepository;
import request.GetUserByIdRequest;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public class AddAvatarHandler implements Handler {

    UserRepository userRepository;
    PostRepository postRepository ;

    public AddAvatarHandler(UserRepository repository, PostRepository postRepository) {
        this.userRepository = repository;
        this.postRepository = postRepository;
    }


    @Override
    public void handle(@NotNull Context context) throws Exception {


        JWT jwt = new JWT();
        String token = context.req.getHeader("X-auth-Token");
        GetUserByIdRequest getUserByIdRequest = new GetUserByIdRequest();


        try {
            if (token == null) {
                throw new Exception("No token, authorization denied");
            }

            jwt.verifyJWT(token);

            String url;

            AwsUploadRepository awsUploadRepository = new AwsUploadRepository();
            UploadedFile uploadedFile = context.uploadedFile("avatar");

            int id = jwt.getUserId(token);

            if (uploadedFile != null) {
                url = awsUploadRepository.uploadImgToAws(uploadedFile);
                userRepository.addAvatar(url, id);
                getUserByIdRequest.setUser( userRepository.selectById(id));
                getUserByIdRequest.setPosts( postRepository.selectAllByUserID(id));
                context.res.setStatus(HttpStatus.OK_200);
                context.res.setHeader("X-auth-Token", token);
                context.res.getOutputStream().print(url);
            } else {
                context.res.setStatus(HttpStatus.BAD_REQUEST_400);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            context.res.setStatus(HttpStatus.BAD_GATEWAY_502);
        } catch (Exception e) {
            if (e.getMessage().contains("Email is not valid")) {
                context.res.setStatus(HttpStatus.NOT_IMPLEMENTED_501);
                context.result("Email is not valid");
                context.res.getOutputStream().print("Email is not valid");
            } else if (e.getMessage().contains("No token, authorization denied")) {
                context.res.setStatus(HttpStatus.FORBIDDEN_403);
                context.result("No token, authorization denied");
                context.res.getOutputStream().print("No token, authorization denied");
                context.res.getOutputStream().close();
            } else if (e.getMessage().contains("Token is not valid")) {
                context.res.setStatus(HttpStatus.FORBIDDEN_403);
                context.result("Token is not valid");
                context.res.getOutputStream().print("Token is not valid");
                context.res.getOutputStream().close();
                System.out.println(e.getMessage());
            } else {
                context.res.setStatus(HttpStatus.FORBIDDEN_403);
                context.result(e.getMessage());
                context.res.getOutputStream().print(e.getMessage());
                context.res.getOutputStream().close();
                System.out.println(e.getMessage());
            }

        }
    }
}
