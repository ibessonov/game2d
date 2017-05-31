package com.ibessonov.game;

import org.junit.Before;
import org.junit.Test;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;

public class GoodListTest {

    private static class Position implements Centered {

        int x;
        int y;

        Position(int x, int y) {
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

    private GoodList<Position> list;

    @Before
    public void setUp() throws Exception {
        list = new GoodList<>();
    }

    @Test
    public void findNearest() {
        Position p1 = new Position(1, 0);
        Position p2 = new Position(3, 0);

        list.add(p1);
        list.add(p2);

        assertEquals(asList(p1, p2), list.findNearest(1, 4));
        assertEquals(singletonList(p1), list.findNearest(1, 3));
        assertEquals(singletonList(p2), list.findNearest(2, 4));
        assertEquals(emptyList(), list.findNearest(2, 3));
    }
}