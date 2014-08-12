package be.khleuven.kvh.ksprong.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.sql.SQLException;
import java.util.ArrayList;

import be.khleuven.kvh.kikkersprong.Constants.KikkersprongConstants;
import be.khleuven.kvh.kikkersprong.R;
import be.khleuven.kvh.kikkersprong.adapters.AttendanceAdapter;
import be.khleuven.kvh.kikkersprong.db.AttendanceDataSource;
import be.khleuven.kvh.kikkersprong.db.UserDataSource;
import be.khleuven.kvh.kikkersprong.model.Attendance;
import be.khleuven.kvh.kikkersprong.model.User;
import butterknife.ButterKnife;

/**
 * Created by KEVIN on 8/08/2014.
 */
public class AttendanceActivity extends Activity {



    private static final String TAG = AttendanceActivity.class.getName();
    private AttendanceDataSource attendanceDb;
    private UserDataSource userDb;
    private int mId;
    private String mFirstName;
    private String mName;
    private ListView listview;
    private ArrayList<Attendance> attendances=null;
    private User user;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        ButterKnife.inject(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mId = extras.getInt(KikkersprongConstants.INTENTID);
            mFirstName = extras.getString(KikkersprongConstants.INTENTUSERNAME);
            mName = extras.getString(KikkersprongConstants.INTENTLASTNAME);
        }

        initializeWelcome();
        initialiseDB();
        initialiseList();
    }


    private void initialiseList(){
        try {
            attendanceDb.open();
            userDb.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        attendances = attendanceDb.getAllAttendancesByChild(user);

        Attendance[] attendancesers = attendances.toArray(new Attendance[attendances.size()]);




        AttendanceAdapter adapter = new AttendanceAdapter(this,R.layout.list_view,attendancesers);

        ListView listViewItems = new ListView(this);
        listViewItems.setAdapter(adapter);
        this.setContentView(listViewItems);
        attendanceDb.close();

    }
    private void initialiseDB(){
        attendanceDb = new AttendanceDataSource(this);
        userDb = new UserDataSource(this);
        user = userDb.getUser(mId);
        try {
            attendanceDb.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        attendanceDb.close();
    }


    private void initializeWelcome() {

        Log.d(TAG, "Splitted: " + mId + " " + mFirstName + " " + mName);
    }


}
