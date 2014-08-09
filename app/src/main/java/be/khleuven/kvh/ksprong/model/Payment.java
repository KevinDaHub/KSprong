package be.khleuven.kvh.ksprong.model;


public class Payment {


    private User user;
    private String month;
    private boolean paid;
    private float total;


    public Payment(User user, String month, boolean paid, float total) {
        setUser(user);
        setMonth(month);
        setPaid(paid);
        setTotal(total);
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
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

