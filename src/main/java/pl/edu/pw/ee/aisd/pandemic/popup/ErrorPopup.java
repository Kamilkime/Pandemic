package pl.edu.pw.ee.aisd.pandemic.popup;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public final class ErrorPopup extends Stage {

    public ErrorPopup(final String title, final String subtitle) {
        this.initStyle(StageStyle.UNDECORATED);
        this.initScene(title, subtitle);
    }

    private void initScene(final String title, final String subtitle) {
        final FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/error.fxml"));

        final Scene scene;
        try {
            scene = new Scene(loader.load());
        } catch (final IOException exception) {
            return;
        }

        final Label titleLabel = (Label) scene.lookup("#title");
        titleLabel.setText(title);
        titleLabel.setAlignment(Pos.CENTER);
        titleLabel.setWrapText(true);

        final Label subtitleLabel = (Label) scene.lookup("#subtitle");
        subtitleLabel.setText(subtitle);
        subtitleLabel.setAlignment(Pos.CENTER);
        subtitleLabel.setWrapText(true);

        ((Button) scene.lookup("#ok")).setOnAction(event -> this.close());
        this.setScene(scene);
    }

}
