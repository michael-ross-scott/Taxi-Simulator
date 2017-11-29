/**
 *
 * @author Michael Scott
 */
public class Passenger 
{
    private Person p;
    private int branch;
    
    public Passenger(Person p, int branch)
    {
        this.p = p;
        this.branch = branch;
    }
    
    public int getBranch()
    {
        return branch;
    }
    
    public Person getPerson()
    {
        return p;
    }
}
