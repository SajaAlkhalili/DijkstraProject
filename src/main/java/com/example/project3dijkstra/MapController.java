package com.example.project3dijkstra;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.util.Pair;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.ResourceBundle;

public class MapController implements Initializable {


    public TextArea pathTextArea;
    @FXML
    private
    HBox mapContainer;
    @FXML
    private ImageView mapImg;
    @FXML
    private Group mapGroup;
    @FXML
    private ComboBox sourceCombo;
    @FXML
    private ComboBox destCombo;
    @FXML
    private Label pathLabel;
    @FXML
    private Label distanceLabel;
    @FXML
    private Button runBtn;


    private String shortestPath;
    private double totalDistance;
    private Graph graph = Graph.getInstance();




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        prepareGraph();
        Platform.runLater(this::showCitiesOnMap);
        prepareCombos();
        runBtn.setOnAction(this::findPath);

    }



    private void findPath(Event e) {

        City src = graph.getSingleCity((String) sourceCombo.getValue());
        City dst = graph.getSingleCity((String) destCombo.getValue());
        if(src == null || dst == null){
            System.out.println("You have to choose a city");
            return;
        }




        List<City> path = graph.shortestPath(src, dst);

        highlightPath(path);
        distanceLabel.setText(String.format("Distance = %.2f KM", totalDistance));
        pathTextArea.setText(shortestPath);

    }



    private void highlightPath(List<City> path) {

        resetMap();
        System.out.println(path);
        City firstCity = path.get(0);
        StringBuilder sb = new StringBuilder();
        sb.append(firstCity.getName());

        for (int i = 1; i < path.size(); i++) {

            if(!path.get(i).getName().matches("S\\d+"))
            sb.append(" -> "+path.get(i).getName());
            setLine(firstCity, path.get(i));
            totalDistance += firstCity.getNeighbors().get(path.get(i));
            firstCity = path.get(i);

        }
        shortestPath = sb.toString();

    }


    private void resetMap() {

        totalDistance = 0;
        shortestPath = "";

        Node[] lines = mapGroup.getChildren().toArray(new Node[0]);
        for (int i = 0; i < lines.length; i++) {
            if (lines[i] instanceof Line) {
                mapGroup.getChildren().remove(lines[i]);
            }
        }

    }



//    private void setLine(City src, City dst) {
//
//        Line line = new Line();
//        line.setStroke(Color.BLUE);
//        line.setStrokeWidth(3);
//
//        Pair<Double, Double> srcCoor = convertMapCorToImgCor(src.getLat(), src.getLon());
//        line.setStartX(srcCoor.getKey());
//        line.setStartY(srcCoor.getValue());
//
//        Pair<Double, Double> dstCoor = convertMapCorToImgCor(dst.getLat(), dst.getLon());
//        line.setStartX(srcCoor.getKey());
//        line.setStartY(srcCoor.getValue());
//
//        line.setEndX(dstCoor.getKey());
//        line.setEndY(dstCoor.getValue());
//        mapGroup.getChildren().add(line);
//
//    }

    private void setLine(City src, City dst) {

        Line line = new Line();
        line.setStroke(Color.BLUE);
        line.setStrokeWidth(3);

        Pair<Double, Double> srcCoor = convertMapCorToImgCor(src.getLat(), src.getLon());
        line.setStartX(srcCoor.getKey());
        line.setStartY(srcCoor.getValue());

        Pair<Double, Double> dstCoor = convertMapCorToImgCor(dst.getLat(), dst.getLon());
        line.setEndX(dstCoor.getKey());
        line.setEndY(dstCoor.getValue());

        // Add an arrow at the end of the line
        double arrowSize = 10.0;
        double angle = Math.atan2(dstCoor.getValue() - srcCoor.getValue(), dstCoor.getKey() - srcCoor.getKey());

        double arrowX = dstCoor.getKey() - arrowSize * Math.cos(angle - Math.PI / 6);
        double arrowY = dstCoor.getValue() - arrowSize * Math.sin(angle - Math.PI / 6);

        Line arrowLine1 = new Line(dstCoor.getKey(), dstCoor.getValue(), arrowX, arrowY);
        Line arrowLine2 = new Line(dstCoor.getKey(), dstCoor.getValue(), arrowX + arrowSize * Math.cos(angle + Math.PI / 6),
                arrowY + arrowSize * Math.sin(angle + Math.PI / 6));

        arrowLine1.setStroke(Color.BLUE);
        arrowLine1.setStrokeWidth(3);
        arrowLine2.setStroke(Color.BLUE);
        arrowLine2.setStrokeWidth(3);

        mapGroup.getChildren().addAll(line, arrowLine1, arrowLine2);

    }

    private void prepareCombos() {

        List<City> list = graph.getCities();
        List<String> names = list.stream()
                .map(city -> city.getName())
                .filter(name -> !name.matches("S\\d+"))
                .sorted().toList();

        sourceCombo.getItems().addAll(names);
        destCombo.getItems().addAll(names);

    }


    private void showCitiesOnMap() {

        List<String> list;
        try {
            list = Files.readAllLines(Path.of("city.txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        for (String line : list) {

            if (!line.startsWith("0")) continue;

            String[] array = line.split(",");

            String name = array[1];
            double lat = Double.parseDouble(array[2]);
            double lon = Double.parseDouble(array[3]);

            Pair<Double, Double> pair = convertMapCorToImgCor(lat, lon);

            Circle redDot = new Circle(5, Color.RED);
            Label label = new Label(name);
            label.setFont(Font.font("Arial", 9));
            VBox vBox = new VBox(label, redDot);
            vBox.setLayoutX(pair.getKey());
            vBox.setLayoutY(pair.getValue());

            mapGroup.getChildren().add(vBox);

        }
    }


    private Pair<Double, Double> convertMapCorToImgCor(double lat, double lon) {

        double latMin = 31.225191, longMax = 34.575816, latMax = 31.598535, longMin = 34.205478;
        double imgWidth = mapImg.getFitWidth();
        double imgHeight = mapImg.getFitHeight();
        double x = imgWidth * (lon - longMin) / (longMax - longMin);
        double y = imgHeight * (latMax - lat) / (latMax - latMin);
        return new Pair<>(x, y);

    }

    private void prepareGraph() {

        String text;

        try {
            text = Files.readString(Path.of("city.txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String[] ar = text.split("--");
        String[] cities = ar[0].split("\n");
        String[] edges = ar[1].split("\n");

        for (String city : cities) {

            if (city.trim().matches("^\\w+,\\w+$")) continue;
            if (city.isBlank()) continue;

            String[] tmp = city.trim().split(",");
            graph.addCity(new City(tmp[1], Double.parseDouble(tmp[2]), Double.parseDouble(tmp[3])));

        }


        for (City city : graph.getCities()) {

            for (String edge : edges) {

                if (edge.contains(city.getName())) {

                    String neighbourName = edge.trim().replace(",", "").replace(city.getName(), "").trim();
                    City neighbour = graph.getSingleCity(neighbourName);

                    if (neighbour != null) city.addNeighbor(neighbour);
                }
            }
        }

    }

}