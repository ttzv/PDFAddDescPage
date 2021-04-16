package fpdf.ui;

import fpdf.service.PDFService;
import javafx.event.ActionEvent;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;

public class MainController {
    public TextArea description;
    private File fpdf;

    public void selectPdf(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fpdf = fileChooser.showOpenDialog(null);
        System.out.println(fpdf.getPath());
    }

    public void describe(ActionEvent actionEvent) {
        try {
            PDFService pdfService = new PDFService(fpdf);
            pdfService.addDescription(description.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
