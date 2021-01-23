package pl.edu.pw.ee.aisd.pandemic.data;

import pl.edu.pw.ee.aisd.pandemic.GUIController;
import pl.edu.pw.ee.aisd.pandemic.data.file.MapDataLoader;
import pl.edu.pw.ee.aisd.pandemic.data.file.PatientDataLoader;
import pl.edu.pw.ee.aisd.pandemic.hospital.Hospital;
import pl.edu.pw.ee.aisd.pandemic.hospital.HospitalConnection;
import pl.edu.pw.ee.aisd.pandemic.map.MapBounds;
import pl.edu.pw.ee.aisd.pandemic.monument.Monument;
import pl.edu.pw.ee.aisd.pandemic.patient.Patient;
import pl.edu.pw.ee.aisd.pandemic.patient.PatientRenderer;
import pl.edu.pw.ee.aisd.pandemic.patient.PatientStatus;
import pl.edu.pw.ee.aisd.pandemic.point.Point;
import pl.edu.pw.ee.aisd.pandemic.popup.ErrorPopup;
import pl.edu.pw.ee.aisd.pandemic.road.Road;
import pl.edu.pw.ee.aisd.pandemic.road.node.RoadNode;
import pl.edu.pw.ee.aisd.pandemic.util.PolygonUtil;
import pl.edu.pw.ee.aisd.pandemic.util.RouteUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Pattern;

public final class DataStorage {

    public static final Pattern LINE_SPLIT_PATTERN = Pattern.compile("\\s+\\|\\s+");

    private final Map<Integer, Patient> patientsByID = new HashMap<>();
    private final List<Patient> patients = new CopyOnWriteArrayList<>();

    private Collection<Hospital> hospitals;
    private Collection<Monument> monuments;
    private Collection<Road> roads;

    private MapBounds mapBounds;

    public boolean loadMap(final File inFile) throws FileNotFoundException {
        final MapDataLoader dataLoader = new MapDataLoader(inFile);
        if (!dataLoader.loadLines()) {
            return false;
        }

        final Map<Integer, Hospital> hospitals = dataLoader.loadHospitals();
        if (hospitals.isEmpty()) {
            return false;
        }

        final Map<Integer, Monument> monuments = dataLoader.loadMonuments();
        if (monuments.isEmpty()) {
            return false;
        }

        final Set<Road> roads = dataLoader.loadRoads(hospitals);
        if (roads.isEmpty()) {
            return false;
        }

        final List<Point> mapPoints = new ArrayList<>(hospitals.values());
        mapPoints.addAll(monuments.values());

        final MapBounds mapBounds = new MapBounds(mapPoints);
        if (mapBounds.getOutsidePoint() == null) {
            new ErrorPopup("NIE UDAŁO SIĘ WCZYTAĆ MAPY", "OBSZAR KRAJU NIE ZOSTAŁ WYZNACZONY").show();
            return false;
        }

        final Set<RoadNode> roadNodes = new HashSet<>();
        for (final Road road : roads) {
            road.getStart().addConnection(road.getFinish(), road.getDistance());
            road.getFinish().addConnection(road.getStart(), road.getDistance());

            roadNodes.add(road.getStart());
            roadNodes.add(road.getFinish());
        }

        for (final Hospital hospital : hospitals.values()) {
            for (final Hospital otherHospital : hospitals.values()) {
                if (hospital.equals(otherHospital)) {
                    continue;
                }

                final List<RoadNode> route = RouteUtil.findRoute(hospital, otherHospital, roadNodes);
                if (route.size() < 2) {
                    continue;
                }

                hospital.addHospitalConnection(otherHospital, new HospitalConnection(route));
            }
        }

        this.hospitals = hospitals.values();
        this.monuments = monuments.values();
        this.mapBounds = mapBounds;
        this.roads = roads;
        this.patients.clear();
        this.patientsByID.clear();

        return true;
    }

    public boolean loadPatients(final File inFile, final GUIController guiController) throws FileNotFoundException {
        final PatientDataLoader dataLoader = new PatientDataLoader(inFile);
        if (!dataLoader.loadLines()) {
            return false;
        }

        final List<Patient> patients = dataLoader.loadPatients();
        if (patients.isEmpty()) {
            return false;
        }

        for (final Patient patient : patients) {
            this.addPatient(patient, guiController);
        }

        return true;
    }

    public void addPatient(final Patient patient, final GUIController guiController) {
        if (PolygonUtil.isInsidePolygon(patient, this.mapBounds)) {
            patient.updateStatus(PatientStatus.WAITING);
        } else {
            patient.updateStatus(PatientStatus.OUTSIDE_BORDERS);
        }

        if (this.patientsByID.containsKey(patient.getID())) {
            int newID = 1;
            while (this.patientsByID.containsKey(newID)) {
                newID++;
            }

            patient.updateID(newID);
        }

        PatientRenderer.add(patient, guiController);

        this.patients.add(patient);
        this.patientsByID.put(patient.getID(), patient);
    }

    public List<Patient> getPatients() {
        return this.patients;
    }

    public Collection<Hospital> getHospitals() {
        return this.hospitals;
    }

    public Collection<Monument> getMonuments() {
        return this.monuments;
    }

    public Collection<Road> getRoads() {
        return this.roads;
    }

    public MapBounds getMapBounds() {
        return this.mapBounds;
    }

    public void setMapBounds(final MapBounds mapBounds) {
        this.mapBounds = mapBounds;
    }

}
