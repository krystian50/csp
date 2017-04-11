package models;

/**
 * Created by Krystian on 2017-04-05.
 */
public class Tuple <K, I> {
    public final K first;
    public final I second;

    public Tuple(K first, I second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean equals(Object T) {
        Tuple t = (Tuple) T;
        return (first == t.first || first == t.second)
                && (second == t.first || second == t.second);
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ")";
    }
}