package be.khleuven.kvh.ksprong.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import be.khleuven.kvh.ksprong.model.User;

/**
 * Created by KEVIN on 8/08/2014.
 */
public class UserDataSource {

    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;
    private String[] allColumns = {SQLiteHelper.COLUMN_ID,SQLiteHelper.COLUMN_UD,SQLiteHelper.COLUMN_NAME,SQLiteHelper.COLUMN_SURNAME,SQLiteHelper.COLUMN_ISCHECKED,SQLiteHelper.COLUMN_CHECKINDATE,SQLiteHelper.COLUMN_CHECKOUTDATE};

    public UserDataSource(Context context){
        dbHelper = new SQLiteHelper(context);
    }

    public void open() throws SQLException{
        database = dbHelper.getWritableDatabase();
    }

    public void close(){
        dbHelper.close();
    }

    public long createUser(User user){
        ContentValues values = new ContentValues();
        user.setCheckedIn(1);
        Date date = new Date();
        user.setCheckInDate(date.toString());

        values.put(SQLiteHelper.COLUMN_NAME,user.getName());
        values.put(SQLiteHelper.COLUMN_SURNAME,user.getSurname());
        values.put(SQLiteHelper.COLUMN_UD,user.getUd());
        values.put(SQLiteHelper.COLUMN_ISCHECKED,user.isCheckedIn());
        values.put(SQLiteHelper.COLUMN_CHECKINDATE,user.getCheckInDate());
        values.put(SQLiteHelper.COLUMN_CHECKOUTDATE,user.getCheckOutDate());
        long todo = database.insert(SQLiteHelper.TABLE_USER,null,values);

        return todo;
    }

    public int updateUser(User user){
        ContentValues values = new ContentValues();


        values.put(SQLiteHelper.COLUMN_NAME,user.getName());
        values.put(SQLiteHelper.COLUMN_SURNAME,user.getSurname());
        values.put(SQLiteHelper.COLUMN_UD,user.getUd());
        values.put(SQLiteHelper.COLUMN_ISCHECKED,user.isCheckedIn());
        values.put(SQLiteHelper.COLUMN_CHECKINDATE,user.getCheckInDate());
        values.put(SQLiteHelper.COLUMN_CHECKOUTDATE,user.getCheckOutDate());

        return database.update(SQLiteHelper.TABLE_USER,values,SQLiteHelper.COLUMN_ID + " =?",new String[]{String.valueOf(user.getId()) });
    }
    public User getUser(long id){
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selectQuery = "SELECT * FROM "+SQLiteHelper.TABLE_USER+" WHERE "+
                SQLiteHelper.COLUMN_UD+" = "+id;
        Cursor c = db.rawQuery(selectQuery,null);
        if(c!=null)
            c.moveToFirst();

        User user = new User(c.getInt(c.getColumnIndex(SQLiteHelper.COLUMN_UD)),c.getString(c.getColumnIndex(SQLiteHelper.COLUMN_NAME)),c.getString(c.getColumnIndex(SQLiteHelper.COLUMN_SURNAME)));
        user.setCheckedIn(c.getInt(c.getColumnIndex(SQLiteHelper.COLUMN_ISCHECKED)));
        user.setId(c.getLong(c.getColumnIndex(SQLiteHelper.COLUMN_ID)));
        user.setCheckInDate(c.getString(c.getColumnIndex(SQLiteHelper.COLUMN_CHECKINDATE)));
        user.setCheckOutDate(c.getString(c.getColumnIndex(SQLiteHelper.COLUMN_CHECKOUTDATE)));
        return user;
    }

    public boolean doesExist(User user){

        boolean res=false;
        for(User u: getAllUsers()){
            if(u.getUd()==user.getUd()){
                res = true;
            }

        }
        return res;
    }

    public ArrayList<User> getAllUsers(){
        ArrayList<User> users = new ArrayList<User>();
        String selectQuery = "SELECT * FROM " + SQLiteHelper.TABLE_USER;

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery,null);


        if(c.moveToFirst()){
            do {
                User user = new User(c.getInt(c.getColumnIndex(SQLiteHelper.COLUMN_UD)),c.getString(c.getColumnIndex(SQLiteHelper.COLUMN_NAME)),c.getString(c.getColumnIndex(SQLiteHelper.COLUMN_SURNAME)));
                user.setCheckedIn(c.getInt(c.getColumnIndex(SQLiteHelper.COLUMN_ISCHECKED)));
                user.setId(c.getLong(c.getColumnIndex(SQLiteHelper.COLUMN_ID)));
                user.setCheckInDate(c.getString(c.getColumnIndex(SQLiteHelper.COLUMN_CHECKINDATE)));
                user.setCheckOutDate(c.getString(c.getColumnIndex(SQLiteHelper.COLUMN_CHECKOUTDATE)));
                users.add(user);

            }
            while (c.moveToNext());
            }

        return users;
        }
    }

