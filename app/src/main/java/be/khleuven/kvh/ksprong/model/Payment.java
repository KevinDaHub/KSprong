package be.khleuven.kvh.ksprong.model;

/**
 * Created by KEVIN on 8/08/2014.
 */
public class Payment {

    private long id;
    private User user;
    private String month;
    private int paid;
    private float total;


    public Payment(User user, String month, int paid, float total) {
        setUser(user);
        setMonth(month);
        setPaid(paid);
        setTotal(total);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int isPaid() {
        return paid;
    }

    public void setPaid(int paid) {
        this.paid = paid;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public User getUser(){
        return user;
    }
}

