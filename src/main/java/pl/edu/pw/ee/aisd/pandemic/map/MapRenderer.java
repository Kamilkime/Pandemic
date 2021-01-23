package pl.edu.pw.ee.aisd.pandemic.map;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeLineCap;
import pl.edu.pw.ee.aisd.pandemic.GUIController;
import pl.edu.pw.ee.aisd.pandemic.data.DataStorage;
import pl.edu.pw.ee.aisd.pandemic.hospital.Hospital;
import pl.edu.pw.ee.aisd.pandemic.monument.Monument;
import pl.edu.pw.ee.aisd.pandemic.point.Point;
import pl.edu.pw.ee.aisd.pandemic.popup.info.HospitalPopup;
import pl.edu.pw.ee.aisd.pandemic.popup.info.MonumentPopup;
import pl.edu.pw.ee.aisd.pandemic.road.Road;

public final class MapRenderer {

    public static void show(final GUIController guiController) {
        final DataStorage dataStorage = guiController.getDataStorage();
        final MapBounds mapBounds = dataStorage.getMapBounds();

        final Polygon country = new Polygon();
        for (final Point vertex : mapBounds.getCountryPolygon()) {
            final Point point = mapBounds.normalizeForPane(vertex);

            country.getPoints().add(point.getX());
            country.getPoints().add(point.getY());
        }

        country.setFill(Paint.valueOf("#BEF5A8"));

        country.setLayoutX(0.0D);
        country.setLayoutY(0.0D);

        guiController.getMapPane().getChildren().add(country);

        for (final Road road : dataStorage.getRoads()) {
            final Line roadLine = new Line();

            roadLine.setStrokeLineCap(StrokeLineCap.ROUND);
            roadLine.setStrokeWidth(5.0D);

            final Point start = mapBounds.normalizeForPane(road.getStart());
            final Point end = mapBounds.normalizeForPane(road.getFinish());

            roadLine.setStartX(start.getX());
            roadLine.setStartY(start.getY());
            roadLine.setEndX(end.getX());
            roadLine.setEndY(end.getY());

            guiController.getMapPane().getChildren().add(roadLine);
        }

        for (final Hospital hospital : dataStorage.getHospitals()) {
            MapRenderer.showIcon(hospital, "/img/Hospital.png", guiController);
        }

        for (final Monument monument : dataStorage.getMonuments()) {
            MapRenderer.showIcon(monument, "/img/Monument.png", guiController);
        }
    }

    private static void showIcon(final Point point, final String fileName, final GUIController guiController) {
        final ImageView icon = new ImageView();
        icon.setImage(new Image(MapRenderer.class.getResourceAsStream(fileName)));

        final Point iconPoint = guiController.getDataStorage().getMapBounds().normalizeForPane(point);

        icon.setLayoutX(iconPoint.getX() - 15.0D);
        icon.setLayoutY(iconPoint.getY() - 15.0D);

        if (point instanceof Hospital) {
            icon.setOnMouseClicked(event -> new HospitalPopup((Hospital) point).show());
        } else {
            icon.setOnMouseClicked(event -> new MonumentPopup((Monument) point).show());
        }

        guiController.getMapPane().getChildren().add(icon);
    }

    public static void clear(final GUIController guiController) {
        guiController.getMapPane().getChildren().clear();
    }

    private MapRenderer() {}

}
