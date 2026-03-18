package dk.easv.eventtickets.GUI.Controllers;

// Project imports
import dk.easv.eventtickets.BE.User;
import dk.easv.eventtickets.GUI.Models.UserModel;
import dk.easv.eventtickets.GUI.Utils.AlertHelper;
import dk.easv.eventtickets.GUI.Utils.ViewHandler;

// MaterialFX imports
import io.github.palexdev.materialfx.controls.MFXPaginatedTableView;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;

// Java imports
import dk.easv.eventtickets.BLL.UTIL.UserSession;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminDashboardController implements Initializable {

    @FXML private MFXPaginatedTableView<User> coordContainer;
    @FXML private MFXTableColumn<User> colFirstName;
    @FXML private MFXTableColumn<User> colLastName;
    @FXML private MFXTableColumn<User> colEmail;
    @FXML private MFXTableColumn<User> colOptions;

    private final UserModel userModel = new UserModel();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (!UserSession.getInstance().isLoggedIn()) {
            Platform.runLater(() -> {
                ViewHandler.ADMIN_DASHBOARD.close();
                ViewHandler.LOGIN.show();
            });
            return;
        }

        setupTable();
        loadContainer();
        userModel.loadCoordinators();
    }

     public void loadContainer(){
            userModel.getCoordinators().addListener((ListChangeListener<User>) c ->
            Platform.runLater(() -> {
                coordContainer.setItems(null);
                coordContainer.setItems(
                        FXCollections.observableArrayList(userModel.getCoordinators())
                );
            })
    );
}

    @FXML
    private void onBtnAddCoord(ActionEvent actionEvent) {
        try {
            ViewHandler.NEW_COORDINATOR.reset();
            ViewHandler.NEW_COORDINATOR.preLoad();

            NewCoordinatorController ctrl = ViewHandler.NEW_COORDINATOR.getController();
            ctrl.init(userModel);

            ViewHandler.NEW_COORDINATOR.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            AlertHelper.showError("Error", "Failed to open NewCoordinatorView");
        }
    }


    private void setupTable() {

        coordContainer.setItems(userModel.getCoordinators());

        colFirstName.setRowCellFactory(u -> new MFXTableRowCell<>(User::getFirstName));
        colLastName.setRowCellFactory(u -> new MFXTableRowCell<>(User::getLastName));
        colEmail.setRowCellFactory(u -> new MFXTableRowCell<>(User::getEmail));

        colOptions.setRowCellFactory(item -> new MFXTableRowCell<>(u -> "") {
            final MenuButton menu;
            private User currentUser = item;
            {
                MenuItem edit   = new MenuItem("Edit");
                MenuItem delete = new MenuItem("Delete");

                edit.setOnAction(e   -> editUser(currentUser));
                delete.setOnAction(e -> deleteUser(currentUser));

                menu = new MenuButton("⋮");
                menu.getItems().addAll(edit, delete);
                setGraphic(menu);
            }

            @Override
            public void update(User user) {
                super.update(user);
                currentUser = user;
            }
        });

        var available = coordContainer.widthProperty().subtract(colOptions.widthProperty());
        colFirstName.prefWidthProperty().bind(available.multiply(0.32));
        colLastName.prefWidthProperty().bind(available.multiply(0.35));
        colEmail.prefWidthProperty().bind(available.multiply(0.27));

        setupTableFilters();
    }

    private void deleteUser(User user) {
        if (user == null) return;
        userModel.deleteUser(user);
        coordContainer.getSelectionModel().clearSelection();
    }

    private void editUser(User user) {
        if (user == null) return;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/EditCoordinatorView.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = new Stage();
            stage.setTitle("Edit Coordinator");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.showAndWait();

        } catch (IOException e) {
            AlertHelper.showError("Error", "Failed to open EditCoordinatorView");
        }
    }

    private void setupTableFilters() {
        coordContainer.getFilters().addAll();

        coordContainer.getFilters().addListener((ListChangeListener<Object>) change ->
                Platform.runLater(() -> coordContainer.setCurrentPage(1))
        );
    }

    private void closeWindow() {
        Stage stage = (Stage) coordContainer.getScene().getWindow();
        stage.close();
    }
}