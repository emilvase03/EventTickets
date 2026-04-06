package dk.easv.eventtickets.GUI.Controllers;

// Project imports
import dk.easv.eventtickets.BE.Event;
import dk.easv.eventtickets.BE.User;
import dk.easv.eventtickets.GUI.Models.EventModel;
import dk.easv.eventtickets.GUI.Models.UserModel;
import dk.easv.eventtickets.GUI.Utils.AlertHelper;
import dk.easv.eventtickets.GUI.Utils.ViewHandler;
import dk.easv.eventtickets.BLL.UTIL.UserSession;

// MaterialFX imports
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPaginatedTableView;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.StringFilter;
import io.github.palexdev.materialfx.filter.base.AbstractFilter;
import javafx.collections.ListChangeListener;

// Java imports
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Objects;
import java.util.ResourceBundle;

public class AdminDashboardController implements Initializable {

    //Coordinators tab
    @FXML private MFXPaginatedTableView<User> coordContainer;
    @FXML private MFXTableColumn<User>        colFirstName;
    @FXML private MFXTableColumn<User>        colLastName;
    @FXML private MFXTableColumn<User>        colEmail;
    @FXML private MFXTableColumn<User>        colOptions;

    //  Events tab
    @FXML private MFXPaginatedTableView<Event> eventsContainer;
    @FXML private MFXTableColumn<Event>        colTitle;
    @FXML private MFXTableColumn<Event>        colStartDate;
    @FXML private MFXTableColumn<Event>        colTickets;
    @FXML private MFXTableColumn<Event>        colEventOptions;

    private final UserModel userModel = new UserModel();
    private EventModel eventModel;

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
        loadCoordinatorContainer();
        userModel.loadCoordinators();


        try {
            eventModel = new EventModel();
        } catch (Exception e) {
            AlertHelper.showError("Error", "Failed to initialize EventModel.");
            return;
        }

        setupEventsTable();
        eventsContainer.setItems(eventModel.getEvents());
        loadEventContainer();
        eventModel.loadAllEvents();
    }

    /**
     *Coordinators tab
     */

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

    private void setupCoordinatorsTable() {
        coordContainer.setItems(userModel.getCoordinators());
        createCoordinatorColumns();
        setupCoordinatorCellFactories();
        bindCoordinatorColumnWidths();
        setupCoordinatorTableFilters();
    }

    public void loadCoordinatorContainer() {
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
    private void setupCoordinatorTableFilters() {

        coordContainer.getFilters().addAll(
                new StringFilter<>("First name", User::getFirstName),
                new StringFilter<>("Last name", User::getLastName),
                new StringFilter<>("Email", User::getEmail)
        );

        coordContainer.getFilters().addListener(
                (ListChangeListener<? super AbstractFilter<User, ?>>) change ->
                        Platform.runLater(() -> coordContainer.setCurrentPage(1))
        );
    }


    private void createCoordinatorColumns() {
        colFirstName.setComparator(Comparator.comparing(User::getFirstName));
        colLastName.setComparator(Comparator.comparing(User::getLastName));
        colEmail.setComparator(Comparator.comparing(User::getEmail));

        colOptions.setPrefWidth(80);
        colOptions.setMinWidth(80);
        colOptions.setMaxWidth(80);
    }

    private void setupCoordinatorCellFactories() {
        colFirstName.setRowCellFactory(u -> new MFXTableRowCell<>(User::getFirstName));
        colLastName.setRowCellFactory(u -> new MFXTableRowCell<>(User::getLastName));
        colEmail.setRowCellFactory(u -> new MFXTableRowCell<>(User::getEmail));

        colOptions.setRowCellFactory(item -> new MFXTableRowCell<>(u -> "") {

            private User currentUser = item;

            final MFXButton btnEdit   = createIconButton("BlueEdit.png",    "Edit ");
            final MFXButton btnDelete = createIconButton("delete-128.png",  "Delete ");
            final HBox box;

            {
                btnEdit.setOnAction(e -> onBtnEditUser(currentUser));
                btnDelete.setOnAction(e -> onBtnDeleteUser(currentUser));

                box = new HBox(6, btnEdit, btnDelete);
                box.setAlignment(Pos.CENTER);
                box.setPadding(new Insets(0, 4, 0, 4));
                setGraphic(box);
            }

            @Override
            public void update(User user) {
                super.update(user);
                currentUser = user;
            }
        });
    }

    private void bindCoordinatorColumnWidths() {
        var available = coordContainer.widthProperty()
                .subtract(colOptions.widthProperty());

        colFirstName.prefWidthProperty().bind(available.multiply(0.33));
        colLastName.prefWidthProperty().bind(available.multiply(0.30));
        colEmail.prefWidthProperty().bind(available.multiply(0.30));
    }

    /**
     * Evets tab
     */


    private void onBtnDeleteEvent(Event event) {
        if (event == null) return;

        boolean confirmed = AlertHelper.showConfirmation(
                "Delete Event",
                "Are you sure you want to delete the event \"" + event.getTitle() + "\"?"
        );


        if (!confirmed) return;

        try {
            eventModel.deleteEvent(event);
            eventsContainer.getSelectionModel().clearSelection();
        } catch (Exception e) {
            AlertHelper.showError("Error", "Failed to delete event.");
            e.printStackTrace();
        }

    }

    private void onBtnAssignCoordinator(Event currentEvent) {

    }


    private void setupEventsTable() {
        createEventColumns();
        setupEventCellFactories();
        bindEventColumnWidths();
        setupEventsTableFilters();
    }

    public void loadEventContainer() {
        eventModel.getEvents().addListener((ListChangeListener<Event>) c ->
                Platform.runLater(() -> {
                    eventsContainer.setItems(null);
                    eventsContainer.setItems(
                            FXCollections.observableArrayList(eventModel.getEvents())
                    );
                    eventsContainer.requestLayout();
                    eventsContainer.setCurrentPage(1);
                })
        );
    }

    private void createEventColumns() {
        colTitle.setComparator(Comparator.comparing(Event::getTitle));
        colStartDate.setComparator(Comparator.comparing(Event::getStartDate));

        colEventOptions.setPrefWidth(80);
        colEventOptions.setMinWidth(80);
        colEventOptions.setMaxWidth(80);
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

        colEventOptions.setRowCellFactory(item -> new MFXTableRowCell<Event, String>(event -> "") {

            private Event currentEvent;

            final MFXButton btnAssign      = createIconButton("Assign.png",    "Assign coordinator");
            final MFXButton btnDeleteEvent = createIconButton("delete-128.png",  "Delete event");
            final HBox box;

            {
                btnAssign.setOnAction(ev -> onBtnAssignCoordinator(currentEvent));
                btnDeleteEvent.setOnAction(ev -> onBtnDeleteEvent(currentEvent));

                box = new HBox(6, btnAssign, btnDeleteEvent);
                box.setAlignment(Pos.CENTER);
                box.setPadding(new Insets(0, 4, 0, 4));
                setGraphic(box);
            }

            @Override
            public void update(Event event) {
                super.update(event);
                currentEvent = event;
            }
        });
    }

    private void bindEventColumnWidths() {
        var available = eventsContainer.widthProperty()
                .subtract(colEventOptions.widthProperty());

        colTitle.prefWidthProperty().bind(available.multiply(0.33));
        colStartDate.prefWidthProperty().bind(available.multiply(0.30));
        colTickets.prefWidthProperty().bind(available.multiply(0.30));
    }

    private void setupEventsTableFilters() {

        eventsContainer.getFilters().addAll(
                new StringFilter<>("Title", Event::getTitle),
                new StringFilter<>("Starts", e -> e.getStartDate().toString()),
                new StringFilter<>("Tickets", e -> String.valueOf(e.getTicketsIssued()))
        );
    }

    @FXML
    private void onBtnLogOut(ActionEvent actionEvent) {
        boolean confirmed = AlertHelper.showConfirmation(
                "Log out",
                "Are you sure you want to log out?"
        );

        if (!confirmed) return;

        UserSession.getInstance().clear();
        ViewHandler.ADMIN_DASHBOARD.close();
        ViewHandler.LOGIN.show(false);
    }

    private MFXButton createIconButton(String fileName,  String tooltip) {
        MFXButton btn = new MFXButton("");

        try {
            Image image = new Image(
                    Objects.requireNonNull(getClass().getResourceAsStream("/icons/" + fileName))
            );
            ImageView icon = new ImageView(image);
            icon.setFitWidth(18);
            icon.setFitHeight(18);
            btn.setGraphic(icon);
        } catch (Exception e) {
            System.err.println("⚠ Icon not found: " + fileName);
            btn.setText("?");
        }

        btn.setTooltip(new Tooltip(tooltip));
        return btn;
    }
}