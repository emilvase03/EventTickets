package dk.easv.eventtickets.GUI.Controllers;

// Project imports
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
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class CoordDashboardController {

    @FXML private MFXScrollPane scrollPane;
    @FXML private TilePane eventContainer;

    @FXML
    private void onCreateEvent(ActionEvent event) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/NewEventView.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();

            NewEventController newEventController = fxmlLoader.getController();
            newEventController.setDashboardController(this);

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
            Scene scene = new Scene(fxmlLoader.load(), 405, 290);
            Stage stage = new Stage();

            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            AlertHelper.showError("Error", "Unable to open SpecialTicketView");
            throw new RuntimeException(e);
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

            Parent ticketRoot;
            if (!specialTicket) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/components/NormalTicket.fxml"));
                ticketRoot = loader.load();
            } else {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/components/SpecialTicket.fxml"));
                ticketRoot = loader.load();
            }

            for (int i = 0; i < ticketsAmount; i++) {
                PrintTicketController controller = fxmlLoader.getController();
                controller.getTicketsContainer().getChildren().add(ticketRoot);
            }
        } catch (Exception e) {
            AlertHelper.showError("Error", "Failed to display tickets.");
        }

    }

    public TilePane getEventContainer() {
        return eventContainer;
    }


}
