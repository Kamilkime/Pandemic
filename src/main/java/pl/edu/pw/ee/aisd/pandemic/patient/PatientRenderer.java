package pl.edu.pw.ee.aisd.pandemic.patient;

import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import pl.edu.pw.ee.aisd.pandemic.GUIController;
import pl.edu.pw.ee.aisd.pandemic.map.MapBounds;
import pl.edu.pw.ee.aisd.pandemic.map.MapRenderer;
import pl.edu.pw.ee.aisd.pandemic.point.Point;
import pl.edu.pw.ee.aisd.pandemic.popup.info.PatientPopup;

public final class PatientRenderer {

    public static void add(final Patient patient, final GUIController guiController) {
        final String iconFileName;

        if (patient.getStatus() == PatientStatus.OUTSIDE_BORDERS) {
            iconFileName = "/img/PatientOut.png";
        } else {
            iconFileName = "/img/PatientIn.png";
        }

        final ImageView icon = new ImageView();
        icon.setId("patient-" + patient.getID());
        icon.setImage(new Image(MapRenderer.class.getResourceAsStream(iconFileName)));

        final Point iconPoint = guiController.getDataStorage().getMapBounds().normalizeForPane(patient);

        icon.setLayoutX(Math.max(0.0D, Math.min(MapBounds.PANE_RIGHT_X, iconPoint.getX() - 15.0D)));
        icon.setLayoutY(Math.max(0.0D, Math.min(MapBounds.PANE_BOTTOM_Y, iconPoint.getY() - 15.0D)));

        icon.setOnMouseClicked(event -> new PatientPopup(patient, guiController.getDataStorage().getPatients()).show());

        guiController.getMapPane().getChildren().add(icon);
        guiController.getPatientTable().getItems().add(patient);
    }

    public static void refresh(final GUIController guiController, final boolean refreshTable) {
        int patientsLeft = 0;
        for (final Patient patient : guiController.getDataStorage().getPatients()) {
            final PatientStatus status = patient.getStatus();
            if (status == PatientStatus.WAITING || status == PatientStatus.FINDING_HOSPITAL || status == PatientStatus.IN_TRANSIT) {
                patientsLeft++;
            }
        }

        guiController.getPatientNumberLabel().setText(String.format("LISTA PACJENTÓW (%d POZOSTAŁYCH DO SYMULACJI)", patientsLeft));

        if (refreshTable) {
            final TableView<Patient> patientTable = guiController.getPatientTable();

            patientTable.getItems().removeAll(patientTable.getItems());
            patientTable.getItems().addAll(guiController.getDataStorage().getPatients());
        }
    }

    public static void clear(final GUIController guiController) {
        guiController.getPatientTable().getItems().clear();
        guiController.getPatientNumberLabel().setText("LISTA PACJENTÓW (0 POZOSTAŁYCH DO SYMULACJI)");
    }

    private PatientRenderer() {}

}
