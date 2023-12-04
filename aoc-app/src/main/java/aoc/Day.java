package aoc;

import java.io.IOException;

import aoc.utils.Tuple;

public interface Day<T, R> {
    T parseA(String input) throws IOException;

    R solveA(T parsedInput);

    T parseB(String input) throws IOException;

    R solveB(T parsedInput);

    default Tuple<Object, Object> solve(String input) throws IOException {

        final T parsedDataA = parseA(input);
        long t1 = System.currentTimeMillis();
        final R solA = solveA(parsedDataA);

        final T parsedDataB = parseB(input);
        long t2 = System.currentTimeMillis();
        final R solB = solveB(parsedDataB);
        long t3 = System.currentTimeMillis();

        return new Tuple<>(solA + " (" + (t2 - t1) + " ms)", solB + " (" + (t3 - t2) + " ms)");

    }

}
