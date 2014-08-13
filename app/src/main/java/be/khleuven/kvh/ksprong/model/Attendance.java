package be.khleuven.kvh.ksprong.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by KEVIN on 8/08/2014.
 */
public class Attendance {


    private long id;
    private String date;
    private long hour;
    private User user;


    public Attendance(String date, long hour, User user) {
        setDate(date);
        setHour(hour);
        setUser(user);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getHour() {
        return hour;
    }

    public void setHour(long hour) {
        this.hour = hour;
    }

    public User getUser(){
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String toString(){
        Calendar cal = Calendar.getInstance();
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
       try{
           date = sdf.parse(this.getDate());
       }catch(ParseException e){
           e.getStackTrace();
       }
        System.out.print(cal.getTime());


        return "yow";
    }
}
