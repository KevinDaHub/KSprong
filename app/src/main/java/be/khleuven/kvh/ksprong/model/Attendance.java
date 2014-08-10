package be.khleuven.kvh.ksprong.model;

/**
 * Created by KEVIN on 8/08/2014.
 */
public class Attendance {


    private long id;
    private String date;
    private float hour;
    private User user;


    public Attendance(String date, float hour, User user) {
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

    public float getHour() {
        return hour;
    }

    public void setHour(float hour) {
        this.hour = hour;
    }

    public User getUser(){
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
