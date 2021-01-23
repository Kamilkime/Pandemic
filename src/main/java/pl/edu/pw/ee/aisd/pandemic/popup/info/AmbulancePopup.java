package pl.edu.pw.ee.aisd.pandemic.popup.info;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pl.edu.pw.ee.aisd.pandemic.patient.Patient;
import pl.edu.pw.ee.aisd.pandemic.patient.PatientStatus;
import pl.edu.pw.ee.aisd.pandemic.simulation.Ambulance;

import java.io.IOException;

public final class AmbulancePopup extends Stage {

    public AmbulancePopup(final Ambulance ambulance) {
        this.initStyle(StageStyle.UNDECORATED);
        this.initScene(ambulance);
    }

    private void initScene(final Ambulance ambulance) {
        final FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/ambulanceInfo.fxml"));

        final Scene scene;
        try {
            scene = new Scene(loader.load());
        } catch (final IOException exception) {
            return;
        }

        final Patient patient = ambulance.getCurrentPatient();

        if (patient == null) {
            ((Label) scene.lookup("#patient")).setText("BRAK PACJENTA");
            ((Label) scene.lookup("#distance")).setText("-");
            ((Label) scene.lookup("#hospital")).setText("-");
        } else {
            ((Label) scene.lookup("#patient")).setText("#" + patient.getID());
            ((Label) scene.lookup("#distance")).setText((Math.round(ambulance.getDistanceTravelled() * 100.0D) / 100.0D) + " km");

            if (patient.getStatus() == PatientStatus.FINDING_HOSPITAL) {
                ((Label) scene.lookup("#hospital")).setText("WYSZUKIWANIE");
            } else {
                ((Label) scene.lookup("#hospital")).setText("#" + patient.getTargetHospital().getID());
            }

        }

        ((Button) scene.lookup("#ok")).setOnAction(event -> this.close());
        this.setScene(scene);
    }

}
