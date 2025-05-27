package dk.easv.belmanqcreport;

import dk.easv.belmanqcreport.BE.MyImage;
import dk.easv.belmanqcreport.BE.Order;
import dk.easv.belmanqcreport.BE.OrderItem;
import dk.easv.belmanqcreport.BLL.UTIL.PDFGeneratorImp;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PDFGeneratorImpTest {
    private final String testPdfPath = "test.pdf";


    @Test
    void testGeneratePDF() throws IOException {
        PDFGeneratorImp pdfGenerator = PDFGeneratorImp.getInstance();
        pdfGenerator.setEmployeeName("Amy Lee");

        Order testOrder = new Order();
        testOrder.setOrderNumber("ORD12345");
        pdfGenerator.setOrder(testOrder);

        dk.easv.belmanqcreport.BLL.UTIL.OrderItem testItem = new dk.easv.belmanqcreport.BLL.UTIL.OrderItem();
        testItem.setItemNumber("Item001");
        pdfGenerator.setOrderItem(testItem);

        MyImage testImage = new MyImage();
        testImage.setImagePath("src/main/resources/dk/easv/belmanqcreport/Images/captured_1747296232726.png");
        testImage.setComment("This is a test comment");

        pdfGenerator.generatePDF(testPdfPath, Collections.singletonList(testImage));

        File file = new File(testPdfPath);
        assertTrue(file.exists(), "PDF file should exists");

        try (PDDocument document = PDDocument.load(file)) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);

            assertTrue(text.contains("ORD12345"), "PDF should contain order number");
            assertTrue(text.contains("Amy Lee"), "PDF should contain employee name");
            assertTrue(text.contains("Item001"), "PDF should contain item number");
            assertTrue(text.contains("This is a test comment"), "PDF should contain image comment");
        }
    }

    @AfterEach
    void cleanUp() {
        File file = new File(testPdfPath);
        if (file.exists()) {
            file.delete();
        }
    }
}
