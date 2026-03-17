package dk.easv.eventtickets.GUI.Controllers;

// Project imports
import dk.easv.eventtickets.BE.Role;
import dk.easv.eventtickets.BLL.UTIL.UserSession;
import dk.easv.eventtickets.GUI.Models.UserModel;
import dk.easv.eventtickets.GUI.Utils.AlertHelper;

import static dk.easv.eventtickets.GUI.Utils.ViewHandler.*;

// MaterialFX imports
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;

// JavaFX imports
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;


public class LoginController {

    private final UserModel userModel = new UserModel();

    @FXML private MFXTextField txtEmail;
    @FXML private MFXTextField txtPassword;
    @FXML private MFXButton btnLogin;

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
                UserSession.getInstance().setCurrentUser(newUser);
                if (newUser.getRole() == Role.ADMIN) {
                    ADMIN_DASHBOARD.show();
                } else {
                    COORD_DASHBOARD.show();
                }
                closeWindow();
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

    private void closeWindow() {
        Stage stage = (Stage) txtEmail.getScene().getWindow();
        stage.close();
    }
}