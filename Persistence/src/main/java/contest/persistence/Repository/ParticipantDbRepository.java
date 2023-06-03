package contest.persistence.Repository;

import contest.persistence.Repository.Interfaces.IParticipantRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import contest.domain.Domain.Entities.Participant;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ParticipantDbRepository implements IParticipantRepository {
    private static Logger logger = LogManager.getLogger();
    @Override
    public Participant findOne(Integer integer) {
        logger.traceEntry("finding participant with id {}", integer);

        Participant participant = null;

        try (Connection connection = DatabaseConnectionProvider.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select * from participants where id = ?")) {

                statement.setInt(1, integer);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {

                        Integer id1 = resultSet.getInt("id");
                        String name = resultSet.getString("name");
                        Integer age = resultSet.getInt("age");

                        participant = new Participant(id1, name, age);

                        logger.trace("participant found {}", participant);
                    }
                }
            }
        } catch (SQLException e) {
            logger.error(e);
        }

        logger.traceExit("exiting with participant: {}", participant);
        return participant;
    }

    @Override
    public Iterable<Participant> findAll() {
        logger.traceEntry();

        ArrayList<Participant> participants = new ArrayList<>();

        try (Connection connection = DatabaseConnectionProvider.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select * from participants")) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        Integer id = resultSet.getInt("id");
                        String name = resultSet.getString("name");
                        Integer age = resultSet.getInt("age");

                        Participant participant = new Participant(id, name, age);
                        participants.add(participant);
                    }
                }
            }
        } catch (SQLException e) {
            logger.error(e);
        }

        logger.traceExit("exiting with participants: {}", participants);
        return participants;
    }

    @Override
    public Integer save(Participant entity) {
        logger.traceEntry("saving participant: {}", entity);

        Integer id = null;

        try (Connection connection = DatabaseConnectionProvider.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("insert into participants (name, age) values (?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, entity.getName());
                statement.setInt(2, entity.getAge());
                statement.executeUpdate();
                id = statement.getGeneratedKeys().getInt(1);

                logger.trace("participant saved: {}", entity);
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
    public void update(Participant entity) {
        throw new UnsupportedOperationException("Not implemented yet.");

    }
}
