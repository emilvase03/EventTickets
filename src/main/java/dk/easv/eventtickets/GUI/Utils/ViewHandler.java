package dk.easv.eventtickets.GUI.Utils;

// Java imports
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;

public enum ViewHandler {

    LOGIN("/views/LoginView.fxml", "Login", Modality.NONE),
    ADMIN_DASHBOARD("/views/AdminDashboardView.fxml", "Dashboard", Modality.NONE),
    COORD_DASHBOARD("/views/CoordDashboardView.fxml", "Dashboard", Modality.NONE),
    NEW_EVENT("/views/NewEventView.fxml", "New Event", Modality.APPLICATION_MODAL),
    NEW_TICKET("/views/NewTicketView.fxml", "New Ticket", Modality.APPLICATION_MODAL),
    PRINT_TICKETS("/views/PrintTicketsView.fxml", "Print Tickets", Modality.APPLICATION_MODAL),
    NORMAL_TICKET("/components/NormalTicket.fxml", "Ticket", Modality.NONE),
    SPECIAL_TICKET("/components/SpecialTicket.fxml", "Special Ticket", Modality.NONE),
    NEW_COORDINATOR("/views/NewCoordinatorView.fxml", "New Coordinator", Modality.APPLICATION_MODAL),
    EDIT_COORDINATOR("/views/EditCoordView.fxml", "", Modality.APPLICATION_MODAL),
    EDIT_EVENT("/views/EditEventView.fxml", "Edit Event", Modality.APPLICATION_MODAL);

    private final String path;
    private final String title;
    private final Modality modality;

    private Scene scene;
    private Stage stage;
    private FXMLLoader loader;

    ViewHandler(String path, String title, Modality modality) {
        this.path = path;
        this.title = title;
        this.modality = modality;
    }

    public void preLoad() {
        try {
            loadScene();
        } catch (IOException e) {
            e.printStackTrace();
            AlertHelper.showError("Error", "Failed to pre-load: " + title);
        }
    }

    public Stage show(boolean shouldBeResizable) {
        try {
            Stage stage = getOrCreateStage(shouldBeResizable);
            stage.show();
            return stage;
        } catch (Exception e) {
            AlertHelper.showError("Error", "Failed to open: " + title);
            e.printStackTrace();
            return null;
        }
    }

    public void showAndWait(boolean shouldBeResizable) {
        try {
            getOrCreateStage(shouldBeResizable).showAndWait();
        } catch (Exception e) {
            AlertHelper.showError("Error", "Failed to open: " + title);
        }
    }

    public void reset() {
        scene = null;
        stage = null;
        loader = null;
    }

    public Parent getRoot() throws IOException {
        if (scene == null) loadScene();
        return (Parent) scene.getRoot();
    }

    @SuppressWarnings("unchecked")
    public <T> T getController() {
        if (loader == null) {
            throw new IllegalStateException(title + " has not been loaded yet. Call show() or preLoad() first.");
        }
        return (T) loader.getController();
    }

    private Stage getOrCreateStage(boolean resizable) throws IOException {
        if (stage == null) {
            stage = new Stage();
            stage.setTitle(title);
            stage.setScene(loadScene());
            stage.initModality(modality);
            stage.setResizable(setResizable(resizable));
        }
        return stage;
    }

    private Scene loadScene() throws IOException {
        if (scene == null) {
            loader = new FXMLLoader(getClass().getResource(path));
            scene = new Scene(loader.load());
        }
        return scene;
    }

    public boolean setResizable(boolean resizable) {
        if (stage != null) {
            stage.setResizable(resizable);
            return true;
        }
        return false;
    }

    public void close() {
        if (stage != null) {
            stage.close();
        }
    }
}