package dk.easv.belmanqcreport.BLL.UTIL;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                float scale = 0.5f;
                float x = 100;
                float y = 300;
                contentStream.drawImage(pdImage, x, y, pdImage.getWidth() * scale, pdImage.getHeight() * scale);
            }

            document.save(fileName);
            System.out.println("PDF created: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void generatePDF(String fileName, List<String> imagePaths) {
        try (PDDocument document = new PDDocument()) {
            for (String imagePath : imagePaths) {
                File imgFile = new File(imagePath);
                if (!imgFile.exists()) {
                    System.out.println("Skipping missing image: " + imagePath);
                    continue;
                }

                PDPage page = new PDPage(PDRectangle.A4);
                document.addPage(page);

                PDImageXObject pdImage = PDImageXObject.createFromFile(imagePath, document);

                PDRectangle mediaBox = page.getMediaBox();
                float pageWidth = mediaBox.getWidth();
                float pageHeight = mediaBox.getHeight();

                float imageWidth = pdImage.getWidth();
                float imageHeight = pdImage.getHeight();

                float scale = Math.min(pageWidth / imageWidth, pageHeight / imageHeight) * 0.9f;
                float drawWidth = imageWidth * scale;
                float drawHeight = imageHeight * scale;

                float x = (pageWidth - drawWidth) / 2;
                float y = (pageHeight - drawHeight) / 2;

                try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                    contentStream.drawImage(pdImage, x, y, drawWidth, drawHeight);
                }

                System.out.println("Added image to PDF: " + imagePath);
            }

            document.save(fileName);
            System.out.println("PDF created successfully at: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

