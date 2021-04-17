# PDFAddDescPage
A small JavaFX application that adds one additional page with your desired text to any selected PDF.

To create runtime image for this app use maven-dependency-plugin to copy dependencies and run the following command from **target** dir:

`jlink --add-modules java.base,javafx.base,javafx.controls,javafx.fxml,javafx.graphics,java.logging --module-path dependency/ --output image/ --compress 2`

To launch the app using created runtime place jars with automatic modules in **auto** directory and application jar in **app** and run the following:

`"bin/java.exe" -p "auto;app" -m fpdf/fpdf.ui.Main`