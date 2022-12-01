package repository.Repository;

import model.Comment;
import repository.ResultSetProcessor;
import request.GetCommentsRequest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentRepository implements Repository {

    private final Connection connection;

    public CommentRepository(Connection connection) {
        this.connection = connection;
    }


    public List<GetCommentsRequest> selectAll(int id) {
        ResultSetProcessor<List<GetCommentsRequest>> findAllComments = new ResultSetProcessor<>() {
            private final List<GetCommentsRequest> result = new ArrayList<>();

            @Override
            public void process(ResultSet resultSet) throws SQLException {
                while (resultSet.next()) {
                    result.add(generateComment(resultSet));
                }
            }

            @Override
            public List<GetCommentsRequest> getResult() {
                return result;
            }
        };
        executeQuery("SELECT c.id, c.user_id as user_id, u.username as username, u.avatar_url as avatar_url, c.text, c.post_id as post_id\n" +
                "FROM comment c\n" +
                "         INNER JOIN \"user\" u on u.id = c.user_id\n" +
                "WHERE post_id =" + id, findAllComments, connection);
        return findAllComments.getResult();
    }

    public GetCommentsRequest selectCommentById(int comment_id){
        try(PreparedStatement statement = connection.prepareStatement("""
                SELECT c.id, c.user_id as user_id, u.username as username, u.avatar_url as avatar_url, c.text, c.post_id as post_id
                FROM comment c
                         INNER JOIN "user" u on u.id = c.user_id
                WHERE c.id = ?""")) {
            statement.setInt(1,comment_id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return generateComment(resultSet);
            }
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public void add(Comment comment) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO comment (text, user_id, post_id) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, comment.getText());
        statement.setInt(2, comment.getUserId());
        statement.setInt(3, comment.getPost_id());
        statement.executeUpdate();
        ResultSet resultSet = statement.getGeneratedKeys();
        if (resultSet.next()) {
            int id = resultSet.getInt(1);
            comment.setId(id);
        }
    }


    public void delete(int id, int userId) {
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM comment WHERE id = ? AND user_id=?")) {
            statement.setInt(1, id);
            statement.setInt(2, userId);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private GetCommentsRequest generateComment(ResultSet resultSet) throws SQLException {
        LikeRepository likeRepository = new LikeRepository(connection);
        GetCommentsRequest getCommentsRequest = new GetCommentsRequest();
        int id = resultSet.getInt("id");
        getCommentsRequest.setAvatarUrl(resultSet.getString("avatar_url"));
        getCommentsRequest.setUserId(resultSet.getInt("user_id"));
        getCommentsRequest.setId(id);
        getCommentsRequest.setText(resultSet.getString("text"));
        getCommentsRequest.setUsername(resultSet.getString("username"));
        getCommentsRequest.setPostId(resultSet.getInt("post_id"));
        return getCommentsRequest;
    }
}
