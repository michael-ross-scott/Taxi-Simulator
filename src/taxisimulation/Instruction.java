/**
 *
 * @author Michael Scott
 */
public class Instruction {
    private int branch;
    private int duration;  
    
    public Instruction(int branch, int duration)
    {
        this.branch = branch;
        this.duration = duration;
    }
    
    public int getBranch()
    {
        return branch;
    }
    
    public int getDuration()
    {
        return duration;
    }
}
