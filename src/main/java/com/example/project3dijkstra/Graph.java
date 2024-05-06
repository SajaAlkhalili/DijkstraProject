package com.example.project3dijkstra;

import java.util.*;

class Graph {

    private static Graph instance;
    private List<City> cities;

    private Graph() {

        this.cities = new LinkedList<>();

    }

    public static Graph getInstance() {

        if (instance == null) {
            instance = new Graph();
        }

        return instance;

    }


    public City getSingleCity(String name) {

        City city = cities.stream().filter(c -> name.equals(c.getName())).findFirst().orElse(null);

        return city;

    }


    public void addCity(City city) {
        cities.add(city);
    }


    public List<City> getCities() {
        return cities;
    }


//    public List<City> shortestPath(City source, City destination) {
//
//        Map<City, Double> distanceMap = new HashMap<>();
//        Map<City, City> predecessorMap = new HashMap<>();
//        Set<City> visited = new HashSet<>();
//
//
//        for (City city : cities) {
//            distanceMap.put(city, Double.MAX_VALUE);
//            predecessorMap.put(city, null);
//        }
//
//
//        distanceMap.put(source, 0.0);
//
//
//        while (!visited.contains(destination)) {
//
//            City current = getClosestUnvisitedCity(distanceMap, visited);
//            visited.add(current);
//
//            for (Map.Entry<City, Double> entry : current.getNeighbors().entrySet()) {
//
//                City neighbor = entry.getKey();
//                double newDistance = distanceMap.get(current) + entry.getValue();
//
//                if (newDistance < distanceMap.get(neighbor)) {
//
//                    distanceMap.put(neighbor, newDistance);
//                    predecessorMap.put(neighbor, current);
//
//                }
//
//            }
//
//        }
//
//
//        List<City> path = new ArrayList<>();
//        City current = destination;
//
//        while (current != null) {
//
//            path.add(current);
//            current = predecessorMap.get(current);
//
//        }
//
//        Collections.reverse(path);
//
//        return path;
//    }
public List<City> shortestPath(City source, City destination) {
    Map<City, Double> distanceMap = new HashMap<>();
    Map<City, City> predecessorMap = new HashMap<>();
    Set<City> visited = new HashSet<>();

    for (City city : cities) {
        distanceMap.put(city, Double.MAX_VALUE);
    }
    distanceMap.put(source, 0.0);

    PriorityQueue<City> priorityQueue = new PriorityQueue<>(new Comparator<City>() {
        @Override
        public int compare(City city1, City city2) {
            return Double.compare(distanceMap.get(city1), distanceMap.get(city2));
        }
    });
    priorityQueue.offer(source);

    // Dijkstra's Algorithm
    while (!priorityQueue.isEmpty()) {
        City current = priorityQueue.poll();
        visited.add(current);

        for (Map.Entry<City, Double> entry : current.getNeighbors().entrySet()) {
            City neighbor = entry.getKey();
            if (!visited.contains(neighbor)) {
                double newDistance = distanceMap.get(current) + entry.getValue();
                if (newDistance < distanceMap.get(neighbor)) {
                    distanceMap.put(neighbor, newDistance);
                    predecessorMap.put(neighbor, current);
                    priorityQueue.offer(neighbor);
                }
            }
        }
    }

    List<City> path = new ArrayList<>();
    City current = destination;
    while (current != null) {
        path.add(current);
        current = predecessorMap.get(current);
    }
    Collections.reverse(path);

    return path;


}
//public List<City> shortestPath(City source, City destination) {
//    Map<City, LinkedList<City>> pathMap = new HashMap<>();
//    Set<City> visited = new HashSet<>();
//
//    for (City city : cities) {
//        pathMap.put(city, new LinkedList<>());
//    }
//    pathMap.get(source).add(source);
//
//    PriorityQueue<City> priorityQueue = new PriorityQueue<>(new Comparator<City>() {
//        @Override
//        public int compare(City city1, City city2) {
//            return Double.compare(calculateDistance(pathMap.get(city1)), calculateDistance(pathMap.get(city2)));
//        }
//
//        private double calculateDistance(LinkedList<City> path) {
//            double distance = 0.0;
//            for (int i = 0; i < path.size() - 1; i++) {
//                City current = path.get(i);
//                City next = path.get(i + 1);
//                distance += current.getNeighbors().get(next);
//            }
//            return distance;
//        }
//    });
//    priorityQueue.offer(source);
//
//    // Dijkstra's Algorithm
//    while (!priorityQueue.isEmpty()) {
//        City current = priorityQueue.poll();
//        visited.add(current);
//
//        for (Map.Entry<City, Double> entry : current.getNeighbors().entrySet()) {
//            City neighbor = entry.getKey();
//            if (!visited.contains(neighbor)) {
//                LinkedList<City> newPath = new LinkedList<>(pathMap.get(current));
//                newPath.add(neighbor);
//                pathMap.put(neighbor, newPath);
//
//                priorityQueue.offer(neighbor);
//            }
//        }
//    }
//
//    return pathMap.get(destination);
//}


//    private City getClosestUnvisitedCity(Map<City, Double> distanceMap, Set<City> visited) {
//
//        return distanceMap.entrySet().stream().filter(entry -> !visited.contains(entry.getKey()))
//                .min(Comparator.comparingDouble(Map.Entry::getValue))
//                .map(Map.Entry::getKey)
//                .orElse(null);
//
//    }

}


