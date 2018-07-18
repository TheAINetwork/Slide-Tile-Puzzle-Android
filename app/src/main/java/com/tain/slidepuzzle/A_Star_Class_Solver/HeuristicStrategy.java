package com.tain.slidepuzzle.A_Star_Class_Solver;

import java.util.ArrayList;

public interface HeuristicStrategy {
    double heuristic(ArrayList<ArrayList<Integer>> table);
}
