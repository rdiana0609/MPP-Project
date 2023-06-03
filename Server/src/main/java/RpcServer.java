import contest.persistence.Repository.ContestDbRepository;
import contest.persistence.Repository.DatabaseConnectionProvider;
import contest.persistence.Repository.ParticipantDbRepository;
import contest.persistence.Repository.RegistrationDbRepository;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import contest.networking.utils.AbstractServer;
import contest.networking.utils.RpcConcurrentServer;
import contest.networking.utils.ServerException;
import contest.persistence.Repository.*;
import contest.persistence.Repository.Interfaces.IContestRepository;
import contest.persistence.Repository.Interfaces.IParticipantRepository;
import contest.persistence.Repository.Interfaces.IRegistrationRepository;
import contest.persistence.Repository.Interfaces.IUserRepository;
import contest.persistence.Repository.orm.UserORMRepository;
import contest.server.ServiceImpl;
import contest.services.IService;
import java.util.*;
import java.io.IOException;


public class RpcServer {
    private static int defaultPort = 55555;
   
    static void initialize() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        try {
            SessionFactory sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            System.err.println("Session creation error: " + e);
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
    public static void main(String[] args) {
        SessionFactory sessionFactory = null;
        // UserRepository userRepo=new UserRepositoryMock();
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            System.err.println("Session creation error: " + e);
            StandardServiceRegistryBuilder.destroy(registry);
        }
        Properties serverProps = new Properties();
        try {
            serverProps.load(RpcServer.class.getResourceAsStream("/server.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find chatserver.properties " + e);
            return;
        }

        DatabaseConnectionProvider.setProperties(serverProps);

        IContestRepository contestRepository = new ContestDbRepository();
        IUserRepository userRepository = new UserORMRepository(sessionFactory);
        IParticipantRepository participantRepository = new ParticipantDbRepository();
        IRegistrationRepository registrationRepository = new RegistrationDbRepository();

        IService chatServerImpl = new ServiceImpl(contestRepository, participantRepository, registrationRepository, userRepository);
        int serverPort = defaultPort;
        try {
            serverPort = Integer.parseInt(serverProps.getProperty("server.port"));
        } catch (NumberFormatException nef) {
            System.err.println("Wrong Port Number" + nef.getMessage());
            System.err.println("Using default port " + defaultPort);
        }
        System.out.println("Starting server on port: " + serverPort);
        AbstractServer server = new RpcConcurrentServer(serverPort, chatServerImpl);
        try {
            server.start();
        } catch (ServerException e) {
            System.err.println("Error starting the server" + e.getMessage());
        } finally {
            try {
                server.stop();
            } catch (ServerException e) {
                System.err.println("Error stopping server " + e.getMessage());
            }
        }
    }
}
