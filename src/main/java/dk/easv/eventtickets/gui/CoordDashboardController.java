package dk.easv.eventtickets.gui;
// Java imports
import dk.easv.eventtickets.gui.utils.AlertHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;

// MaterialFX imports
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class CoordDashboardController {

    @FXML private MFXScrollPane scrollPane;
    @FXML private FlowPane eventContainer;

    @FXML
    private void onCreateEvent(ActionEvent event) {

        /*
        *  Open 'create event'-window
        */

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

    public FlowPane getEventContainer() {
        return eventContainer;
    }
}
