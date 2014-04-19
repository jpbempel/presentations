import ch.usi.overseer.OverHpc;
import java.util.concurrent.atomic.AtomicInteger;
 
public class Increment
{
    static int counter;
    static AtomicInteger atomicCounter = new AtomicInteger();
 
    static void stdIncrement()
    {
        counter++;
    }
 
    static void atomicIncrement()
    {
        atomicCounter.incrementAndGet();
    }
 
    static void benchStdIncrement(int loopCount)
    {
        for (int i = 0; i < loopCount; i++)
        {
            stdIncrement();
        }
    }
 
    static void benchAtomicIncrement(int loopCount)
    {
        for (int i = 0; i < loopCount; i++)
        {
            atomicIncrement();
        }
    }
 
    public static void main(String[] args) throws Exception
    {
        boolean std = args.length > 0 && args[0].equals("std");
	HWCounters.init();
        // warmup
        if (std)
        {
            benchStdIncrement(10000);
            benchStdIncrement(10000);
        }
        else
        {
            benchAtomicIncrement(10000);
            benchAtomicIncrement(10000);
        }
        Thread.sleep(1000);
        System.out.println("warmup done");

        HWCounters.start();
        // bench
        if (std)
            benchStdIncrement(5*1000*1000);
        else
            benchAtomicIncrement(5*1000*1000);
 
	HWCounters.stop();
	HWCounters.printResults();
        HWCounters.shutdown();
   }
}