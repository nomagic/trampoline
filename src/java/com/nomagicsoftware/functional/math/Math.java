
package com.nomagicsoftware.functional.math;

import com.nomagicsoftware.functional.TailCall;
import java.math.BigInteger;
import java.util.Objects;

/**
 *
 * @author thurston
 */
public abstract class Math
{
    private Math()
    {
        
    }
    
    /**
     *
     * @param nth 0-based index
     * @return the nth-value of the fibonacci sequence
     */
    public static int fibonacci(int nth)
    {
        switch (nth)
        {
            case 0:
                return 0;
            case 1:
                return 1;
            default:
                return TailCall.trampoline(fibonacci(nth, 1, 0));
        }
    }
    
    private static TailCall<Integer> fibonacci(int nth, int prior, int prior_prior)
    {
        if (2 == nth)
            return TailCall.terminate(prior + prior_prior);
        return () -> fibonacci(nth - 1, prior + prior_prior, prior);
    }
    
    public static BigInteger factorial(int nth)
    {
        if (nth <= 1)
            return BigInteger.ONE;
        return TailCall.trampoline(factorial(nth, BigInteger.ONE));
    }
    
    private static TailCall<BigInteger> factorial(int countdown, BigInteger total)
    {
        if (countdown == 1)
            return TailCall.terminate(total);
        return () -> factorial(countdown - 1, total.multiply(BigInteger.valueOf(countdown)));
    }
    
    public static long sum(int[] input)
    {
        Objects.requireNonNull(input);
        if (input.length == 0)
            return 0L;
        //return TailCall.trampoline(sum(input, input.length - 1, 0L));
        return sum(input, input.length - 1, 0L).trampoline();
    }
    
    private static TailCall<Long> sum(int[] input, int index, long total)
    {
        if (0 == index)
            return TailCall.terminate(input[0] + total);
        return () -> sum(input, index - 1, input[index] + total);
    }
}
