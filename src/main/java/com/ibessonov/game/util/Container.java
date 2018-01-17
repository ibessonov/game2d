package com.ibessonov.game.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Predicate;

import static java.util.Collections.singletonList;

/**
 * @author ibessonov
 */
public interface Container<T> extends Iterable<T> {

    static <T> Container<T> singleton(T elem) {
        return singletonList(elem)::iterator;
    }

    static <T> Container<T> list(Collection<T> collection) {
        return collection::iterator;
    }

    @SafeVarargs
    static <T> Container<T> join(Container<? extends T>... containers) {
        assert containers.length != 0;
        return new JoinedContainer<>(containers);
    }

    default void removeIf(Predicate<? super T> predicate) {
        for (Iterator<T> iterator = this.iterator(); iterator.hasNext(); ) {
            T element = iterator.next();
            if (predicate.test(element)) {
                iterator.remove();
            }
        }
    }

    final class JoinedContainer<T> implements Container<T> {

        private final Container<? extends T>[] containers;

        JoinedContainer(Container<? extends T>[] containers) {
            this.containers = containers;
        }

        @Override
        public Iterator<T> iterator() {
            return new JoinedIterator();
        }

        @Override
        public void removeIf(Predicate<? super T> predicate) {
            for (Container<? extends T> container : containers) container.removeIf(predicate);
        }

        private class JoinedIterator implements Iterator<T> {
            private int index = 0;
            private Iterator<? extends T> current = containers[0].iterator();

            @Override
            public boolean hasNext() {
                while (!current.hasNext()) {
                    if (++index == containers.length) return false;
                    current = containers[index].iterator();
                }
                return true;
            }

            @Override
            public T next() {
                return current.next();
            }

            @Override
            public void remove() {
                current.remove();
            }
        }

    }
}
