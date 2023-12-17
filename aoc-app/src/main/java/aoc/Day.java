package aoc;

import java.io.IOException;

import aoc.utils.Tuple;

public interface Day<T, R> {
    R solveA(T parsedInput);

    R solveB(T parsedInput);

    T parseA(String input) throws IOException;

    T parseB(String input) throws IOException;

    default Tuple<Object, Object> solve(String input) throws IOException {

        final T parsedDataA = parseA(input);
        long t1 = System.currentTimeMillis();
        final R solA = solveA(parsedDataA);

        final T parsedDataB = parseB(input);
        long t2 = System.currentTimeMillis();
        final R solB = solveB(parsedDataB);
        long t3 = System.currentTimeMillis();

        return Tuple.of(solA + " (" + (t2 - t1) + " ms)", solB + " (" + (t3 - t2) + " ms)");

    }

}
