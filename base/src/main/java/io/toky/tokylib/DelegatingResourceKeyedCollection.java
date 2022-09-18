package io.toky.tokylib;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class DelegatingResourceKeyedCollection<T> implements Collection<T>, ResourceKeyed<T> {
    protected final Collection<T> collection;
    protected final ResourceKey<T> key;

    public static <T> DelegatingResourceKeyedCollection<T> create(ResourceKey<T> key, Collection<T> c) {
        return new DelegatingResourceKeyedCollection<>(key, c);
    }

    protected DelegatingResourceKeyedCollection(ResourceKey<T> key, Collection<T> collection) {
        this.key = key;
        this.collection = collection;
    }

    @Override
    public int size() {
        return collection.size();
    }

    @Override
    public boolean isEmpty() {
        return collection.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return collection.contains(o);
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return collection.iterator();
    }

    @NotNull
    @Override
    public Object[] toArray() {
        return collection.toArray();
    }

    @NotNull
    @Override
    public <T1> T1[] toArray(@NotNull T1[] a) {
        return collection.toArray(a);
    }

    @Override
    public <T1> T1[] toArray(IntFunction<T1[]> generator) {
        return collection.toArray(generator);
    }

    @Override
    public boolean add(T t) {
        return collection.add(t);
    }

    @Override
    public boolean remove(Object o) {
        return collection.remove(o);
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return collection.containsAll(c);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends T> c) {
        return collection.addAll(c);
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        return collection.removeAll(c);
    }

    @Override
    public boolean removeIf(Predicate<? super T> filter) {
        return collection.removeIf(filter);
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        return collection.retainAll(c);
    }

    @Override
    public void clear() {
        collection.clear();
    }

    @Override
    public Spliterator<T> spliterator() {
        return collection.spliterator();
    }

    @Override
    public Stream<T> stream() {
        return collection.stream();
    }

    @Override
    public Stream<T> parallelStream() {
        return collection.parallelStream();
    }

    @Override
    public ResourceKey<T> key() {
        return this.key;
    }

    @Override
    public String toString() {
        return collection.toString();
    }

    @Override
    public boolean equals(Object obj) {
        return collection.equals(obj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(collection, key);
    }
}
