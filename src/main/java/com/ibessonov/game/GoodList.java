package com.ibessonov.game;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.binarySearch;
import static java.util.Collections.unmodifiableList;

public class GoodList<T extends Centered & Disposable> {

    private List<T> list = new ArrayList<>();
    private ModifiablePosition mp = new ModifiablePosition(0, 0);

    public void add(T t) {
        list.add(t);
    }

    public void sort() {
        list.sort(Centered.COMPARATOR);
    }

    /**
     * @param lX inclusive
     * @param rX exclusive
     */
    public List<T> findNearest(int lX, int rX) {
        mp.x = lX;
        mp.y = Integer.MIN_VALUE;
        int from = -1 - binarySearch(list, mp, Centered.COMPARATOR);

        mp.x = rX;
        mp.y = Integer.MIN_VALUE;
        int to = -1 - binarySearch(list, mp, Centered.COMPARATOR);

        return unmodifiableList(list.subList(from, to));
    }

    public List<T> list() {
        return unmodifiableList(list);
    }

    public void disposeWaste() {
        list.removeIf(t -> t.disposed()); // cannot be converted to method reference :(
    }

    private static class ModifiablePosition implements Centered {

        public int x;
        public int y;

        public ModifiablePosition(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public int centerX() {
            return x;
        }

        @Override
        public int centerY() {
            return y;
        }
    }
}
