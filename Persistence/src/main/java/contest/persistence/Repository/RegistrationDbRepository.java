package contest.persistence.Repository;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import contest.domain.Domain.Entities.Registration;
import contest.persistence.Repository.Interfaces.IRegistrationRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RegistrationDbRepository implements IRegistrationRepository {
    private static Logger logger = LogManager.getLogger();

    @Override
    public Iterable<Registration> findByContest(Integer idContest) {
        logger.traceEntry();

        ArrayList<Registration> registrations = new ArrayList<>();

        try (Connection connection = DatabaseConnectionProvider.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select * from registrations where id_contest = ?")) {
                statement.setInt(1, idContest);

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {

                        Integer id = resultSet.getInt("id");
                        Integer idParticipant = resultSet.getInt("id_participant");
                        Integer idContest1 = resultSet.getInt("id_contest");

                        Registration registration = new Registration(id, idParticipant, idContest1);

                        registrations.add(registration);
                    }
                }
            }
        } catch (SQLException e) {
            logger.error(e);
        }

        logger.traceExit("exiting with registrations: {}", registrations);
        return registrations;
    }

    @Override
    public Iterable<Registration> findByParticipant(Integer idParticipant) {
        logger.traceEntry();

        ArrayList<Registration> registrations = new ArrayList<>();

        try (Connection connection = DatabaseConnectionProvider.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select * from registrations where id_participant = ?")) {
                statement.setInt(1, idParticipant);

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {

                        Integer id = resultSet.getInt("id");
                        Integer idParticipant1 = resultSet.getInt("id_participant");
                        Integer idContest = resultSet.getInt("id_contest");

                        Registration registration = new Registration(id, idParticipant1, idContest);

                        registrations.add(registration);
                    }
                }
            }
        } catch (SQLException e) {
            logger.error(e);
        }

        logger.traceExit("exiting with registrations: {}", registrations);
        return registrations;
    }

    @Override
    public Registration findOne(Integer integer) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public Iterable<Registration> findAll() {
        logger.traceEntry();

        ArrayList<Registration> registrations = new ArrayList<>();

        try (Connection connection = DatabaseConnectionProvider.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select * from registrations")) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {

                        Integer id = resultSet.getInt("id");
                        Integer idParticipant = resultSet.getInt("id_participant");
                        Integer idContest = resultSet.getInt("id_contest");

                        Registration registration = new Registration(id, idParticipant, idContest);

                        registrations.add(registration);
                    }
                }
            }
        } catch (SQLException e) {
            logger.error(e);
        }

        logger.traceExit("exiting with registrations: {}", registrations);
        return registrations;
    }

    @Override
    public Integer save(Registration entity) {
        logger.traceEntry("saving registration: {}", entity);

        Integer id = null;

        try (Connection connection = DatabaseConnectionProvider.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("insert into registrations (id_participant, id_contest) values (?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
                statement.setInt(1, entity.getParticipantId());
                statement.setInt(2, entity.getContestId());
                statement.executeUpdate();
                id = statement.getGeneratedKeys().getInt(1);

                logger.trace("registration saved: {}", entity);
            }
        } catch (SQLException e) {
            logger.error(e);
        }

        logger.traceExit("exiting with id: {}", id);
        return id;
    }

    @Override
    public void delete(Integer integer) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public void update(Registration entity) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
