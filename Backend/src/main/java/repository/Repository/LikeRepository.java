package repository.Repository;

import model.Like;
import repository.ResultSetProcessor;
import request.GetLikesRequest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LikeRepository implements Repository {
    private final Connection connection;

    public LikeRepository(Connection connection) {
        this.connection = connection;
    }


    public List<GetLikesRequest> selectAllLikePost(int postId) {
        ResultSetProcessor<List<GetLikesRequest>> findAllLikesForPosts = findLikes();
        executeQuery("SELECT l.id, l.user_id as user_id, u.username as username, u.avatar_url as avatar_url\n" +
                "FROM \"like\" l\n" +
                "         INNER JOIN \"user\" u on u.id = l.user_id\n" +
                "WHERE post_id =" + postId, findAllLikesForPosts, connection);
        return findAllLikesForPosts.getResult();
    }


    private ResultSetProcessor<List<GetLikesRequest>> findLikes() {
        return new ResultSetProcessor<>() {
            private final List<GetLikesRequest> result = new ArrayList<>();

            @Override
            public void process(ResultSet resultSet) throws SQLException {
                while (resultSet.next()) {
                    result.add(generateLikesForPosts(resultSet));
                }
            }

            @Override
            public List<GetLikesRequest> getResult() {
                return result;
            }
        };
    }

    private GetLikesRequest generateLikesForPosts(ResultSet resultSet) throws SQLException {
        GetLikesRequest getLikesRequest = new GetLikesRequest();
        getLikesRequest.setId(resultSet.getInt("id"));
        getLikesRequest.setUserId(resultSet.getInt("user_id"));
        getLikesRequest.setUsername(resultSet.getString("username"));
        getLikesRequest.setAvatarUrl(resultSet.getString("avatar_url"));
        return getLikesRequest;
    }




    public void add(Like like) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO \"like\" (user_id, post_id) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, like.getUserId());

            statement.setInt(2, like.getPost_id());

            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                int id = resultSet.getInt(1);
                like.setId(id);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public GetLikesRequest selectLikeById(int id) {
        try (PreparedStatement statement = connection.prepareStatement("""
              SELECT l.id, l.user_id as user_id, u.username as username, u.avatar_url as avatar_url
              FROM \"like\" l
              INNER JOIN \"user\" u on u.id = l.user_id
              WHERE l.id = ?""")) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return generateLikesForPosts(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public void delete(int postId, int userId) {
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM \"like\" WHERE post_id = ? AND user_id = ?")) {
            statement.setInt(1, postId);
            statement.setInt(2, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
