package be.khleuven.kvh.ksprong.model;

import java.util.ArrayList;

/**
 * Created by KEVIN on 8/08/2014.
 */
public class User {

    private long id;
    private int ud;
    private String name;
    private String surname;
    private int checkedIn;
    private String checkInDate;
    private String checkOutDate;
    private ArrayList<Attendance> attendances;
    private ArrayList<Payment> payments;

    public User(int ud, String name, String surname){
        setUd(ud);
        setName(name);
        setSurname(surname);
        setCheckedIn(checkedIn);
        attendances = new ArrayList<Attendance>();
        payments = new ArrayList<Payment>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public ArrayList<Attendance> getAttendances(){
        return attendances;
    }
    public ArrayList<Payment> getPayments(){
        return payments;
    }

    public int getUd() {
        return ud;
    }

    public void setUd(int ud) {
        this.ud = ud;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int isCheckedIn() {
        return checkedIn;
    }

    public void setCheckedIn(int checkedIn) {
        this.checkedIn = checkedIn;
    }
}
