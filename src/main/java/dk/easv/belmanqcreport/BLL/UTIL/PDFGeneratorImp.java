package dk.easv.belmanqcreport.BLL.UTIL;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import dk.easv.belmanqcreport.BE.*;
import dk.easv.belmanqcreport.GUI.Controller.QcController;
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
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PDFGeneratorImp implements IPDFGenerator {
    private float pageWidth;
    private float pageHeight;
    private PathGraphics contentStream;
    private Order order;
    private OrderItem orderItem;
    private QcController qcController;
    private String employeeName = "";
    private String orderNumber = "";

    private PDFGeneratorImp() {
        order = new Order();
        orderItem = new OrderItem();
    }

    private static class Holder{
        private static final PDFGeneratorImp INSTANCE = new PDFGeneratorImp();
    }

    public static PDFGeneratorImp getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public void generatePDF(String fileName, String imagePath) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            File imgFile = new File(imagePath);
            if (!imgFile.exists()) {
                System.out.println("Image file does not exist: " + imgFile.getAbsolutePath());
                return;
            }
            BufferedImage bufferedImage = ImageIO.read(new File("dk/easv/belmanqcreport/Images"));
            if (bufferedImage == null) {
                System.out.println("Could not load image:" + imagePath);
                return;
            }
            PDImageXObject pdImage = LosslessFactory.createFromImage(document, bufferedImage);
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
                System.out.println("Checking image file: " + imgFile.getAbsolutePath() + ",exists?" + imgFile.exists());
                if (!imgFile.exists()) {
                    System.out.println("Skipping missing image: " + myImage.getImagePath());
                    continue;
                }

                BufferedImage bufferedImage = ImageIO.read(imgFile);

                if (bufferedImage == null) {
                    System.out.println("Failed to load image file:" + imgFile.getAbsolutePath());
                    continue;
                }
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
                    InputStream in = getClass().getClassLoader().getResourceAsStream("dk/easv/belmanqcreport/Icons/Belman.png");
                    if(in!= null) {
                        BufferedImage logoImage = ImageIO.read(in);
                        if(logoImage != null) {
                            PDImageXObject logoImageObj = LosslessFactory.createFromImage(document, logoImage);

                            float logoWidth = 80;
                            float logoHeight = (float) logoImage.getHeight() / logoImage.getWidth() * logoWidth;

                            float logoX = 40;
                            float logoY = pageHeight - logoHeight - 40;
                            contentStream.drawImage(logoImageObj, logoX, logoY, logoWidth, logoHeight);

                            float headerX = logoX + logoWidth + 10;
                            float headerHeight = logoHeight;
                            float headerWidth = 435;

                            contentStream.setNonStrokingColor(0,75,136);
                            contentStream.addRect(headerX, logoY, headerWidth, headerHeight);
                            contentStream.fill();

                            contentStream.beginText();
                            contentStream.setNonStrokingColor(255,255,255);
                            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);

                            contentStream.endText();

                            String orderNumber = (order != null && order.getOrderNumber() != null) ? order.getOrderNumber() : "Unknown";
                            String orderText = "Order " + orderNumber;

                            float fontSize = 18;
                            float textWidth = PDType1Font.HELVETICA_BOLD.getStringWidth(orderText) / 1000 * fontSize;
                            float textX = headerX + (headerWidth - textWidth) / 2;
                            float textY = logoY + (headerHeight / 2) - (fontSize / 2.5f);  // Adjust for vertical alignment

                            contentStream.beginText();
                            contentStream.setNonStrokingColor(255,255,255);
                            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);
                            contentStream.newLineAtOffset(textX, textY);
                            contentStream.showText(orderText);
                            contentStream.endText();


                            contentStream.beginText();
                            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                            String userLabel = employeeName;

                            float userLabelWidth = PDType1Font.HELVETICA.getStringWidth(userLabel) / 1000 * 12;
                            float rightTextX = headerX + headerWidth - userLabelWidth - 10;

                            contentStream.newLineAtOffset(rightTextX, textY);
                            contentStream.showText(userLabel);
                            contentStream.endText();

                            contentStream.setNonStrokingColor(0,0,0);
                        } else {
                            System.out.println("Failed to load image from stream: logoImage is null");
                        }
                    } else {
                        System.out.println("Could not find Belman logo");
                    }

                    contentStream.drawImage(pdImage, x, y, drawWidth, drawHeight);

                    String dateTakenStr = "Unknown";
                    try{
                        Metadata metadata = ImageMetadataReader.readMetadata(imgFile);
                        ExifSubIFDDirectory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
                        if (directory !=null && directory.getDateOriginal() != null) {
                            dateTakenStr = directory.getDateOriginal().toString();
                        }else{
                            long lastModified = imgFile.lastModified();
                            dateTakenStr = new Date(lastModified).toString();
                        }
                    }catch (Exception e){
                        System.out.println("Could not extract EXIF date for:" + imgFile.getName());
                    }

                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA, 12);
                    contentStream.setLeading(14.5f);
                    contentStream.newLineAtOffset(50, y - 20);


                    contentStream.showText("Items number: " + orderItem.getItemNumber());
                    contentStream.newLine();
                    contentStream.showText("Comment: " + myImage.getComment());
                    contentStream.newLine();
                    contentStream.showText("Position: " + myImage.getImagePosition());
                    contentStream.newLine();
                    contentStream.showText("Date taken: " + dateTakenStr);

                    contentStream.endText();

                }
         }

            document.save(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String getOrderNumberFromDatabase(OrderItem orderItem) {
        this.orderItem = orderItem;
        return "";
    }

    public void setEmployeeName(String name){
        this.employeeName = name;
    }

    public void setOrder(Order order){
        this.order = order;
    }

    public void setOrderItem(OrderItem orderItem){
        this.orderItem = orderItem;
    }

}

