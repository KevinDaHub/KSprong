package be.khleuven.kvh.ksprong.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by KEVIN on 8/08/2014.
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHelper.class.getName();

    private static final String DATABASE_NAME = "Kikkersprongdb";
    private static final int DATABASE_WERSION = 2;

    // user table
    public static final String TABLE_USER = "User";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_UD = "ud";
    public static final String COLUMN_SURNAME = "surname";
    public static final String COLUMN_ISCHECKED = "ischecked";
    public static final String COLUMN_CHECKINDATE = "checkInDate";
    public static final String COLUMN_CHECKOUTDATE = "checkOutDate";

    // attendance table
    public static final String TABLE_ATTENDANCE = "Attendance";
    public static final String COLUMN_IDE = "_id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_HOUR = "hour";

    // payment table
    public static final String TABLE_PAYMENT = "Payment";
    public static final String COLUMN_ISPAID = "ispaid";
    public static final String COLUMN_MONTH = "month";
    public static final String COLUMN_TOTAL ="total";
    public static final String COLUMN_USER = "user";


    private static final String CREATE_TABLE_USER=
            "create table "+TABLE_USER+" ( "+COLUMN_ID+" integer primary key autoincrement, "+COLUMN_UD+" integer not null," +COLUMN_NAME+" text not null," +
                    COLUMN_SURNAME+" text not null,"+COLUMN_ISCHECKED+" boolean not null,"+COLUMN_CHECKINDATE+" Date,"
                    +COLUMN_CHECKOUTDATE+" Date );";

    private static final String CREATE_TABLE_ATTENDANCE=
            "create table "+TABLE_ATTENDANCE+" ( "+COLUMN_IDE+" integer primary key autoincrement, "+COLUMN_DATE+" Date not null," +COLUMN_HOUR+" int not null,"+COLUMN_USER+"integer not null,FOREIGN KEY("+COLUMN_USER+") REFERENCES "+TABLE_USER+"("+COLUMN_ID+"));";

    private static final String CREATE_TABLE_PAYMENT=
            "create table "+TABLE_PAYMENT+" ( "+COLUMN_IDE+" integer primary key autoincrement, "+COLUMN_MONTH+" char(10) not null," +COLUMN_TOTAL+" float," +
                    COLUMN_ISPAID+" boolean not null,"+COLUMN_USER+"integer not null,FOREIGN KEY("+COLUMN_USER+") REFERENCES "+TABLE_USER+"("+COLUMN_ID+"));";


    public SQLiteHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_WERSION);
    }




    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d(TAG,"onCreate");
        sqLiteDatabase.execSQL(CREATE_TABLE_USER);
        sqLiteDatabase.execSQL(CREATE_TABLE_ATTENDANCE);
        sqLiteDatabase.execSQL(CREATE_TABLE_PAYMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        Log.w(SQLiteHelper.class.getName(),
                "Upgrading database from version " + i + " to "
                        + i2 + ", which will destroy all old data"
        );
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_USER);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_ATTENDANCE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_PAYMENT);
                onCreate(sqLiteDatabase);
    }
}
