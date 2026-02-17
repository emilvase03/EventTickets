package dk.easv.eventtickets.GUI.Utils;

// Java imports
import javafx.geometry.Pos;
import javafx.print.PageLayout;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class TicketPrinter {

    public static void print(FlowPane ticketsContainer) {
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null && job.showPrintDialog(ticketsContainer.getScene().getWindow())) {

            PageLayout pageLayout = job.getJobSettings().getPageLayout();
            double printableWidth = pageLayout.getPrintableWidth();

            boolean allSuccess = true;

            List<Node> tickets = new ArrayList<>(ticketsContainer.getChildren());

            for (int i = 0; i < tickets.size(); i += 3) {

                VBox pageContent = new VBox(10);
                pageContent.setAlignment(Pos.TOP_CENTER);

                int ticketsOnThisPage = Math.min(3, tickets.size() - i);
                for (int j = 0; j < ticketsOnThisPage; j++) {
                    Node ticket = tickets.get(i + j);

                    WritableImage snapshot = ticket.snapshot(null, null);
                    ImageView imageView = new ImageView(snapshot);
                    double scale = (printableWidth / snapshot.getWidth()) * 0.9;
                    imageView.setFitWidth(snapshot.getWidth() * scale);
                    imageView.setFitHeight(snapshot.getHeight() * scale);
                    imageView.setPreserveRatio(true);

                    pageContent.getChildren().add(imageView);
                }


                boolean success = job.printPage(pageContent);
                if(!success) {
                    allSuccess = false;
                    break;
                }
            }


            job.endJob();

            if (!allSuccess) {
                AlertHelper.showError("Error", "Failed to print ticket(s).");
            }

        }
    }

}
