package be.khleuven.kvh.ksprong.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.exception.DropboxUnlinkedException;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import be.khleuven.kvh.ksprong.Base.BaseActivity;
import be.khleuven.kvh.ksprong.R;
import be.khleuven.kvh.ksprong.db.PaymentDataSource;
import be.khleuven.kvh.ksprong.db.UserDataSource;
import be.khleuven.kvh.ksprong.model.Payment;
import be.khleuven.kvh.ksprong.model.User;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by KEVIN on 8/08/2014.
 */
public class FinancialActivity extends BaseActivity {


    private static final String TAG = FinancialActivity.class.getName();
    // offline databases
    private UserDataSource userDb;
    private PaymentDataSource paymentDb;

    private ListView listview;
    private ArrayList<User> users=null;
    private ArrayList<Payment> payments=null;
    private User user;
    NetworkInfo.State mobile;
    //wifi
    NetworkInfo.State wifi;
    ConnectivityManager conMan;

    //dropbox
    final static public String APP_KEY = "79eavqlyqmrjlu9";
    final static public String APP_SECRET = "p5gcu2prsw59on2";
    final static private Session.AccessType ACCESS_TYPE = Session.AccessType.APP_FOLDER;
    private DropboxAPI<AndroidAuthSession> mDBApi;
    private FileInputStream inputStream = null;
    private File file;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_financial);
        ButterKnife.inject(this);


        conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        //mobile
        mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        //wifi
        wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();

        if (mobile == NetworkInfo.State.CONNECTED || mobile == NetworkInfo.State.CONNECTING) {
            initialiseDB();
        } else if (wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING) {
            initialiseDB();

        } else {
            initialiseDB();
            setContentView(R.layout.activity_financial_offline);
            Toast.makeText(FinancialActivity.this, "Please turn on your internet!", Toast.LENGTH_LONG).show();
        }
    }

    private void initialiseDB(){
        userDb = new UserDataSource(this);
        paymentDb = new PaymentDataSource(this);
    }


    @OnClick(R.id.attendancefinbut)
    public void attendanceView(){


        Intent attendanceIntent = new Intent(this, FinAttendanceActivity.class);
        startActivity(attendanceIntent);
    }

    @OnClick(R.id.paymentfinbut)
    public void paymentView(){

        Intent paymentIntent = new Intent(this, FinPaymentActivity.class);
        startActivity(paymentIntent);

    }



    public void generateInvoices(String sFileName, ArrayList<Payment> payments){
        File root=null;
        //Create txt file in external storage
        try
        {
            root = new File(Environment.getExternalStorageDirectory(), "Notes");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            for(Payment payment:payments) {
                if (payment.isPaid() == 1) {

                    writer.append(payment.getMonth() + ": "+"Betaald"+"\n");
                }else {

                    writer.append(payment.getMonth() + ": "+"Niet Betaald - "+payment.getTotal()+"\n");
                }
            }
            writer.flush();
            writer.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();

        }
        file = new File(root,sFileName);

        try {
            inputStream = new FileInputStream(file);
            Log.i("DbExampleLog", "The uploaded file's rev is: ");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        //upload the txt files into dropbox.
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    //   DropboxAPI.Entry newEntry = mDBApi.putFileOverwrite("Kevin.txt", inputStream, file.length(), null);
                    DropboxAPI.Entry response = mDBApi.putFileOverwrite("/" + file.getName(), inputStream,
                            file.length(), null);
                } catch (DropboxUnlinkedException e) {
                    Log.e("DbExampleLog", "User has unlinked.");
                } catch (DropboxException e) {
                    e.getStackTrace();


                }
            }

        });
        thread.start();
    }


    public void initialiseInvoices(){


        try {
            userDb.open();
            paymentDb.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        users = userDb.getAllUsers();
        for(User user: users){
            payments = paymentDb.getAllPaymentsByChild(user);
            generateInvoices(user.getName()+user.getSurname()+".txt",payments);
            }

        userDb.close();
        paymentDb.close();



    }


    @OnClick(R.id.dropboxbut)
    public void initialiseDropbox(){

        AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(appKeys, ACCESS_TYPE);
        mDBApi = new DropboxAPI<AndroidAuthSession>(session);
        mDBApi.getSession().startOAuth2Authentication(FinancialActivity.this);
        onResume();



    }




    protected void onResume() {
        super.onResume();


        if (mobile == NetworkInfo.State.CONNECTED || mobile == NetworkInfo.State.CONNECTING) {
            if(mDBApi!=null){
                if (mDBApi.getSession().authenticationSuccessful()) {
                    try {
                        // Required to complete auth, sets the access token on the session
                        mDBApi.getSession().finishAuthentication();

                        String accessToken = mDBApi.getSession().getOAuth2AccessToken();
                        initialiseInvoices();
                        mDBApi=null;
                        Toast.makeText(FinancialActivity.this, "Uploaded to Dropbox", Toast.LENGTH_LONG).show();
                    } catch (IllegalStateException e) {
                        Log.i("DbAuthLog", "Error authenticating", e);
                    }
                }else{
                }
            }
    } else if (wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING) {
                if(mDBApi!=null){
                         if (mDBApi.getSession().authenticationSuccessful()) {
                              try {
                    // Required to complete auth, sets the access token on the session
                                     mDBApi.getSession().finishAuthentication();

                                    String accessToken = mDBApi.getSession().getOAuth2AccessToken();
                                    initialiseInvoices();
                                  mDBApi=null;
                                    Toast.makeText(FinancialActivity.this, "Uploaded to Dropbox", Toast.LENGTH_LONG).show();
                                } catch (IllegalStateException e) {
                                    Log.i("DbAuthLog", "Error authenticating", e);
                                }
                            }else{
                         }
                }


        } else {

        }


    }

}
