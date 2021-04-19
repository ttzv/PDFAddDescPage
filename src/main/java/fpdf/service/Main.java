package fpdf.service;

import fpdf.config.Configuration;
import javafx.scene.input.DataFormat;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        System.out.println(DataFormat.lookupMimeType("application/pdf"));
        DataFormat dataFormat = new DataFormat("application/pdf");
        System.out.println(DataFormat.lookupMimeType("application/pdf"));
    }
}
