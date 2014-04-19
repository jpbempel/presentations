

import java.util.Random;
import java.util.Arrays;


public class CondLoop
{
    final static int COUNT = 64*1024;
    static Random random = new Random(System.currentTimeMillis());

    private static int[] createData(int count, boolean warmup, boolean predictable)
    {
	int[] data = new int[count];
	for (int i = 0; i < count; i++)
	{
	    data[i] = warmup ? random.nextInt(2) : (predictable ? 1 : random.nextInt(2));
	}
	return data;
    }
    
    private static int benchCondLoop(int[] data)
    {
	long ms = System.currentTimeMillis();
	HWCounters.start();
	int sum = 0;
	for (int i = 0; i < data.length; i++)
	{
	    if (i+ms > 0 && data[i] == 1)
		sum += i;
	}
	HWCounters.stop();
	return sum;
    }

    public static void main(String[] args) throws Exception
    {
	boolean predictable = Boolean.parseBoolean(args[0]);
	HWCounters.init();
	int count = 0;
	for (int i = 0; i < 10000; i++)
	{
	    int[] data = createData(1024, true, predictable); 
	    count += benchCondLoop(data);
	}
	System.out.println("warmup done");
	Thread.sleep(1000);
	int[] data = createData(512*1024, false, predictable); 
	//Arrays.sort(data);
	count += benchCondLoop(data);
	HWCounters.printResults();
	System.out.println(count);
	HWCounters.shutdown();
	
    }
}