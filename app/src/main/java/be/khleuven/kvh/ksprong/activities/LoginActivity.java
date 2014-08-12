package be.khleuven.kvh.ksprong.activities;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import be.khleuven.kvh.kikkersprong.Base.BaseActivity;
import be.khleuven.kvh.kikkersprong.Constants.KikkersprongConstants;
import be.khleuven.kvh.kikkersprong.R;
import be.khleuven.kvh.kikkersprong.db.AttendanceDataSource;
import be.khleuven.kvh.kikkersprong.db.UserDataSource;
import be.khleuven.kvh.kikkersprong.model.Attendance;
import be.khleuven.kvh.kikkersprong.model.User;
import butterknife.ButterKnife;


/**
 * Created by KEVIN on 8/08/2014.
 */
public class LoginActivity extends BaseActivity {


    private static final String TAG = LoginActivity.class.getName();
    private User user;
    private UserDataSource userDb;
    private AttendanceDataSource attendanceDb;
    private int mId;
    private String mFirstName;
    private String mName;
    private String contents;
    private boolean firstTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        initializeDB();

        startQRScanner();
    }

    private void initializeDB() {

        //SQLiteDatabase database = sqlHelper.getWritableDatabase();
        userDb = new UserDataSource(this);
        attendanceDb = new AttendanceDataSource(this);


        //Toast.makeText(this, user.getCheckInDate(),
        //Toast.LENGTH_LONG).show();


    }

    private void checkOut(User user) {

        try {
            userDb.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

                user.setCheckedIn(0);
                Date date = new Date();
                Date date2 = null;

                user.setCheckOutDate(date.toString());
                userDb.updateUser(user);

                String string = user.getCheckInDate();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        try{
            date2 = sdf.parse(user.getCheckInDate());
        }catch(ParseException e){
            e.getStackTrace();
        }
                long starthours = date2.getTime();
                long endhours = date.getTime();

                long finalhours = endhours - starthours;

                Attendance attendance = new Attendance(date.toString(), finalhours, user);
                try {
                    attendanceDb.open();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                attendanceDb.createAttendance(attendance);
        userDb.close();
        attendanceDb.close();
            }





    private void checkIn(User user) {

        User person = null;
        try {
            userDb.open();


        } catch (SQLException e) {
            e.printStackTrace();
        }

                user.setCheckedIn(1);
                Date date = new Date();
                user.setCheckInDate(date.toString());
                userDb.updateUser(user);




        userDb.close();

    }

    private void initializeWelcome() {
        String[] result = splitStringWithDelimiter(contents, "/");
        mId = Integer.parseInt(result[KikkersprongConstants.Info.ID.ordinal()]);
        mFirstName = result[KikkersprongConstants.Info.FIRSTNAME.ordinal()];
        mName = result[KikkersprongConstants.Info.NAME.ordinal()];

        Log.d(TAG, "Splitted: " + mId + " " + mFirstName + " " + mName);
        // mWelcomeTxt.setText(String.format(getResources().getString(R.string.welcomeText), mFirstName, mName));
    }


    private String[] splitStringWithDelimiter(String content, String delimiter) {
        String[] result;
        if (content.contains("/")) {
            result = content.split(delimiter);
        } else {
            throw new IllegalArgumentException("String " + content + " does not contain " + delimiter);
        }
        return result;
    }

    public void startQRScanner() {
        (new IntentIntegrator(this)).initiateScan();
    }

    private boolean isCheckedIn(User user) {

        try {
            userDb.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        boolean res = false;
        User person = userDb.getUser(user.getUd());

        if (person.isCheckedIn() == 1) {
            res = true;
        } else if (person.isCheckedIn() == 0) {
            res = false;
        }

        userDb.close();
        return res;


    }
    private void createUser(User user) {
        User person = null;
        try {
            userDb.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
            userDb.createUser(user);
            if (user.getUd() > 0) {

                Date date = new Date();
                user.setCheckInDate(date.toString());
                userDb.updateUser(user);
            }
        userDb.close();
    }



    public void onActivityResult(int request, int result, Intent i) {
        IntentResult scan = IntentIntegrator.parseActivityResult(request, result, i);

        if (scan != null) {
            contents = scan.getContents();
            initializeWelcome();

            User user = new User(mId, mFirstName, mName);
            //does the user exist
            boolean exist = userDb.doesExist(user);
            Log.d(TAG, "User exists: " + exist);
            Intent welcomeIntent = new Intent(this, WelcomeActivity.class);
            if (!exist) {
                //if user does not exist create the user.
                createUser(user);
                welcomeIntent.putExtra(KikkersprongConstants.INTENTCHECKEDIN, false);
                welcomeIntent.putExtra(KikkersprongConstants.INTENTUSERNAME, mFirstName);
                welcomeIntent.putExtra(KikkersprongConstants.INTENTLASTNAME, mName);
                welcomeIntent.putExtra(KikkersprongConstants.INTENTID, mId);

                startActivity(welcomeIntent);

            } else {
                User person = userDb.getUser(mId);

                if (mId > 0) {

                    if (person.isCheckedIn() == 0) {
                        if (exist) {
                            //if user exists and needs to check in
                            checkIn(person);

                            welcomeIntent.putExtra(KikkersprongConstants.INTENTCHECKEDIN, false);
                            welcomeIntent.putExtra(KikkersprongConstants.INTENTUSERNAME, mFirstName);
                            welcomeIntent.putExtra(KikkersprongConstants.INTENTLASTNAME, mName);
                            welcomeIntent.putExtra(KikkersprongConstants.INTENTID, mId);

                            startActivity(welcomeIntent);
                        }
                    } else {
                        checkOut(person);
                        welcomeIntent.putExtra(KikkersprongConstants.INTENTCHECKEDIN, true);
                        welcomeIntent.putExtra(KikkersprongConstants.INTENTUSERNAME, mFirstName);
                        welcomeIntent.putExtra(KikkersprongConstants.INTENTLASTNAME, mName);
                        welcomeIntent.putExtra(KikkersprongConstants.INTENTID, mId);

                        startActivity(welcomeIntent);
                    }


                } else if (mId == -1) {
                    Intent financialIntent = new Intent(this, FinancialActivity.class);
                    financialIntent.putExtra(KikkersprongConstants.INTENTUSERNAME, mFirstName);
                    financialIntent.putExtra(KikkersprongConstants.INTENTLASTNAME, mName);
                    financialIntent.putExtra(KikkersprongConstants.INTENTID, mId);
                    startActivity(financialIntent);

                }

            }
        }
        }
    }




