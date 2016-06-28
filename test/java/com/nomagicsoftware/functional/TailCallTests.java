package com.nomagicsoftware.functional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import static org.junit.Assert.*;
/**
 *
 * @author thurston
 */
public class TailCallTests 
{
    @Test
    public void equality()
    {
        assertFalse("null ^", TailCall.equals(TailCall.terminate(5), null));
        assertFalse("null ^", TailCall.equals(null, TailCall.terminate(5)));
        assertTrue("", TailCall.equals(null, null));
        assertTrue("", TailCall.equals(TailCall.terminate(null), TailCall.terminate(null)));
        assertTrue("", TailCall.equals(TailCall.terminate("Hello"), TailCall.terminate("Hello")));
        
        assertTrue("", TailCall.equals(TailCall.terminate(6L), factorial(3, 1L)));
        assertTrue("", TailCall.equals(factorial(10, 1L), factorial(9, 10L)));
        assertFalse("", TailCall.equals(factorial(10, 1L), factorial(8, 10L)));
        
    }
    
    @Test
    public void async() throws Throwable
    {
        ExecutorService pool = Executors.newFixedThreadPool(1);
        
        
        TailCall<Long> factorial_10 = factorial(10, 1L);
//        Future<Long> submit = es.submit((Callable<Long>) factorial_10::trampoline);
        Future<Long> async = factorial_10.async(pool);
        pool.shutdown();
        assertEquals("", 3_628_800L, (long) async.get());
        assertTrue(pool.awaitTermination(1L, TimeUnit.DAYS));
        
    }
    
    @Test
    public void unnecessary() throws Throwable
    {
        ExecutorService pool = Executors.newFixedThreadPool(1);
        pool.shutdown(); // the ES shouldn't be used
        assertTrue(pool.awaitTermination(1L, TimeUnit.DAYS));
        TailCall<Long> factorial_1 = factorial(1, 1L);
        Future<Long> async = factorial_1.async(pool);
        assertEquals("", 1L, (long) async.get());
        //assertTrue(null, async instanceof FinishedFuture);
        
    }
    static TailCall<Long> factorial(int countdown, long total)
    {
        if (countdown <= 1)
            return TailCall.terminate(total);
        return () -> factorial(countdown - 1, countdown * total);
    }
    
    
}
