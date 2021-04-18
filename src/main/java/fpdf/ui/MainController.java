package fpdf.ui;

import com.jfoenix.controls.JFXCheckBox;
import fpdf.config.Configuration;
import fpdf.service.PDFService;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.input.DataFormat;
import javafx.scene.input.TransferMode;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainController {
    public TextArea description;
    public ListView<String> filesDropArea;
    public JFXCheckBox autoclearCbx;
    public ProgressBar progressBar;
    private List<File> fileList;

    private Configuration configuration;

    @FXML
    private void initialize(){
        initConfig();

        autoclearCbx.selectedProperty().setValue(willClearOnDragIn());

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
            List<File> droppedFiles = dragEvent.getDragboard().getFiles().stream()
                    .filter(file -> !fileNames.contains(file.getName()))
                    .collect(Collectors.toList());
            addFiles(droppedFiles, willClearOnDragIn());
            refreshDropArea();
        });
    }

    public void selectPdf(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        Path inFolder = getConfigFolder(Configuration.IN_PATH);
        if (inFolder != null){
            fileChooser.setInitialDirectory(inFolder.getParent().toFile());
        }
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("PDF Files","*.pdf"));
        List<File> files = fileChooser.showOpenMultipleDialog(null);
        if (files != null){
            fileList.clear();
            fileList.addAll(files);
            refreshDropArea();
            setConfigFolder(Configuration.IN_PATH, files.get(0));
        }
    }

    public void describe(ActionEvent actionEvent) {
        Path outFolder = getConfigFolder(Configuration.OUT_PATH);
        if(outFolder == null){
            outFolder = chooseOutDir();
        }
        if(outFolder != null){
            double i =.0;
            Path finalOutFolder = outFolder;
            Task<Void> describeTask = new Task<Void>(){
                @Override
                protected Void call() throws Exception {
                    double index = 0;
                    for (File fpdf :
                            fileList) {
                        try {
                            PDFService pdfService = new PDFService(fpdf);
                            pdfService.addDescription(description.getText());
                            pdfService.save(finalOutFolder);
                        } catch (Exception e) {
                            e.printStackTrace();
                            showExceptionAlert(e);
                            break;
                        }
                        index++;
                        updateProgress(index / fileList.size(), 1);
                    }
                    Thread.sleep(500);
                    updateProgress(0, 1);
                    return null;
                }
            };
            progressBar.progressProperty().bind(describeTask.progressProperty());
            new Thread(describeTask).start();
        }
    }

    private void addFiles(List<File> files){
        fileList.addAll(files);
    }

    private void addFiles(List<File> files, boolean clear){
        if (clear) fileList.clear();
        addFiles(files);
    }

    private void refreshDropArea(){
        filesDropArea.getItems().clear();
        fileList.forEach(file -> {
            filesDropArea.getItems().add(file.getName());
        });
    }

    private void initConfig(){
        try {
            configuration = Configuration.getInstance();
        } catch (IOException e) {
            e.printStackTrace();
            showExceptionAlert(e);
        }
    }

    private void showExceptionAlert(Exception e){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }

    private boolean willClearOnDragIn(){
        String value = configuration.get().getProperty(Configuration.CLEAR_ON_DROP);
        if (value == null) return false;
        return value.equals("true");
    }

    private Path getConfigFolder(String key){
        String in = configuration.get().getProperty(key);
        if (in == null) return null;
        Path path = Paths.get(in);
        if(!Files.exists(path)) return null;
        return Paths.get(configuration.get().getProperty(key));
    }

    private void setConfigFolder(String key, File file){
        String val = configuration.get().getProperty(key);
        if(val == null || !val.equals(file.getPath())){
            configuration.get().setProperty(key, file.getPath());
        }
    }

    private Path chooseOutDir(){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        Path out = getConfigFolder(Configuration.OUT_PATH);
        if(out != null){
            directoryChooser.setInitialDirectory(out.toFile());
        }
        File outDir = directoryChooser.showDialog(null);
        if(outDir != null){
            setConfigFolder(Configuration.OUT_PATH, outDir);
            return outDir.toPath();
        }
        return null;
    }

    public void handleAutoClear(ActionEvent actionEvent) {
        configuration.get().setProperty(Configuration.CLEAR_ON_DROP, String.valueOf(autoclearCbx.selectedProperty().get()));
    }


    public void selectSavePath(ActionEvent actionEvent) {
        chooseOutDir();
    }
}
