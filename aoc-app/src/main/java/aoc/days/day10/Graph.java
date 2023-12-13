package aoc.days.day10;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import aoc.utils.Tuple;

public record Graph(Map<Tuple<Integer, Integer>, List<Tuple<Integer, Integer>>> graph) {

}
