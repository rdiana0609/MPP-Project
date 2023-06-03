package contest.client.Controller;


import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import contest.client.RpcClient;
import contest.domain.Domain.Entities.Contest;
import contest.services.ComException;
import contest.services.IObserver;
import contest.services.IService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainpageController implements IObserver {

    public TableView tvContests;
    public TableColumn tcContestId;
    public TableColumn tcStyle;
    public TableColumn tcDistance;
    public TableColumn tcNoParticipants;
    public TableView tvParticipants;
    public TableColumn tcParticipantId;
    public TableColumn tcName;
    public TableColumn tcAge;
    public TableColumn tcContestRegistrations;
    public TableView tvContests1;
    public TableColumn tcContestId1;
    public TableColumn tcStyle1;
    public TableColumn tcDistance1;
    public TableColumn tcNoParticipants1;
    public Spinner sAge;
    public TextField tfParticipantName;
    public Button bAddParticipant;
    public Button bLogout;
    public Pane pnProbe;
    public Pane pnInscriere;
    public ComboBox comboboxVarsta;
    public ComboBox comboboxDistanta;
    public Pane pnParticipanti;
    public Button butonProbe;
    public Button butonInscriere;
    public Button butonParticipanti;
    public TableColumn tcContestsId;
    public TableColumn tcContests1Id;
    public Label textUsername;
    public TableView tvContests2;
    public TableColumn tcContestsId1;
    public TableColumn tcStyle2;
    public TableColumn tcDistance2;
    private IService service;

    public void setService(IService service) {
        this.service = service;

        refresh();
    }
    @FXML
    void handleClicks(ActionEvent event)  {
        if (event.getSource() == butonParticipanti) {
            pnParticipanti.toFront();
            pnParticipanti.setVisible(true);
            pnProbe.setVisible(false);
            pnInscriere.setVisible(false);

        }
        if (event.getSource() ==butonInscriere) {
            pnInscriere.toFront();
            pnInscriere.setVisible(true);
            pnParticipanti.setVisible(false);
            pnProbe.setVisible(false);
        }
        if (event.getSource() ==butonProbe) {
            pnProbe.toFront();
            pnProbe.setVisible(true);
            pnParticipanti.setVisible(false);
            pnInscriere.setVisible(false);
        }
        if (event.getSource() == bLogout) {
            Platform.exit();
        }
    }
    public void initialize() {
        pnProbe.toFront();
        pnProbe.setVisible(true);
        pnInscriere.setVisible(false);
        pnParticipanti.setVisible(false);
        // Create a value factory to populate the spinner

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(7, 70, 7, 1);
        sAge.setValueFactory(valueFactory);

        tcContestsId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tcStyle.setCellValueFactory(new PropertyValueFactory<>("style"));
        tcDistance.setCellValueFactory(new PropertyValueFactory<>("distance"));
        tcNoParticipants.setCellValueFactory(new PropertyValueFactory<>("noOfParticipants"));

        tcContests1Id.setCellValueFactory(new PropertyValueFactory<>("id"));
        tcStyle1.setCellValueFactory(new PropertyValueFactory<>("style"));
        tcDistance1.setCellValueFactory(new PropertyValueFactory<>("distance"));

        tcContestsId1.setCellValueFactory(new PropertyValueFactory<>("id"));
        tcStyle2.setCellValueFactory(new PropertyValueFactory<>("style"));
        tcDistance2.setCellValueFactory(new PropertyValueFactory<>("distance"));
        tcNoParticipants1.setCellValueFactory(new PropertyValueFactory<>("noOfParticipants"));

        //tcNoParticipants1.setCellValueFactory(new PropertyValueFactory<>("noOfParticipants"));

        tvContests2.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        tcParticipantId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tcName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tcAge.setCellValueFactory(new PropertyValueFactory<>("age"));
        tcContestRegistrations.setCellValueFactory(new PropertyValueFactory<>("registeredContestIds"));

        tvContests2.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Contest selectedContest = (Contest) newSelection;
                try {
                    tvParticipants.setItems(FXCollections.observableArrayList(service.getParticipantsOfContest(selectedContest.getId())));
                } catch (ComException e) {
                    showAlert("Error", "Error getting participants", e.getMessage());
                }
            }
        });

        bAddParticipant.setOnAction(event -> addParticipant());
        bLogout.setOnAction(event -> logout());
    }
    @FXML
    private void logout() {
        try {
            service.logout(null);
            openLogin();
        } catch (ComException e) {
            showAlert("Error", "Error logging out", e.getMessage());
        }
    }

    private void openLogin() {
        FXMLLoader fxmlLoader = new FXMLLoader(RpcClient.class.getResource("login2.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        LoginController controller = fxmlLoader.getController();
        controller.setService(service);
        Stage stage = (Stage) bLogout.getScene().getWindow();
        stage.setScene(scene);
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.showAndWait();
    }

    private void addParticipant() {
        String name = tfParticipantName.getText();
        int age = (int) sAge.getValue();
        List<Contest> selectedContests = new ArrayList<>();
        try {
            tvContests1.getSelectionModel().getSelectedItems().forEach(contest -> selectedContests.add((Contest) contest));

            if (selectedContests.size() > 0) {
                service.addParticipant(name, age, selectedContests);
                refresh();
            } else
                showAlert("Error", "No contest selected", "Please select a contest");
        }
        catch (ComException e) {
            showAlert("Error", "Error adding participant", e.getMessage());
        }

    }

    private void refresh(){
        try {
            tvContests.setItems(FXCollections.observableArrayList(service.getContests()));
            tvContests1.setItems(FXCollections.observableArrayList(service.getContests()));
            tvContests2.setItems(FXCollections.observableArrayList(service.getContests()));
        } catch (ComException e) {
            throw new RuntimeException(e);
        }
        tvParticipants.setItems(FXCollections.observableArrayList());
    }


    @Override
    public void participantAdded() {
        Platform.runLater(()->{
            refresh();
        });
    }

    public void setPnParticipanti(ActionEvent actionEvent) {
        pnParticipanti.toFront();
        pnParticipanti.setVisible(true);
        pnInscriere.setVisible(false);
        pnProbe.setVisible(false);
    }

    public void setPnInscriere(ActionEvent actionEvent) {
        pnInscriere.setVisible(true);
        pnInscriere.toFront();
        pnProbe.setVisible(false);
        pnParticipanti.setVisible(false);
    }

    public void setPnProbe(ActionEvent actionEvent) {
        pnProbe.toFront();
        pnProbe.setVisible(true);
        pnInscriere.setVisible(false);
        pnParticipanti.setVisible(false);
    }

    public void AddParticipant(ActionEvent actionEvent) {
        String name = tfParticipantName.getText();
        int age = (int) sAge.getValue();
        List<Contest> selectedContests = new ArrayList<>();
        try {
            tvContests1.getSelectionModel().getSelectedItems().forEach(contest -> selectedContests.add((Contest) contest));

            if (selectedContests.size() > 0) {
                service.addParticipant(name, age, selectedContests);
                refresh();
            } else
                showAlert("Error", "No contest selected", "Please select a contest");
        }
        catch (ComException e) {
            showAlert("Error", "Error adding participant", e.getMessage());
        }
    }
}
