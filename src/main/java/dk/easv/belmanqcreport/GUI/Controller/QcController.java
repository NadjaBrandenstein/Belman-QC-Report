package dk.easv.belmanqcreport.GUI.Controller;
// JavaFX Imports
import javafx.event.ActionEvent;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.io.IOException;

public class QcController {


    public void btnBack(ActionEvent actionEvent) {
    }

    public void btnRefresh(ActionEvent actionEvent) {
    }

    public void btnLogout(ActionEvent actionEvent) {
    }

    public void btnPrevious(ActionEvent actionEvent) {
    }

    public void btnNext(ActionEvent actionEvent) {
    }

    public void btnCamera(ActionEvent actionEvent) {
    }

    public void btnPDFSave(ActionEvent actionEvent) {
        File file = new File("GeneratedReport.pdf");

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);


            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);


            contentStream.newLineAtOffset(100, 750);

            contentStream.showText("This is a sample PDF report.");
            contentStream.endText();
            contentStream.close();

            document.save(file);
            System.out.println("PDF saved to " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
