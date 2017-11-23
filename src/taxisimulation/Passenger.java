/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package taxisimulation;

/**
 *
 * @author Setup
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
