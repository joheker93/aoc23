package com.aoc.app;

import java.io.IOException;
import java.util.Arrays;

import aoc.Solver;

/**
 * main
 */
public class App {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Running AOC solver");

        for(var x : args){
            System.out.println("ARg : " + x);
        }

        Thread t = new Thread(() -> {
            try {
                if(Arrays.asList(args).contains("-latest")){
                    System.out.println("Running latest day");
                    Solver.start(true);

                } else {
                    System.out.println("Running all days");
                    Solver.start(false);
                }
            } catch (IOException | InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });

        t.start();
        if(args.length == 0){
            t.join();
            System.out.println("\nAborting, run with -extend to continue slow solutions");    
            System.exit(1);   
        } else if(args[0].equals("-extend")){
            t.join();
            System.out.println("\nContinuing with slow solutions, be patient...");
        }

        

    }

}