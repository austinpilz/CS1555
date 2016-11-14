/**
 * Created by austinpilz on 11/9/16.
 */
public class vmsim
{
    public static void main(String[] args)
    {

        int numberOfFrames = 0;
        String algorithm = "";
        String traceFileName = "";

        //For aging & working set
        int refresh = 0;
        int tau = 0;
        boolean verbose = false;

        //Create our memory simulator object
        MemorySimulator memorySimulator = new MemorySimulator();

        //Argument usage: ./vmsim –n <numframes> -a <opt|clock|aging|work> [-r <refresh>] [-t <tau>] <tracefile>
        for (int i = 0; i < args.length; ++i)
        {
            if (args[i].equals("-n"))
            {
                //Get the number of frames

                try
                {
                    numberOfFrames = Integer.parseInt(args[1]);
                }
                catch (Exception e)
                {
                    System.out.println("\n**ERROR: No number supplied for numframes parameter\n");
                    printSyntaxUsage();
                    System.exit(0);
                }

                if (numberOfFrames!=8 && numberOfFrames!=16 && numberOfFrames!=32 && numberOfFrames!=64 && numberOfFrames!=128 )
                {
                    System.out.println("\n**ERROR: Invalid number of frames supplied! Must be 8, 16, 32, 64, or 128\n");
                    System.exit(0);
                }

            }
            else if (args[i].equals("-a"))
            {
                //Get the algorithm
                algorithm = args[i+1];

                //Verify that supplied algorithm is one of our supported
                if (!algorithm.equalsIgnoreCase("opt") && !algorithm.equalsIgnoreCase("clock") && !algorithm.equalsIgnoreCase("aging") && !algorithm.equalsIgnoreCase("work"))
                {
                    System.out.println("\n**ERROR: Unknown algorithm " + algorithm + ". Must be opt, clock, aging or work. \n");
                    System.exit(0);
                }
            }
            else if (args[i].equals("-r"))
            {
                //Get the refresh rate
                refresh = Integer.parseInt(args[i+1]);
            }
            else if (args[i].equals("-t"))
            {
                //Get tau
                tau = Integer.parseInt(args[i+1]);
            }
            else if (args[i].equals("-v"))
            {
                //Get tau
                verbose = true;
            }
            else if (i == args.length-1)
            {
                //Get the name of the trace file
                traceFileName = args[i];
            }
        }

        //Now for the actual execution
        if (algorithm.equals("opt"))
        {
            memorySimulator.opt(numberOfFrames, traceFileName);
        }
        else if (algorithm.equals("clock"))
        {
            memorySimulator.clock(numberOfFrames, traceFileName);
        }
        else if (algorithm.equals("aging"))
        {
            memorySimulator.aging(numberOfFrames, refresh, traceFileName, verbose);
        }
        else if (algorithm.equals("work"))
        {
            memorySimulator.workingSetClock(numberOfFrames, refresh, tau, traceFileName, verbose);
        }
    }

    public static void printSyntaxUsage()
    {
        System.out.println("\nThe following is the proper vmsim syntax:");
        System.out.println("java vmsim –n <numframes> -a <opt> <tracefile>");
        System.out.println("java vmsim –n <numframes> -a <clock> <tracefile>");
        System.out.println("java vmsim –n <numframes> -a <lru> -r <refresh> <tracefile>");
        System.out.println("java vmsim –n <numframes> -a <work> -r <refresh> -t <tau> <tracefile>");
    }
}
