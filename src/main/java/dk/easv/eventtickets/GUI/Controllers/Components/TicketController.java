package dk.easv.eventtickets.GUI.Controllers.Components;

// Project imports
import dk.easv.eventtickets.BE.TicketData;

// MaterialFX imports
import io.github.palexdev.mfxcore.controls.Label;

// Java imports
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;

public class TicketController implements Initializable {
    // Normal ticket
    @FXML private Label lblOptionText;
    @FXML private Label lblCustomerName;
    @FXML private Label lblCustomerEmail;
    @FXML private Label lblEventName;
    @FXML private Label lblDateTime;
    @FXML private Label lblAddress;

    // Special ticket
    @FXML private Label lblSpecialTicketHeader;
    @FXML private Label lblTicketTitle;
    @FXML private Label lblDiscount;
    @FXML private Label lblEventTitle;

    private TicketData ticketData;

    public TicketController(TicketData ticketData) {
        this.ticketData = ticketData;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (!ticketData.isSpecial()) {
            // Option text
            if (ticketData.optionText() == null)
                lblOptionText.setText("");
            else
                lblOptionText.setText(ticketData.optionText());
            // Customer info
            lblCustomerName.setText(ticketData.customerName());
            lblCustomerEmail.setText(ticketData.customerEmail());
            // Event info
            lblEventName.setText(ticketData.eventTitle());
            lblDateTime.setText(ticketData.startDate().toString() + " - " + ticketData.startTime().toString());
            lblAddress.setText(ticketData.address());
        } else {
            lblSpecialTicketHeader.setText("Voucher!");
            // Info from newTicketController
            lblTicketTitle.setText(ticketData.ticketTitle());
            lblDiscount.setText(ticketData.discount());
            // Event info
            if (ticketData.eventTitle() == "No Event")
                lblEventTitle.setText("");
            else
                lblEventTitle.setText(ticketData.eventTitle());
        }
    }
}
