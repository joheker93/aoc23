package aoc.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Extended Stream functionality. Backs up the stream with a string for parsing.
 * Saves the inner stream for further usage (aka re-opening it)
 */
public class EStream<T> {

    private String _in;
    private Stream<T> _stream;

    private EStream(List<T> it) {
        _stream = it.stream();
    }

    private EStream(String str) {
        _in = str;
    }

    private EStream(Stream<T> stream) {
        _stream = stream;
    }

    public Stream<T> get() {
        return _stream;
    }

    public List<T> toList() {
        return _stream.toList();
    }

    public List<T> toModList(){
        return new ArrayList<>(_stream.toList());
    }

    public static <K> EStream<K> stream(Stream<K> stream) {
        return new EStream<K>(stream);
    }

    public static EStream<String> stream(String str) {
        return new EStream<>(str);
    }

    public static EStream<String> sstream(String str) {
        List<String> l = new ArrayList<>();
        for (char c : str.toCharArray()) {
            l.add(String.valueOf(c));
        }

        return stream(l);
    }

    public static <K> EStream<K> stream(List<K> l) {
        return new EStream<K>(l);
    }

    public static <K> EStream<K> stream(Collection<K> col) {
        return new EStream<K>(col.stream());
    }

    public static <K> EStream<K> stream(K[] arr){
        return new EStream<K>(new ArrayList<>(Arrays.asList(arr)));
    }

    public EStream<String> split(String regex) {
        return stream(Arrays.stream(_in.split(regex)));
    }

    public EStream<String> words() {
        return stream(Arrays.stream(_in.split("\\s+")));
    }

    public EStream<String> lines() {
        return stream(Arrays.stream(_in.split("\\n")));
    }

    public EStream<T> take(int n) {
        var l = _stream.toList();
        if (n > l.size()) {
            return stream(l);
        }
        _stream = l.stream();

        return stream(l.subList(0, n));

    }

    public EStream<T> takeWhile(Predicate<T> pred) {
        var l = _stream.toList();
        _stream = l.stream();

        return stream(l.stream().takeWhile(pred));
    }

    public EStream<T> drop(int n) {
        var l = _stream.toList();
        if (n > l.size()) {
            return stream(l);
        }
        _stream = l.stream();

        return stream(l.subList(n, l.size()));
    }

    public EStream<T> dropWhile(Predicate<T> pred) {
        var l = _stream.toList();
        _stream = l.stream();

        return stream(l.stream().dropWhile(pred));
    }

    public T head() {
        var l = _stream.toList();
        _stream = l.stream();
        return l.get(0);
    }

    public EStream<T> tail() {
        var l = _stream.toList();
        _stream = l.stream();
        return stream(l.subList(1, l.size()));
    }

    public T last() {
        var l = _stream.toList();
        _stream = l.stream();
        return l.get(l.size() - 1);
    }

    public int min() {
        var l = _stream.toList();
        var s = l.stream().mapToInt(x -> (int) x).min().orElse(Integer.MIN_VALUE);
        _stream = l.stream();
        return s;
    }

    public int max() {
        var l = _stream.toList();
        var s = l.stream().mapToInt(x -> (int) x).max().orElse(Integer.MAX_VALUE);
        _stream = l.stream();
        return s;
    }

    public int count(){
        var l = _stream.toList();
        _stream = l.stream();
        return (int) l.stream().count();
    }

    public <B> EStream<B> map(Function<T, B> f) {
        return stream(_stream.map(f));
    }

    public EStream<T> filter(Predicate<T> pred) {
        return stream(_stream.filter(pred));
    }

    public T reduce(T acc, BinaryOperator<T> f) {
        var l = _stream.toList();
        _stream = l.stream();
        return l.stream().reduce(acc, f);
    }

    public <A> A foldWith(A acc, BiFunction<A, T, A> f) {
        var res = acc;
        var l = _stream.toList();
        _stream = l.stream();
        for (T t : l) {
            res = f.apply(res, t);
        }
        return res;

    }

    public T foldWith(BinaryOperator<T> f) {
        var l = _stream.toList();

        _stream = l.stream();
        return l.stream().skip(1).reduce(l.get(0), f);

    }

    public EStream<Tuple<Integer, T>> enumerate() {

        var l = _stream.toList();
        var range = Utils.range(1, l.size());
        _stream = l.stream();

        return stream(range.stream().map(x -> new Tuple<>(x, l.get(x - 1))));
    }

}
