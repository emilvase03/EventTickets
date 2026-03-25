package dk.easv.eventtickets.GUI.Controllers;

// Project imports
import dk.easv.eventtickets.BE.Role;
import dk.easv.eventtickets.BE.User;
import dk.easv.eventtickets.GUI.Models.UserModel;
import dk.easv.eventtickets.GUI.Utils.AlertHelper;

// MaterialFX imports
import io.github.palexdev.materialfx.controls.MFXTextField;

// Java imports
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;

public class EditCoordController {
    @FXML
    private MFXTextField firstNameFieldEdit;
    @FXML
    private MFXTextField passwordFieldEdit;
    @FXML
    private MFXTextField emailFieldEdit;
    @FXML
    private MFXTextField lastNameFieldEdit;
    @FXML
    private UserModel userModel;
    @FXML
    private User userToEdit;

    public void init(UserModel userModel, User user) {
        this.userModel  = userModel;
        this.userToEdit = user;


        firstNameFieldEdit.setText(user.getFirstName());
        lastNameFieldEdit.setText(user.getLastName());
        emailFieldEdit.setText(user.getEmail());
        passwordFieldEdit.setText("");

    }

    @FXML
    private void onBntSaveEdit(ActionEvent actionEvent) {
        try {
            String firstName = firstNameFieldEdit.getText().trim();
            String lastName  = lastNameFieldEdit.getText().trim();
            String email     = emailFieldEdit.getText().trim();
            String password  = passwordFieldEdit.getText().trim();

            if (firstName.isBlank() || lastName.isBlank() || email.isBlank()) {
                AlertHelper.showError("Validation", "First name, last name and email are required.");
                return;
            }


            userToEdit.setFirstName(firstName);
            userToEdit.setLastName(lastName);
            userToEdit.setEmail(email);


            if (!password.isBlank()) {
                userToEdit.setPassword(password);
            }


            userToEdit.setRole(Role.EVENT);

            userModel.updateUser(userToEdit);

            close(actionEvent);

        } catch (Exception ex) {
            AlertHelper.showError("Error", ex.getMessage());
        }
    }


    @FXML
    private void onBtnCancelEdit(ActionEvent actionEvent) {
   close(actionEvent); }
    private void close(ActionEvent e) {
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.close();
    }


}
