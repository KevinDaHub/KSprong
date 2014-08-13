package be.khleuven.kvh.ksprong.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;

import be.khleuven.kvh.ksprong.model.Attendance;
import be.khleuven.kvh.ksprong.model.User;

/**
 * Created by KEVIN on 10/08/2014.
 */
public class AttendanceDataSource {



    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;
    private UserDataSource userDB;
    private String[] allColumns = {SQLiteHelper.COLUMN_ID,SQLiteHelper.COLUMN_DATE,SQLiteHelper.COLUMN_HOUR,SQLiteHelper.COLUMN_USER};

    public AttendanceDataSource(Context context){
        userDB = new UserDataSource(context);
        dbHelper = new SQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close(){
        dbHelper.close();
    }

    public long createAttendance(Attendance attendance){
        ContentValues values = new ContentValues();


        values.put(SQLiteHelper.COLUMN_DATE,attendance.getDate());
        values.put(SQLiteHelper.COLUMN_HOUR,attendance.getHour());
        values.put(SQLiteHelper.COLUMN_USER,attendance.getUser().getUd());
        long todo = database.insert(SQLiteHelper.TABLE_ATTENDANCE,null,values);

        return todo;
    }

    public int updateAttendance(Attendance attendance){
        ContentValues values = new ContentValues();


        values.put(SQLiteHelper.COLUMN_DATE,attendance.getDate());
        values.put(SQLiteHelper.COLUMN_HOUR,attendance.getHour());
        values.put(SQLiteHelper.COLUMN_USER,attendance.getUser().getUd());

        return database.update(SQLiteHelper.TABLE_ATTENDANCE,values,SQLiteHelper.COLUMN_IDE + " =?",new String[]{String.valueOf(attendance.getId()) });
    }
    public Attendance getAttendance(long id){
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selectQuery = "SELECT * FROM "+SQLiteHelper.TABLE_ATTENDANCE+" WHERE "+
                SQLiteHelper.COLUMN_IDE+" = "+id;
        Cursor c = db.rawQuery(selectQuery,null);
        if(c!=null)
            c.moveToFirst();


        Attendance attendance  = new Attendance(c.getString(c.getColumnIndex(SQLiteHelper.COLUMN_DATE)),c.getLong(c.getColumnIndex(SQLiteHelper.COLUMN_HOUR)),userDB.getUser(c.getInt(c.getColumnIndex(SQLiteHelper.COLUMN_USER))));

        return attendance;
    }

    public ArrayList<Attendance> getAllAttendancesByChild(User user){
        ArrayList<Attendance> attendances = new ArrayList<Attendance>();
        String selectQuery = "SELECT * FROM " + SQLiteHelper.TABLE_ATTENDANCE+" WHERE "+SQLiteHelper.COLUMN_USER+"="+user.getUd()+" ORDER BY " +SQLiteHelper.COLUMN_ID+" DESC";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery,null);

        if(c.moveToFirst()){
            do {
                Attendance attendance = new Attendance(c.getString(c.getColumnIndex(SQLiteHelper.COLUMN_DATE)),c.getLong(c.getColumnIndex(SQLiteHelper.COLUMN_HOUR)),userDB.getUser(c.getInt(c.getColumnIndex(SQLiteHelper.COLUMN_USER))));
                attendances.add(attendance);

            }
            while (c.moveToNext());
        }

        return attendances;
    }


    public ArrayList<Attendance> getAllAttendances(){
        ArrayList<Attendance> attendances = new ArrayList<Attendance>();
        String selectQuery = "SELECT * FROM " + SQLiteHelper.TABLE_ATTENDANCE + " ORDER BY "+SQLiteHelper.COLUMN_ID+" DESC";

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery,null);


        if(c.moveToFirst()){
            do {
                Attendance attendance = new Attendance(c.getString(c.getColumnIndex(SQLiteHelper.COLUMN_DATE)),c.getLong(c.getColumnIndex(SQLiteHelper.COLUMN_HOUR)),userDB.getUser(c.getInt(c.getColumnIndex(SQLiteHelper.COLUMN_USER))));
                attendances.add(attendance);

            }
            while (c.moveToNext());
        }

        return attendances;
    }
}





