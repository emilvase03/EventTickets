// dk/easv/eventtickets/GUI/Controllers/AssignCoordinatorController.java
package dk.easv.eventtickets.GUI.Controllers;

import dk.easv.eventtickets.BE.EventUser;
import dk.easv.eventtickets.BE.Role;
import dk.easv.eventtickets.BE.User;
import dk.easv.eventtickets.BLL.UserEventManager;
import dk.easv.eventtickets.GUI.Utils.AlertHelper;
import dk.easv.eventtickets.GUI.Utils.ViewHandler;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.util.List;

public class AssignCoordinatorController {

    @FXML private MFXComboBox<User> coordinatorDropdown;
    @FXML private MFXListView<User> assignedList;

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

        coordinatorDropdown.setItems(FXCollections.observableArrayList(allCoordinators));
        loadAssignedCoordinators();
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

            // Only show coordinators not already assigned
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

    /*@FXML
    private void handleRemove() {
        User selected = assignedList.getSelectionModel().getSelectedValues()
                .stream().findFirst().orElse(null);

        if (selected == null) {
            AlertHelper.showError("No selection", "Please select a coordinator to remove.");
            return;
        }

        try {
            userEventManager.removeCoordinator(selected.getId(), currentEventId);
            assignedList.getItems().remove(selected);
            coordinatorDropdown.getItems().add(selected);
        } catch (Exception e) {
            AlertHelper.showError("Failed to remove", e.getMessage());
        }
    }*/

    public void handleClose(ActionEvent actionEvent) {
        ViewHandler.ASSIGN_COORDINATOR.close();
    }
}