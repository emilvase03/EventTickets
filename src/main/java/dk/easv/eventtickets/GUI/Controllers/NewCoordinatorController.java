package dk.easv.eventtickets.GUI.Controllers;

// MaterialFX imports
import io.github.palexdev.materialfx.controls.MFXTextField;

// Java imports
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;

public class NewCoordinatorController {

    @FXML
    private MFXTextField emailField;
    @FXML
    private MFXTextField passwordField;
    @FXML
    private MFXTextField firstNameField;
    @FXML
    private MFXTextField lastNameField;
    @FXML

    private void onBntSaveAddedCoordinator(ActionEvent actionEvent) {

     String firstName =firstNameField.getText();
     String lastName = lastNameField.getText();

     String password = passwordField.getText();
     String email = emailField.getText();


        // CLOSE POPUP
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();



    }
}
