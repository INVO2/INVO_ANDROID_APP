package com.example.invo.Direction;

import com.example.invo.Interface.Coordinates;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class Dijkstra implements Coordinates {
    static int na;
    public static int[] buildMarker ;
    public static void computePaths(Vertex source) {
        source.minDistance = 0.;
        PriorityQueue<Vertex> vertexQueue = new PriorityQueue<Vertex>();
        vertexQueue.add(source);

        while (!vertexQueue.isEmpty()) {
            Vertex u = vertexQueue.poll();

            // Visit each edge exiting u
            for (Edge e : u.adjacencies)
            {
                Vertex v = e.target;
                double weight = e.weight;
                double distanceThroughU = u.minDistance + weight;
                if (distanceThroughU < v.minDistance) {
                    vertexQueue.remove(v);

                    v.minDistance = distanceThroughU ;
                    v.previous = u;
                    vertexQueue.add(v);
                }
            }
        }
    }

    public static List<Vertex> getShortestPathTo(Vertex target) {
        List<Vertex> path = new ArrayList<Vertex>();
        for (Vertex vertex = target; vertex != null; vertex = vertex.previous)
            path.add(vertex);
        Collections.reverse(path);
        return path;
    }

    public static void doNavigation() {
        Map<Integer,Vertex> allPath = new HashMap<>();
        for (int i = 0; i <7; i++) {
            allPath.put(i,new Vertex(Integer.toString(i)));
        }
        // set the edges and weight
        allPath.get(0).adjacencies = new Edge[]{ new Edge(allPath.get(closePath(0)), 1) };
        allPath.get(1).adjacencies = new Edge[]{ new Edge(allPath.get(2), 1) };
        allPath.get(2).adjacencies = new Edge[]{ new Edge(allPath.get(3), 1) };
        allPath.get(3).adjacencies = new Edge[]{ new Edge(allPath.get(4), 1) };
        allPath.get(4).adjacencies = new Edge[]{ new Edge(allPath.get(5), 1) };
        allPath.get(5).adjacencies = new Edge[]{ new Edge(allPath.get(0), 1000) };
        allPath.get(closePath(6)).adjacencies = new Edge[]{ new Edge(allPath.get(6), 1) };
        allPath.get(6).adjacencies = new Edge[]{ new Edge(allPath.get(0), 1000) };
        computePaths(allPath.get(0));// run Dijkstra
        List<Vertex> path = getShortestPathTo(allPath.get(6));
        buildMarker= new int[path.size()];
        for (int i = 0; i < path.size(); i++) {
            buildMarker[i]=Integer.parseInt(path.get(i).name);
        }
    }

    public static int closePath(int key){
        LatLng thiss =coordinatesMap.get(key);
        double d=11110,buff=10000;
        int bufInt=0;
        for (int i = 1; i <coordinatesMap.size()-1 ; i++) {
            LatLng next = coordinatesMap.get(i);
            d=Math.sqrt((next.latitude-thiss.latitude)* (next.latitude-thiss.latitude)+ (next.longitude-thiss.longitude)* (next.longitude-thiss.longitude));
            if(d<buff){
                buff=d;
                bufInt=i;
            }
        }
        return bufInt;
    }
}
