package dk.easv.eventtickets.GUI.Controllers;

// Project imports
import dk.easv.eventtickets.BE.Event;
import dk.easv.eventtickets.BE.User;
import dk.easv.eventtickets.BLL.UTIL.UserSession;
import dk.easv.eventtickets.GUI.Models.EventModel;
import dk.easv.eventtickets.GUI.Utils.AlertHelper;
import dk.easv.eventtickets.GUI.Utils.ViewHandler;

// MaterialFX imports
import dk.easv.eventtickets.GUI.Utils.Validator;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.mfxcore.controls.Label;

// Java imports
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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
    private EventModel eventModel;
    private FXMLLoader newEventLoader;

    @FXML private MFXScrollPane scrollPane;
    @FXML private TilePane eventContainer;
    @FXML private Label lblNumOfEvents;
    @FXML private Label lblUsername;


    @FXML
    private void initialize() {
        if (!UserSession.getInstance().isLoggedIn()) {
            Platform.runLater(() -> {
                ViewHandler.COORD_DASHBOARD.close();
                ViewHandler.LOGIN.show(false);
            });
            return;
        }

        instance = this;
        currentUser = UserSession.getInstance().getCurrentUser();
        lblUsername.setText("Welcome, " + currentUser.getFirstName());

        try {
            eventModel = new EventModel();
        } catch (Exception e) {
            AlertHelper.showError("Error", "EventModel failed in CoordDashboardController.");
        }

        setupListeners();
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
        ViewHandler.NEW_EVENT.show(false);
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
            AlertHelper.showError("Error", "Unable to open NewTicketView");
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

    private void setupListeners() {
        // Event Pool listener, for total events label
        eventContainer.getChildren().addListener((ListChangeListener<Node>) change -> {
            int numOfEvents = Integer.parseInt(lblNumOfEvents.getText());

            while (change.next()) {

                if (change.wasAdded()) {
                    lblNumOfEvents.setText(String.valueOf(numOfEvents+1));
                }
                if (change.wasRemoved()) {
                    lblNumOfEvents.setText(String.valueOf(numOfEvents-1));
                }

            }
        });
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

    @FXML
    private void onBtnLogOut(ActionEvent actionEvent) {
        boolean confirmed = AlertHelper.showConfirmation(
                "Log out",
                "Are you sure you want to log out?"
        );

        if (!confirmed)
            return;

        UserSession.getInstance().clear();
        ViewHandler.COORD_DASHBOARD.close();
        ViewHandler.LOGIN.show(false);
    }
}
