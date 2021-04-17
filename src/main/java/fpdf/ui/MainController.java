package fpdf.ui;

import fpdf.service.PDFService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.DataFormat;
import javafx.scene.input.TransferMode;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainController {
    public TextArea description;
    public ListView<String> filesDropArea;
    private List<File> fileList;

    @FXML
    private void initialize(){
        if(fileList == null) fileList = new ArrayList<>();

        filesDropArea.setOnDragOver(dragEvent -> {
            if (dragEvent.getGestureSource() != filesDropArea &&
                    dragEvent.getDragboard().hasContent(DataFormat.FILES)){
                dragEvent.acceptTransferModes(TransferMode.COPY);
            }
        });
        filesDropArea.setOnDragEntered(dragEvent -> {
            filesDropArea.setStyle("-fx-background-color: lightgray; -fx-border-color: lightgray;");
        });
        filesDropArea.setOnDragExited(dragEvent -> {
            filesDropArea.setStyle("-fx-background-color: white; -fx-border-color: lightgray;");
        });
        filesDropArea.setOnDragDropped(dragEvent -> {
            List<String> fileNames = fileList.stream().map(File::getName).collect(Collectors.toList());
            List<File> droppedFiles = dragEvent.getDragboard().getFiles().stream().filter(file -> !fileNames.contains(file.getName())).collect(Collectors.toList());
            addFiles(droppedFiles);
            refreshDropArea();
        });
    }

    public void selectPdf(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        if (file != null){
            fileList.clear();
            fileList.add(file);
            refreshDropArea();
        }
    }

    public void describe(ActionEvent actionEvent) {
        for (File fpdf :
                fileList) {
            try {
                PDFService pdfService = new PDFService(fpdf);
                pdfService.addDescription(description.getText());
            } catch (Exception e) {
                System.out.println("Asdadasdasdas");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }
    }

    private void addFiles(List<File> files){
        fileList.addAll(files);
    }

    private void refreshDropArea(){
        filesDropArea.getItems().clear();
        fileList.forEach(file -> {
            filesDropArea.getItems().add(file.getName());
        });
    }


}
