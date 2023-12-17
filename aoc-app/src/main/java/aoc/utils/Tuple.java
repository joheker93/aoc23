package aoc.utils;

public class Tuple<A, B> {

    private final A _fst;
    private final B _snd;
	private int _hash;

    public Tuple(A a, B b, int hash) {
        _fst = a;
        _snd = b;
		_hash = hash;
    }

    public static <A,B> Tuple<A,B> of(A a, B b){
        return new Tuple<>(a,b,1);
    }

    public static <A,B> Tuple<A,B> of(A a, B b,int hash){
        return new Tuple<>(a,b,hash);
    }

    public Tuple<B,A> flip(){
        return of(_snd,_fst);
    }

    public A fst() {
        return _fst;
    }

    public B snd() {
        return _snd;
    }

    @Override
    public String toString() {
        return "(" + _fst + "," + _snd + ")";
    }

    @Override
    public boolean equals(Object o) {
 
        if(o.hashCode() == hashCode()){
            return true;
        }

        if(o instanceof Tuple tup){
            return _fst.equals(tup._fst) && _snd.equals(tup._snd);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return _hash;
    }
}
