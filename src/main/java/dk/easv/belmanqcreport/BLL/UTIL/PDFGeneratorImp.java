package dk.easv.belmanqcreport.BLL.UTIL;

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
        System.out.print("Generate PDF:" + fileName);
    }
}
