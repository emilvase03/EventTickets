package dk.easv.eventtickets.GUI.Controllers;

// Project imports
import dk.easv.eventtickets.BE.Event;
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
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.ResourceBundle;

public class AdminDashboardController implements Initializable {

    //Cordinators tab
    @FXML private MFXPaginatedTableView<User> coordContainer;
    @FXML private MFXTableColumn<User> colFirstName;
    @FXML private MFXTableColumn<User> colLastName;
    @FXML private MFXTableColumn<User> colEmail;
    @FXML private MFXTableColumn<User> colOptions;

    //Events tab
    @FXML private MFXPaginatedTableView<Event> eventsContainer;
    private MFXTableColumn<Event> colTitle;
    private MFXTableColumn<Event> colStartDate;
    private MFXTableColumn<Event> colTickets;

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

        setupCoordinatorsTable();
        setupEventsTable();
        loadContainer();
        userModel.loadCoordinators();

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

    private void onBtnEditUser(User user) {
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

    private void onBtnDeleteUser(User user) {
        if (user == null) return;

        boolean confirmed = AlertHelper.showConfirmation(
                "Delete Coordinator",
                "Are you sure you want to delete the coordinator \"" + user.getFullName() + "\"?"
        );

        if (!confirmed) return;

        userModel.deleteUser(user);
        coordContainer.getSelectionModel().clearSelection();
    }

    /**
     * Set up for Coordinators tab
     * */

    private void setupCoordinatorsTable() {
        createCoordinatorColumns();
        addCoordinatorColumnsToTable();
        setupCoordinatorCellFactories();
        bindCoordinatorColumnWidths();
        setupTableFilters();
    }

    public void loadContainer(){
        userModel.getCoordinators().addListener((ListChangeListener<User>) c ->
                Platform.runLater(() -> {
                    coordContainer.setItems(null);
                    coordContainer.setItems(
                            FXCollections.observableArrayList(userModel.getCoordinators())
                    );

                    coordContainer.requestLayout();
                    coordContainer.setCurrentPage(1);
                })
        );
    }

    private void createCoordinatorColumns() {

        colFirstName = new MFXTableColumn<>("First name", true, Comparator.comparing(User::getFirstName));
        colLastName  = new MFXTableColumn<>("Last name",  true, Comparator.comparing(User::getLastName));
        colEmail     = new MFXTableColumn<>("Email",      true, Comparator.comparing(User::getEmail));
        colOptions   = new MFXTableColumn<>("Manage");

        colOptions.setPrefWidth(80);
        colOptions.setMinWidth(40);
        colOptions.setMaxWidth(80);
    }

    private void addCoordinatorColumnsToTable() {
        coordContainer.getTableColumns().addAll(
                colFirstName,
                colLastName,
                colEmail,
                colOptions
        );
        coordContainer.setItems(userModel.getCoordinators());
    }
       //move to fxml later
    private void setupCoordinatorCellFactories() {

        colFirstName.setRowCellFactory(u -> new MFXTableRowCell<>(User::getFirstName));
        colLastName.setRowCellFactory(u -> new MFXTableRowCell<>(User::getLastName));
        colEmail.setRowCellFactory(u -> new MFXTableRowCell<>(User::getEmail));

        colOptions.setRowCellFactory(item -> new MFXTableRowCell<>(u -> "") {

            final MenuButton menu;
            private User currentUser = item;

            {
                MenuItem edit   = new MenuItem("Edit");
                MenuItem delete = new MenuItem("Delete");

                edit.setOnAction(e   -> onBtnEditUser(currentUser));
                delete.setOnAction(e -> onBtnDeleteUser(currentUser));

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
    }

    private void bindCoordinatorColumnWidths() {

        var available = coordContainer
                .widthProperty()
                .subtract(colOptions.widthProperty());

        colFirstName.prefWidthProperty().bind(available.multiply(0.32));
        colLastName.prefWidthProperty().bind(available.multiply(0.35));
        colEmail.prefWidthProperty().bind(available.multiply(0.27));
    }

    /**
     * Set up for Events tab
     * */


    private void setupEventsTable() {
        createEventColumns();
        addEventColumnsToTable();
        setupEventCellFactories();
        bindEventColumnWidths();
        setupEventsTableFilters();
    }


    private void createEventColumns() {
        colTitle     = new MFXTableColumn<>("Title", true, Comparator.comparing(Event::getTitle));
        colStartDate = new MFXTableColumn<>("Start Date", true, Comparator.comparing(Event::getStartDate));
        colTickets   = new MFXTableColumn<>("Tickets Issued");

    }

    private void addEventColumnsToTable() {
        eventsContainer.getTableColumns().addAll(
                colTitle,
                colStartDate,
                colTickets
        );
    }


    private void setupEventCellFactories() {

        colTitle.setRowCellFactory(e ->
                new MFXTableRowCell<>(Event::getTitle)
        );

        colStartDate.setRowCellFactory(e ->
                new MFXTableRowCell<>(event ->
                        event.getStartDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                )
        );


        colTickets.setRowCellFactory(e ->
                new MFXTableRowCell<>(event ->
                        event.getTicketsIssued() + " / " + event.getTotalTickets()
                )
        );
    }

    private void bindEventColumnWidths() {

        var available = eventsContainer.widthProperty();

        colTitle.prefWidthProperty().bind(available.multiply(0.33));
        colStartDate.prefWidthProperty().bind(available.multiply(0.33));
        colTickets.prefWidthProperty().bind(available.multiply(0.34));
    }


    private void setupEventsTableFilters() {
        eventsContainer.getFilters().addAll();

        eventsContainer.getFilters().addListener((ListChangeListener<Object>) change ->
                Platform.runLater(() -> eventsContainer.setCurrentPage(1))
        );
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