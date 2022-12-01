package org.example;

import handler.*;
import io.javalin.Javalin;
import io.javalin.http.Handler;

import Connection.ConnectionFactory;
import repository.Repository.CommentRepository;
import repository.Repository.LikeRepository;
import repository.Repository.PostRepository;
import repository.Repository.UserRepository;

import java.sql.Connection;
import java.sql.SQLException;

public class App {
    public static void main(String[] args) {
        Javalin app = Javalin.create(config ->
            config.enableCorsForAllOrigins()).start(7070);

        try {
            Connection connection = ConnectionFactory.getInstance();

            // REPOSITORIES
            UserRepository userRepository = new UserRepository(connection);
            PostRepository postRepository = new PostRepository(connection);
            CommentRepository commentRepository = new CommentRepository(connection);
            LikeRepository likeRepository = new LikeRepository(connection);

            // USER HANDLERS
            Handler getUserHandler = new GetUsersHandler(userRepository);
            Handler addUserHandler = new AddUserHandler(userRepository);
            Handler getUserByIDHandler = new GetUserByIdHandler(userRepository, postRepository);
            Handler signInHandler = new SignInHandler(userRepository);
            Handler addAvatarHandler = new AddAvatarHandler(userRepository, postRepository);

            //POST HANDLERS
            Handler getPostHandler = new GetAllPostsHandler(postRepository);
            Handler addPostHandler = new AddPostHandler(postRepository);
            Handler deletePostHandler = new DeletePostHandler(postRepository);

            //COMMENT HANDLERS
            Handler addCommentHandler = new AddCommentHandler(commentRepository);
            Handler deleteCommentHandler = new DeleteCommentHandler(commentRepository);

            //LIKE HANDLERS
            Handler addLikeHandler = new AddLikeHandler(likeRepository);
            Handler deleteLikeHandler = new DeleteLikeHandler(likeRepository);

            // GET REQUESTS
            app.get("/user", getUserByIDHandler);
            app.get("/users", getUserHandler);
            app.get("/posts", getPostHandler);

            //POST REQUESTS
            app.post("/comment", addCommentHandler);
            app.post("/like", addLikeHandler);
            app.post("/post", addPostHandler);
            app.post("/signUp", addUserHandler);
            app.post("/signIn", signInHandler);

            //DELETE REQUESTS
            app.delete("/comment", deleteCommentHandler);
            app.delete("/like", deleteLikeHandler);
            app.delete("/post", deletePostHandler);

            //PUT REQUESTS
            app.put("/avatar", addAvatarHandler);
        } catch (SQLException e) {
            System.err.println("DB connection failed");
            System.exit(1);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
