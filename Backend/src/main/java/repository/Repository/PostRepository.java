package repository.Repository;

import model.Post;
import repository.ResultSetProcessor;
import request.GetPostsRequest;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PostRepository implements Repository {
    private final Connection connection;

    public PostRepository(Connection connection) {
        this.connection = connection;
    }

    public List<GetPostsRequest> selectAllByUserID(int userId) {
        List<GetPostsRequest> getPostsRequests = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement("""
                SELECT  u.id as userId,
                        u.avatar_url as avatar_url,
                       u.username   as username,
                       p.img_url    as img_url,
                       p.id         as post_id,
                       p.text       as text,
                       p.date       as  date
                FROM post p
                         INNER JOIN "user" u on u.id = p.user_id
                WHERE user_id = ?
                ORDER BY p.date DESC;""")) {
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                getPostsRequests.add(generatePostGetAllRequest(resultSet));
            }
            resultSet.close();
            return getPostsRequests;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }


    public List<GetPostsRequest> selectAll(int offset, int page) {
        ResultSetProcessor<List<GetPostsRequest>> findAllPosts = findAllPosts();
        executeQuery("""
                SELECT u.id as userId,
                       u.avatar_url as avatar_url,
                       u.username   as username,
                       p.img_url    as img_url,
                       p.id         as post_id,
                       p.text       as text,
                       p.date       as  date
                FROM post p
                         INNER JOIN "user" u on u.id = p.user_id
                ORDER BY p.date DESC
                LIMIT\040""" + offset + " OFFSET " + page, findAllPosts, connection);
        return findAllPosts.getResult();
    }

    public GetPostsRequest selectPostById(int post_id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT u.id as userId,\n" +
                "                       u.avatar_url as avatar_url,\n" +
                "                       u.username   as username,\n" +
                "                       p.img_url    as img_url,\n" +
                "                       p.id         as post_id,\n" +
                "                       p.text       as text,\n" +
                "                       p.date       as  date\n" +
                "                FROM post p\n" +
                "                         INNER JOIN \"user\" u on u.id = p.user_id where p.id = ?")) {
            preparedStatement.setInt(1, post_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return generatePostGetAllRequest( resultSet);
            }
            resultSet.close();
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    public int getPostCount() throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT count(*) from post ")) {
            int count = 0;
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
            resultSet.close();
            return count;
        }
    }

    public void add(Post post) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO post (text, img_url, date, user_id) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, post.getText());
        statement.setString(2, post.getImgUrl());
        statement.setObject(3, dateNow(), Types.DATE);
        statement.setInt(4, post.getUserId());
        statement.executeUpdate();
        ResultSet resultSet = statement.getGeneratedKeys();
        if (resultSet.next()) {
            int id = resultSet.getInt(1);
            post.setId(id);
        }
    }

    public void delete(int id, int userId) {
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM post WHERE id = ? AND user_id = ?")) {
            statement.setInt(1, id);
            statement.setInt(2, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private GetPostsRequest generatePostGetAllRequest(ResultSet resultSet) throws SQLException {
        GetPostsRequest post = new GetPostsRequest();
        CommentRepository commentRepository = new CommentRepository(connection);
        LikeRepository likeRepository = new LikeRepository(connection);
        int id = resultSet.getInt("post_id");
        post.setId(id);
        post.setUserId(resultSet.getInt("userId"));
        post.setText(resultSet.getString("text"));
        post.setImg_url(resultSet.getString("img_url"));
        post.setDate(resultSet.getString("date"));
        post.setUsername(resultSet.getString("username"));
        post.setAvatar_url(resultSet.getString("avatar_url"));
        post.setLikes(likeRepository.selectAllLikePost(id));
        post.setComments(commentRepository.selectAll(id));

        return post;
    }

    private ResultSetProcessor<List<GetPostsRequest>> findAllPosts() {
        return new ResultSetProcessor<>() {
            private final List<GetPostsRequest> result = new ArrayList<>();

            @Override
            public void process(ResultSet resultSet) throws SQLException {
                while (resultSet.next()) {
                    result.add(generatePostGetAllRequest(resultSet));
                }
            }

            @Override
            public List<GetPostsRequest> getResult() {
                return result;
            }
        };
    }


    private String dateNow() {
        LocalDate dateObj = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return dateObj.format(formatter);
    }
}
