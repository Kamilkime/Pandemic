package pl.edu.pw.ee.aisd.pandemic.simulation;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import pl.edu.pw.ee.aisd.pandemic.GUIController;
import pl.edu.pw.ee.aisd.pandemic.Pandemic;
import pl.edu.pw.ee.aisd.pandemic.data.DataStorage;
import pl.edu.pw.ee.aisd.pandemic.hospital.Hospital;
import pl.edu.pw.ee.aisd.pandemic.hospital.HospitalConnection;
import pl.edu.pw.ee.aisd.pandemic.map.MapBounds;
import pl.edu.pw.ee.aisd.pandemic.patient.Patient;
import pl.edu.pw.ee.aisd.pandemic.patient.PatientRenderer;
import pl.edu.pw.ee.aisd.pandemic.patient.PatientStatus;
import pl.edu.pw.ee.aisd.pandemic.point.Point;
import pl.edu.pw.ee.aisd.pandemic.popup.ErrorPopup;
import pl.edu.pw.ee.aisd.pandemic.popup.info.AmbulancePopup;
import pl.edu.pw.ee.aisd.pandemic.road.node.PickupPoint;
import pl.edu.pw.ee.aisd.pandemic.road.node.RoadNode;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class SimulationThread extends Thread {

    private boolean systemCollapsed;
    private final AtomicBoolean running = new AtomicBoolean(true);

    private final Ambulance ambulance = new Ambulance();
    private final ImageView ambulanceIcon = new ImageView();

    private final GUIController guiController;

    public SimulationThread(final GUIController guiController) {
        this.guiController = guiController;

        this.guiController.getMapPane().getChildren().add(this.ambulanceIcon);

        this.ambulanceIcon.setImage(new Image(SimulationThread.class.getResourceAsStream("/img/Ambulance.png")));
        this.ambulanceIcon.setOnMouseClicked(event -> new AmbulancePopup(this.ambulance).show());

        this.hideAmbulance();
    }

    @Override
    public void run() {
        while (this.running.get()) {
            this.waitUntilEnabled();

            if (!this.running.get()) {
                return;
            }

            final DataStorage dataStorage = this.guiController.getDataStorage();

            if (this.ambulance.getCurrentPatient() == null) {
                Patient newPatient = null;
                for (final Patient patient : dataStorage.getPatients()) {
                    if (patient.getStatus() != PatientStatus.WAITING) {
                        continue;
                    }

                    newPatient = patient;
                    break;
                }

                if (newPatient == null) {
                    this.sleepBeforeNextStep();
                    continue;
                }

                this.ambulance.loadNewPatient(newPatient);

                this.sleepBeforeNextStep();
                continue;
            }

            final Patient patient = this.ambulance.getCurrentPatient();
            final PatientStatus patientStatus = patient.getStatus();

            if (patientStatus == PatientStatus.WAITING) {
                final Pane mapPane = this.guiController.getMapPane();
                final Node patientIcon = mapPane.lookup("#patient-" + patient.getID());

                Platform.runLater(() -> mapPane.getChildren().remove(patientIcon));

                this.moveAmbulance(patient);
                patient.updateStatus(PatientStatus.FINDING_HOSPITAL);
            } else if (patientStatus == PatientStatus.FINDING_HOSPITAL) {
                final Set<Hospital> visitedHospitals = patient.getVisitedHospitals();

                double lowestDistance = Double.MAX_VALUE;
                if (visitedHospitals.isEmpty()) {
                    Hospital closestHospital = null;

                    for (final Hospital hospital : dataStorage.getHospitals()) {
                        final double distance = hospital.distanceSquared(patient);
                        if (distance >= lowestDistance) {
                            continue;
                        }

                        lowestDistance = distance;
                        closestHospital = hospital;
                    }

                    this.ambulance.setNewRoute(Arrays.asList(new PickupPoint(patient), closestHospital));

                    patient.setTargetHospital(closestHospital);
                    patient.updateStatus(PatientStatus.IN_TRANSIT);
                } else {
                    final Hospital targetHospital = patient.getTargetHospital();
                    List<RoadNode> newRoute = null;

                    for (final Map.Entry<Hospital, HospitalConnection> connectionEntry : targetHospital.getHospitalConnections().entrySet()) {
                        if (visitedHospitals.contains(connectionEntry.getKey())) {
                            continue;
                        }

                        final double distance = connectionEntry.getValue().getTotalDistance();
                        if (distance >= lowestDistance) {
                            continue;
                        }

                        lowestDistance = distance;
                        newRoute = connectionEntry.getValue().getRoute();
                    }

                    if (newRoute == null) {
                        if (!this.systemCollapsed) {
                            this.systemCollapsed = true;
                            Platform.runLater(() -> new ErrorPopup("ZAŁAMANIE SYSTEMU ZDROWOTNEGO", "W ŻADNYM SZPITALU NIE MA JUŻ WOLNYCH ŁÓŻEK").show());
                        }

                        targetHospital.addToQueue();
                        patient.updateStatus(PatientStatus.IN_QUEUE);
                    } else {
                        this.ambulance.setNewRoute(newRoute);

                        patient.setTargetHospital((Hospital) newRoute.get(newRoute.size() - 1));
                        patient.updateStatus(PatientStatus.IN_TRANSIT);
                    }
                }

            } else if (patientStatus == PatientStatus.IN_TRANSIT) {
                final RoadNode nextNode = this.ambulance.getNextStop();

                final double distanceLeft = this.getDistance();
                if (distanceLeft <= 100.0D) {
                    this.ambulance.travel(distanceLeft);

                    if (this.ambulance.getRouteLength() == 2) {
                        final Hospital targetHospital = (Hospital) nextNode;
                        patient.addVisitedHospital(targetHospital);

                        if (targetHospital.getFreeBeds() == 0) {
                            patient.updateStatus(PatientStatus.FINDING_HOSPITAL);
                        } else {
                            patient.updateStatus(PatientStatus.IN_HOSPITAL);
                            targetHospital.occupyBed();
                        }

                        this.ambulance.move(targetHospital);
                        this.hideAmbulance();
                    } else {
                        this.ambulance.removeLastStop();
                        this.moveAmbulance(nextNode);
                    }
                } else {
                    this.moveAmbulance(this.getNextPoint(distanceLeft));
                    this.ambulance.travel(100.0D);
                }
            } else {
                this.ambulance.empty();
            }

            Platform.runLater(() -> PatientRenderer.refresh(this.guiController, true));
            this.sleepBeforeNextStep();
        }
    }

    public void terminate() {
        this.running.set(false);
    }

    private void waitUntilEnabled() {
        while (!this.guiController.isSimulationRunning()) {
            try {
                synchronized (Pandemic.class) {
                    Pandemic.class.wait();
                }
            } catch (final InterruptedException exception) {
                Platform.runLater(() -> new ErrorPopup("BŁĄD KRYTYCZNY PROGRAMU", "NIE UDAŁO SIĘ ZAWIESIĆ SYMULACJI").show());
            }
        }
    }

    private void sleepBeforeNextStep() {
        try {
            Thread.sleep(this.guiController.getSimulationSpeed());
        } catch (final InterruptedException ignored) {}
    }

    private void moveAmbulance(final Point point) {
        this.ambulance.move(point);

        final Point iconPoint = this.guiController.getDataStorage().getMapBounds().normalizeForPane(point);

        Platform.runLater(() -> {
            this.ambulanceIcon.setLayoutX(Math.max(0.0D, Math.min(MapBounds.PANE_RIGHT_X, iconPoint.getX() - 15.0D)));
            this.ambulanceIcon.setLayoutY(Math.max(0.0D, Math.min(MapBounds.PANE_BOTTOM_Y, iconPoint.getY() - 15.0D)));
            this.ambulanceIcon.setVisible(true);
        });
    }

    private void hideAmbulance() {
        Platform.runLater(() -> {
            this.ambulanceIcon.setVisible(false);
            this.ambulanceIcon.setLayoutX(0.0D);
            this.ambulanceIcon.setLayoutY(0.0D);
        });
    }

    private Point getNextPoint(final double routeDistance) {
        final Point current = this.ambulance.getCurrentPosition();
        final RoadNode start = this.ambulance.getLastStop();
        final RoadNode destination = this.ambulance.getNextStop();

        final double distance2D = start.distance(destination);
        final double multiplier = 100 * distance2D / routeDistance;

        final double x = current.getX() + ((destination.getX() - current.getX()) / distance2D * multiplier);
        final double y = current.getY() + ((destination.getY() - current.getY()) / distance2D * multiplier);

        return Point.of(x, y);
    }

    private double getDistance() {
        final Point current = this.ambulance.getCurrentPosition();
        final RoadNode start = this.ambulance.getLastStop();
        final RoadNode destination = this.ambulance.getNextStop();

        final double routeDistance;
        if (start instanceof PickupPoint) {
            routeDistance = 300.0D;
        } else {
            routeDistance = start.getDistance(destination);
        }

        final double routeDistance2D = start.distance(destination);
        final double distance2D = current.distance(destination);

        return distance2D * routeDistance / routeDistance2D;
    }

}
