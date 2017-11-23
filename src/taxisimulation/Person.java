/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package taxisimulation;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Setup
 */
public class Person extends Thread
{
    private LinkedList <Instruction> Instructions;
    private int currentLocation;
    private int nextLocation;
    private final Taxi taxi;
    private final int identifier;
    
    public Person(String line, Taxi taxi)
    {
        this.taxi = taxi;
        
        identifier = Integer.parseInt(line.substring(0,1));
        
        line = line.substring(2,line.length());
        
        String newLine = line.replaceAll("[(),]","");
        String[] characters = newLine.split(" ");
        
        Instructions = new LinkedList<Instruction>();
        
        int counter = 1;
        
        for(int i = 0; i < characters.length; i+=2)
        {
            int branch = Integer.parseInt(characters[i]);
            int duration = Integer.parseInt(characters[counter]);
            Instruction temp = new Instruction(branch,duration);
            Instructions.add(temp);
            counter+=2;
        }
    }
    
    private void setCurrentLocation(int location)
    {
        currentLocation = location;
    }
    
    private void setDestination(int location)
    {
        nextLocation = location;
    }
    
    public int getCurrentLocation()
    {
        return currentLocation;
    }
    
    public int getDestination()
    {
        return nextLocation;
    }
    
    public int getIdentifier()
    {
        return identifier;
    }
    
    /*
    * This method causes the thread to do work in the office, before going to
    * the next stop.
    */
    private void work(int duration)
    {
        try 
        {
            sleep(duration);
        } 
        catch (InterruptedException ex) 
        {
            Logger.getLogger(Person.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /*
    * This run functions by setting the current location of the thread as its
    * pickup location and the location it wants to get to as its destination,
    * then the person calls hail in order to travel to another branch to work.
    */
    public void run()
    {
        Instruction currentInstruction = null;
        Instruction previousInstruction = null;
        while(!Instructions.isEmpty())
        {
            previousInstruction = currentInstruction;
            currentInstruction = Instructions.removeFirst();
            
            if(previousInstruction==null)
            {
                setCurrentLocation(0);
                setDestination(currentInstruction.getBranch());
                taxi.hail(this);
                
                //System.out.println("Thread: "+identifier+" has gotten off at: "+nextLocation);
                
                work(currentInstruction.getBranch());
                //System.out.println("Thread: "+identifier+" has worked at: "+nextLocation);
            }
            else
            {
                setCurrentLocation(previousInstruction.getBranch());
                setDestination(currentInstruction.getBranch());
                taxi.hail(this);
                
                //System.out.println("Thread: "+identifier+" has gotten off at: "+nextLocation);
                
                work(currentInstruction.getDuration());
                //System.out.println("Thread: "+identifier+" has worked at: "+nextLocation);
            }
        }
    }
    
    /*
    * This method ensures that the person cannot get straight back into the taxi
    * if the taxi is still at the same stop.
    */
    public void stallHail()
    {
        synchronized(this)
        {
            try 
            {
                this.wait();
            } 
            catch (InterruptedException ex) 
            {
               ex.printStackTrace();
            }
        }
    }
    
    /*
    * This method ensures the person can call hail AFTER the taxi has left.
    */
    public void quitStalling()
    {
        synchronized(this)
        {
            this.notify();
        }
    }
}
