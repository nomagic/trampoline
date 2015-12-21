
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
}
