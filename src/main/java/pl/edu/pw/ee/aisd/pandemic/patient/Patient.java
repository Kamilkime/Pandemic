package pl.edu.pw.ee.aisd.pandemic.patient;

import pl.edu.pw.ee.aisd.pandemic.data.DataStorage;
import pl.edu.pw.ee.aisd.pandemic.hospital.Hospital;
import pl.edu.pw.ee.aisd.pandemic.point.Point;
import pl.edu.pw.ee.aisd.pandemic.popup.ErrorPopup;

import java.util.HashSet;
import java.util.Set;

public final class Patient extends Point {

    private int id;

    private PatientStatus status;
    private Hospital targetHospital;
    private final Set<Hospital> visitedHospitals;

    public Patient(final int id, final double x, final double y) {
        super(x, y);

        this.id = id;
        this.visitedHospitals = new HashSet<>();
    }

    public int getID() {
        return this.id;
    }

    public void updateID(final int id) {
        this.id = id;
    }

    public PatientStatus getStatus() {
        return this.status;
    }

    public void updateStatus(final PatientStatus status) {
        this.status = status;
    }

    public Hospital getTargetHospital() {
        return this.targetHospital;
    }

    public void setTargetHospital(final Hospital targetHospital) {
        this.targetHospital = targetHospital;
    }

    public Set<Hospital> getVisitedHospitals() {
        return this.visitedHospitals;
    }

    public void addVisitedHospital(final Hospital hospital) {
        this.visitedHospitals.add(hospital);
    }

    public String getIdString() {
        return "#" + this.id;
    }

    public String getStatusString() {
        return this.status.getStatusString(this.targetHospital);
    }

    public static Patient fromString(final String line) throws IllegalArgumentException {
        final String[] lineSplit = DataStorage.LINE_SPLIT_PATTERN.split(line);

        if (lineSplit.length != 3) {
            new ErrorPopup("ZŁA LICZBA PARAMETRÓW, WYMAGANE: 3", "LINIA: " + line).show();
            throw new IllegalArgumentException();
        }

        final int id;
        try {
            id = Integer.parseInt(lineSplit[0]);
        } catch (final NumberFormatException exception) {
            new ErrorPopup("ID MUSI BYĆ LICZBĄ CAŁKOWITĄ", "LINIA: " + line).show();
            throw new IllegalArgumentException();
        }

        final double x;
        try {
            x = Double.parseDouble(lineSplit[1]);
        } catch (final NumberFormatException exception) {
            new ErrorPopup("WSPÓŁRZĘDNA X MUSI BYĆ LICZBĄ RZECZYWISTĄ", "LINIA: " + line).show();
            throw new IllegalArgumentException();
        }

        final double y;
        try {
            y = Double.parseDouble(lineSplit[2]);
        } catch (final NumberFormatException exception) {
            new ErrorPopup("WSPÓŁRZĘDNA Y MUSI BYĆ LICZBĄ RZECZYWISTĄ", "LINIA: " + line).show();
            throw new IllegalArgumentException();
        }

        return new Patient(id, x, y);
    }

}
