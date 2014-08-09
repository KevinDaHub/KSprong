package be.khleuven.kvh.ksprong.model;


public class Attendance {

    private String date;
    private float hour;


    public Attendance(String date, float hour) {
      setDate(date);
      setHour(hour);
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
}
