import javafx.beans.property.SimpleStringProperty;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GameTimer extends Thread {
    private Thread thread = null;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
    private int time;
    private String[] display;
    private SimpleStringProperty mins, secs, totaltime;
    GameTimer(){
        mins = new SimpleStringProperty("00");
        secs = new SimpleStringProperty("00");
        totaltime = new SimpleStringProperty("00:00");
    }
    public void adjustTimes(int time){
        this.time = time;
        display = dateFormat.format(new Date(time)).split(":");
        mins.set(display[0]);
        secs.set(display[1]);
        totaltime.set(mins.get() + ":" + secs.get());
    }
    public void startTime(int time){
        this.time = time;
        thread = new Thread(this);
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
    }
    public void stopTime(int time){
        if (thread != null)
        {
            thread.interrupt();
        }
        this.time = time;
        adjustTimes(time);
    }
    public SimpleStringProperty getTotalTime(){
        return totaltime;
    }


    @Override
    public void run() {
        try {
            while (!thread.isInterrupted()) {
                adjustTimes(time);
                sleep(10);
                time = time + 10;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
