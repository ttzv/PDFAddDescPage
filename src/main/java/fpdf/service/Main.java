package fpdf.service;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        PDFService pdfService = new PDFService(new File("test.pdf"));
        pdfService.addDescription("this is a random ass text");
    }
}
