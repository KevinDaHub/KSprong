package be.khleuven.kvh.ksprong.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import be.khleuven.kvh.kikkersprong.Base.BaseActivity;
import be.khleuven.kvh.kikkersprong.Constants.KikkersprongConstants;
import be.khleuven.kvh.kikkersprong.R;
import be.khleuven.kvh.kikkersprong.db.UserDataSource;
import be.khleuven.kvh.kikkersprong.model.Attendance;
import be.khleuven.kvh.kikkersprong.model.User;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by KEVIN on 10/08/2014.
 */
public class WelcomeActivity extends BaseActivity {

    private static final String TAG = WelcomeActivity.class.getName();

    @InjectView(R.id.welcomeTxtView)
    TextView mWelcomeTxt;

    private String contents;
    private User user;
    private int mId;
    private String mFirstName;
    private String mName;
    private UserDataSource userDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.inject(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mId = extras.getInt(KikkersprongConstants.INTENTID);
            mFirstName = extras.getString(KikkersprongConstants.INTENTUSERNAME);
            mName = extras.getString(KikkersprongConstants.INTENTLASTNAME);
        }
        initializeDB();
        initializeWelcome();

    }

    @OnClick(R.id.attendancebut)
    public void attendanceView(){
        Date date = new Date();
        Attendance attendance = new Attendance(date.toString(),15,new User(1,"ke","vh"));

        Intent attendanceIntent = new Intent(this, AttendanceActivity.class);
        attendanceIntent.putExtra(KikkersprongConstants.INTENTUSERNAME, mFirstName);
        attendanceIntent.putExtra(KikkersprongConstants.INTENTLASTNAME, mName);
        attendanceIntent.putExtra(KikkersprongConstants.INTENTID, mId);
        startActivity(attendanceIntent);
    }

    @OnClick(R.id.paymentbut)
    public void paymentView(){

        Intent paymentIntent = new Intent(this, PaymentActivity.class);
        paymentIntent.putExtra(KikkersprongConstants.INTENTUSERNAME, mFirstName);
        paymentIntent.putExtra(KikkersprongConstants.INTENTLASTNAME, mName);
        paymentIntent.putExtra(KikkersprongConstants.INTENTID, mId);
        startActivity(paymentIntent);

    }

    private void initializeDB() {
        userDb = new UserDataSource(this);
        try {
            userDb.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        user = userDb.getUser(mId);
        userDb.close();
    }


    private void initializeWelcome() {

        Log.d(TAG, "Splitted: " + mId + " " + mFirstName + " " + mName);
        Date now = new Date();

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE", Locale.US);
        String asWeek = dateFormat.format(now);

        if (user.isCheckedIn() == 0) {
            if (asWeek.equals("Fri")) {
                mWelcomeTxt.setText(String.format(getResources().getString(R.string.weekendText), mFirstName + " ", mName));
            } else {
                mWelcomeTxt.setText(String.format(getResources().getString(R.string.goodbyeText), mFirstName + " ", mName));
            }
        } else {
            mWelcomeTxt.setText(String.format(getResources().getString(R.string.welcomeText), mFirstName, mName));
        }
    }



}
