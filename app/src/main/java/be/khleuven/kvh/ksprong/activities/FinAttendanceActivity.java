package be.khleuven.kvh.ksprong.activities;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;

import java.sql.SQLException;
import java.util.ArrayList;

import be.khleuven.kvh.ksprong.R;
import be.khleuven.kvh.ksprong.adapters.FinAttendanceAdapter;
import be.khleuven.kvh.ksprong.db.AttendanceDataSource;
import be.khleuven.kvh.ksprong.db.UserDataSource;
import be.khleuven.kvh.ksprong.model.Attendance;
import be.khleuven.kvh.ksprong.model.User;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class FinAttendanceActivity extends Activity {




    private static final String TAG = AttendanceActivity.class.getName();
    private AttendanceDataSource attendanceDb;
    private UserDataSource userDb;
    FinAttendanceAdapter adapter;
    private ListView listview;
    private ArrayList<Attendance> attendances=null;
    private User user;
    @InjectView(R.id.listViewAtt)
    ListView listViewAtt;

    @InjectView(R.id.editTxtatt)
    EditText editText;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fin_attendance);
        ButterKnife.inject(this);

        initialiseDB();
        initialiseList();

        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println("Text ["+s+"]");
                adapter.getFilter().filter(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


    }




    private void initialiseList(){
        try {
            attendanceDb.open();
            userDb.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        attendances = attendanceDb.getAllAttendances();

        Attendance[] attendancesers = attendances.toArray(new Attendance[attendances.size()]);

        adapter = new FinAttendanceAdapter(this,R.layout.list_view_user,attendancesers);

        ListView listViewItems = new ListView(this);
        listViewAtt.setAdapter(adapter);
        attendanceDb.close();

    }
    private void initialiseDB(){
        attendanceDb = new AttendanceDataSource(this);
        userDb = new UserDataSource(this);

        try {
            attendanceDb.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        attendanceDb.close();
    }

}
