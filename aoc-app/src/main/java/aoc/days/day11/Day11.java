package aoc.days.day11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import aoc.Day;
import aoc.utils.Tuple;
import aoc.utils.Utils;

public class Day11 implements Day<char[][], Long> {
    
    @Override
    public Long solveA(char[][] universe) {
        return solve(universe, 1);
    }
    
    @Override
    public Long solveB(char[][] universe) {
        return solve(universe,999999);
    }
    
    private long solve(char[][] universe, long expansion) {
        List<Integer> expandedRows = findExpansions(universe);
        List<Integer> expandedCols = findExpansions(Utils.rotateRight(universe));
    
        List<Tuple<Integer,Integer>> galaxies = galaxies(universe);
        long res = 0;

        for(int i = 0;i < galaxies.size();i++){
            for(int j = i+1;j<galaxies.size();j++){
                var p1 = galaxies.get(i);
                var p2 = galaxies.get(j);
    
                res+=gridDistance(p1.fst(),p1.snd(),p2.fst(),p2.snd(),expandedRows,expandedCols,expansion);
            }
        }
        
        return res;
    }

    private List<Tuple<Integer,Integer>> galaxies(char[][] universe){
        List<Tuple<Integer,Integer>> galaxies = new ArrayList<>();

        for(int i = 0;i <universe.length;i++){
            for(int j = 0;j<universe[i].length;j++){
                if(universe[i][j] == '#'){
                    galaxies.add(Tuple.of(i,j));
                }
            }
        }

        return galaxies;
    }

    private long gridDistance(int row1,int col1,int row2,int col2, List<Integer> expandedRows, List<Integer> expandedCols,long galaxyExpansion){
        long rowDistance = 0;
        for(int pos : expandedRows){
            if((row1 < pos && row2 > pos) || (row1 > pos && row2 < pos)){
                rowDistance++;
            }
        }
        
        long colDistance = 0;
        for(int pos : expandedCols){
            if((col1 < pos && col2 > pos) || (col1 > pos && col2 < pos)){
                colDistance++;
            }
        }

        rowDistance*=galaxyExpansion;
        colDistance*=galaxyExpansion;

        return (Math.abs(row1-row2) + Math.abs(col1 - col2) + rowDistance + colDistance);
    }

    private boolean allEqual(char[] chars, char ref) {
        for (char c : chars) {
            if (c != ref) {
                return false;
            }
        }
        return true;
    }

    private List<Integer> findExpansions(char[][] universe) {
        int exp = 0;
        List<Integer> expansions = new ArrayList<>();

        for(char[] l : universe){
            if(allEqual(l, '.')){
                expansions.add(exp);
            }
            exp++;
        }
        return expansions;
    }

	@Override
    public char[][] parseA(String input) throws IOException {
        var inputLines = input.lines().toList();
        int height = inputLines.size();
        int width = inputLines.get(0).length();
        char[][] universe = new char[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                universe[i][j] = inputLines.get(i).charAt(j);
            }
        }

        return universe;

    }

    @Override
    public char[][] parseB(String input) throws IOException {
       return parseA(input);

    }

}
