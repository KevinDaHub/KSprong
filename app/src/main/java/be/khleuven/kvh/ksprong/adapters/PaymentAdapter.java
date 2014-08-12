package be.khleuven.kvh.ksprong.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import be.khleuven.kvh.kikkersprong.R;
import be.khleuven.kvh.kikkersprong.model.Payment;

/**
 * Created by KEVIN on 11/08/2014.
 */
public class PaymentAdapter  extends ArrayAdapter<Payment> {

    Context mContext;
    int layoutResourceId;
    Payment data[] = null;

    public PaymentAdapter(Context mContext, int layoutResourceId, Payment[] data) {
        super(mContext, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
        }

        Payment payment = data[position];

        TextView txtMonth = (TextView) convertView.findViewById(R.id.txtMonth);
        txtMonth.setText(payment.getMonth());



        return convertView;


    }
}