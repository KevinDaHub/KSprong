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
import be.khleuven.kvh.ksprong.adapters.FinPaymentAdapter;
import be.khleuven.kvh.ksprong.db.PaymentDataSource;
import be.khleuven.kvh.ksprong.db.UserDataSource;
import be.khleuven.kvh.ksprong.model.Payment;
import be.khleuven.kvh.ksprong.model.User;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class FinPaymentActivity extends Activity {


    private static final String TAG = FinPaymentActivity.class.getName();
    private PaymentDataSource paymentDb;
    private UserDataSource userDb;
    private ArrayList<Payment> payments=null;
    FinPaymentAdapter adapter;

    private User user;
    @InjectView(R.id.listViewFin)
    ListView listViewFin;

    @InjectView(R.id.editTxtfin)
    EditText editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fin_payment);
        ButterKnife.inject(this);
        initialiseDB();
        initialiseList();

        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println("Text [" + s + "]");
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

    private void initialiseDB(){
        paymentDb = new PaymentDataSource(this);
        userDb = new UserDataSource(this);

        try {
            paymentDb.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

      //  Payment payment = new Payment(user,"July",1,50);
       // paymentDb.createPayment(payment);
        paymentDb.close();
    }


    private void initialiseList(){
        try {
            paymentDb.open();
            userDb.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        payments = paymentDb.getAllPayments();

        Payment[] paymentsers = payments.toArray(new Payment[payments.size()]);




        adapter = new FinPaymentAdapter(this,R.layout.list_view_fin_pay,paymentsers);

        ListView listViewItems = new ListView(this);
        listViewFin.setAdapter(adapter);
        paymentDb.close();

    }
}
