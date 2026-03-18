package dk.easv.eventtickets.GUI.Controllers;

//java imports
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;

import io.github.palexdev.materialfx.controls.MFXTextField;

//project imports
import dk.easv.eventtickets.BE.Role;
import dk.easv.eventtickets.GUI.Models.UserModel;
import dk.easv.eventtickets.GUI.Utils.AlertHelper;


public class NewCoordinatorController {

    private UserModel userModel;


    @FXML private MFXTextField firstNameField;
    @FXML private MFXTextField lastNameField;
    @FXML private MFXTextField emailField;
    @FXML private MFXTextField passwordField;


    public void init(UserModel userModel) {
        this.userModel = userModel;
    }


    @FXML
    private void onBntSaveAddedCoordinator(ActionEvent actionEvent) {
     try {
            String firstName = firstNameField.getText();
            String lastName  = lastNameField.getText();
            String email     = emailField.getText();
            String password  = passwordField.getText();

            if (firstName.isBlank() || lastName.isBlank() || email.isBlank() || password.isBlank()) {
                AlertHelper.showError("Validation", "Please fill all fields.");
                return;
            }

            userModel.createUser(firstName, lastName, email, password, Role.EVENT);


            close(actionEvent);

        } catch (Exception ex) {
            AlertHelper.showError("Error", ex.getMessage());
        }
    }

    @FXML
    private void onBtnCancel(ActionEvent actionEvent) {
        close(actionEvent);
    }

    private void close(ActionEvent e) {
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.close();
    }


}