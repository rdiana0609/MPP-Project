package contest.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import contest.client.Controller.LoginController;
import contest.networking.rpcprotocol.ServiceRpcProxy;
import contest.services.IService;
import java.util.*;
import java.io.IOException;


public class RpcClient extends Application {
    private static int defaultChatPort = 55555;
    private static String defaultServer = "localhost";


    public void start(Stage stage) throws Exception {
        System.out.println("In start");
        Properties clientProps = new Properties();
        try {
            clientProps.load(RpcClient.class.getResourceAsStream("/client.properties"));
            System.out.println("Client properties set. ");
            clientProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find client.properties " + e);
            return;
        }
        String serverIP = clientProps.getProperty("server.host", defaultServer);
        int serverPort = defaultChatPort;

        try {
            serverPort = Integer.parseInt(clientProps.getProperty("server.port"));
        } catch (NumberFormatException ex) {
            System.err.println("Wrong port number " + ex.getMessage());
            System.out.println("Using default port: " + defaultChatPort);
        }
        System.out.println("Using server IP " + serverIP);
        System.out.println("Using server port " + serverPort);

        IService service = new ServiceRpcProxy(serverIP, serverPort);
        FXMLLoader fxmlLoader = new FXMLLoader(RpcClient.class.getResource("login2.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        LoginController controller = fxmlLoader.getController();

//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("mainpage.fxml"));
//        Scene scene = new Scene(fxmlLoader.load());
//        MainpageController controller = fxmlLoader.getController();

        controller.setService(service);

        stage.setTitle("Swimming Contest");
        stage.setScene(scene);
        stage.show();
    }
}


