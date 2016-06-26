package com.nomagicsoftware.functional;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * A utility class that adapts any {@code Object} (including {@code null}) into a {@link Future} <br>
 * Immutable, therefore multi-threadsafe <br>
 * Really an equivalent should be included in the JDK
 * @author thurston
 * @param <T> the adapted type
 */

public final class FinishedFuture<T> implements Future<T>
{

    final T value;

    public FinishedFuture(T value)
    {
        this.value = value;
    }
    
    
    @Override
    public boolean cancel(boolean mayInterruptIfRunning)
    {
        return false;
    }

    @Override
    public boolean isCancelled()
    {
        return false;
    }

    @Override
    public boolean isDone()
    {
        return true;
    }

    @Override
    public T get()
    {
        return this.value;
    }

    @Override
    public T get(long timeout, TimeUnit unit)
    {
        return get();
    }

}
