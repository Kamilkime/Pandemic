package pl.edu.pw.ee.aisd.pandemic.popup;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pl.edu.pw.ee.aisd.pandemic.GUIController;

import java.io.IOException;

public final class ClosePopup extends Stage {

    public ClosePopup(final Stage gui, final GUIController guiController) {
        this.initStyle(StageStyle.UNDECORATED);
        this.initScene(gui, guiController);
    }

    private void initScene(final Stage guiStage, final GUIController guiController) {
        final FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/close.fxml"));

        final Scene scene;
        try {
            scene = new Scene(loader.load());
        } catch (final IOException exception) {
            return;
        }

        ((Button) scene.lookup("#no")).setOnAction(event -> this.close());
        ((Button) scene.lookup("#yes")).setOnAction(event -> {
            this.close();
            guiStage.close();
            guiController.stopSimulationThread();
        });

        this.setScene(scene);
    }

}
