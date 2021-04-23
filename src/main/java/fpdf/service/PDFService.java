package fpdf.service;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class PDFService {

    private final PDDocument fpdf;
    private final String fpdfName;
    private String suffix;

    public PDFService(File file) throws IOException, NullPointerException {
        fpdf = Loader.loadPDF(file);
        fpdfName = file.getName();
        suffix = "";
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
        String ext = getExtensionByStringHandling(fpdfName).orElse("");
        String name = fpdfName.substring(0, fpdfName.lastIndexOf(ext));
        String fileName = name + suffix + ext;
        fpdf.save(path.resolve(fileName).toString());
        fpdf.close();
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    private List<String> getLines(String string){
        return Arrays.asList(string.split("\\n"));
    }

    public Optional<String> getExtensionByStringHandling(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".")));
    }
}
