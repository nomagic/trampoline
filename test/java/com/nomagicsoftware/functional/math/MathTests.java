package com.nomagicsoftware.functional.math;
import com.nomagicsoftware.functional.TailCall;
import java.math.BigInteger;
import java.util.stream.IntStream;
import org.junit.Test;
import static org.junit.Assert.*;
/**
 *
 * @author thurston
 */
public class MathTests 
{
    @Test
    public void fibonacciSmall()
    {
        assertEquals("", 0, Math.fibonacci(0));
        assertEquals("", 1, Math.fibonacci(1));
        assertEquals("", 1, Math.fibonacci(2));
        
    }
    
    @Test
    public void fibonacciLarger()
    {
        assertEquals("", 2, Math.fibonacci(3));
        assertEquals("fibonacci[9] ==> 34", 34, Math.fibonacci(9));
        assertEquals("fibonacci[39] ==> 34", 63_245_986, Math.fibonacci(39));
        //63_245_986
    }
    
    @Test
    public void factorialSmall()
    {
        assertEquals("", BigInteger.ONE, Math.factorial(0));
        assertEquals("", BigInteger.ONE, Math.factorial(1));
        assertEquals("", BigInteger.valueOf(2L), Math.factorial(2));
    }
    
    @Test
    public void factorialLarge()
    {
        assertEquals("", BigInteger.valueOf(3_628_800L), Math.factorial(10));
        assertEquals("", BigInteger.valueOf(355_687_428_096_000L), Math.factorial(17));
        
    }
    
    @Test
    public void sumArray()
    {
        assertEquals("", 0L, Math.sum(new int[0]));
        assertEquals("", 8L, Math.sum(new int[] { 8 }));
        assertEquals("", 4L, Math.sum(new int[] { 8, -4 }));
        
        assertEquals("", 11L * 10 / 2, Math.sum(IntStream.rangeClosed(0, 10).toArray()));
        assertEquals("", 101L * 100 / 2, Math.sum(IntStream.rangeClosed(0, 100).toArray()));
        assertEquals("", 1_001L * 1_000 / 2, Math.sum(IntStream.rangeClosed(0, 1_000).toArray()));
        assertEquals("", 10_001L * 10_000 / 2, Math.sum(IntStream.rangeClosed(0, 10_000).toArray()));
        assertEquals("", 100_001L * 100_000 / 2, Math.sum(IntStream.rangeClosed(0, 100_000).toArray()));
        assertEquals("", 1_000_001L * 1_000_000 / 2, Math.sum(IntStream.rangeClosed(0, 1_000_000).toArray()));
    }
    
    @Test
    public void trampolineMember()
    {
        assertEquals("", 8L, (long) sum(new int[] { 8 }, 0, 0).trampoline());
        assertEquals("", 11L, (long) sum(new int[] { 8, -4, 7 }, 2, 0).trampoline());
        assertEquals("", 11L * 10 / 2, (long) sum(IntStream.rangeClosed(0, 10).toArray(), 10, 0).trampoline());
        
    }
    
    static TailCall<Long> sum(int[] input, int index, long total)
    {
        if (0 == index)
            return TailCall.terminate(input[0] + total);
        return () -> sum(input, index - 1, input[index] + total);
    }
}
