package dk.easv.eventtickets.GUI.Controllers;

// Project imports
import dk.easv.eventtickets.GUI.Utils.AlertHelper;

// MaterialFX imports
import io.github.palexdev.materialfx.controls.MFXTextField;

// Java imports
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
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

        openCoordDashboard();

    }

    private void openCoordDashboard() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/CoordDashboardView.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();

            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();

            handleClose();
        } catch (Exception e) {
            AlertHelper.showError("Error", "Failed to open XYZView"); // <- Edit this error message!
        }
    }

    private void handleClose() {
        Stage stage = (Stage) txtUsername.getScene().getWindow();
        stage.close();
    }
}
