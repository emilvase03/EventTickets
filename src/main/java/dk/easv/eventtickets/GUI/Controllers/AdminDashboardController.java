package dk.easv.eventtickets.GUI.Controllers;

// Project imports
import dk.easv.eventtickets.BE.User;
import dk.easv.eventtickets.GUI.Models.UserModel;
import dk.easv.eventtickets.GUI.Utils.AlertHelper;
import dk.easv.eventtickets.GUI.Utils.ViewHandler;
import dk.easv.eventtickets.BLL.UTIL.UserSession;

// MaterialFX imports
import io.github.palexdev.materialfx.controls.MFXPaginatedTableView;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;

// Java imports
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import java.net.URL;
import java.util.Comparator;
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
                ViewHandler.LOGIN.show(false);
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

                coordContainer.requestLayout();// rollout for Material
                coordContainer.setCurrentPage(1);
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

            ViewHandler.NEW_COORDINATOR.showAndWait(false);

        } catch (Exception e) {
            e.printStackTrace();
            AlertHelper.showError("Error", "Failed to open NewCoordinatorView");
        }
    }

    private void setupTable() {
        // Instantiate columns here instead of via FXML injection
        colFirstName = new MFXTableColumn<>("First name", true, Comparator.comparing(User::getFirstName));
        colLastName  = new MFXTableColumn<>("Last name",  true, Comparator.comparing(User::getLastName));
        colEmail     = new MFXTableColumn<>("Email",      true, Comparator.comparing(User::getEmail));
        colOptions   = new MFXTableColumn<>("Manage");

        colOptions.setPrefWidth(80);
        colOptions.setMinWidth(40);
        colOptions.setMaxWidth(80);

        coordContainer.getTableColumns().addAll(colFirstName, colLastName, colEmail, colOptions);
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

        boolean confirmed = AlertHelper.showConfirmation(
                "Delete Coordinator",
                "Are you sure you want to delete the coordinator \"" + user.getFullName() + "\"?"
        );

        if (!confirmed) return;

        userModel.deleteUser(user);
        coordContainer.getSelectionModel().clearSelection();
    }

    private void editUser(User user) {
        if (user == null) return;
        try {
            ViewHandler.EDIT_COORDINATOR.reset();
            ViewHandler.EDIT_COORDINATOR.preLoad();

            EditCoordController ctrl = ViewHandler.EDIT_COORDINATOR.getController();
            ctrl.init(userModel, user);

            ViewHandler.EDIT_COORDINATOR.showAndWait(false);
        } catch (Exception e) {

            AlertHelper.showError("Error", "Failed to open EditCoordinatorView");
        }
    }

    private void setupTableFilters() {
        coordContainer.getFilters().addAll();

        coordContainer.getFilters().addListener((ListChangeListener<Object>) change ->
                Platform.runLater(() -> coordContainer.setCurrentPage(1))
        );
    }

    @FXML
    private void onBtnLogOut(ActionEvent actionEvent) {
        boolean confirmed = AlertHelper.showConfirmation(
                "Log out",
                "Are you sure you want to log out?"
        );

        if (!confirmed)
            return;

        UserSession.getInstance().clear();
        ViewHandler.ADMIN_DASHBOARD.close();
        ViewHandler.LOGIN.show(false);
    }
}