package com.tain.slidepuzzle.A_Star_Class_Solver;

import java.util.ArrayList;

public class Item {
    public ArrayList<ArrayList<Integer>> table;
    public ArrayList<Integer> path = new ArrayList<>();
    public int minSteps;
    double heuristic;

    public Item(ArrayList<ArrayList<Integer>> table, int minSteps, double heuristic) {
        this.table = table;
        this.minSteps = minSteps;
        this.heuristic = heuristic;
    }

    public Item(ArrayList<ArrayList<Integer>> table, int minSteps, double heuristic, ArrayList<Integer> newPath) {
        this.table = table;
        this.minSteps = minSteps;
        this.heuristic = heuristic;
        this.path = new ArrayList<>(newPath);
    }
}
