package dk.easv.eventtickets.GUI.Controllers;

import dk.easv.eventtickets.BE.EventUser;
import dk.easv.eventtickets.BE.Role;
import dk.easv.eventtickets.BE.User;
import dk.easv.eventtickets.BLL.UTIL.UserSession;
import dk.easv.eventtickets.BLL.UserEventManager;
import dk.easv.eventtickets.GUI.Utils.AlertHelper;
import dk.easv.eventtickets.GUI.Utils.ViewHandler;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;

import java.util.List;

public class AssignCoordinatorController {

    @FXML private MFXComboBox<User> coordinatorDropdown;
    @FXML private ListView<User> assignedList;
    @FXML private MFXButton assignButton;
    @FXML private MFXButton closeButton;

    private UserEventManager userEventManager;
    private int currentEventId;
    private List<User> allCoordinators;

    @FXML
    public void initialize() {
        try {
            userEventManager = new UserEventManager();
        } catch (Exception e) {
            AlertHelper.showError("Failed to initialize", e.getMessage());
        }
    }

    public void setup(List<User> coordinators, int eventId) {
        this.currentEventId = eventId;
        this.allCoordinators = coordinators.stream()
                .filter(u -> u.getRole() == Role.EVENT)
                .toList();

        setupAssignedListCellFactory();
        coordinatorDropdown.setItems(FXCollections.observableArrayList(allCoordinators));
        loadAssignedCoordinators();
    }

    private void setupAssignedListCellFactory() {
        assignedList.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    HBox row;

                    if (isEventCoordinator()) {
                        MFXButton btnRemove = new MFXButton("✕");
                        btnRemove.getStyleClass().add("remove-button");
                        btnRemove.setPrefSize(24, 24);
                        btnRemove.setOnAction(e -> handleRemove(item));

                        row = new HBox(8, btnRemove, new Label(item.getFullName()));
                    } else {
                        row = new HBox(8, new Label(item.getFullName()));
                    }

                    row.setAlignment(Pos.CENTER_LEFT);
                    setGraphic(row);
                    setText(null);
                }
            }
        });
    }

    private void loadAssignedCoordinators() {
        try {
            List<EventUser> assigned = userEventManager.getCoordinatorsForEvent(currentEventId);
            ObservableList<User> assignedUsers = FXCollections.observableArrayList();

            for (EventUser ue : assigned) {
                allCoordinators.stream()
                        .filter(u -> u.getId() == ue.getUserId())
                        .findFirst()
                        .ifPresent(assignedUsers::add);
            }

            assignedList.setItems(assignedUsers);

            ObservableList<User> available = allCoordinators.stream()
                    .filter(u -> assignedUsers.stream().noneMatch(a -> a.getId() == u.getId()))
                    .collect(FXCollections::observableArrayList, ObservableList::add, ObservableList::addAll);

            coordinatorDropdown.setItems(available);

        } catch (Exception e) {
            AlertHelper.showError("Failed to load coordinators", e.getMessage());
        }
    }

    @FXML
    private void handleAssign() {
        User selected = coordinatorDropdown.getValue();
        if (selected == null) {
            AlertHelper.showError("No selection", "Please select a coordinator to assign.");
            return;
        }

        try {
            userEventManager.assignCoordinator(selected.getId(), currentEventId);
            assignedList.getItems().add(selected);
            coordinatorDropdown.getItems().remove(selected);
            coordinatorDropdown.setValue(null);
        } catch (Exception e) {
            AlertHelper.showError("Failed to assign", e.getMessage());
        }
    }

    @FXML
    private void handleRemove(User user) {
        if (user == null) return;

        try {
            userEventManager.removeCoordinator(user.getId(), currentEventId);
            assignedList.getItems().remove(user);
            coordinatorDropdown.getItems().add(user);
        } catch (Exception e) {
            AlertHelper.showError("Failed to remove", e.getMessage());
        }
    }

    @FXML
    public void handleClose(ActionEvent actionEvent) {
        ViewHandler.ASSIGN_COORDINATOR.close();
    }

    private boolean isEventCoordinator() {
        return UserSession.getInstance().getCurrentUser().getRole() == Role.EVENT;
    }
}