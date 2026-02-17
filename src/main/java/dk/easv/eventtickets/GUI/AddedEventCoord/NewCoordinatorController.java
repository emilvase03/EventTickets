package dk.easv.eventtickets.GUI.AddedEventCoord;

import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;

public class NewCoordinatorController {
    @FXML
    private MFXTextField usernameField;
    @FXML
    private MFXTextField emailField;
    @FXML
    private MFXTextField passwordField;
    @FXML
    private MFXTextField fullNameField;

    @FXML
    private void onBntSaveAddedCoordinator(ActionEvent actionEvent) {
     String fullName = fullNameField.getText();
     String password = passwordField.getText();
     String email = emailField.getText();
     String username = usernameField.getText();


        // CLOSE POPUP
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();



    }
}
