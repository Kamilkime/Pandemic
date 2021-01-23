package pl.edu.pw.ee.aisd.pandemic.popup.info;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pl.edu.pw.ee.aisd.pandemic.patient.Patient;
import pl.edu.pw.ee.aisd.pandemic.patient.PatientStatus;

import java.io.IOException;
import java.util.List;

public final class PatientPopup extends Stage {

    public PatientPopup(final Patient patient, final List<Patient> patients) {
        this.initStyle(StageStyle.UNDECORATED);
        this.initScene(patient, patients);
    }

    private void initScene(final Patient patient, final List<Patient> patients) {
        final FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/patientInfo.fxml"));

        final Scene scene;
        try {
            scene = new Scene(loader.load());
        } catch (final IOException exception) {
            return;
        }

        int queue = 1;
        for (final Patient otherPatient : patients) {
            if (otherPatient.equals(patient)) {
                break;
            }

            final PatientStatus status = otherPatient.getStatus();
            if (status == PatientStatus.IN_HOSPITAL || status == PatientStatus.IN_QUEUE) {
                continue;
            }

            queue++;
        }

        ((Label) scene.lookup("#id")).setText("#" + patient.getID());
        ((Label) scene.lookup("#pos")).setText(patient.getPositionString());

        if (patient.getStatus() == PatientStatus.OUTSIDE_BORDERS) {
            ((Label) scene.lookup("#queue")).setText("NIGDY - POZA GRANICĄ");
        } else {
            ((Label) scene.lookup("#queue")).setText(String.valueOf(queue));
        }

        ((Button) scene.lookup("#ok")).setOnAction(event -> this.close());
        this.setScene(scene);
    }

}
