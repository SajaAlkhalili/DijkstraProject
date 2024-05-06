module com.example.project3dijkstra {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;


    opens com.example.project3dijkstra to javafx.fxml;
    exports com.example.project3dijkstra;
}