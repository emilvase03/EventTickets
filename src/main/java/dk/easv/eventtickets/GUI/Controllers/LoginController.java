package dk.easv.eventtickets.GUI.Controllers;

// Project imports
import dk.easv.eventtickets.GUI.Utils.AlertHelper;

// Java imports
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class LoginController {

    @FXML private MFXTextField txtUsername;
    @FXML private MFXTextField txtPassword;

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = txtUsername.getText() == null ? "" : txtUsername.getText().trim();
        String password = txtPassword.getText() == null ? "" : txtPassword.getText();

        if (username.isEmpty()) {
            AlertHelper.showError("Login error", "Please enter a username.");
            return;
        }

        if (password.isEmpty()) {
            AlertHelper.showError("Login error", "Please enter a password.");
            return;
        }

        // proceed to login logic
        // here

        Stage stage = (Stage) txtUsername.getScene().getWindow();
        stage.close();
    }
}
