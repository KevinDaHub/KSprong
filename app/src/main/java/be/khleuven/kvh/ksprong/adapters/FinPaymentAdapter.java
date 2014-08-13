package be.khleuven.kvh.ksprong.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import be.khleuven.kvh.kikkersprong.R;
import be.khleuven.kvh.kikkersprong.db.PaymentDataSource;
import be.khleuven.kvh.kikkersprong.model.Payment;

/**
 * Created by KEVIN on 13/08/2014.
 */
public class FinPaymentAdapter extends ArrayAdapter<Payment> {

    Context mContext;
    int layoutResourceId;
    Payment data[] = null;
    private PaymentDataSource paymentDb;

    public FinPaymentAdapter(Context mContext, int layoutResourceId, Payment[] data) {
        super(mContext, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
        paymentDb = new PaymentDataSource(mContext);

    }


    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public Filter getFilter(){
        return new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                // Log.d(SyncStateContract.Constants.TAG, "**** PUBLISHING RESULTS for: " + constraint);
                //Attendance[] attendancesers = attendances.toArray(new Attendance[attendances.size()]);
                List<Payment> payments = (List)results.values;
                data = payments.toArray(new Payment[payments.size()]);

                FinPaymentAdapter.this.notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();

                if (constraint == null || constraint.length() == 0) {
                    // No filter implemented we return all the list
                    try {
                        paymentDb.open();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    ArrayList<Payment> payments = paymentDb.getAllPayments();


                    results.values =payments;
                    results.count = payments.size();
                    paymentDb.close();

                }
                else {
                    // We perform filtering operation
                    List<Payment> nPaymentList = new ArrayList<Payment>();

                    for (Payment payment : data) {
                        String fullName = payment.getUser().getName() + " " + payment.getUser().getSurname();
                        if (fullName.toUpperCase().startsWith(constraint.toString().toUpperCase()))
                            nPaymentList.add(payment);
                    }

                    results.values = nPaymentList;
                    results.count = nPaymentList.size();

                }
                return results;



            }
        };
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
        }

        Payment payment = data[position];

        TextView txtMonth = (TextView) convertView.findViewById(R.id.txtPayDate);
        txtMonth.setText(payment.getMonth());
        TextView txtPaid = (TextView) convertView.findViewById(R.id.txtPayPaid);
        if(payment.isPaid()==1){
            txtPaid.setText("Paid");
        }else{
            txtPaid.setText("Not paid");
        }
        TextView txtUser = (TextView) convertView.findViewById(R.id.txtPayUser);
        txtUser.setText(payment.getUser().getName()+" "+payment.getUser().getSurname());
        TextView txtPrice = (TextView) convertView.findViewById(R.id.txtPayPrice);
        String total = String.valueOf(payment.getTotal());
        txtPrice.setText(total);




        return convertView;


    }
}
