package com.tain.slidepuzzle.A_Star_Class_Solver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

public class IDAStarClassSolver {

    protected PriorityQueue<Item> pq;
    protected ArrayList<ArrayList<Integer>> input;
    protected SolverStepCallback callback;
    protected HeuristicStrategy strategy;
    protected HashMap<String, Boolean> visited;
    protected int minSteps = -1;
    public static final int[] dy = {-1, 0, 1, 0}, dx = {0, 1, 0, -1};

    public IDAStarClassSolver(ArrayList<ArrayList<Integer>> input, SolverStepCallback callback, HeuristicStrategy strategy) {
        pq = new PriorityQueue<>(1000, Helper.getComparator());
        this.input = input;
        this.callback = callback;
        this.strategy = strategy;
        visited = new HashMap<>();
    }

    private boolean isPuzzleSolved() {
        boolean solved = true;
        int count = 1;
        outerloop:
        for (int i = 0; i < input.size(); i++) {
            for (int j = 0; j < input.get(i).size(); j++) {
                if (input.get(i).get(j) != count && input.get(i).get(j) != 0) {
                    solved = false;
                    break outerloop;
                }
                count++;
            }
        }
        return solved;
    }

    private boolean isValidPosition(int i, int j) {
        return (!(i < 0 || i >= input.size() || j < 0 || j >= input.size()));
    }

    private void swapTablePositions(int from1, int from2, int to1, int to2) {
        int backup = input.get(from1).get(from2);
        input.get(from1).set(from2, input.get(to1).get(to2));
        input.get(to1).set(to2, backup);
    }

    public ArrayList<ArrayList<Integer>> solve(int si, int sj) {
        double limit = strategy.heuristic(this.input);
        while (true) {

            double temp = idastar(si, sj, 0, limit, this.input);
            if (temp < 0) {
                break;
            }
            limit = temp;
        }
        return input;
    }

    private ArrayList<ArrayList<Integer>> duplicateInput() {
        ArrayList<ArrayList<Integer>> arr = new ArrayList<>();
        for (int i = 0; i < input.size(); i++) {
            arr.add(new ArrayList<Integer>());
            for (int j = 0; j < input.get(i).size(); j++) {
                arr.get(i).add(input.get(i).get(j));
            }
        }
        return arr;
    }


    private String buildState() {
        String aux = "";
        for (int i = 0; i < input.size(); i++) {
            for (int j = 0; j < input.get(i).size(); j++) {
                aux += input.get(i).get(j) + "|";
            }
        }
        return aux;
    }

    public double idastar(int si, int sj, int g, double limit, ArrayList<ArrayList<Integer>> nodes) {
        double f = g + strategy.heuristic(nodes);
        if (f > limit)
            return f;
        if (isPuzzleSolved())
            return -1;
        double min = Integer.MAX_VALUE;

        for (int k = 0; k < 4; k++) {
            if (isValidPosition(si + dy[k], sj + dx[k])) {
                swapTablePositions(si, sj, si + dy[k], sj + dx[k]);
                String currentState = buildState();
                if (!visited.containsKey(currentState)) {
                    visited.put(currentState, true);
                    double temp = idastar(si + dy[k], sj + dx[k], g + 1, limit, duplicateInput());
                    if (temp < 0) {
                        return -1;
                    }
                    if (temp < min) {
                        min = temp;
                    }
                }
                swapTablePositions(si, sj, si + dy[k], sj + dx[k]);
            }
        }
        return min;
    }

}
