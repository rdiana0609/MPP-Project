package contest.persistence.Repository;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import contest.domain.Domain.Entities.Contest;
import contest.domain.Domain.Enums.ContestDistance;
import contest.domain.Domain.Enums.ContestAge;
import contest.persistence.Repository.Interfaces.IContestRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class ContestDbRepository implements IContestRepository {
    private static Logger logger = LogManager.getLogger();

    @Override
    public Contest findOne(Integer integer) {
        logger.traceEntry();

        Contest contest = null;

        try (Connection connection = DatabaseConnectionProvider.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select * from contests where id = ?")) {
                statement.setInt(1, integer);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        Integer id = resultSet.getInt("id");
                        String distance=resultSet.getString("distance");
                        ContestDistance distance_ = ContestDistance.getByLength(distance);
                        String style=resultSet.getString("style");
                        ContestAge style_ = ContestAge.getByType(style);
                        Integer noParticipants = resultSet.getInt("no_participants");

                        contest = new Contest(id, distance_, style_, noParticipants);
                    }
                }
            }
        } catch (SQLException e) {
            logger.error(e);
        }

        logger.traceExit("exiting with contest: {}", contest);
        return contest;
    }

    @Override
    public Iterable<Contest> findAll() {
        logger.traceEntry();

        ArrayList<Contest> contests = new ArrayList<>();

        try (Connection connection = DatabaseConnectionProvider.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select * from contests")) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {

                        Integer id = resultSet.getInt("id");
                        String distance=resultSet.getString("distance");
                        ContestDistance distance_ = ContestDistance.getByLength(distance);
                        String style=resultSet.getString("style");
                        ContestAge style_ = ContestAge.getByType(style);
                        Integer noParticipants = resultSet.getInt("no_participants");

                        Contest contest = new Contest(id, distance_, style_, noParticipants);

                        contests.add(contest);
                    }
                }
            }
        } catch (SQLException e) {
            logger.error(e);
        }

        logger.traceExit("exiting with contests: {}", contests);
        return contests;
    }

    @Override
    public Integer save(Contest entity) {
        logger.traceEntry();

        Integer id = null;

        try (Connection connection = DatabaseConnectionProvider.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("insert into contests values (null, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, entity.getDistance().length);
                statement.setString(2, entity.getStyle().type);
                statement.setInt(3, entity.getNoOfParticipants());

                statement.executeUpdate();

                ResultSet resultSet = statement.getGeneratedKeys();

                if (resultSet.next()) {
                    id = resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
        }

        logger.traceExit("exiting with returned id: {}", id);
        return id;
    }

    @Override
    public void delete(Integer integer) {
        logger.traceEntry();

        try (Connection connection = DatabaseConnectionProvider.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("delete from contests where id = ?")) {
                statement.setInt(1, integer);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            logger.error(e);
        }

        logger.traceExit();
    }

    @Override
    public void update(Contest entity) {
        logger.traceEntry();

        try (Connection connection = DatabaseConnectionProvider.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("update contests set distance = ?, style = ?, no_participants = ? where id = ?")) {
                statement.setString(1, entity.getDistance().length);
                statement.setString(2, entity.getStyle().type);
                statement.setInt(3, entity.getNoOfParticipants());
                statement.setInt(4, entity.getId());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            logger.error(e);
        }

        logger.traceExit();
    }

    @Override
    public void updateNoOfParticipants(Contest contest) {
        logger.traceEntry("update noOfParticipants for contest: {}", contest);

        try (Connection connection = DatabaseConnectionProvider.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("update contests set no_participants = ? where id = ?")) {
                statement.setInt(1, contest.getNoOfParticipants());
                statement.setInt(2, contest.getId());
                statement.executeUpdate();

                logger.trace("contest updated: {}", contest);
            }
        } catch (SQLException e) {
            logger.error(e);
        }

        logger.traceExit("exiting with contest: {}", contest);
    }
}
