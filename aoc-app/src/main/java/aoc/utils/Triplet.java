package aoc.utils;

public class Triplet<A,B,C> {
    private final A fst;
    private final B snd;
    private final C trd;

    public Triplet(A a, B b,C c) {
        fst = a;
        snd = b;
        trd = c;
    }

    public static <A,B,C> Triplet<A,B,C> of(A a, B b,C c){
        return new Triplet<>(a,b,c);
    }

    public A fst() {
        return fst;
    }

    public B snd() {
        return snd;
    }

    public C trd() {
        return trd;
    }


    @Override
    public String toString() {
        return "(" + fst + "," + snd + "," + trd + ")";
    }

    @Override
    public boolean equals(Object o) {
 
        if(o instanceof Triplet trip){
            return fst.equals(trip.fst) && snd.equals(trip.snd) && trd.equals(trip.trd);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return 1;
    }
}
