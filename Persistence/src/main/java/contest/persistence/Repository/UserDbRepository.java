package contest.persistence.Repository;


import contest.persistence.Repository.Interfaces.IUserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import contest.domain.Domain.Entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserDbRepository implements IUserRepository {
    private static Logger logger = LogManager.getLogger();
    @Override
    public User findOne(String s) {
        logger.traceEntry("finding user with id {}", s);

        User user = null;

        try (Connection connection = DatabaseConnectionProvider.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select * from users where username = ?")) {

                statement.setString(1, s);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {

                        String username = resultSet.getString("username");
                        String name = resultSet.getString("name");
                        String password = resultSet.getString("password");

                        user = new User(username, name, password);

                        logger.trace("user found {}", user);
                    }
                }
            }
        } catch (SQLException e) {
            logger.error(e);
        }

        logger.traceExit("exiting with user: {}", s);
        return user;
    }

    @Override
    public Iterable<User> findAll() {
        logger.traceEntry();

        ArrayList<User> users = new ArrayList<>();

        try (Connection connection = DatabaseConnectionProvider.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select * from users")) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {

                        String username = resultSet.getString("username");
                        String name = resultSet.getString("name");
                        String password = resultSet.getString("password");

                        User user = new User(username, name, password);
                        users.add(user);
                    }
                }
            }
        } catch (SQLException e) {
            logger.error(e);
        }

        logger.traceExit("exiting with users: {}", users);
        return users;
    }

    @Override
    public String save(User entity) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public void delete(String s) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public void update(User entity) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public User findUser(String username, String password) {
        logger.traceEntry("finding user with username {} and password {}", username, password);

        Boolean found = Boolean.FALSE;
        User user=null;
        try (Connection connection = DatabaseConnectionProvider.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select * from users where username = ? and password = ?")) {

                statement.setString(1, username);
                statement.setString(2, password);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        user=new User(resultSet.getString("name"),username,password);
                    }
                }
            }
        } catch (SQLException e) {
            logger.error(e);
        }

        logger.traceExit("exiting with found: {}", found);
        return user;
    }
}
