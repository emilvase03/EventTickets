package dk.easv.eventtickets.GUI.Controllers;

// Project imports
import dk.easv.eventtickets.BE.Role;
import dk.easv.eventtickets.BE.User;
import dk.easv.eventtickets.BLL.UserSession;
import dk.easv.eventtickets.GUI.Models.UserModel;
import dk.easv.eventtickets.GUI.Utils.AlertHelper;

// MaterialFX imports
import io.github.palexdev.materialfx.controls.MFXTextField;

// JavaFX imports
import javafx.concurrent.Task;
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

    private final UserModel userModel;

    public LoginController() {
        userModel = new UserModel();
    }

    @FXML
    private void handleLogin(ActionEvent event) {

        String email = txtEmail.getText().trim();
        String password = txtPassword.getText();

        if (email.isEmpty() || password.isEmpty()) {
            AlertHelper.showError("Login error", "Please fill in all fields.");
            return;
        }

        Task<User> loginTask = new Task<>() {
            @Override
            protected User call() throws Exception {
                return userModel.loginUser(email, password);
            }
        };

        loginTask.setOnSucceeded(workerStateEvent -> {
            User user = loginTask.getValue();

            if (user == null) {
                AlertHelper.showError("Login failed", "Invalid email or password.");
                return;
            }

            UserSession.setCurrentUser(user);

            if (user.getRole() == Role.ADMIN) {
                openDashboard("/views/AdminDashboardView.fxml");
            } else {
                openDashboard("/views/UserDashboardView.fxml");
            }
        });

        loginTask.setOnFailed(workerStateEvent -> {
            AlertHelper.showError("Error", "Login failed due to system error.");
        });

        Thread thread = new Thread(loginTask);
        thread.setDaemon(true);
        thread.start();
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