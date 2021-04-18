package fpdf.service;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1CFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class PDFService {

    private final PDDocument fpdf;
    private final String fpdfName;
    private String description;

    public PDFService(File file) throws IOException, NullPointerException {
        fpdf = Loader.loadPDF(file);
        fpdfName = file.getName();
        description = "";
    }

    public void addDescription(String description) throws IOException {
        List<String> lines = getLines(description);
        PDPage page = new PDPage();
        fpdf.addPage(page);
        PDFont font = PDType0Font.load(fpdf, new File("C:/Windows/fonts/times.ttf")); //use Times New Roman font
        try (PDPageContentStream contents = new PDPageContentStream(fpdf, page)){
            contents.beginText();
            contents.setFont(font, 12);
            contents.newLineAtOffset(100, 700);
            contents.setLeading(14.5f);
            for (String line :
                    lines) {
                contents.showText(line);
                contents.newLine();
            }
            contents.endText();
        }
    }

    public void save(Path path) throws IOException {
        String fileName = fpdfName + description + ".pdf";
        fpdf.save(path.resolve(fileName).toString());
        fpdf.close();
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private List<String> getLines(String string){
        return Arrays.asList(string.split("\\n"));
    }


}
