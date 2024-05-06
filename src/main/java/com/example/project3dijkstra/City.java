package com.example.project3dijkstra;

import java.util.HashMap;
import java.util.Map;

class City {

    private String name;
    private Map<City, Double> neighbors;
    private double lat;
    private double lon;

    public City(String name, double lat, double lon) {

        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.neighbors = new HashMap<>();

    }


    public void addNeighbor(City neighbourCity) {

        if (neighbors.containsKey(neighbourCity)) return;

        double distance = calculateDistance(this, neighbourCity);
        neighbors.put(neighbourCity, distance);
        neighbourCity.getNeighbors().put(this, distance);

    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public Map<City, Double> getNeighbors() {
        return neighbors;
    }


    public void setNeighbors(Map<City, Double> neighbors) {
        this.neighbors = neighbors;
    }


    public double getLat() {
        return lat;
    }


    public void setLat(double lat) {
        this.lat = lat;
    }


    public double getLon() {
        return lon;
    }


    public void setLon(double lon) {
        this.lon = lon;
    }


    public static double calculateDistance(City src, City dst) {

        double lat1 = Math.toRadians(src.getLat());
        double lon1 = Math.toRadians(src.getLon());
        double lat2 = Math.toRadians(dst.getLat());
        double lon2 = Math.toRadians(dst.getLon());

        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;

        double a = Math.pow(Math.sin(dLat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dLon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double radius = 6371.0;


        return radius * c;

    }


    @Override
    public String toString() {
        return "name=" + name;
    }
}
