package dk.easv.eventtickets.GUI.Controllers.Components;

// Java imports
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleGroup;
import java.net.URL;
import java.util.ResourceBundle;

public class EventFilterController implements Initializable {

    @FXML
    private ToggleGroup filterGroup;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        filterGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null) filterGroup.selectToggle(oldVal);
        });
    }

    public ToggleGroup getFilterGroup() { return filterGroup; }
}
