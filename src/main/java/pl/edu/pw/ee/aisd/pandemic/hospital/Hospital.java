package pl.edu.pw.ee.aisd.pandemic.hospital;

import pl.edu.pw.ee.aisd.pandemic.data.DataStorage;
import pl.edu.pw.ee.aisd.pandemic.popup.ErrorPopup;
import pl.edu.pw.ee.aisd.pandemic.road.node.RoadNode;

import java.util.HashMap;
import java.util.Map;

public final class Hospital extends RoadNode {

    private final int id;
    private final String name;
    private final int totalBeds;
    private int freeBeds;
    private int queue;

    private final Map<Hospital, HospitalConnection> hospitalConnections = new HashMap<>();

    public Hospital(final int id, final double x, final double y, final String name, final int totalBeds, final int freeBeds) {
        super(x, y);

        this.id = id;
        this.name = name;
        this.totalBeds = totalBeds;
        this.freeBeds = freeBeds;
    }

    public int getID() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getTotalBeds() {
        return this.totalBeds;
    }

    public int getFreeBeds() {
        return this.freeBeds;
    }

    public void occupyBed() {
        this.freeBeds--;
    }

    public int getQueue() {
        return this.queue;
    }

    public void addToQueue() {
        this.queue++;
    }

    public Map<Hospital, HospitalConnection> getHospitalConnections() {
        return this.hospitalConnections;
    }

    public void addHospitalConnection(final Hospital target, final HospitalConnection connection) {
        this.hospitalConnections.put(target, connection);
    }

    public static Hospital fromString(final String line) throws IllegalArgumentException {
        final String[] lineSplit = DataStorage.LINE_SPLIT_PATTERN.split(line);

        if (lineSplit.length != 6) {
            new ErrorPopup("ZŁA LICZBA PARAMETRÓW, WYMAGANE: 6", "LINIA: " + line).show();
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
            x = Double.parseDouble(lineSplit[2]);
        } catch (final NumberFormatException exception) {
            new ErrorPopup("WSPÓŁRZĘDNA X MUSI BYĆ LICZBĄ RZECZYWISTĄ", "LINIA: " + line).show();
            throw new IllegalArgumentException();
        }

        final double y;
        try {
            y = Double.parseDouble(lineSplit[3]);
        } catch (final NumberFormatException exception) {
            new ErrorPopup("WSPÓŁRZĘDNA Y MUSI BYĆ LICZBĄ RZECZYWISTĄ", "LINIA: " + line).show();
            throw new IllegalArgumentException();
        }

        final int totalBeds;
        try {
            totalBeds = Integer.parseInt(lineSplit[4]);

            if (totalBeds <= 0) {
                throw new NumberFormatException();
            }
        } catch (final NumberFormatException exception) {
            new ErrorPopup("LICZBA ŁÓŻEK MUSI BYĆ LICZBĄ CAŁKOWITĄ, DODATNIĄ", "LINIA: " + line).show();
            throw new IllegalArgumentException();
        }

        final int freeBeds;
        try {
            freeBeds = Integer.parseInt(lineSplit[5]);

            if (freeBeds > totalBeds) {
                throw new NumberFormatException();
            }
        } catch (final NumberFormatException exception) {
            new ErrorPopup("LICZBA WOLNYCH ŁÓŻEK MUSI BYĆ\n      MNIEJSZA NIŻ LICZBA ŁÓŻEK", "LINIA: " + line).show();
            throw new IllegalArgumentException();
        }

        return new Hospital(id, x, y, lineSplit[1], totalBeds, freeBeds);
    }

}
