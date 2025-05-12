package dk.easv.belmanqcreport.BLL.UTIL;

import dk.easv.belmanqcreport.BE.MyImage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import sun.print.PathGraphics;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PDFGeneratorImp implements PDFGenerator {
    private float pageWidth;
    private float pageHeight;
    private PathGraphics contentStream;

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

    public void generatePDF(String fileName, List<MyImage> images) {
        try (PDDocument document = new PDDocument()) {
            for (MyImage myImage : images) {

                File imgFile = new File(myImage.getImagePath());
                if (!imgFile.exists()) {
                    System.out.println("Skipping missing image: " + myImage.getImagePath());
                    continue;
                }

                BufferedImage bufferedImage = ImageIO.read(imgFile);

                PDPage page = new PDPage(PDRectangle.A4);
                document.addPage(page);

                PDImageXObject pdImage = LosslessFactory.createFromImage(document, bufferedImage);

                PDRectangle mediaBox = page.getMediaBox();
                float pageWidth = mediaBox.getWidth();
                float pageHeight = mediaBox.getHeight();

                float imageWidth = pdImage.getWidth();
                float imageHeight = pdImage.getHeight();

                float scale = Math.min(pageWidth / imageWidth, pageHeight / imageHeight) * 0.9f;
                float drawWidth = imageWidth * scale;
                float drawHeight = imageHeight * scale;

                float x = (pageWidth - drawWidth) / 2;
                float y = (pageHeight - drawHeight) / 2 + 100;

                try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                    File logoFile = new File("dk/easv/belmanqcreport/Icons/Belman.png");
                    if (!logoFile.exists()) {
                        BufferedImage logoImage = ImageIO.read(logoFile);
                        PDImageXObject logoImageObj = LosslessFactory.createFromImage(document, logoImage);

                        float logoWidth = 80;
                        float logoHeight = (float) logoImage.getHeight() / logoImage.getWidth() * logoWidth;

                        float logoX = pageWidth - logoWidth - 50;
                        float logoY = pageHeight - logoHeight - 40;
                        contentStream.drawImage(logoImageObj, logoX, logoY, logoWidth, logoHeight);
                    }

                    contentStream.drawImage(pdImage, x, y, drawWidth, drawHeight);

                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA, 12);
                    contentStream.setLeading(14.5f);
                    contentStream.newLineAtOffset(50, y - 20);

                    contentStream.showText("Image ID: " + myImage.getImagePath());
                    contentStream.newLine();
                    contentStream.showText("Order ID: " + myImage.getOrderID());
                    contentStream.newLine();
                    contentStream.showText("Comment: " + myImage.getImagePath());

                    contentStream.endText();

                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA, 16);
                    contentStream.newLineAtOffset(50, y - 50);
                    contentStream.showText("Belman QC Report");
                    contentStream.endText();

                    contentStream.moveTo(50, y - 30);
                    contentStream.lineTo(pageWidth -50, y - 30);
                    contentStream.stroke();

                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA, 10);
                    contentStream.newLineAtOffset(pageWidth -100,30);
                    contentStream.showText("Generated by Belman QC Report");
                    contentStream.endText();

                }

            }

            document.save(fileName);
            System.out.println("PDF created successfully at: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }





    }

           
}

