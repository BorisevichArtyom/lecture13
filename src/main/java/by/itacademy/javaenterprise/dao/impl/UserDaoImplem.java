package by.itacademy.javaenterprise.dao.impl;

import by.itacademy.javaenterprise.connection.DataSourcee;
import by.itacademy.javaenterprise.dao.UserDAO;
import by.itacademy.javaenterprise.entity.Role;
import by.itacademy.javaenterprise.entity.User;
import by.itacademy.javaenterprise.exception.DAOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserDaoImplem implements UserDAO<User> {

    private static final String ADD_QUERY = "INSERT Diary_user(email,user_password,first_name,last_name," +
            "balance_amount,user_role_id)" +
            "VALUES (?,?,?,?,?,?)";

    private static final int EMAIL_ADD_QUERY_INDEX = 1;
    private static final int PASSWORD_ADD_QUERY_INDEX = 2;
    private static final int FIRST_NAME_ADD_QUERY_INDEX = 3;
    private static final int LAST_NAME_ADD_QUERY_INDEX = 4;
    private static final int BALANCE_AMOUNT_ADD_QUERY_INDEX = 5;
    private static final int ROLE_INDEX_ADD_QUERY = 6;

    private static final String UPDATE_QUERY = "UPDATE Diary_user " +
            "SET email=?,user_password=?,first_name=?,last_name=?,balance_amount=?,user_role_id=? WHERE user_id=?";
    private static final int UPDATE_QUERY_EMAIL_INDEX = 1;
    private static final int UPDATE_QUERY_PASSWORD_INDEX = 2;
    private static final int UPDATE_QUERY_FIRST_NAME_INDEX = 3;
    private static final int UPDATE_QUERY_LAST_NAME_INDEX = 4;
    private static final int UPDATE_QUERY_BALANCE_AMOUNT_INDEX = 5;
    private static final int UPDATE_QUERY_ROLE_INDEX = 6;
    private static final int UPDATE_QUERY_CONDITION_USER_ID = 7;

    private static final String SELECT_BY_EMAIL = "Select * From Diary_user Where email =?";

    public static final String SELECT_ALL =
            "SELECT * From Diary_user LIMIT ? OFFSET ?";
    public static final String FIND_USER_BY_ID =
            "SELECT * From Diary_user WHERE user_id=?";

    private static final String DELETE_QUERY = "DELETE FROM Diary_user WHERE email=?";

    private static final int EMAIL_DELETE_QUERY_INDEX = 1;
    private static Integer userID;

    private static final Logger logger = LoggerFactory.getLogger(UserDaoImplem.class);

    private static DataSourcee connect;

    private UserDaoImplem(@Autowired DataSourcee connect) {

        UserDaoImplem.connect = connect;
    }


    @Override
    public void addUser(User user) throws DAOException {
        try (Connection con = connect.getConnection();
             PreparedStatement pst = con.prepareStatement(ADD_QUERY)) {
            prepareAddStatement(pst, user);
            con.setAutoCommit(false);
            pst.executeUpdate();
            con.commit();
            con.setAutoCommit(true);
        } catch (SQLException throwables) {
            logger.error("Error while adding a user{}", user.toString(), throwables);
            throw new DAOException(throwables);
        }
    }

    @Override
    public void updateUser(User user) throws DAOException {
        try (Connection connection = connect.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
            prepareUpdateStatement(statement, user);
            connection.setAutoCommit(false);
            statement.executeUpdate();
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            logger.error("Error while updating a user{}", user.toString(), e);
            throw new DAOException(e);

        }
    }

    @Override
    public void deleteUser(User user) throws DAOException {
        try (Connection connection = connect.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            connection.setAutoCommit(false);
            prepareDeleteStatement(statement, user);
            statement.executeUpdate();
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            logger.error("Error while deleting a user{}", user.toString(), e);
            throw new DAOException(e);
        }
    }

    @Override
    public List<User> getAllUsersPagination(int limit, int offset) throws DAOException {
        List<User> users = new ArrayList<>();
        try (Connection connection = connect.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL)) {

            statement.setInt(1, limit);
            statement.setInt(2, offset);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                users.add(User.builder().id(rs.getInt(1)).email(rs.getString(2))
                        .password(rs.getString(3)).firstName(rs.getString(4))
                        .lastName(rs.getString(5)).balanceAmount(rs.getBigDecimal(6))
                        .role(Role.getRoleByid(rs.getInt(7))).build());
            }
        } catch (SQLException e) {
            logger.error("Error with selecting users", e);
        }
        if (users.isEmpty()) {
            throw new DAOException("Cant find users");
        }
        else return users;
    }


    public static int FindIdUserByEmail(User user) throws DAOException {
        try (Connection connection = connect.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_BY_EMAIL)) {
            prepareSelectStatement(statement, user.getEmail());
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                userID = rs.getInt(1);
            }

        } catch (SQLException e) {
            logger.error("Error with select by id", e);
        }
        if (userID == null) {
            throw new DAOException("Cant find userId");
        } else return userID;
    }

    public User getUserById(Integer userId) throws DAOException {
        User user = null;
        try (Connection connection = connect.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_USER_BY_ID)) {
            statement.setInt(1, userId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                user = User.builder().id(rs.getInt(1)).email(rs.getString(2))
                        .password(rs.getString(3)).firstName(rs.getString(4))
                        .lastName(rs.getString(5)).balanceAmount(rs.getBigDecimal(6))
                        .role(Role.getRoleByid(rs.getInt(7))).build();
            }
        } catch (SQLException e) {
            logger.error("Error with getting user by id", e);
        }
        if (user == null) {
            throw new DAOException("Cant find user");
        } else return user;
    }

    private static void prepareSelectStatement(PreparedStatement statement, String email) throws SQLException {
        statement.setString(1, email);
    }

    private void prepareDeleteStatement(PreparedStatement statement, User user) throws SQLException {
        statement.setString(EMAIL_DELETE_QUERY_INDEX, user.getEmail());
    }

    private void prepareAddStatement(PreparedStatement statement, User user) throws SQLException {
        statement.setString(EMAIL_ADD_QUERY_INDEX, user.getEmail());
        statement.setString(PASSWORD_ADD_QUERY_INDEX, user.getPassword());
        statement.setString(FIRST_NAME_ADD_QUERY_INDEX, user.getFirstName());
        statement.setString(LAST_NAME_ADD_QUERY_INDEX, user.getLastName());
        statement.setBigDecimal(BALANCE_AMOUNT_ADD_QUERY_INDEX, user.getBalanceAmount());
        statement.setInt(ROLE_INDEX_ADD_QUERY, user.getRole().getId());

    }

    private void prepareUpdateStatement(PreparedStatement statement, User user) throws SQLException, DAOException {
        statement.setString(UPDATE_QUERY_EMAIL_INDEX, user.getEmail());
        statement.setString(UPDATE_QUERY_PASSWORD_INDEX, user.getPassword());
        statement.setString(UPDATE_QUERY_FIRST_NAME_INDEX, user.getFirstName());
        statement.setString(UPDATE_QUERY_LAST_NAME_INDEX, user.getLastName());
        statement.setBigDecimal(UPDATE_QUERY_BALANCE_AMOUNT_INDEX, user.getBalanceAmount());
        statement.setInt(UPDATE_QUERY_ROLE_INDEX, user.getRole().getId());
        statement.setInt(UPDATE_QUERY_CONDITION_USER_ID, FindIdUserByEmail(user));
    }


}
