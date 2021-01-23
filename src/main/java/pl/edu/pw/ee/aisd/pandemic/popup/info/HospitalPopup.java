package pl.edu.pw.ee.aisd.pandemic.popup.info;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pl.edu.pw.ee.aisd.pandemic.hospital.Hospital;

import java.io.IOException;

public final class HospitalPopup extends Stage {

    public HospitalPopup(final Hospital hospital) {
        this.initStyle(StageStyle.UNDECORATED);
        this.initScene(hospital);
    }

    private void initScene(final Hospital hospital) {
        final FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/hospitalInfo.fxml"));

        final Scene scene;
        try {
            scene = new Scene(loader.load());
        } catch (final IOException exception) {
            return;
        }

        ((Label) scene.lookup("#id")).setText("#" + hospital.getID());
        ((Label) scene.lookup("#name")).setText(hospital.getName());
        ((Label) scene.lookup("#pos")).setText(hospital.getPositionString());
        ((Label) scene.lookup("#beds")).setText(String.valueOf(hospital.getTotalBeds()));
        ((Label) scene.lookup("#freebeds")).setText(String.valueOf(hospital.getFreeBeds()));
        ((Label) scene.lookup("#queue")).setText(String.valueOf(hospital.getQueue()));

        ((Button) scene.lookup("#ok")).setOnAction(event -> this.close());
        this.setScene(scene);
    }

}
