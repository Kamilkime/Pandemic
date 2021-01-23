package pl.edu.pw.ee.aisd.pandemic;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public final class Pandemic extends Application {

    public static void main(final String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage primaryStage) throws IOException {
        final FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/gui.fxml"));

        final Scene guiScene = new Scene(loader.load());
        ((GUIController) loader.getController()).setup();

        primaryStage.setTitle("Pandemic");
        primaryStage.initStyle(StageStyle.UNDECORATED);

        primaryStage.setScene(guiScene);
        primaryStage.show();
    }

}
