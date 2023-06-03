package contest.client.Controller;

import contest.client.RpcClient;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import contest.services.ComException;
import contest.services.IObserver;
import contest.services.IService;

import java.io.IOException;

public class LoginController {
    public Button bLogin;
    public TextField tfUsername;
    public PasswordField pfPassword;
    public DialogPane fereastraeroare;
    private IService service;

    public void initialize() {
        bLogin.setOnAction(event -> login());
    }

    public void login() {
        String username = tfUsername.getText();
        String password = pfPassword.getText();

        FXMLLoader fxmlLoader = new FXMLLoader(RpcClient.class.getResource("main_view.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        MainpageController mainpageController = fxmlLoader.getController();

        try {
            if (service.login(username, password, (IObserver) mainpageController)) {
                mainpageController.setService(service);
                Stage stage = (Stage) bLogin.getScene().getWindow();
                stage.setScene(scene);
            }
            else {
                showAlert("Swimming Contest", "Login failed", "Username and password don't match!");
            }
        } catch (ComException e) {
            showAlert("Swimming Contest", "Login failed", e.getMessage());
        }
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.showAndWait();
    }

    private void openMainPage() {
        FXMLLoader fxmlLoader = new FXMLLoader(RpcClient.class.getResource("main_view.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        MainpageController controller = fxmlLoader.getController();
        controller.setService(service);
        Stage stage = (Stage) bLogin.getScene().getWindow();
        stage.setScene(scene);
    }

    public void setService(IService service) {
        this.service = service;
    }

    public void Close(MouseEvent mouseEvent) {
    }
}
