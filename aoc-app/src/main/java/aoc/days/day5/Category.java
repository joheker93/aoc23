package aoc.days.day5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import aoc.utils.Triplet;
import aoc.utils.Tuple;

public class Category {
    private final String _name;
    private final String _targetName;
    private final List<Triplet<Long, Long, Long>> _ranges = new ArrayList<>();

    public Category(final String name, final String targetName) {
        _name = name;
        _targetName = targetName;
    }

    public void addRange(long source, long destination, long length) {
        _ranges.add(Triplet.of(source, destination, length));
    }

    public String getName() {
        return _name;
    }

    public String getTargetName() {
        return _targetName;
    }

    public long getTarget(long l) {
        for (var triplet : _ranges) {
            if (l >= triplet.fst() && l < triplet.fst() + triplet.trd()) {
                long diff = Math.abs(l - triplet.fst());
                long offset = triplet.snd() + diff;
                return offset;
            }
        }
        return l;
    }

    @Override
    public String toString() {
        return _name + "->" + _targetName + ";ranges=" + _ranges;
    }

    @Override
    public int hashCode() {
        return 1;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Category cat) {
            return _name == cat._name;
        }

        return false;
    }
}
