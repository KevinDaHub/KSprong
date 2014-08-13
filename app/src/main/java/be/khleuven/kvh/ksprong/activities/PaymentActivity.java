package be.khleuven.kvh.ksprong.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.sql.SQLException;
import java.util.ArrayList;

import be.khleuven.kvh.ksprong.Constants.KikkersprongConstants;
import be.khleuven.kvh.ksprong.R;
import be.khleuven.kvh.ksprong.adapters.PaymentAdapter;
import be.khleuven.kvh.ksprong.db.PaymentDataSource;
import be.khleuven.kvh.ksprong.db.UserDataSource;
import be.khleuven.kvh.ksprong.model.Payment;
import be.khleuven.kvh.ksprong.model.User;

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

        ListView viewPay = (ListView)findViewById(R.id.listViewPay);


        PaymentAdapter adapter = new PaymentAdapter(this,R.layout.list_view_pay,paymentsers);

       viewPay.setAdapter(adapter);

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

        Payment payment2 = new Payment(user,"April",0,500);

        Payment payment3 = new Payment(user,"Mai",0,150);
        Payment payment4 = new Payment(user,"January",1,50);
        Payment payment5 = new Payment(user,"December",0,250);
        Payment payment6 = new Payment(user,"October",1,50);
        Payment payment7 = new Payment(user,"September",0,450);

        paymentDb.createPayment(payment);
        paymentDb.createPayment(payment2);
        paymentDb.createPayment(payment3);
        paymentDb.createPayment(payment4);
        paymentDb.createPayment(payment5);
        paymentDb.createPayment(payment6);
        paymentDb.createPayment(payment7);
        paymentDb.close();
    }


    private void initializeWelcome() {

        Log.d(TAG, "Splitted: " + mId + " " + mFirstName + " " + mName);
    }


}
