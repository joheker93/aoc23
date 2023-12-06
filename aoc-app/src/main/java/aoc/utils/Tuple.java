package aoc.utils;

public class Tuple<A, B> {

    private final A fst;
    private final B snd;

    public Tuple(A a, B b) {
        fst = a;
        snd = b;
    }

    public static <A,B> Tuple<A,B> of(A a, B b){
        return new Tuple<>(a,b);
    }

    public Tuple<B,A> flip(){
        return of(snd,fst);
    }

    public A fst() {
        return fst;
    }

    public B snd() {
        return snd;
    }

    @Override
    public String toString() {
        return "(" + fst + "," + snd + ")";
    }

    @Override
    public boolean equals(Object o) {
 
        if(o instanceof Tuple tup){
            return fst.equals(tup.fst) && snd.equals(tup.snd);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return 1;
    }
}
