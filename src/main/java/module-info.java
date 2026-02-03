module dk.easv.eventtickets {
    requires javafx.controls;
    requires javafx.fxml;


    opens dk.easv.eventtickets to javafx.fxml;
    exports dk.easv.eventtickets;
}