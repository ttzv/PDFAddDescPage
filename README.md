# PDFAddDescPage
A small JavaFX application that adds one additional page with your desired text to any selected PDF.

![How it works](https://user-images.githubusercontent.com/35802406/115839684-2cf10f80-a41b-11eb-9b18-b863d8d8d7e1.png)

# Build and run

To create runtime image for this app use maven-dependency-plugin to copy dependencies and run the following command from **target** dir:

`jlink --add-modules java.base,javafx.base,javafx.controls,javafx.fxml,javafx.graphics,java.logging --module-path dependency/ --output image/ --compress 2`

To launch the app using created runtime place jars with automatic modules in **auto** directory and application jar in **app** and run the following:

`"bin/java.exe" -p "auto;app" -m fpdf/fpdf.ui.Main`
