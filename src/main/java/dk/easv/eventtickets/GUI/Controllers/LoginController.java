package dk.easv.eventtickets.GUI.Controllers;

// Project imports
import dk.easv.eventtickets.BE.Role;
import dk.easv.eventtickets.BE.User;
import dk.easv.eventtickets.BLL.UserSession;
import dk.easv.eventtickets.GUI.Models.UserModel;
import dk.easv.eventtickets.GUI.Utils.AlertHelper;

// MaterialFX imports
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;

// JavaFX imports
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private MFXTextField txtEmail;

    @FXML
    private MFXTextField txtPassword;

    @FXML
    private MFXButton btnLogin;

    private final UserModel userModel = new UserModel();

    @FXML
    private void initialize() {
        btnLogin.disableProperty().bind(userModel.loadingProperty());

        userModel.loginFailedProperty().addListener((obs, wasFailed, isFailed) -> {
            if (isFailed) {
                AlertHelper.showError("Login failed", "Invalid email or password.");
            }
        });

        userModel.loggedInUserProperty().addListener((obs, oldUser, newUser) -> {
            if (newUser != null) {
                UserSession.setCurrentUser(newUser);
                if (newUser.getRole() == Role.ADMIN) {
                    openDashboard("/views/AdminDashboardView.fxml");
                } else {
                    openDashboard("/views/UserDashboardView.fxml");
                }
            }
        });
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        String email = txtEmail.getText().trim();
        String password = txtPassword.getText();

        if (email.isEmpty() || password.isEmpty()) {
            AlertHelper.showError("Login error", "Please fill in all fields.");
            return;
        }

        userModel.loginUser(email, password);
    }

    private void openDashboard(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();

            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();

            closeWindow();

        } catch (Exception e) {
            AlertHelper.showError("Error", "Failed to open dashboard.");
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) txtEmail.getScene().getWindow();
        stage.close();
    }
}