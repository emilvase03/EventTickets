package dk.easv.eventtickets.GUI.Controllers;

// Project imports
import dk.easv.eventtickets.BE.Event;
import dk.easv.eventtickets.BE.User;
import dk.easv.eventtickets.BLL.UTIL.UserSession;
import dk.easv.eventtickets.GUI.Models.EventModel;
import dk.easv.eventtickets.GUI.Models.UserModel;
import dk.easv.eventtickets.GUI.Utils.AlertHelper;

// MaterialFX imports
import io.github.palexdev.materialfx.controls.MFXScrollPane;

// Java imports
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class CoordDashboardController {

    private static CoordDashboardController instance;
    private User currentUser;
    private UserModel userModel = new UserModel();
    private EventModel eventModel;

    @FXML private MFXScrollPane scrollPane;
    @FXML private TilePane eventContainer;

    private FXMLLoader newEventLoader;

    @FXML
    private void initialize() {
        instance = this;
        currentUser = UserSession.getInstance().getCurrentUser();
        try {
            eventModel = new EventModel();
        } catch (Exception e) {
            AlertHelper.showError("Error", "EventModel failed in CoordDashboardController.");
        }

        setupEventPool(currentUser);

        // Preload once when dashboard opens, not when button is clicked
        newEventLoader = new FXMLLoader(getClass().getResource("/views/NewEventView.fxml"));
        try {
            newEventLoader.load();
        } catch (IOException e) {
            AlertHelper.showError("Error", "Failed to pre-load NewEventView");
        }
    }

    @FXML
    private void onCreateEvent(ActionEvent event) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/NewEventView.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();

            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            AlertHelper.showError("Error", "Unable to open NewEventView");
            throw new RuntimeException(e);
        }

    }

    @FXML
    private void onCreateTicket(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/NewTicketView.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 405, 320);
            Stage stage = new Stage();

            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            AlertHelper.showError("Error", "Unable to open SpecialTicketView");
            throw new RuntimeException(e);
        }
    }

    private void setupEventPool(User currentUser) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/components/Card.fxml"));
            VBox root = loader.load();
            CardController cardController = loader.getController();

            for (Event e : eventModel.getMyEvents(currentUser)) {
                getEventContainer().getChildren().add(cardController.createEventCard(e));
            }

        } catch (IOException e) {
            AlertHelper.showError("Error","Failed to get CardController.");
        } catch (Exception e) {
            AlertHelper.showError("Error","Failed to get my events from event model.");
            System.out.println(e.getMessage());
        }
    }

    public void launchTicketsWindow(int ticketsAmount, boolean specialTicket) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/PrintTicketsView.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();


            String ticketPath;
            if (!specialTicket) {
                ticketPath = "/components/NormalTicket.fxml";
            } else {
                ticketPath = "/components/SpecialTicket.fxml";
            }

            for (int i = 0; i < ticketsAmount; i++) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(ticketPath));
                Parent ticketRoot = loader.load();
                PrintTicketController controller = fxmlLoader.getController();
                controller.getTicketsContainer().getChildren().add(ticketRoot);
            }
        } catch (Exception e) {
            AlertHelper.showError("Error", "Failed to display tickets." + e.getMessage());
        }

    }

    public TilePane getEventContainer() {
        return eventContainer;
    }

    public static CoordDashboardController getInstance() {
        return instance;
    }

}
