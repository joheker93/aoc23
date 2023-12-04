package com.aoc.app;

import aoc.Solver;

/**
 * main
 */
public class App {
    public static void main(String[] args) {
        System.out.println("Running AOC solver");
        try {
            Solver.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}