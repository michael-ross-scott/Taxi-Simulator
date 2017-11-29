import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 *
 * @author Michael Scott
 */
public class Taxi extends Thread{
    
    private Semaphore[] stops;
    private List <Passenger> Hailed;
    private List <Passenger> Passengers;
    private List <Person> Banned;
    
    private enum Status{INBOUND,OUTBOUND,IDLE};
    
    private Status status;
    
    private int currentBranch;
    private int destinationBranch;
    
    private final int end;
    private Time timer;
    
    private boolean firstStop;
    
    public Taxi(int numberOfBranches)
    {   
        timer = new Time();
        firstStop = true;
        
        List<Passenger> tempHailed = new ArrayList <Passenger>();
        List<Passenger> tempPassengers = new ArrayList <Passenger> ();
        List<Person> tempBanned = new ArrayList <Person> ();
        
        Hailed = Collections.synchronizedList(tempHailed);
        Passengers = Collections.synchronizedList(tempPassengers);
        Banned = Collections.synchronizedList(tempBanned);
        
        // Creates an array of Semaphores for each stop
        stops = new Semaphore[numberOfBranches];
        
        for(int i = 0; i < numberOfBranches; i++)
        {
            // Fair semaphore for ordering
            
            Semaphore stop = new Semaphore(1,true);
            stops[i] = stop;
            
            // Taxi needs to acquire all the locks for the branches
            try 
            {
                stops[i].acquire();
            } 
            catch (InterruptedException ex) 
            {
                ex.printStackTrace();
            }
        }
        
        currentBranch = 0;
        end = numberOfBranches - 1;
        destinationBranch = end;
    }
    
    public void run()
    {
        while(Thread.activeCount() > 2)
        {
            checkStatus();
            while(status == Status.IDLE && Thread.activeCount() > 2)
            {
                checkStatus();
            }
            
            while(status != Status.IDLE)
            {
                checkStatus();
                drive();
            }
        }
    }
    
    
    /*
    * This method adds the passenger to the hailed list (which is a synchronized list),
    * the first Semaphore is only acquired when the taxi is released. The method
    * then adds to the passengers (which is a synchronized list) it then acquires
    * the second semaphore (which is only released when the taxi gets to the 
    * destination).
    */
    
    public void hail(Person person)
    {
        int currentLocation = person.getCurrentLocation();
        int destination = person.getDestination();
        int identifier = person.getIdentifier();
        
        Passenger tempHailed = new Passenger(person,currentLocation);
        
        Hailed.add(tempHailed);
            
        try 
        {
            // Makes the Person wait if they have attempt to get back onto the taxi
            if(Banned.contains(person))
            {
                person.stallHail();
            }
            System.out.println(timer.getTime()+" branch "+currentLocation+": person "+identifier+" hail");
            stops[currentLocation].acquire();
                    
            Passenger tempPassenger = new Passenger(person,destination);
            System.out.println(timer.getTime()+" branch "+currentBranch+": person "+identifier+" request "+destination);
            Passengers.add(tempPassenger);
            
            Hailed.remove(tempHailed);
            
            stops[currentLocation].release();
            
            stops[destination].acquire();
            
            Passengers.remove(tempPassenger);
            Banned.add(person);
            
            stops[destination].release();
        }
        catch (InterruptedException ex)
        {
            ex.printStackTrace();
        }
    }
    
    /*
     * Changes the status of the taxi
    */
    private void checkStatus()
    {
        if(Hailed.isEmpty() && Passengers.isEmpty())
        {
            status = Status.IDLE;
            //System.out.println("Taxi: is idle");
            clearBannedList();
        }
        else if(currentBranch<destinationBranch && destinationBranch==end)
        {
            status = Status.OUTBOUND;
            //System.out.println("Taxi: Outbound");
        }
        else if(currentBranch>destinationBranch && destinationBranch==0)
        {
            status = Status.INBOUND;
            //System.out.println("Taxi: Inbound");
        }
        else if(currentBranch == destinationBranch && destinationBranch==end)
        {
            destinationBranch=0;
            status = Status.INBOUND;
            //System.out.println("Taxi: Switching from Outbond to inbound");
        }
        else if(currentBranch == destinationBranch && destinationBranch==0)
        {
            destinationBranch=end;
            status = Status.OUTBOUND;
            //System.out.println("Taxi: Switching from Inbound to outbound");
        }
    }
    
    // Drives the taxi
    private void drive()
    {
        if(hasBranch(Passengers,currentBranch) || hasBranch(Hailed,currentBranch))
        {
            dropOffAndPickup();
        }
        else
        {
            moveOn();
        }
    }
    
    /*
     * Checks if a Passenger list has a certain branch in it (to determine
     * whether to dropOff or just move on).
    */
    
    private boolean hasBranch(List<Passenger> p, int branchNumber)
    {
        for(int i = 0; i < p.size(); i++)
        {
            if(p.get(i).getBranch()==branchNumber)
            {
                return true;
            }
        }
        return false;
    }
    
    /*
     * This method increments/decrements if status is inbound/outbound, when it
     * 'gets' to the branch it releases that branch's semaphore and tries to
     * acquire that semaphore again, it will acquire that semaphore when all
     * passengers getting off are disembarked and all passengers getting on
     * are embarked.
    */
    
    private void dropOffAndPickup()
    {
        if(status == Status.OUTBOUND)
        {
            try 
            {
                if(firstStop == true)
                {
                    firstStop = false;
                }
                else
                {
                    timer.incrementTime(2);
                    this.sleep(17*2);
                }
            } 
            catch (InterruptedException ex)
            {
                ex.printStackTrace();
            }
            System.out.println(timer.getTime()+" branch "+currentBranch+": taxi arrive");
            
            stops[currentBranch].release();
            
            try
            {
                this.sleep(17);
                timer.incrementTime(1);
            }
            catch (InterruptedException ex)
            {
                ex.printStackTrace();
            }
            try
            {
                stops[currentBranch].acquire();
                String time = timer.getTime();
                System.out.println(time+" branch "+currentBranch+": taxi depart.");
                
                //Clear list of band people after getting out of stop
                clearBannedList();
            }
            catch (InterruptedException ex)
            {
                ex.printStackTrace();
            }
            
            currentBranch++;
        }
        else
        {
            try 
            {
                timer.incrementTime(2);
                this.sleep(17*2);
            } 
            catch (InterruptedException ex)
            {
                ex.printStackTrace();
            }
            
            System.out.println(timer.getTime()+" branch "+currentBranch+": taxi arrive");
            
            stops[currentBranch].release();
            
            try
            {
                this.sleep(17);
                timer.incrementTime(1);
            }
            catch (InterruptedException ex)
            {
                ex.printStackTrace();
            }
            try
            {
                stops[currentBranch].acquire();
                
                System.out.println(timer.getTime()+" branch "+currentBranch+": taxi depart.");
                
                //Clear list of band people after getting out of stop
                clearBannedList();
            }
            catch (InterruptedException ex)
            {
                ex.printStackTrace();
            }
            
            currentBranch--;
        }
    }
    
    private void clearBannedList()
    {
        for(int i = 0; i < Banned.size();i++)
        {
            Banned.get(i).quitStalling();
            Banned.remove(i);
        }
    }
    
    /*
    * Method: If there are no threads looking to get on or off at the currentBranch
    * then the taxi just advances to the next stop (increment/decrement).
    */
    
    private void moveOn()
    {
        if(status == Status.INBOUND)
        {
            //System.out.println("Going past branch: "+currentBranch);
            
            try 
            {
                this.sleep(17*2);
            } 
            catch (InterruptedException ex)
            {
                ex.printStackTrace();
            }
            
            currentBranch--;
        }
        else if(status == Status.OUTBOUND)
        {
            //System.out.println("Going past branch: "+currentBranch);
            
            try 
            {
                this.sleep(17*2);
            } 
            catch (InterruptedException ex)
            {
                ex.printStackTrace();
            }
            
            currentBranch++;
        }
    }
}
