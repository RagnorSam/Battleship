import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GameTimer extends Thread {
    private Thread thread = null; // thread that manages counting
    private SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss"); // timer format
    private int time; // time used for simple addition/math operations(counting up)
    private String[] display; // string array for manipulating the display
    private SimpleStringProperty mins, secs, totaltime; //string properties for minutes seconds and the total
    GameTimer(){ // start all timers at 0 when initialized
        mins = new SimpleStringProperty("00");
        secs = new SimpleStringProperty("00");
        totaltime = new SimpleStringProperty("00:00");
    }
    //sets time to the specified time
    public void adjustTimes(int time){
        this.time = time; // change time
        display = dateFormat.format(new Date(time)).split(":"); // update format
        mins.set(display[0]); // set minutes
        secs.set(display[1]); // set seconds
        totaltime.set(mins.get() + ":" + secs.get()); // set total time
    }
    //starts the timer at the specified time
    public void startTime(int time){
        this.time = time;
        thread = new Thread(this); // creates thread to begin counting
        thread.setPriority(Thread.MIN_PRIORITY); // sets priority to lowest
        thread.start(); // starts thread, the counting happens in the overrided run function
    }
    //stop timer at a specified time
    public void stopTime(int time){
        if (thread != null) // check if a thread is running
        {
            thread.interrupt(); // interrupt the thread
        }
        this.time = time; // set time to specified
        adjustTimes(time); // update the time
    }
    //returns the total time
    public SimpleStringProperty getTotalTime(){
        return totaltime;
    }

    // Runs on the thread, updates the time accordingly
    @Override
    public void run() {
        try {
            while (!thread.isInterrupted()) {
                adjustTimes(time);
                Thread.sleep(10); // 10 milliseconds = 0.01 second
                time += 10;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
