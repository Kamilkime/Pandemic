package pl.edu.pw.ee.aisd.pandemic.popup;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public final class AddPatientPopup extends Stage {

    private boolean cancelled;

    private double x;
    private double y;

    public AddPatientPopup() {
        this.initStyle(StageStyle.UNDECORATED);
        this.initScene();
    }

    private void initScene() {
        final FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/addPatient.fxml"));

        final Scene scene;
        try {
            scene = new Scene(loader.load());
        } catch (final IOException exception) {
            return;
        }

        ((Button) scene.lookup("#add")).setOnAction(event -> {
            try {
                this.x = Double.parseDouble(((TextField) scene.lookup("#x")).getText());
            } catch (final NumberFormatException exception) {
                ((Label) scene.lookup("#error")).setText("WSP. X MUSI BYĆ LICZBĄ RZECZYWISTĄ");
                return;
            }

            try {
                this.y = Double.parseDouble(((TextField) scene.lookup("#y")).getText());
            } catch (final NumberFormatException exception) {
                ((Label) scene.lookup("#error")).setText("WSP. Y MUSI BYĆ LICZBĄ RZECZYWISTĄ");
                return;
            }

            this.close();
        });

        ((Button) scene.lookup("#cancel")).setOnAction(event -> {
            this.cancelled = true;
            this.close();
        });

        this.setScene(scene);
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public double getXValue() {
        return this.x;
    }

    public double getYValue() {
        return this.y;
    }

}
