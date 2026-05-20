package dk.easv.eventtickets.GUI.Controllers;

// Project imports
import dk.easv.eventtickets.BE.Event;
import dk.easv.eventtickets.BE.User;
import dk.easv.eventtickets.BLL.UTIL.UserSession;
import dk.easv.eventtickets.GUI.Models.EventModel;
import dk.easv.eventtickets.GUI.Models.UserModel;
import dk.easv.eventtickets.GUI.Utils.AlertHelper;
import dk.easv.eventtickets.GUI.Utils.ViewHandler;

// MaterialFX imports
import io.github.palexdev.materialfx.controls.MFXButton;

// JavaFX imports
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

// Java imports
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;


public class AdminDashboardController implements Initializable {

    private final UserModel userModel = new UserModel();
    private EventModel eventModel;

    // COORDINATORS

    @FXML private TableView<User>          coordinatorsContainer;
    @FXML private TableColumn<User, String> colFirstName;
    @FXML private TableColumn<User, String> colLastName;
    @FXML private TableColumn<User, String> colEmail;
    @FXML private TableColumn<User, Void>   colManageCoord;

    @FXML private MFXButton btnToggleCoordSearch;
    @FXML private TextField  tfCoordSearch;

    private FilteredList<User> filteredUsers;

    // EVENTS

    @FXML private TableView<Event>           eventsContainer;
    @FXML private TableColumn<Event, String> colTitle;
    @FXML private TableColumn<Event, String> colStartDate;
    @FXML private TableColumn<Event, String> colTickets;
    @FXML private TableColumn<Event, Void>   colManageEvents;

    @FXML private MFXButton btnToggleEventSearch;
    @FXML private TextField  tfEventSearch;

    private FilteredList<Event> filteredEvents;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        if (!UserSession.getInstance().isLoggedIn()) {
            Platform.runLater(() -> {
                ViewHandler.ADMIN_DASHBOARD.close();
                ViewHandler.LOGIN.show(false);
            });
            return;
        }

        setupCoordinatorTable();
        setupCoordinatorSearch();
        userModel.loadCoordinators();

        try {
            eventModel = new EventModel();
        } catch (Exception e) {
            AlertHelper.showError("Error", "Failed to initialize EventModel");
            return;
        }

        setupEventTable();
        setupEventSearch();
        eventModel.loadAllEvents();
    }

    // COORDINATOR TABLE

    private void setupCoordinatorTable() {

        colFirstName.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().getFirstName()));

        colLastName.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().getLastName()));

        colEmail.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().getEmail()));

        colManageCoord.setCellFactory(col -> new TableCell<>() {

            private final MFXButton btnEdit   = createIconButton("BlueEdit.png",    "Edit coordinator");
            private final MFXButton btnDelete = createIconButton("delete-128.png",  "Delete coordinator");
            private final HBox      box       = new HBox(6, btnEdit, btnDelete);

            {
                box.setAlignment(Pos.CENTER);
                btnEdit.setOnAction(e ->
                        onBtnEditUser(getSafeItem(getIndex(), coordinatorsContainer)));
                btnDelete.setOnAction(e ->
                        onBtnDeleteUser(getSafeItem(getIndex(), coordinatorsContainer)));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : box);
            }
        });
    }

    private void setupCoordinatorSearch() {
        filteredUsers = new FilteredList<>(userModel.getCoordinators(), u -> true);
        coordinatorsContainer.setItems(filteredUsers);

        //  Always filter while typing
        tfCoordSearch.textProperty().addListener((obs, o, n) -> applyCoordinatorFilter());

        tfCoordSearch.setVisible(true);
        tfCoordSearch.setManaged(true);

        btnToggleCoordSearch.setOnAction(e -> {
            tfCoordSearch.clear();
            tfCoordSearch.requestFocus();
        });
    }


    private void applyCoordinatorFilter() {
        String q = tfCoordSearch.getText().toLowerCase().trim();
        filteredUsers.setPredicate(u ->
                q.isEmpty()
                        || u.getFirstName().toLowerCase().contains(q)
                        || u.getLastName().toLowerCase().contains(q)
                        || u.getEmail().toLowerCase().contains(q)
        );
    }

    // EVENT TABLE

    private void setupEventTable() {

        colTitle.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().getTitle()));

        colStartDate.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().getStartDate().toString()));

        colTickets.setCellValueFactory(d ->
                new SimpleStringProperty(
                        d.getValue().getTicketsIssued() + " / " +
                                d.getValue().getTotalTickets()));

        colManageEvents.setCellFactory(col -> new TableCell<>() {

            private final MFXButton btnAssign = createIconButton("Assign.png",       "Assign coordinator");
            private final MFXButton btnDelete = createIconButton("delete-128.png",   "Delete event");
            private final HBox      box       = new HBox(6, btnAssign, btnDelete);

            {
                box.setAlignment(Pos.CENTER);
                btnAssign.setOnAction(e ->
                        onBtnAssignCoordinator(getSafeItem(getIndex(), eventsContainer)));
                btnDelete.setOnAction(e ->
                        onBtnDeleteEvent(getSafeItem(getIndex(), eventsContainer)));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : box);
            }
        });
    }

    private void setupEventSearch() {
        filteredEvents = new FilteredList<>(eventModel.getEvents(), e -> true);
        eventsContainer.setItems(filteredEvents);

        tfEventSearch.textProperty().addListener((obs, o, n) -> applyEventFilter());

        tfEventSearch.setVisible(true);
        tfEventSearch.setManaged(true);

        btnToggleEventSearch.setOnAction(e -> {
            tfEventSearch.clear();
            tfEventSearch.requestFocus();
        });
    }

    private void applyEventFilter() {
        String q = tfEventSearch.getText().toLowerCase().trim();
        filteredEvents.setPredicate(e ->
                q.isEmpty()
                        || e.getTitle().toLowerCase().contains(q)
                        || e.getStartDate().toString().toLowerCase().contains(q)
        );
    }


    @FXML
    private void onBtnAddCoord(ActionEvent event) {
        try {
            ViewHandler.NEW_COORDINATOR.reset();
            ViewHandler.NEW_COORDINATOR.preLoad();

            NewCoordinatorController controller =
                    ViewHandler.NEW_COORDINATOR.getController();
            controller.init(userModel);

            ViewHandler.NEW_COORDINATOR.showAndWait(false);
            applyCoordinatorFilter();

        } catch (Exception e) {
            AlertHelper.showError("Error", "Failed to open New Coordinator window");
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
        if (AlertHelper.showConfirmation("Delete", "Delete " + user.getFullName() + "?")) {
            userModel.deleteUser(user);
        }
    }

    private void onBtnAssignCoordinator(Event event) {
        if (event == null) return;
        try {
            ViewHandler.ASSIGN_COORDINATOR.reset();
            ViewHandler.ASSIGN_COORDINATOR.preLoad();

            AssignCoordinatorController ctrl =
                    ViewHandler.ASSIGN_COORDINATOR.getController();
            ctrl.setup(userModel.getCoordinators(), event.getId());

            ViewHandler.ASSIGN_COORDINATOR.showAndWait(false);
        } catch (Exception e) {
            AlertHelper.showError("Error", "Failed to open Assign Coordinator window");
        }
    }

    private void onBtnDeleteEvent(Event event) {
        if (event == null) return;
        if (AlertHelper.showConfirmation(
                "Delete Event",
                "Delete event \"" + event.getTitle() + "\"?")) {
            eventModel.deleteEvent(event);
        }
    }

    @FXML
    private void onBtnLogOut(ActionEvent e) {
        UserSession.getInstance().clear();
        ViewHandler.ADMIN_DASHBOARD.close();
        ViewHandler.LOGIN.show(false);
    }

    private <T> T getSafeItem(int index, TableView<T> table) {
        if (index < 0 || index >= table.getItems().size()) return null;
        return table.getItems().get(index);
    }

    private MFXButton createIconButton(String file, String tooltip) {
        MFXButton btn = new MFXButton("");
        Image img = new Image(
                Objects.requireNonNull(
                        getClass().getResourceAsStream("/icons/" + file)));
        ImageView iv = new ImageView(img);
        iv.setFitWidth(18);
        iv.setFitHeight(18);
        btn.setGraphic(iv);
        btn.setTooltip(new Tooltip(tooltip));
        return btn;
    }
}