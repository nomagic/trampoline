
package com.nomagicsoftware.functional;

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
 *  TailCall<Long> factorial(int remaining, long total)
 *  {
 *      if (remaining &lt;= 1)
 *          return TailCall.terminate(total);
 *      return () -> factorial(remaining - 1, total * remaining)
 *  }
 * </pre>
 * 
 * 
 * @author thurston
 * @param <T>
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
    default T value() throws IllegalStateException //Hmm, maybe change this to protected?
    {
        throw new IllegalStateException("#value() mandates a #complete() TailCall");
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
}
