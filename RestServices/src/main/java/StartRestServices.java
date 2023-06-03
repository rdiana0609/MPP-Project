import contest.persistence.Repository.ContestDbRepository;
import contest.persistence.Repository.DatabaseConnectionProvider;
import contest.persistence.Repository.Interfaces.IContestRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import java.util.*;
import java.io.IOException;


@ComponentScan("contest")
@SpringBootApplication
public class StartRestServices {
    public static void main(String[] args) {
        SpringApplication.run(StartRestServices.class, args);
    }

    @Bean
    IContestRepository createRepository() {
        Properties serverProps = new Properties();
        try {
            serverProps.load(StartRestServices.class.getResourceAsStream("/server.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find chatserver.properties " + e);
        }

        int serverPort = 55555;
        try {
            serverPort = Integer.parseInt(serverProps.getProperty("server.port"));
        } catch (NumberFormatException nef) {
            System.err.println("Wrong Port Number" + nef.getMessage());
            System.err.println("Using default port " + 55555);
        }
        System.out.println("Starting server on port: " + serverPort);
        DatabaseConnectionProvider.setProperties(serverProps);

        return new ContestDbRepository();
    }


}
