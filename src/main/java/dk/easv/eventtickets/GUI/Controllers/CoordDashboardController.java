package dk.easv.eventtickets.GUI.Controllers;

// Project imports
import dk.easv.eventtickets.BE.Event;
import dk.easv.eventtickets.BE.TicketData;
import dk.easv.eventtickets.BE.User;
import dk.easv.eventtickets.BLL.UTIL.UserSession;
import dk.easv.eventtickets.GUI.Controllers.Components.CardController;
import dk.easv.eventtickets.GUI.Controllers.Components.EventFilterController;
import dk.easv.eventtickets.GUI.Controllers.Components.TicketController;
import dk.easv.eventtickets.GUI.Models.EventModel;
import dk.easv.eventtickets.GUI.Models.Filter;
import dk.easv.eventtickets.GUI.Utils.AlertHelper;
import dk.easv.eventtickets.GUI.Utils.ViewHandler;

// MaterialFX imports
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class CoordDashboardController {

    private static CoordDashboardController instance;
    private User currentUser;
    private EventModel eventModel;
    private FXMLLoader newEventLoader;
    private List<Event> events = new ArrayList<>();

    @FXML private MFXScrollPane scrollPane;
    @FXML private TilePane eventContainer;
    @FXML private Label lblNumOfEvents;
    @FXML private Label lblUsername;
    @FXML private EventFilterController filterBarController;


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
            CardController cardController = new CardController();
            events = eventModel.getMyEvents(currentUser);

            for (Event e : events) {
                getEventContainer().getChildren().add(cardController.createEventCard(e));
            }

        } catch (IOException e) {
            AlertHelper.showError("Error","Failed to get CardController.");
            System.out.println(e.getMessage());
        } catch (Exception e) {
            AlertHelper.showError("Error","Failed to get my events from event model.");
        }
    }

    private void setupListeners() {
        // Event Pool listener, for total events label
        eventContainer.getChildren().addListener((ListChangeListener<Node>) change -> {
            int eventsAmount = eventContainer.getChildren().size();
            lblNumOfEvents.setText(String.valueOf(eventsAmount));
        });

        // Listener for event filters
        filterBarController.getFilterGroup().selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null) { filterBarController.getFilterGroup().selectToggle(oldVal); return;}
            Filter filter = Filter.valueOf((String) newVal.getUserData());
            applyFilter(filter);
        });
    }

    public void launchTicketsWindow(int ticketsAmount, TicketData ticketData) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/PrintTicketsView.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();


            String ticketPath;
            if (!ticketData.isSpecial()) {
                ticketPath = "/components/NormalTicket.fxml";
            } else {
                ticketPath = "/components/SpecialTicket.fxml";
            }

            PrintTicketController printController = fxmlLoader.getController();

            for (int i = 0; i < ticketsAmount; i++) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(ticketPath));
                loader.setController(new TicketController(ticketData));
                Parent ticketRoot = loader.load();
                printController.getTicketsContainer().getChildren().add(ticketRoot);
            }
        } catch (Exception e) {
            AlertHelper.showError("Error", "Failed to display tickets.");
        }

    }

    public TilePane getEventContainer() {
        return eventContainer;
    }

    public static CoordDashboardController getInstance() {
        return instance;
    }

    private void applyFilter(Filter filter) {
        Comparator<Event> byDateTime = Comparator.comparing(Event::getStartDate)
                .thenComparing(Event::getStartTime);

        List<Event> filtered = switch (filter) {
            case TODAY      -> events.stream().filter(e -> e.getStartDate().isEqual(LocalDate.now())).sorted(byDateTime).toList();
            case FUTURE     -> events.stream().filter(e -> e.getStartDate().isAfter(LocalDate.now())).sorted(byDateTime).toList();
            case PAST       -> events.stream().filter(e -> e.getStartDate().isBefore(LocalDate.now())).sorted(byDateTime.reversed()).toList();
            case ALL_EVENTS -> events.stream().sorted(byDateTime).toList();
        };

        try {
            CardController cardController = new CardController();
            eventContainer.getChildren().clear();
            for (Event e : filtered)
                eventContainer.getChildren().add(cardController.createEventCard(e));
        } catch (IOException e) {
            AlertHelper.showError("Error", "Failed to sort events");
        }

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

    public void refreshEvents() {
        try {
            eventContainer.getChildren().clear();

            events = eventModel.getMyEvents(currentUser);

            CardController cardController = new CardController();

            for (Event e : events) {
                eventContainer.getChildren().add(cardController.createEventCard(e));
            }

        } catch (Exception e) {
            AlertHelper.showError("Error", "Failed to refresh events.");
        }
    }
}
