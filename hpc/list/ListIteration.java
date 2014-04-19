import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import ch.usi.overseer.OverHpc;

public class ListIteration
{
    private static List<String> arrayList = new ArrayList<>();
    private static List<String> linkedList = new LinkedList<>();

    public static void initializeList(List<String> list, int bufferSize)
    {
        for (int i = 0; i < 5000; i++)
        {
            byte[] buffer = null;
            if (bufferSize > 0)
            {
                buffer = new byte[bufferSize];
            }
            String s = String.valueOf(i);
            list.add(s);
            // avoid buffer to be optimized away
            if (System.currentTimeMillis() == 0)
            {
                System.out.println(buffer);
            }
        }
    }

    public static void bench(List<String> list)
    {
        if (list.contains("bar"))
        {
            System.out.println("bar found");
        }
    }

    public static void main(String[] args) throws Exception
    {
        if (args.length != 2) return;
        List<String> benchList = "array".equals(args[0]) ? arrayList : linkedList;
        int bufferSize = Integer.parseInt(args[1]);
        initializeList(benchList, bufferSize);
        HWCounters.init();
        System.out.println("init done");
        // warmup
        for (int i = 0; i < 10000; i++)
        {
            bench(benchList);
        }
        Thread.sleep(1000);
        System.out.println("warmup done");

        HWCounters.start();
        for (int i = 0; i < 1000; i++)
        {
            bench(benchList);
        }
        HWCounters.stop();
        HWCounters.printResults();
        HWCounters.shutdown();
    }
}
