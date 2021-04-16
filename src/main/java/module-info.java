module fpdf {
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;
    requires pdfbox;

    opens fpdf.ui to javafx.fxml;

    exports fpdf.ui;
    exports fpdf.service;
}