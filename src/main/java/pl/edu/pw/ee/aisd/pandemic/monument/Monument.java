package pl.edu.pw.ee.aisd.pandemic.monument;

import pl.edu.pw.ee.aisd.pandemic.data.DataStorage;
import pl.edu.pw.ee.aisd.pandemic.point.Point;
import pl.edu.pw.ee.aisd.pandemic.popup.ErrorPopup;

public final class Monument extends Point {

    private final int id;
    private final String name;

    public Monument(final int id, final double x, final double y, final String name) {
        super(x, y);

        this.id = id;
        this.name = name;
    }

    public int getID() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public static Monument fromString(final String line) throws IllegalArgumentException {
        final String[] lineSplit = DataStorage.LINE_SPLIT_PATTERN.split(line);

        if (lineSplit.length != 4) {
            new ErrorPopup("ZŁA LICZBA PARAMETRÓW, WYMAGANE: 4", "LINIA: " + line).show();
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

        return new Monument(id, x, y, lineSplit[1]);
    }

}
