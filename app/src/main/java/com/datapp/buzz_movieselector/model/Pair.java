package com.datapp.buzz_movieselector.model;
import android.support.annotation.NonNull;

public class Pair<L extends Comparable<L>, R extends Comparable<R>> implements Comparable<Pair<L, R>> {

    private final L left;
    private final R right;

    public Pair(L left, R right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Get left object of pair
     * @return left object
     */
    public L getLeft() { return left; }

    /**
     * Get right object of pair
     * @return right object
     */
    public R getRight() { return right; }

    @Override
    public int hashCode() { return left.hashCode() ^ right.hashCode(); }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Pair)) return false;
        Pair pairo = (Pair) o;
        return this.left.equals(pairo.getLeft()) &&
                this.right.equals(pairo.getRight());
    }
    // inverse order
    @Override
    public int compareTo(@NonNull Pair<L, R> pair) {
        int comp = 0;
        if (right.compareTo(pair.right) > 0) comp = -1;
        else if (right.compareTo(pair.right) < 0) comp = 1;
        return comp;
    }
}
