package by.itacademy.javaenterprise.dao.impl;

import by.itacademy.javaenterprise.connection.DataSourcee;
import by.itacademy.javaenterprise.dao.TrainingDAO;
import by.itacademy.javaenterprise.entity.Training;
import by.itacademy.javaenterprise.exception.DAOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class TrainingDAOImplem implements TrainingDAO<Training> {

    private static final Logger logger = LoggerFactory.getLogger(TrainingDAOImplem.class);

    private static final String ADD_QUERY = "INSERT Training(Training_id,Training_date,user_id) VALUES(?,?,?)";
    private static final int TRAINING_ID_ADD_QUERY_INDEX = 1;
    private static final int DATE_ADD_QUERY_INDEX = 2;
    private static final int USER_ID_QUERY_INDEX = 3;

    private static final String UPDATE_QUERY = "UPDATE Training SET Training_date=?,user_id=? WHERE Training_id=?";
    private static final int UPDATE_DATE_QUERY_INDEX = 1;
    private static final int UPDATE_USER_ID_QUERY_INDEX = 2;
    private static final int UPDATE_TRAINING_ID_QUERY_INDEX = 3;

    private static final String DELETE_QUERY = "DELETE FROM Training WHERE Training_id=?";
    private static final int TRAINING_ID_DELETE_QUERY_INDEX = 1;

    public static final String SELECT_ALL = "SELECT * From Training LIMIT ? OFFSET ?";

    private DataSourcee connect;

    public TrainingDAOImplem(@Autowired DataSourcee connect) {
        this.connect = connect;
    }

    @Override
    public void addTraining(Training training) throws DAOException {
        try (Connection con = connect.getConnection();
             PreparedStatement pst = con.prepareStatement(ADD_QUERY)) {
            prepareAddStatement(pst, training);
            pst.executeUpdate();
        } catch (SQLException throwables) {
            logger.error("Error while adding a training{}", training.toString(), throwables);
            throw new DAOException(throwables);
        }

    }

    @Override
    public void updateTraining(Training training) throws DAOException {
        try (Connection connection = connect.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
            prepareUpdateStatement(statement, training);
            connection.setAutoCommit(false);
            statement.executeUpdate();
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            logger.error("Error while updating training{}", training.toString(), e);
            throw new DAOException(e);

        }
    }

    @Override
    public void deleteTraining(Training training) throws DAOException {
        try (Connection connection = connect.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            prepareDeleteStatement(statement, training);
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error while deleting training{}", training.toString(), e);
            throw new DAOException(e);
        }
    }

    @Override
    public List<Training> getAllTrainingsPagination(int limit, int offset) throws DAOException {
        List<Training> trainings = new ArrayList<>();
        try (Connection connection = connect.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL)) {

            statement.setInt(1, limit);
            statement.setInt(2, offset);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                trainings.add(Training.builder().id(rs.getInt(1)).
                        date(LocalDateTime.parse(rs.getString(2).replaceAll(" ", "T")))
                        .userID(rs.getInt(3)).build());
            }
        } catch (SQLException e) {
            logger.error("Error with selecting trainings", e);
        }
        if (trainings.isEmpty()) {
            throw new DAOException("Cant find trainings");
        }
        else return trainings;
    }


    private void prepareUpdateStatement(PreparedStatement statement, Training training) throws SQLException {
        statement.setString(UPDATE_DATE_QUERY_INDEX, String.valueOf(training.getDate()));
        statement.setInt(UPDATE_USER_ID_QUERY_INDEX, training.getUserID());
        statement.setInt(UPDATE_TRAINING_ID_QUERY_INDEX, training.getId());
    }

    private void prepareAddStatement(PreparedStatement statement, Training training) throws SQLException {
        statement.setInt(TRAINING_ID_ADD_QUERY_INDEX, training.getId());
        statement.setString(DATE_ADD_QUERY_INDEX, String.valueOf(training.getDate()));
        statement.setInt(USER_ID_QUERY_INDEX, training.getUserID());
    }
    private void prepareDeleteStatement(PreparedStatement statement, Training training) throws SQLException {
        statement.setInt(TRAINING_ID_DELETE_QUERY_INDEX, training.getId());
    }
}
