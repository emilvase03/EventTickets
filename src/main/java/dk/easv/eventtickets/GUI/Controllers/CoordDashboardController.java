package dk.easv.eventtickets.GUI.Controllers;
// Java imports
import dk.easv.eventtickets.GUI.Utils.AlertHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.FlowPane;

// MaterialFX imports
import io.github.palexdev.materialfx.controls.MFXScrollPane;

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/components/Card.fxml"));
            Parent cardRoot = loader.load();

            CardController cardController = loader.getController();

            eventContainer.getChildren().add(cardRoot);

        } catch (IOException e) {
            AlertHelper.showError("Error", "Failed to create new event.");
        }

    }

}
