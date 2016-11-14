import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Set;

/**
 * Created by austinpilz on 11/9/16.
 */

public class MemorySimulator
{
    /**
     * Opt algorithim
     * @param numberOfFrames
     * @param traceFileName
     */
    public void opt(int numberOfFrames, String traceFileName)
    {
        //Page Table Objects
        Hashtable<Integer, PageTableEntry> pageTable = new Hashtable<>();
        Hashtable<Integer, LinkedList<Integer>> future = new Hashtable<>();

        //Data tracking
        int numberMemoryAccesses = 0;
        int numberPageFaults = 0;
        int numberDiskWrites = 0;
        int[] page_frames = new int[numberOfFrames];
        BufferedReader bufferedReader = null;

        try
        {
            /**
             * -- Init Page Tables --
             */

            //Log
            System.out.println("-- Opt Algorithm -- ");
            System.out.println("* Beginning page table init...");

            //Init the page and future tables
            for (int i = 0; i < 1024*1024; i++)
            {
                pageTable.put(i, new PageTableEntry());
                future.put(i, new LinkedList<>());
            }

            // Init page_frames array to -1
            for (int i = 0; i < numberOfFrames; i++)
            {
                page_frames[i] = -1;
            }

            //Log
            System.out.println("* Page table init complete.");

            /**
             * -- Opt PreProcessing ---
             * Future HashTable has page index as the key, value is a LL which records all page locations
             */

            //Begin opt pre-process
            System.out.println("Beginning opt pre-process...");

            int count = 0;
            bufferedReader = new BufferedReader(new FileReader(traceFileName));
            while (bufferedReader.ready())
            {
                //Get the page number, first 5 digits are the hex
                String [] line_arr = bufferedReader.readLine().split(" ");

                //Add location to the page's LinkedList
                future.get(Integer.decode("0x" + line_arr[0].substring(0, 5))).add(count++);
            }

            System.out.println("Opt pre-process complete");

            /**
             * -- Begin Opt --
             */
            System.out.println("Beginning opt...");

            int currentFrame = 0;

            //Open a new buffered reader to start from beginning of the file
            bufferedReader = new BufferedReader(new FileReader(traceFileName));
            while (bufferedReader.ready())
            {
                String [] line_arr = bufferedReader.readLine().split(" ");
                int page_number = Integer.decode("0x"+line_arr[0].substring(0, 5));
                future.get(page_number).removeFirst();


                PageTableEntry entry = pageTable.get(page_number);
                entry.referenced = true;
                entry.index = page_number;

                //R or W char follows Hex (page number/index) to indicate Read or Write
                if (line_arr[1].equals("W"))
                {
                    //Write accesses result in dirty PTE's as they need to be saved to disk
                    entry.dirty = true;
                }

                //Determine if page fault
                if(entry.valid)
                {
                    //Print page hit
                    System.out.println(page_number + "("+line_arr[0]+")" + " (hit)");
                }
                else
                {
                    //Page fault occurred

                    if (currentFrame < numberOfFrames)
                    {
                        //If there is still room for page frame
                        page_frames[currentFrame] = page_number;
                        entry.frame = currentFrame++;
                        entry.valid = true;

                        System.out.println(line_arr[0]+" (page fault – non-eviction)");
                    }
                    else
                    {
                        //Page_Frames is full - we need to find the furthest page to evict
                        int longestDistance = findFurthestPage(page_frames, future);
                        PageTableEntry furthestPTE = pageTable.get(longestDistance);

                        //Need to increment disk write if PTE to evict is dirty
                        if (furthestPTE.dirty)
                        {
                            System.out.println(line_arr[0] + " (page fault – dirty eviction)");
                            numberDiskWrites++;
                        }
                        else
                        {
                            System.out.println(line_arr[0] + " (page fault – clean eviction)");
                        }

                        //Perform page eviction/swap
                        page_frames[furthestPTE.frame] = entry.index;
                        entry.frame = furthestPTE.frame;
                        entry.valid = true;

                        furthestPTE.dirty = false;
                        furthestPTE.referenced = false;
                        furthestPTE.valid = false;
                        furthestPTE.frame = -1;

                        pageTable.put(longestDistance, furthestPTE);
                    }

                    //Increment page fault counter
                    numberPageFaults++;
                }

                //Update the page table
                pageTable.put(page_number, entry);

                //Increment mem access counter
                numberMemoryAccesses++;
            }

            //Print the stats to the console
            printStats("Opt", numberOfFrames, numberMemoryAccesses, numberPageFaults, numberDiskWrites);
        }
        catch (Exception e)
        {
            //Print errors
            e.printStackTrace();
        }
    }

    public void clock(int numberOfFrames, String traceFileName)
    {
        //Algorithm Variables
        int clockHandPosition = 0;
        int numberMemoryAccesses = 0;
        int numberPageFaults = 0;
        int numberDiskWrites = 0;

        //Create data struct
        Hashtable<Integer, PageTableEntry> pageTable = new Hashtable<Integer, PageTableEntry>();
        int[] page_frames = new int[numberOfFrames];

        BufferedReader br = null;
        try
        {
            /**
             * -- Init Page Tables --
             */

            //Log
            System.out.println("-- Clock Algorithm -- ");
            System.out.println("* Beginning page table init...");

            for (int i = 0; i < 1024*1024; i++)
            {
                pageTable.put(i, new PageTableEntry());
            }

            // Init page_frames array to -1
            for (int i = 0; i < numberOfFrames; i++)
            {
                page_frames[i] = -1;
            }

            /**
             * -- Begin Clock --
             */
            System.out.println("Beginning clock...");

            int currentFrame = 0;

            //Open a new buffered reader to start from beginning of the file
            br = new BufferedReader(new FileReader(traceFileName));
            while (br.ready())
            {
                String [] line_arr = br.readLine().split(" ");
                int page_number = Integer.decode("0x" + line_arr[0].substring(0, 5));

                PageTableEntry entry = pageTable.get(page_number);
                entry.referenced = true;
                entry.index = page_number;


                //R or W char follows Hex (page number/index) to indicate Read or Write
                if (line_arr[1].equals("W"))
                {
                    //Write accesses result in dirty PTE's as they need to be saved to disk
                    entry.dirty = true;
                }

                //Determine if page fault
                if(entry.valid)
                {
                    //Print page hit
                    System.out.println(page_number + "("+line_arr[0]+")" + " (hit)");
                }
                else 
                {
                    //Page fault occurred
                    if (currentFrame < numberOfFrames)
                    {
                        //If there is still room for page frame
                        page_frames[currentFrame] = page_number;
                        entry.frame = currentFrame++;
                        entry.valid = true;
                        System.out.println(line_arr[0]+" (page fault – non-eviction)");
                    } 
                    else 
                    {
                        //Need to perform an eviction
                        int pageNumberToEvict = 0;
                        boolean pageFound = false;

                        while (pageFound == false)
                        {
                            //If we've reached the end of the page frames, reset to beginning
                            if (clockHandPosition == page_frames.length || clockHandPosition < 0 ) 
                            { 
                                clockHandPosition = 0; 
                            }

                            //If current page frame is not referenced, we've found page to evict
                            if (!pageTable.get(page_frames[clockHandPosition]).referenced) 
                            {
                                pageFound = true; //Mark to end the search loop
                                pageNumberToEvict = page_frames[clockHandPosition];
                            } 
                            else 
                            {
                                //The page was referenced, but the cost is to be marked as not referenced
                                pageTable.get(page_frames[clockHandPosition]).referenced = false;
                            }

                            //Increment clock hand position, so next time we search through it will remember where we left off
                            clockHandPosition++;
                        }

                        PageTableEntry pageTableEntryToEvict = pageTable.get(pageNumberToEvict);

                        if(pageTableEntryToEvict.dirty)
                        {
                            //If the page table entry to evict is dirty, increment disk write counter
                            numberDiskWrites++;
                            System.out.println(line_arr[0] + " (page fault – dirty eviction)");
                        } 
                        else 
                        {
                            //Otherwise it's clean
                            System.out.println(line_arr[0] + " (page fault – clean eviction)");
                        }

                        //Perform swap/eviction
                        page_frames[pageTableEntryToEvict.frame] = entry.index;
                        entry.frame = pageTableEntryToEvict.frame;
                        entry.valid = true;

                        pageTableEntryToEvict.valid = false;
                        pageTableEntryToEvict.dirty = false;
                        pageTableEntryToEvict.frame = -1;
                        pageTableEntryToEvict.referenced = false;


                        pageTable.put(pageNumberToEvict, pageTableEntryToEvict);
                    }

                    //Increment # of page faults
                    numberPageFaults++;
                }

                //Update the page table
                pageTable.put(page_number, entry);

                //Increment mem access counter
                numberMemoryAccesses++;
            }

            //Print stats to console
            printStats("Clock", numberOfFrames, numberMemoryAccesses, numberPageFaults, numberDiskWrites);
        }
        catch (Exception e)
        {
            //Print errors
            e.printStackTrace();
        }
    }

    public void aging(int numberOfFrames, int refresh, String traceFileName, boolean verbose)
    {
        //Algorithm Variables
        int numberMemoryAccesses = 0;
        int numberPageFaults = 0;
        int numberDiskWrites = 0;

        //Create data struct
        Hashtable<Integer, PageTableEntry> pageTable = new Hashtable<Integer, PageTableEntry>();
        int[] page_frames = new int[numberOfFrames];
        int time = 0; //keep track of line number

        //Check to make refresh rate is not 0
        if (refresh == 0)
        {
            //Assign a random value
            refresh = 55;
        }

        BufferedReader br = null;
        try
        {
            /**
             * -- Init Page Tables --
             */

            //Log
            System.out.println("-- Aging Algorithm -- ");
            System.out.println("* Beginning page table init...");

            for (int i = 0; i < 1024*1024; i++)
            {
                pageTable.put(i, new PageTableEntry());
            }

            // Init page_frames array to -1
            for (int i = 0; i < numberOfFrames; i++)
            {
                page_frames[i] = -1;
            }

            /**
             * -- Begin Aging --
             */
            System.out.println("Beginning aging...");

            int currentFrame = 0;

            //Open a new buffered reader to start from beginning of the file
            br = new BufferedReader(new FileReader(traceFileName));
            while (br.ready())
            {
                //Check for time refresh, and take this moment to inc time since new line
                if (time++ % refresh == 0)
                {
                    System.out.println("**Time Refresh**");
                    //Time to time shift all page table entries


                    for (int i = 0; i < page_frames.length; i++)
                    {
                        if (page_frames[i] != -1) {
                            pageTable.get(page_frames[i]).timeShift();
                        }
                    }
                }

                String [] line_arr = br.readLine().split(" ");
                int page_number = Integer.decode("0x" + line_arr[0].substring(0, 5));

                PageTableEntry entry = pageTable.get(page_number);
                entry.referenced = true;
                entry.index = page_number;

                //R or W char follows Hex (page number/index) to indicate Read or Write
                if (line_arr[1].equals("W"))
                {
                    //Write accesses result in dirty PTE's as they need to be saved to disk
                    entry.dirty = true;
                }

                //Determine if page is loaded into memory
                if(entry.valid)
                {
                    System.out.println(page_number + "(" + line_arr[0] + ")" + " (hit)");

                }
                else
                {
                    //Not loaded into memory
                    numberPageFaults++;

                    if (currentFrame < numberOfFrames)
                    {
                        //There's still room in memory for this page, no eviction necessary
                        page_frames[currentFrame] = page_number;
                        entry.frame = currentFrame++;
                        entry.valid = true;
                        //System.out.println(line_arr[0]+" (page fault – non-eviction) Inserting page_number as " + page_number);
                    }
                    else
                    {
                        //Eviction required

                        boolean [] temp;
                        int minValue = 999999999;
                        int currentValue = 0;
                        int pageNumberToEvict = 0;

                        int exponent = 7;

                        for (int i = 0; i < page_frames.length; i++)
                        {
                            PageTableEntry temporaryPageTableEntry = pageTable.get(page_frames[i]);

                            currentValue = 0;
                            temp = temporaryPageTableEntry.time;
                            exponent = 7;
                            for (int j = 0; j < temp.length; j++)
                            {
                                if (temp[j])
                                {
                                    currentValue += Math.pow(2, exponent);
                                }

                                exponent--;
                            }

                            if (temporaryPageTableEntry.referenced)
                            {
                                currentValue += Math.pow(2, 8);
                            }

                            if (currentValue < minValue)
                            {
                                minValue = currentValue;
                                pageNumberToEvict = i;
                            }
                        }

                        PageTableEntry pageTableEntryToEvict = pageTable.get(page_frames[pageNumberToEvict]);

                        System.out.println("Evicting page# " + pageNumberToEvict + " -> page frame index #" + page_frames[pageNumberToEvict] +" -> frame # " + pageTable.get(page_frames[pageNumberToEvict]).frame);
                        System.out.println("Replacing PTE Index " + pageTableEntryToEvict.index + " with PTE index " + entry.index);



                        //Determine if PTE to evict is clean/dirty
                        if(pageTableEntryToEvict.dirty)
                        {
                            System.out.println(line_arr[0] + " (page fault – dirty eviction)");
                            numberDiskWrites++;
                        }
                        else
                        {
                            System.out.println(line_arr[0] + " (page fault – clean eviction)");

                        }

                        //Perform swap/eviction
                        page_frames[pageTableEntryToEvict.frame] = entry.index;
                        entry.frame = pageTableEntryToEvict.frame;
                        entry.valid = true;

                        pageTableEntryToEvict.valid = false;
                        pageTableEntryToEvict.dirty = false;
                        pageTableEntryToEvict.frame = -1;
                        pageTableEntryToEvict.referenced = false;


                        pageTable.put(page_number, pageTableEntryToEvict);

                    }

                    numberPageFaults++;
                }

                //Insert into page table
                //System.out.println("Putting Page# " + page_number + " and entry frame" + entry.frame);
                pageTable.put(page_number, entry);

                //Increment mem access counter
                numberMemoryAccesses++;
            }

            //Print stats to console
            printStats("Aging", numberOfFrames, numberMemoryAccesses, numberPageFaults, numberDiskWrites);
        }
        catch (Exception e)
        {
            //Print errors
            e.printStackTrace();
        }
    }

    public void workingSetClock(int numberOfFrames, int refresh, int tau, String traceFileName, boolean verbose)
    {
        //Algorithm Variables
        int numberMemoryAccesses = 0;
        int numberPageFaults = 0;
        int numberDiskWrites = 0;

        //Create data struct
        Hashtable<Integer, PageTableEntry> pageTable = new Hashtable<Integer, PageTableEntry>();
        int[] page_frames = new int[numberOfFrames];
        int time = 0; //keep track of line number

        //Check to make refresh rate is not 0
        if (refresh == 0)
        {
            //Assign a random value
            refresh = 10; //optimal
        }

        if (tau == 0)
        {
            //Assign a random value
            tau = 5;
        }

        BufferedReader br = null;
        try
        {
            /**
             * -- Init Page Tables --
             */

            //Log
            System.out.println("-- Working Set Algorithm -- ");
            System.out.println("* Beginning page table init...");

            for (int i = 0; i < 1024*1024; i++)
            {
                pageTable.put(i, new PageTableEntry());
            }

            // Init page_frames array to -1
            for (int i = 0; i < numberOfFrames; i++)
            {
                page_frames[i] = -1;
            }

            /**
             * -- Begin Working Set --
             */
            System.out.println("Beginning working set...");

            int currentFrame = 0;
            int searchFrame = 0;

            //Open a new buffered reader to start from beginning of the file
            br = new BufferedReader(new FileReader(traceFileName));
            while (br.ready())
            {
                //Check for time refresh, and take this moment to inc time since new line
                if (time++ % refresh == 0)
                {
                    System.out.println("**Time Refresh**");

                    //On refresh - return all pages ref flags back to false
                    for (int i = 0; i < page_frames.length; i++)
                    {
                        if (page_frames[i] != -1)
                        {
                            PageTableEntry entry = pageTable.get(page_frames[i]);

                            //When setting referenced to false, record the current virtual time in your page table entry for that page.
                            entry.referenced = false;
                            entry.age = time;
                        }
                    }
                }

                String [] line_arr = br.readLine().split(" ");
                int page_number = Integer.decode("0x" + line_arr[0].substring(0, 5));

                PageTableEntry entry = pageTable.get(page_number);
                entry.referenced = true;
                entry.index = page_number;

                //R or W char follows Hex (page number/index) to indicate Read or Write
                if (line_arr[1].equals("W"))
                {
                    //Write accesses result in dirty PTE's as they need to be saved to disk
                    entry.dirty = true;
                }

                //Determine if page is loaded into memory
                if(entry.valid)
                {
                    //Print page hit
                    System.out.println(page_number + "(" + line_arr[0] + ")" + " (hit)");

                }
                else
                {
                    //Not loaded into memory
                    numberPageFaults++;

                    if (currentFrame < numberOfFrames)
                    {
                        //There's still room in memory for this page, no eviction necessary
                        page_frames[currentFrame] = page_number;
                        entry.frame = currentFrame++;
                        entry.valid = true;
                        entry.age = time;
                    }
                    else
                    {
                        //Eviction required

                        /*
                        Scan the page table looking at valid pages, each time continuing from where you left off previously
                        If you find one that is unreferenced and clean, evict it and stop.
                        Along the way, if you encounter a page that is unreferenced, older than tau, and dirty, write it out to disk and mark it as clean.
                        If you get through the whole page table with no unreferenced, clean pages, evict the page with the oldest timestamp
                         */

                        boolean search = true;
                        boolean found = false;
                        int originalSearchFrame = searchFrame;
                        int location = 0; //page table index

                        while (search && !found && location < page_frames.length-1)
                        {
                            PageTableEntry viewingEntry = pageTable.get(page_frames[searchFrame++]);

                            if (!viewingEntry.referenced && !viewingEntry.dirty)
                            {
                                //Stop search, mark for eviction
                                search = false;
                                found = true;

                                //Eviction
                                PageTableEntry pageTableEntryToEvict = viewingEntry;

                                //Determine if PTE to evict is clean/dirty
                                if(pageTableEntryToEvict.dirty)
                                {
                                    System.out.println(line_arr[0] + " (page fault – dirty eviction)");
                                    numberDiskWrites++;
                                }
                                else
                                {
                                    System.out.println(line_arr[0] + " (page fault – clean eviction)");
                                }

                                //Perform swap/eviction
                                page_frames[pageTableEntryToEvict.frame] = entry.index;
                                entry.frame = pageTableEntryToEvict.frame;
                                entry.valid = true;

                                pageTableEntryToEvict.valid = false;
                                pageTableEntryToEvict.dirty = false;
                                pageTableEntryToEvict.frame = -1;
                                pageTableEntryToEvict.referenced = false;


                                pageTable.put(page_number, pageTableEntryToEvict);
                            }

                            //Time - age of entry compared to tau to see if it's past the limit
                            if (!viewingEntry.referenced && viewingEntry.dirty && time - viewingEntry.age > tau)
                            {
                                numberDiskWrites++;
                                viewingEntry.dirty = false;
                            }

                            //If the frame search index gets to the end of the valid pages, then reset it back to the beginning
                            if (searchFrame == page_frames.length-1)
                            {
                                searchFrame = 0;
                            }

                            //We've gone all the way around
                            if (searchFrame == originalSearchFrame)
                            {
                                search = false;
                            }
                        }

                        if (!found)
                        {
                            //Search was not ended due to finding it, so evict the page with the oldest timestamp
                            int oldestFrame = -1;
                            int oldestAge = -1;

                            for (int i = 0; i < page_frames.length; i++)
                            {
                                if (page_frames[i] != -1)
                                {
                                    PageTableEntry searchEntry = pageTable.get(page_frames[i]);

                                    if (searchEntry.age > oldestAge)
                                    {
                                        oldestAge = searchEntry.age;
                                        oldestFrame = i;
                                    }
                                }
                            }

                            //Eviction
                            PageTableEntry pageTableEntryToEvict = pageTable.get(page_frames[oldestFrame]);

                            //Determine if PTE to evict is clean/dirty
                            if(pageTableEntryToEvict.dirty)
                            {
                                System.out.println(line_arr[0] + " (page fault – dirty eviction)");
                                numberDiskWrites++;
                            }
                            else
                            {
                                System.out.println(line_arr[0] + " (page fault – clean eviction)");

                            }

                            //Perform swap/eviction
                            page_frames[pageTableEntryToEvict.frame] = entry.index;
                            entry.frame = pageTableEntryToEvict.frame;
                            entry.valid = true;

                            pageTableEntryToEvict.valid = false;
                            pageTableEntryToEvict.dirty = false;
                            pageTableEntryToEvict.frame = -1;
                            pageTableEntryToEvict.referenced = false;


                            pageTable.put(page_number, pageTableEntryToEvict);
                        }
                    }

                    numberPageFaults++;
                }

                //Insert into page table
                //System.out.println("Putting Page# " + page_number + " and entry frame" + entry.frame);
                pageTable.put(page_number, entry);

                //Increment mem access counter
                numberMemoryAccesses++;
            }

            //Print stats to console
            printStats("Aging", numberOfFrames, numberMemoryAccesses, numberPageFaults, numberDiskWrites);
        }
        catch (Exception e)
        {
            //Print errors
            e.printStackTrace();
        }
    }

    /**
     * Find the furthest page for eviction used by opt.
     * @param page_frames
     * @param future
     * @return
     */
    private static int findFurthestPage(int[] page_frames, Hashtable<Integer, LinkedList<Integer>> future)
    {
        int index = 0, max = 0;
        for (int i = 0; i < page_frames.length; i++)
        {
            if(future.get(page_frames[i]).isEmpty())
            {
                return page_frames[i];
            }
            else
            {
                if(future.get(page_frames[i]).get(0) > max)
                {
                    max = future.get(page_frames[i]).get(0);
                    index = page_frames[i];
                }
            }
        }

        return index;
    }

    /**
     * Prints the statistics for the supplied algorithm to the console
     * @param alg
     * @param numberFrames
     * @param numberMemoryAccesses
     * @param numberPageFaults
     * @param numberDiskWrites
     */
    private static void printStats(String alg, int numberFrames, int numberMemoryAccesses, int numberPageFaults, int numberDiskWrites)
    {
        /**
         * Format:
         * [Name of Algorithim]
         * # Mem Accesses
         * # Page Faults
         * # Disk Writes
         * (but follow exact wording from project description)
         */


        System.out.println("Algorithm: " + alg);
        System.out.println("Number of frames:\t" + numberFrames);
        System.out.println("Total memory accesses:\t" + numberMemoryAccesses);
        System.out.println("Total page faults:\t" + numberPageFaults);
        System.out.println("Total writes to disk:\t" + numberDiskWrites);
    }
}
