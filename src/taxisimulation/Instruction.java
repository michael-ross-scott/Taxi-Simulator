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
