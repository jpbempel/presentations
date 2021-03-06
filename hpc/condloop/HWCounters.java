import ch.usi.overseer.OverHpc;

public class HWCounters
{
    private static String[] EVENTS = {
	
	"CPU-CYCLES",	
	"RETIRED_MISPREDICTED_BRANCH_INSTRUCTIONS",
    };
    
    private static String[] EVENTS_NAME = {
	
	"Cycles",
	"branch mispredicted",
    };

    private static long[] results = new long[EVENTS.length];
    
    private static OverHpc oHpc = OverHpc.getInstance();
    
    public static void init()	
    {
	StringBuilder sb = new StringBuilder();
	for (int i = 0; i < EVENTS.length; i++)	    
	{
	    if (i > 0)
	    {
		sb.append(",");
	    }
	    sb.append(EVENTS[i]);
	    
	}
	oHpc.initEvents(sb.toString());
    }
    
    public static void start()
    {
	int tid = oHpc.getThreadId();
	oHpc.bindEventsToThread(tid);	
    }

    public static void stop()
    {
	int tid = oHpc.getThreadId();
	for (int i = 0; i < EVENTS.length; i++)
	{
	    results[i] = oHpc.getEventFromThread(tid, i);
	}
    }

    public static void printResults()	
    {
	for (int i = 0; i < EVENTS.length; i++)
	{
	    System.out.println(EVENTS_NAME[i] + ": " + String.format("%,d", results[i]));
	}	
    }

    public static void shutdown()
    {
	oHpc.shutdown();
	
    }
}