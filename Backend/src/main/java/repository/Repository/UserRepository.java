package repository.Repository;

import model.User;
import repository.ResultSetProcessor;
import request.GetUsersRequest;
import request.SignInRequest;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserRepository implements Repository {

    private final Connection connection;

    public UserRepository(Connection connection) {
        this.connection = connection;
    }


    public List<GetUsersRequest> selectAll() {
        ResultSetProcessor<List<GetUsersRequest>> findAllUsers = findUsers();

        executeQuery("SELECT id, username, name, surname, avatar_url FROM \"user\" ORDER BY name", findAllUsers, connection);
        return findAllUsers.getResult();
    }

    public User selectById(int userId) {
        ResultSetProcessor<User> findUser = new ResultSetProcessor<>() {
            private User user;

            @Override
            public void process(ResultSet resultSet) throws SQLException {
                if (resultSet.next()) {
                    user = generateUser(resultSet);
                }
            }

            @Override
            public User getResult() {
                return user;
            }
        };
        executeQuery("SELECT id, name, surname, username, email, user_gender, avatar_url, bio, birth_date, registeredAt, profession FROM public.\"user\" WHERE id = " + userId, findUser, connection);
        return findUser.getResult();
    }

    public SignInRequest selectByUsernameSignIn(String username) {
        SignInRequest signInRequest = new SignInRequest();
        try (PreparedStatement statement = connection.prepareStatement("SELECT id, username, passwordhash FROM \"user\" WHERE username = ?")) {
            statement.setObject(1, username);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                signInRequest.setId(resultSet.getInt("id"));
                signInRequest.setUsername(resultSet.getString("username"));
                signInRequest.setPassword(resultSet.getString("passwordhash"));
            }
            return signInRequest;

        } catch (SQLException e) {
            System.out.println("SignIn" + e.getMessage());
        }
        return null;
    }

    public List<GetUsersRequest> selectByUserName(String username) {
        ResultSetProcessor<List<GetUsersRequest>> findUsers = findUsers();
        executeQuery("SELECT id, username, name, surname, avatar_url FROM \"user\" WHERE username LIKE '%" + username + "%'", findUsers, connection);
        return findUsers.getResult();
    }


    public void add(User user) throws Exception {
        user.setRegisteredAt(dateNow());
        if (!isValidEmail(user.getEmail())) {
            throw new Exception("Email is not valid");
        }
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO \"user\" (name, surname, username, user_gender, email, passwordHash, avatar_url, bio, birth_date, registeredAt, profession) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getSurname());
            statement.setString(3, user.getUsername());
            statement.setObject(4, user.getGender(), Types.OTHER);
            statement.setString(5, user.getEmail());
            statement.setString(6, user.getPassword());

            statement.setString(7, user.getAvatarUrl());
            statement.setString(8, user.getBio());
            statement.setObject(9, user.getBirthDate(), Types.DATE);
            statement.setObject(10, user.getRegisteredAt(), Types.DATE);
            statement.setString(11, user.getProfession());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                int id = resultSet.getInt(1);
                user.setId(id);
            }
        }
    }

    public void addAvatar(String url, int user_id) {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE \"user\" SET avatar_url=? WHERE id=?;")) {
            statement.setString(1, url);
            statement.setInt(2, user_id);
            statement.executeUpdate();
            System.out.println(statement.executeUpdate());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private User generateUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setName(resultSet.getString("name"));
        user.setSurname(resultSet.getString("surname"));
        user.setUsername(resultSet.getString("username"));
        user.setGender(User.GENDER.valueOf(resultSet.getString("user_gender")));
        user.setBio(resultSet.getString("bio"));
        user.setEmail(resultSet.getString("email"));
        user.setAvatarUrl(resultSet.getString("avatar_url"));
        user.setProfession(resultSet.getString("profession"));
        user.setBirthDate(resultSet.getString("birth_date"));
        user.setRegisteredAt(resultSet.getString("registeredAt"));
        return user;
    }

    private GetUsersRequest generateUserRequest(ResultSet resultSet) throws SQLException {
        GetUsersRequest getUserRequest = new GetUsersRequest();
        getUserRequest.setName(resultSet.getString("name"));
        getUserRequest.setUsername(resultSet.getString("username"));
        getUserRequest.setId(resultSet.getInt("id"));
        getUserRequest.setAvatar_url(resultSet.getString("avatar_url"));
        getUserRequest.setSurname(resultSet.getString("surname"));
        return getUserRequest;
    }

    private String dateNow() {
        LocalDate dateObj = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return dateObj.format(formatter);
    }

    private boolean isValidEmail(String email) {
        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private ResultSetProcessor<List<GetUsersRequest>> findUsers() {
        return new ResultSetProcessor<>() {
            private final List<GetUsersRequest> result = new ArrayList<>();

            @Override
            public void process(ResultSet resultSet) throws SQLException {
                while (resultSet.next()) {
                    result.add(generateUserRequest(resultSet));
                }
            }

            @Override
            public List<GetUsersRequest> getResult() {
                return result;
            }
        };
    }


}
