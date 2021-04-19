package fpdf.ui;

import fpdf.config.Configuration;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/main.fxml"));
        primaryStage.setTitle("Opisz fakturę");
        primaryStage.setScene(new Scene(root));
        primaryStage.setOnCloseRequest(windowEvent -> {
            try {
                Configuration.getInstance().store();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/icon.png")));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
