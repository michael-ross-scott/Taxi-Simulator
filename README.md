# Taxi-Simulator

Simulation of a simplified version of a taxi cab developed using java.

# Scenario

Each morning M people start off at a branch headquaters (hence known as branch 0). They need to get to different branches in the company in order to complete their work schedule. One taxi is scheduled for the root from branch 0 to branch m, where m is the last branch. The taxi has n capacity (where n is the number of passengers).

The taxi drives the root from 0 to m (OUTBOUND) and subsequently from m to 0, picking passengers up and dropping them off. 

# Rules

     (1) Passengers get onto the taxi no matter which direction it is going.
     (2) Having arrived at a branch, the taxi remains there if 
          (2.1) there are no passengers to be picked up, 
          (2.2) there are no passengers waiting to be dropped off at another branch, and 
          (2.3) it has not been hailed by passengers elsewhere.  
     (3) The taxi takes two minutes to move from one branch to the next.
     (4) If the driver needs to stop at a particular branch to pick up or discharge passengers, it takes
         one minute to do so. 
     (5) After a person disembarks, that person cannot board the taxi again until: the taxi leaves and 
         returns to that branch, unless: the taxi is idle (no Hail button was pushed and no one has told the driver where to take them next)     
         right after that person disembarks

# Input

The program requires you specify a filename to start the simulation.This consists of a file containing the number of people using this service, the number of branches on the route, and the work/travel patterns for each person.The filename will need to be inputted when running TaxiSimulation:

    % java TaxiSimulaton
    <filename>
  
The file format is as follows:

    <number of people>
    <newline>
    <number of branches>
    <newline>
    {<person number> (<branch, duration>) { , ( <branch, duration>)} <newline> }
      
An expression of the form ‘{<n>}’ indicates that the element <n> is repeated one or more times. A duration is expressed as a quantity of minutes.
  
Example:

    3
    5
    0
    (1, 10), (0, 5), (3, 40)
    1 (2, 15), (1, 23), (2, 18), (4, 5), (3, 50)
    2 (3, 100) 
