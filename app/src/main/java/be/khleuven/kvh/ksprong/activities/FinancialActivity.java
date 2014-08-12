package be.khleuven.kvh.ksprong.activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;

import be.khleuven.kvh.kikkersprong.Base.BaseActivity;
import be.khleuven.kvh.kikkersprong.R;
import be.khleuven.kvh.kikkersprong.adapters.UserAdapter;
import be.khleuven.kvh.kikkersprong.db.UserDataSource;
import be.khleuven.kvh.kikkersprong.model.User;

/**
 * Created by KEVIN on 8/08/2014.
 */
public class FinancialActivity extends BaseActivity {

    final static private String APP_KEY = "79eavqlyqmrjlu9";
    final static private String APP_SECRET = "p5gcu2prsw59on2";
    final static private Session.AccessType ACCESS_TYPE = Session.AccessType.DROPBOX;
    private DropboxAPI<AndroidAuthSession> mDBApi;
    private static final String TAG = FinancialActivity.class.getName();

    private UserDataSource userDb;

    private ListView listview;
    private ArrayList<User> users=null;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_financial);


        ConnectivityManager conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        //mobile
        NetworkInfo.State mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();

        //wifi
        NetworkInfo.State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();

        if (mobile == NetworkInfo.State.CONNECTED || mobile == NetworkInfo.State.CONNECTING) {
        initialiseDropbox();
            exportInvoices();
            Toast.makeText(FinancialActivity.this, "Mobile is Enabled :) ....", Toast.LENGTH_LONG).show();
        } else if (wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING) {
            initialiseDropbox();
            exportInvoices();

            Toast.makeText(FinancialActivity.this, "Wifi is Enabled  :) ....", Toast.LENGTH_LONG).show();
        } else {
            initialiseDB();
            initialiseList();
            Toast.makeText(FinancialActivity.this, "No Wifi or Gprs Enabled :( ....", Toast.LENGTH_LONG).show();

        }
    }

    private void initialiseDB(){

        userDb = new UserDataSource(this);


    }
    private void initialiseList(){
        try {

            userDb.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        users = userDb.getAllUsers();

        User[] usersers = users.toArray(new User[users.size()]);




        UserAdapter adapter = new UserAdapter(this,R.layout.list_view_user,usersers);

        ListView listViewItems = new ListView(this);
        listViewItems.setAdapter(adapter);
        this.setContentView(listViewItems);
        userDb.close();

    }


    private void exportInvoices(){

        File file = new File("app/working-draft.txt");
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        DropboxAPI.Entry response = null;
        try {
            response = mDBApi.putFile("/magnum-opus.txt", inputStream,
                    file.length(), null, null);
        } catch (DropboxException e) {
            e.printStackTrace();
        }
        Log.i("DbExampleLog", "The uploaded file's rev is: " + response.rev);
    }
private void initialiseDropbox(){
    AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
    AndroidAuthSession session = new AndroidAuthSession(appKeys, ACCESS_TYPE);
    mDBApi = new DropboxAPI<AndroidAuthSession>(session);
    mDBApi.getSession().startOAuth2Authentication(FinancialActivity.this);
}

   /* protected void onResume() {
        super.onResume();

        if (mDBApi.getSession().authenticationSuccessful()) {
            try {
                // Required to complete auth, sets the access token on the session
                mDBApi.getSession().finishAuthentication();

                String accessToken = mDBApi.getSession().getOAuth2AccessToken();
            } catch (IllegalStateException e) {
                Log.i("DbAuthLog", "Error authenticating", e);
            }
        }
    }*/
}
