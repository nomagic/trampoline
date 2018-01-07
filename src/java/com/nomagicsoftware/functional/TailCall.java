
package com.nomagicsoftware.functional;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * A TailCall is a higher-order function, viz. one that takes no arguments and returns
 * a TailCall when {@link #call() called} <br>
 * In addition, A TailCall may be {@link #finished()}, which implies that its computation
 * is complete and one can retrieve its computation's {@link #value()}<br>
 * Note: it is undefined to invoke {@link #value()} on an unfinished TailCall<br>
 * 
 * TailCalls are especially useful in implementing tail-recursive algorithms, as they
 * allow such algorithms to be used without concern for overflowing the stack; <br>
 * For example, when implementing factorial:
 * 
 * <pre>
 *  long factorial(int i)
 *  {
 *      return factorial(i, 1L);
 *  }
 *  long factorial(int remaining, long total)
 *  {
 *      if (remaining &lt;= 1)
 *          return total;
 *      return factorial(remaining - 1, total * remaining)
 *  }
 * </pre>
 * This code is susceptible to a stack overflow in languages that don't support tail
 * call optimization (like Java)<br>
 * 
 * Here's how you can use TailCall to implement the same algorithm safely:
 * <pre>
 * 
 *  long factorial(int i)
 *  {
 *      return factorial(i, 1L).trampoline()
 *  }
 *  TailCall&lt;Long&gt; factorial(int remaining, long total)
 *  {
 *      if (remaining &lt;= 1)
 *          return TailCall.terminate(total);
 *      return () -&gt; factorial(remaining - 1, total * remaining)
 *  }
 *  factorial(10, 1L).trampoline();
 * </pre>
 * 
 * 
 * @author thurston
 * @param <T> the return value type
 */
@FunctionalInterface
public interface TailCall<T>
{
    TailCall<T> call();
    
    /**
     * Returns the <em>value</em> of this {@link TailCall} <br>
     * Is undefined, if this TailCall is not <em>finished</em> (may throw an exception)<br>
     * Implementations should not use trampolining (i.e. {@link #call()} on this TailCall
     * or any other
     * @return the <em>value</em> of this {@link TailCall}
     * @throws IllegalStateException when invoked 
     * @see #finished() 
     */
    default T value() throws IllegalStateException
    {
        throw new IllegalStateException("#value() mandates a #finished() TailCall");
    }
    
    /**
     * A TailCall that is finished has its <em>value</em> computed and invoking {@link #call() }
     * on it is a no-op<br>
     * Clients then can retrieve its <em>value</em>, by {@link #value()}
     * 
     * @return whether this {@linkplain TailCall} has its value computed
     * @see #value() 
     */
    default boolean finished()
    {
        return false;
    }

    /**
     * Evaluates (via trampolining) this {@linkplain TailCall} until it is {@linkplain #finished() }<br>
     * @return the final value of this computation
     * @see TailCall#trampoline(TailCall) 
     */
    default T trampoline()
    {
        return TailCall.trampoline(this);
    }

    /**
     *
     * @param service the {@link ExecutorService} to execute this {@code TailCall} to completion
     * @return {@link Future future}
     * @throws NullPointerException if {@code #service} is null
     */
    default Future<T> async(ExecutorService service) throws NullPointerException
    {
        Objects.requireNonNull(service);
        if (finished())
            return new FinishedFuture<>(value());
        return service.submit((Callable<T>) this::trampoline);
    }
    
    static <R> TailCall<R> terminate(final R value)
    {
        return new TailCall<R>()
        {

            @Override
            public R value()
            {
                return value;
            }

            @Override
            public boolean finished()
            {
                return true;
            }
            
            @Override
            public TailCall<R> call()
            {
                return this;
            }
        };
    }
    
    static <R> R trampoline(TailCall<R> function)
    {
        for(; ! function.finished(); function = function.call())
        {}    
        return function.value();
    }
    
    /**
     * Note: you may not define a default {@link Object#equals(Object) equals} method
     *       (method dispatch on any interface type prefers concrete class implementations
     *        over any default method implementations)
     * @param <R> type of TailCalls
     * @param one a tailcall
     * @param another a tailcall
     * @return whether the two tailcalls' values are equal
     */
    static <R> boolean equals(TailCall<R> one, TailCall<R> another)
    {
        if (one == another)
            return true;
        if (one == null ^ another == null)
            return false;
        return Objects.equals(one.trampoline(), another.trampoline());
    }
}
