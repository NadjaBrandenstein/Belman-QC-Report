package dk.easv.belmanqcreport.BLL.UTIL;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.IOException;

public class PDFGeneratorImp implements PDFGenerator {
    private PDFGeneratorImp() {
    }

    private static class Holder{
        private static final PDFGeneratorImp INSTANCE = new PDFGeneratorImp();
    }

    public static PDFGeneratorImp getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public void generatePDF(String fileName) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            PDImageXObject pdImage = PDImageXObject.createFromFile("saved_image.png", document);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            float scale = 0.5f; // scale image to fit page
            contentStream.drawImage(pdImage, 100, 300, pdImage.getWidth() * scale, pdImage.getHeight() * scale);
            contentStream.close();

            document.save(fileName);
            System.out.println("PDF created: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
