
package com.nomagicsoftware.functional.math;

import com.nomagicsoftware.functional.TailCall;
import java.math.BigInteger;

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
}
