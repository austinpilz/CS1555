/**
 * Created by austinpilz on 11/9/16.
 */
public class PageTableEntry
{
    int index;
    int frame;
    boolean valid;
    boolean dirty;
    boolean referenced;
    boolean [] time;
    int age;

    /**
     * Default constructor for creating the page table entry object
     */
    public PageTableEntry()
    {
        this.index = 0;
        this.frame = -1;
        this.valid = false;
        this.dirty = false;
        this.referenced = false;
        this.time = new boolean [8];
        this.age = 0;
    }


    public void timeShift()
    {
        boolean [] temporaryTime = new boolean [8];

        for (int i = 0; i < time.length - 1; i++)
        {
            temporaryTime[i + 1] = time[i];
        }

        if (referenced)
        {
            temporaryTime[0] = true;
        }
        else
        {
            temporaryTime[0] = false;
        }

        referenced = false;

        time = temporaryTime;
    }
}
