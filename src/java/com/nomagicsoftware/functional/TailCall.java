
package com.nomagicsoftware.functional;

/**
 *
 * @author thurston
 * @param <T>
 */
@FunctionalInterface
public interface TailCall<T>
{
    TailCall<T> call();
    
    default T value()
    {
        return null; //could throw  Exception here I guess
    }
    
    default boolean finished()
    {
        return false;
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
