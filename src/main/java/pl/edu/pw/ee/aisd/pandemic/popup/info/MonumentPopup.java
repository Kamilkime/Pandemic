package pl.edu.pw.ee.aisd.pandemic.popup.info;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pl.edu.pw.ee.aisd.pandemic.monument.Monument;

import java.io.IOException;

public final class MonumentPopup extends Stage {

    public MonumentPopup(final Monument monument) {
        this.initStyle(StageStyle.UNDECORATED);
        this.initScene(monument);
    }

    private void initScene(final Monument monument) {
        final FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/monumentInfo.fxml"));

        final Scene scene;
        try {
            scene = new Scene(loader.load());
        } catch (final IOException exception) {
            return;
        }

        ((Label) scene.lookup("#id")).setText("#" + monument.getID());
        ((Label) scene.lookup("#name")).setText(monument.getName());
        ((Label) scene.lookup("#pos")).setText(monument.getPositionString());

        ((Button) scene.lookup("#ok")).setOnAction(event -> this.close());
        this.setScene(scene);
    }

}
