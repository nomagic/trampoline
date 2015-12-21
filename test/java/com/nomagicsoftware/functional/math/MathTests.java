package com.nomagicsoftware.functional.math;
import java.math.BigInteger;
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
}
