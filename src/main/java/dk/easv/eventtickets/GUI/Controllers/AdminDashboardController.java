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
import io.github.palexdev.materialfx.controls.MFXPopup;

// javafx imports
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

//Java imports
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;


public class AdminDashboardController implements Initializable {

    private final UserModel userModel = new UserModel();
    private EventModel eventModel;

    //COORDINATORS

    @FXML private TableView<User> coordinatorsContainer;
    @FXML private TableColumn<User, String> colFirstName;
    @FXML private TableColumn<User, String> colLastName;
    @FXML private TableColumn<User, String> colEmail;
    @FXML private TableColumn<User, Void> colManageCoord;

    @FXML private MFXButton btnOpenCoordFilters;
    @FXML private MFXButton btnClearCoordFilters;

    private FilteredList<User> filteredUsers;
    private MFXPopup coordinatorFilterPopup;
    private TextField tfFilterFirst;
    private TextField tfFilterLast;
    private TextField tfFilterEmail;

  //EVENTS

    @FXML private TableView<Event> eventsContainer;
    @FXML private TableColumn<Event, String> colTitle;
    @FXML private TableColumn<Event, String> colStartDate;
    @FXML private TableColumn<Event, String> colTickets;
    @FXML private TableColumn<Event, Void> colManageEvents;

    @FXML private MFXButton btnOpenEventFilters;
    @FXML private MFXButton btnClearEventFilters;

    private FilteredList<Event> filteredEvents;
    private MFXPopup eventFilterPopup;
    private TextField tfEventTitle;
    private TextField tfEventStartDate;

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
        setupCoordinatorFilterPopup();
        setupCoordinatorFiltering();
        userModel.loadCoordinators();

        try {
            eventModel = new EventModel();
        } catch (Exception e) {
            AlertHelper.showError("Error", "Failed to initialize EventModel");
            return;
        }

        setupEventTable();
        setupEventFilterPopup();
        setupEventFiltering();
        eventModel.loadAllEvents();
    }

    // try to move to fxml again

    private void setupCoordinatorTable() {

        colFirstName.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().getFirstName()));

        colLastName.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().getLastName()));

        colEmail.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().getEmail()));

        colManageCoord.setCellFactory(col -> new TableCell<>() {

            private final MFXButton btnEdit =
                    createIconButton("BlueEdit.png", "Edit coordinator");
            private final MFXButton btnDelete =
                    createIconButton("delete-128.png", "Delete coordinator");

            private final HBox box = new HBox(6, btnEdit, btnDelete);

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
    private void setupCoordinatorFilterPopup() {

        tfFilterFirst = new TextField();
        tfFilterFirst.setPromptText("First name");

        tfFilterLast = new TextField();
        tfFilterLast.setPromptText("Last name");

        tfFilterEmail = new TextField();
        tfFilterEmail.setPromptText("Email");

        MFXButton popupClearBtn = new MFXButton("Clear filters");
        popupClearBtn.getStyleClass().add("text-button");

        popupClearBtn.setOnAction(e -> {
            clearCoordinatorFilters();
            coordinatorFilterPopup.hide();
        });

        VBox pane = new VBox(10,
                new Label("Filter by"),
                tfFilterFirst,
                tfFilterLast,
                tfFilterEmail,
                popupClearBtn
        );
        pane.getStyleClass().add("filter-pane");

        coordinatorFilterPopup = new MFXPopup(pane);

        btnOpenCoordFilters.setOnAction(e -> {
            coordinatorFilterPopup.show(coordinatorsContainer);
            Platform.runLater(() -> {

                double popupWidth = pane.getBoundsInLocal().getWidth();
                double tableWidth = coordinatorsContainer.getBoundsInLocal().getWidth();

                pane.setTranslateX((tableWidth - popupWidth) / 2);
                pane.setTranslateY(-pane.getBoundsInLocal().getHeight() - 12);
            });
        });
        btnClearCoordFilters.setOnAction(e -> clearCoordinatorFilters());
    }

    private void setupCoordinatorFiltering() {

        filteredUsers = new FilteredList<>(
                userModel.getCoordinators(), u -> true);

        coordinatorsContainer.setItems(filteredUsers);

        ChangeListener<String> listener =
                (obs, o, n) -> applyCoordinatorFilter();

        tfFilterFirst.textProperty().addListener(listener);
        tfFilterLast.textProperty().addListener(listener);
        tfFilterEmail.textProperty().addListener(listener);
    }

    private void applyCoordinatorFilter() {

        String first = tfFilterFirst.getText().toLowerCase();
        String last  = tfFilterLast.getText().toLowerCase();
        String email = tfFilterEmail.getText().toLowerCase();

        filteredUsers.setPredicate(u ->
                (first.isEmpty() || u.getFirstName().toLowerCase().contains(first)) &&
                        (last.isEmpty()  || u.getLastName().toLowerCase().contains(last)) &&
                        (email.isEmpty() || u.getEmail().toLowerCase().contains(email))
        );
    }

    private void clearCoordinatorFilters() {
        tfFilterFirst.clear();
        tfFilterLast.clear();
        tfFilterEmail.clear();
    }

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

            private final MFXButton btnAssign =
                    createIconButton("Assign.png", "Assign coordinator");
            private final MFXButton btnDelete =
                    createIconButton("delete-128.png", "Delete event");

            private final HBox box = new HBox(6, btnAssign, btnDelete);

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

    private void setupEventFilterPopup() {

        tfEventTitle = new TextField();
        tfEventTitle.setPromptText("Event title");

        tfEventStartDate = new TextField();
        tfEventStartDate.setPromptText("Start date");
        MFXButton popupClearBtn = new MFXButton("Clear filters");
        popupClearBtn.getStyleClass().add("text-button");

        popupClearBtn.setOnAction(e -> {
            clearEventFilters();
            eventFilterPopup.hide();
        });

        VBox pane = new VBox(10,
                new Label("Filter by"),
                tfEventTitle,
                tfEventStartDate,
                popupClearBtn
        );
        pane.getStyleClass().add("filter-pane");

        eventFilterPopup = new MFXPopup(pane);
        eventFilterPopup.setAutoHide(true);
        btnOpenEventFilters.setOnAction(e -> {
            eventFilterPopup.show(eventsContainer);
            Platform.runLater(() -> {

                double popupWidth = pane.getBoundsInLocal().getWidth();
                double popupHeight = pane.getBoundsInLocal().getHeight();
                double tableWidth = eventsContainer.getBoundsInLocal().getWidth();
                pane.setTranslateX((tableWidth - popupWidth) / 2);
                pane.setTranslateY(-popupHeight - 12);
            });
        });
        btnClearEventFilters.setOnAction(e -> {
            clearEventFilters();
            eventFilterPopup.hide();
        });
    }

    private void setupEventFiltering() {

        filteredEvents = new FilteredList<>(
                eventModel.getEvents(), e -> true);

        eventsContainer.setItems(filteredEvents);

        ChangeListener<String> listener =
                (obs, o, n) -> applyEventFilter();

        tfEventTitle.textProperty().addListener(listener);
        tfEventStartDate.textProperty().addListener(listener);

    }

    private void applyEventFilter() {

        String title = tfEventTitle.getText().toLowerCase();
        String date  = tfEventStartDate.getText().toLowerCase();

        filteredEvents.setPredicate(e ->
                (title.isEmpty() || e.getTitle().toLowerCase().contains(title)) &&
                        (date.isEmpty()  || e.getStartDate().toString().contains(date))
        );
    }

    private void clearEventFilters() {
        tfEventTitle.clear();
        tfEventStartDate.clear();
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

            AssignCoordinatorController ctrl = ViewHandler.ASSIGN_COORDINATOR.getController();
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

    //getIndex() is not always valid when a button inside a cell is clicked.prevent crashes

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