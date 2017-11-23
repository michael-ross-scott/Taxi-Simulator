/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package taxisimulation;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Setup
 */
public class TaxiSimulation {
    
    public static void startSimulation(String filename) throws FileNotFoundException
    {
        Scanner sc = new Scanner(new File(filename));

        int numberPeople = sc.nextInt();
        int branches = sc.nextInt();
        
        sc.nextLine();
        
        // Initialises and starts taxi, then causes it to wait until all 
        // the person threads are created
        Taxi taxi = new Taxi(branches);
        
        // allocate memory to threads
        Person [] threads = new Person[numberPeople];
        int counter = 0;
        
        while(sc.hasNextLine())
        {
            String personInstruction = sc.nextLine();
            // Populate the person threads with info
            Person thread = new Person(personInstruction,taxi);
            threads[counter] = thread;
            counter++;
        }
        
        for(int i = 0; i < numberPeople; i++)
        {
            // Start the person threads
            threads[i].start();
        }
        
        taxi.start();
        
        sc.close();
    }
    
  
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try 
        {
            Scanner sc = new Scanner(System.in);
            
            String filename = sc.next();
            
            startSimulation(filename);
        } 
        catch (FileNotFoundException ex) 
        {
            ex.printStackTrace();
        }
    }
    
}
