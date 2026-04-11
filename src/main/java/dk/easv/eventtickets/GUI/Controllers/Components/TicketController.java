package dk.easv.eventtickets.GUI.Controllers.Components;

// Project imports
import dk.easv.eventtickets.BE.TicketData;
import dk.easv.eventtickets.GUI.Utils.AlertHelper;
import dk.easv.eventtickets.GUI.Utils.BarcodeGenerator;

// MaterialFX imports
import io.github.palexdev.mfxcore.controls.Label;

// ZXing imports
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

// Java imports
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
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

    // Both tickets
    @FXML private HBox barcodeHbox;

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

        // Barcode generation
        String title = ticketData.eventTitle();
        long hash = Math.abs((long) title.hashCode() & 0xFFFFFFFFL);
        String twelveDigits = String.format("%012d", hash % 1_000_000_000_000L);

        int sum = 0;
        for (int i = 0; i < 12; i++) {
            int digit = Character.getNumericValue(twelveDigits.charAt(i));
            sum += (i % 2 == 0) ? digit : digit * 3;
        }
        int checksum = (10 - (sum % 10)) % 10;
        String data = twelveDigits + checksum;

        try {
            barcodeHbox.getChildren().add(BarcodeGenerator.generateBarcode(ticketData.customerEmail(), BarcodeFormat.QR_CODE, 200, 200));
        } catch (WriterException e) {
            AlertHelper.showError("Error", "Failed to generate QR code for ticket");
        }

        try {
            barcodeHbox.getChildren().add(BarcodeGenerator.generateBarcode(data, BarcodeFormat.EAN_13, 150, 180));
        } catch (WriterException e) {
            AlertHelper.showError("Error", "Failed to generate barcode for ticket");
        }
    }
}
