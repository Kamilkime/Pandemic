package pl.edu.pw.ee.aisd.pandemic;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pl.edu.pw.ee.aisd.pandemic.data.DataStorage;
import pl.edu.pw.ee.aisd.pandemic.map.MapRenderer;
import pl.edu.pw.ee.aisd.pandemic.patient.Patient;
import pl.edu.pw.ee.aisd.pandemic.patient.PatientRenderer;
import pl.edu.pw.ee.aisd.pandemic.popup.AddPatientPopup;
import pl.edu.pw.ee.aisd.pandemic.popup.ClosePopup;
import pl.edu.pw.ee.aisd.pandemic.popup.ErrorPopup;
import pl.edu.pw.ee.aisd.pandemic.simulation.SimulationThread;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.atomic.AtomicBoolean;

public final class GUIController {

    private SimulationThread simulationThread;
    private final AtomicBoolean simulationRunning = new AtomicBoolean(false);

    private final DataStorage dataStorage = new DataStorage();

    @FXML
    private Label patientNumberLabel;

    @FXML
    private Button toggleSimulationButton;

    @FXML
    private TableView<Patient> patientTable;

    @FXML
    private Pane mapPane;

    @FXML
    private Slider simulationSpeedSlider;

    @FXML
    private synchronized void toggleSimulation() {
        if (this.simulationThread == null) {
            new ErrorPopup("NIE MOŻNA ROZPOCZĄĆ SYMULACJI", "NAJPIERW MUSISZ WCZYTAĆ MAPĘ").show();
            return;
        }

        this.simulationRunning.set(!this.simulationRunning.get());

        if (this.simulationRunning.get()) {
            this.toggleSimulationButton.setText("ZATRZYMAJ SYMULACJĘ");

            synchronized (Pandemic.class) {
                Pandemic.class.notifyAll();
            }
        } else {
            this.toggleSimulationButton.setText("ZACZNIJ SYMULACJĘ");
        }
    }

    @FXML
    private void loadMap() {
        if (this.simulationThread != null) {
            final Thread.State state = this.simulationThread.getState();

            if (state != Thread.State.WAITING) {
                new ErrorPopup("SYMULACJA JEST NADAL URUCHOMIONA", "NIE MOŻNA ZAŁADOWAĆ NOWEJ MAPY").show();
                return;
            }
        }

        final FileChooser fileChooser = new FileChooser();

        final File chosenFile = fileChooser.showOpenDialog(this.mapPane.getScene().getWindow());
        if (chosenFile == null || chosenFile.isDirectory()) {
            return;
        }

        try {
            if (!this.dataStorage.loadMap(chosenFile)) {
                return;
            }
        }  catch (final FileNotFoundException exception) {
            new ErrorPopup("BŁĄD KRYTYCZNY PROGRAMU", "NIE UDAŁO SIĘ WCZYTAĆ MAPY").show();
            return;
        }

        this.stopSimulationThread();

        MapRenderer.clear(this);
        PatientRenderer.clear(this);

        MapRenderer.show(this);

        this.simulationRunning.set(false);
        this.simulationThread = new SimulationThread(this);
        this.simulationThread.start();
    }

    @FXML
    private void addPatient() {
        if (this.dataStorage.getMapBounds() == null) {
            new ErrorPopup("NIE MOŻNA DODAĆ PACJENTA", "NAJPIERW MUSISZ WCZYTAĆ MAPĘ").show();
            return;
        }

        final AddPatientPopup addPatient = new AddPatientPopup();
        addPatient.showAndWait();

        if (addPatient.isCancelled()) {
            return;
        }

        this.dataStorage.addPatient(new Patient(1, addPatient.getXValue(), addPatient.getYValue()), this);
        PatientRenderer.refresh(this, false);
    }

    @FXML
    private void loadPatients() {
        if (this.dataStorage.getMapBounds() == null) {
            new ErrorPopup("NIE MOŻNA WCZYTAĆ PACJENTÓW", "NAJPIERW MUSISZ WCZYTAĆ MAPĘ").show();
            return;
        }

        final FileChooser fileChooser = new FileChooser();

        final File chosenFile = fileChooser.showOpenDialog(this.mapPane.getScene().getWindow());
        if (chosenFile == null || chosenFile.isDirectory()) {
            return;
        }

        try {
            if (!this.dataStorage.loadPatients(chosenFile, this)) {
                return;
            }
        }  catch (final FileNotFoundException exception) {
            new ErrorPopup("BŁĄD KRYTYCZNY PROGRAMU", "NIE UDAŁO SIĘ WCZYTAĆ PACJENTÓW").show();
            return;
        }

        PatientRenderer.refresh(this, false);
    }

    public void setup() {
        for (final TableColumn<Patient, ?> rawTableColumn : this.patientTable.getColumns()) {
            final TableColumn<Patient, String> tableColumn = (TableColumn<Patient, String>) rawTableColumn;

            if ("ID Pacjenta".equals(tableColumn.getText())) {
                tableColumn.setCellValueFactory(new PropertyValueFactory<>("idString"));
            } else if ("Pozycja początkowa".equals(tableColumn.getText())) {
                tableColumn.setCellValueFactory(new PropertyValueFactory<>("positionString"));
            } else {
                tableColumn.setCellValueFactory(new PropertyValueFactory<>("statusString"));
            }
        }

        final Scene guiScene = this.mapPane.getScene();
        guiScene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                new ClosePopup((Stage) guiScene.getWindow(), this).show();
            }
        });

        this.patientTable.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                new ClosePopup((Stage) guiScene.getWindow(), this).show();
            }
        });
    }

    public void stopSimulationThread() {
        if (this.simulationThread == null) {
            return;
        }

        try {
            this.simulationThread.terminate();

            this.simulationRunning.set(true);
            synchronized (Pandemic.class) {
                Pandemic.class.notifyAll();
            }

            this.simulationThread.join();
        } catch (final InterruptedException exception) {
            new ErrorPopup("BŁĄD KRYTYCZNY PROGRAMU", "NIE UDAŁO SIĘ ZATRZYMAĆ SYMULACJI").show();
        }
    }

    public boolean isSimulationRunning() {
        return this.simulationRunning.get();
    }

    public DataStorage getDataStorage() {
        return this.dataStorage;
    }

    public Label getPatientNumberLabel() {
        return this.patientNumberLabel;
    }

    public TableView<Patient> getPatientTable() {
        return this.patientTable;
    }

    public Pane getMapPane() {
        return this.mapPane;
    }

    public long getSimulationSpeed() {
        return (long) (5000L - this.simulationSpeedSlider.getValue());
    }

}
