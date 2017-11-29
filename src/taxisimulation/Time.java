/**
 *
 * @author Michael Scott
 */
public class Time {
    private int minutes;
    private int hours;
    
    public Time()
    {
        this.minutes = 0;
        this.hours = 9;
    }
    
    public String getTime()
    {
        String s = "";
        if ( hours < 10 && minutes < 10)
            s = "0" + hours + ":0" + minutes;
        else if ( hours < 10 && minutes >= 10)
            s = "0" + hours + ":" + minutes;
        else if ( hours >= 10 && minutes < 10)
            s = hours + ":0" + minutes;
        else if (hours >= 10 && minutes >= 10)
        {
            s = hours +":"+ minutes;
        }
        else if ( hours == 0)
            s = "0" + hours + ":" + minutes;
        else if ( minutes == 0)
            s = hours + "0:" + minutes;

        return s;
    }
    
    public void incrementTime(int min)
    {
        minutes+=min;
        if(minutes >= 60)
        {
            minutes -= 60;
            hours++;
        }
    }
}
