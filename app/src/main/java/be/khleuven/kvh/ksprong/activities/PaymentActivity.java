package be.khleuven.kvh.ksprong.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.sql.SQLException;
import java.util.ArrayList;

import be.khleuven.kvh.kikkersprong.Constants.KikkersprongConstants;
import be.khleuven.kvh.kikkersprong.R;
import be.khleuven.kvh.kikkersprong.adapters.PaymentAdapter;
import be.khleuven.kvh.kikkersprong.db.PaymentDataSource;
import be.khleuven.kvh.kikkersprong.db.UserDataSource;
import be.khleuven.kvh.kikkersprong.model.Payment;
import be.khleuven.kvh.kikkersprong.model.User;

/**
 * Created by KEVIN on 8/08/2014.
 */
public class PaymentActivity extends Activity {


    private static final String TAG = PaymentActivity.class.getName();
    private PaymentDataSource paymentDb;
    private UserDataSource userDb;
    private int mId;
    private String mFirstName;
    private String mName;
    private ListView listview;
    private ArrayList<Payment> payments=null;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
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
            paymentDb.open();
            userDb.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        payments = paymentDb.getAllPaymentsByChild(user);

        Payment[] paymentsers = payments.toArray(new Payment[payments.size()]);




        PaymentAdapter adapter = new PaymentAdapter(this,R.layout.list_view_pay,paymentsers);

        ListView listViewItems = new ListView(this);
        listViewItems.setAdapter(adapter);
        this.setContentView(listViewItems);
        paymentDb.close();

    }
    private void initialiseDB(){
        paymentDb = new PaymentDataSource(this);
        userDb = new UserDataSource(this);
        user = userDb.getUser(mId);
        try {
            paymentDb.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Payment payment = new Payment(user,"July",1,50);
        paymentDb.createPayment(payment);
        paymentDb.close();
    }


    private void initializeWelcome() {

        Log.d(TAG, "Splitted: " + mId + " " + mFirstName + " " + mName);
    }


}
