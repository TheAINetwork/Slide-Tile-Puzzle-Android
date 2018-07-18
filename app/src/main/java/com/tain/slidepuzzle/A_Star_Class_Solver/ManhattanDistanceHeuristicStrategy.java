package com.tain.slidepuzzle.A_Star_Class_Solver;

import java.util.ArrayList;

public class ManhattanDistanceHeuristicStrategy implements HeuristicStrategy {
    @Override
    public double heuristic(ArrayList<ArrayList<Integer>> table) {
        double dist = 0;
        for (int i = 0; i < table.size(); i++) {
            for (int j = 0; j < table.get(i).size(); j++) {
                int at = table.get(i).get(j) - 1;
                dist += Math.abs(at / table.size() - i) + Math.abs(at % table.size() - j);
            }
        }
        return dist;
    }
}
