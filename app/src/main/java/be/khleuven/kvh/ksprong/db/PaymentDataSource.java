package be.khleuven.kvh.ksprong.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;

import be.khleuven.kvh.ksprong.model.Payment;
import be.khleuven.kvh.ksprong.model.User;

/**
 * Created by KEVIN on 10/08/2014.
 */
public class PaymentDataSource {


    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;
    private UserDataSource userDB;
    private String[] allColumns = {SQLiteHelper.COLUMN_ID,SQLiteHelper.COLUMN_MONTH,SQLiteHelper.COLUMN_TOTAL,SQLiteHelper.COLUMN_ISPAID,SQLiteHelper.COLUMN_USER};

    public PaymentDataSource(Context context){
        userDB = new UserDataSource(context);
        dbHelper = new SQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close(){
        dbHelper.close();
    }

    public long createPayment(Payment payment){
        ContentValues values = new ContentValues();


        values.put(SQLiteHelper.COLUMN_MONTH, payment.getMonth());
        values.put(SQLiteHelper.COLUMN_TOTAL, payment.getTotal());
        values.put(SQLiteHelper.COLUMN_ISPAID, payment.isPaid());
        values.put(SQLiteHelper.COLUMN_USER, payment.getUser().getUd());
        long todo = database.insert(SQLiteHelper.TABLE_PAYMENT,null,values);

        return todo;
    }

    public int updatePayment(Payment payment){
        ContentValues values = new ContentValues();



        values.put(SQLiteHelper.COLUMN_MONTH,payment.getMonth());
        values.put(SQLiteHelper.COLUMN_TOTAL,payment.getTotal());
        values.put(SQLiteHelper.COLUMN_ISPAID,payment.isPaid());
        values.put(SQLiteHelper.COLUMN_USER,payment.getUser().getUd());

        return database.update(SQLiteHelper.TABLE_PAYMENT,values,SQLiteHelper.COLUMN_IDE + " =?",new String[]{String.valueOf(payment.getId()) });
    }
    public Payment getPayment(long id){
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selectQuery = "SELECT * FROM "+SQLiteHelper.TABLE_PAYMENT+" WHERE "+
                SQLiteHelper.COLUMN_IDE+" = "+id;
        Cursor c = db.rawQuery(selectQuery,null);
        if(c!=null)
            c.moveToFirst();


        Payment payment  = new Payment(userDB.getUser(c.getInt(c.getColumnIndex(SQLiteHelper.COLUMN_USER))),c.getString(c.getColumnIndex(SQLiteHelper.COLUMN_MONTH)),c.getInt(c.getColumnIndex(SQLiteHelper.COLUMN_ISPAID)),c.getFloat(c.getColumnIndex(SQLiteHelper.COLUMN_TOTAL)));

        return payment;
    }

    public ArrayList<Payment> getAllPaymentsByChild(User user){
        ArrayList<Payment> payments= new ArrayList<Payment>();
        String selectQuery = "SELECT * FROM " + SQLiteHelper.TABLE_PAYMENT+" WHERE "+SQLiteHelper.COLUMN_USER+"="+user.getUd();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery,null);

        if(c.moveToFirst()){
            do {
                Payment payment  = new Payment(userDB.getUser(c.getInt(c.getColumnIndex(SQLiteHelper.COLUMN_USER))),c.getString(c.getColumnIndex(SQLiteHelper.COLUMN_MONTH)),c.getInt(c.getColumnIndex(SQLiteHelper.COLUMN_ISPAID)),c.getFloat(c.getColumnIndex(SQLiteHelper.COLUMN_TOTAL)));
                payments.add(payment);

            }
            while (c.moveToNext());
        }

        return payments;
    }


    public ArrayList<Payment> getAllPayments(){
        ArrayList<Payment> payments = new ArrayList<Payment>();
        String selectQuery = "SELECT * FROM " + SQLiteHelper.TABLE_PAYMENT;

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery,null);


        if(c.moveToFirst()){
            do {
                Payment payment  = new Payment(userDB.getUser(c.getInt(c.getColumnIndex(SQLiteHelper.COLUMN_USER))),c.getString(c.getColumnIndex(SQLiteHelper.COLUMN_MONTH)),c.getInt(c.getColumnIndex(SQLiteHelper.COLUMN_ISPAID)),c.getFloat(c.getColumnIndex(SQLiteHelper.COLUMN_TOTAL)));
                payments.add(payment);

            }
            while (c.moveToNext());
        }

        return payments;
    }
}
