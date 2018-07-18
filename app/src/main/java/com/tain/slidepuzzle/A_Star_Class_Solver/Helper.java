package com.tain.slidepuzzle.A_Star_Class_Solver;

import android.graphics.Point;

import com.tain.slidepuzzle.model.Board;
import com.tain.slidepuzzle.model.Place;

import java.util.ArrayList;
import java.util.Comparator;

public class Helper {
    public static Comparator<Item> getComparator() {
        return new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                return Double.compare(o1.heuristic, o2.heuristic);
            }
        };
    }

    public static ParsedBoard convertToSolverRepresentation(Board b) {
        ArrayList<ArrayList<Integer>> table = new ArrayList<>();
        ArrayList<Integer> currentArrayList = null;
        ParsedBoard pb = new ParsedBoard();
        for (Place p : b.places()) {
            if (p.getY() == 1) {
                table.add(new ArrayList<Integer>());
                currentArrayList = table.get(table.size() - 1);
            }
            if (p.getTile() != null)
                currentArrayList.add(p.getTile().number());
            else {
                currentArrayList.add(0);
                pb.start = new Point(p.getY() - 1, p.getX() - 1);
            }
        }
        pb.table = transform(table);
        return pb;
    }

    private static ArrayList<ArrayList<Integer>> transform(ArrayList<ArrayList<Integer>> old) {
        ArrayList<ArrayList<Integer>> table = new ArrayList<>();
        for (int i = 0; i < old.get(0).size(); i++) {
            ArrayList<Integer> k = new ArrayList<>();
            for (int j = 0; j < old.size(); j++) {
                k.add(old.get(j).get(i));
            }
            table.add(k);
        }
        return table;
    }
 }
