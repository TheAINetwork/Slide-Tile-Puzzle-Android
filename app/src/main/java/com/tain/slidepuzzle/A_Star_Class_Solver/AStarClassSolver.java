package com.tain.slidepuzzle.A_Star_Class_Solver;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.PriorityQueue;

public class AStarClassSolver {
    private PriorityQueue<Item> pq;
    private ArrayList<ArrayList<Integer>> input;
    private SolverStepCallback callback;
    private HeuristicStrategy strategy;
    private HashMap<String, Boolean> visited;
    private int minSteps = -1;
    public static final int[] dy = { -1, 0, 1, 0 }, dx = { 0, 1, 0, -1 };

    public AStarClassSolver(ArrayList<ArrayList<Integer>> input, SolverStepCallback callback, HeuristicStrategy strategy) {
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
        return(!(i < 0 || i >= input.size() || j < 0 || j >= input.size()));
    }

    private void swapTablePositions(int from1, int from2, int to1, int to2) {
        int backup = input.get(from1).get(from2);
        input.get(from1).set(from2, input.get(to1).get(to2));
        input.get(to1).set(to2, backup);
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

    public ArrayList<Integer> solve(int si, int sj) {
        pq.add(new Item(duplicateInput(), 0, strategy.heuristic(this.input), new ArrayList<Integer>));
        ArrayList<Integer> path = null;
        
        while (!pq.isEmpty()) {
            input = pq.peek().table;
            minSteps = pq.peek().minSteps;
            path = new ArrayList<Integer>(pq.peek().path);
            for (int i = 0; i < input.size(); i++) {
                for (int j = 0; j < input.get(i).size(); j++) {
                    if (input.get(i).get(j) == 0) {
                        si = i;
                        sj = j;
                    }
                }
            }
            if (isPuzzleSolved()) {
                break;
            }
            pq.poll();
            for (int k = 0; k < 4; k++) {
                if (isValidPosition(si + dy[k], sj + dx[k])) {
                    swapTablePositions(si, sj, si + dy[k], sj + dx[k]);
                    String currentState = buildState();
                    if (!visited.containsKey(currentState)) {
                        visited.put(currentState, true);
                        path.add(k);
                        pq.add(new Item(duplicateInput(), minSteps + 1, strategy.heuristic(input), path));
                        path.remove(path.size() - 1);
                    }
                    swapTablePositions(si, sj, si + dy[k], sj + dx[k]);
                }
            }

        }
        pq.clear();
        return path;
    }
}
