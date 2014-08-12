package be.khleuven.kvh.ksprong.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Date;

import be.khleuven.kvh.kikkersprong.R;
import be.khleuven.kvh.kikkersprong.model.Attendance;

/**
 * Created by KEVIN on 11/08/2014.
 */
public class AttendanceAdapter extends ArrayAdapter<Attendance> {

    Context mContext;
    int layoutResourceId;
    Attendance data[] = null;

    public AttendanceAdapter(Context mContext, int layoutResourceId, Attendance[] data){
        super(mContext,layoutResourceId,data);
        this.layoutResourceId=layoutResourceId;
        this.mContext = mContext;
        this.data =data;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        if(convertView==null){
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId,parent,false);
        }

        Attendance attendance = data[position];

        Date date2 = new Date();
        String today = date2.toString().substring(0,10);

        TextView txtDate = (TextView) convertView.findViewById(R.id.txtDate);
        String date = attendance.getDate().substring(0,10);
        if(date.equals(today)){
            txtDate.setText("Today: ");
        }else {
            txtDate.setText(date + ": ");
        }
        TextView txtHour = (TextView) convertView.findViewById(R.id.txtHour);
        int hours = (int)attendance.getHour();
        String hour = String.valueOf(hours);
        txtHour.setText(hour+" Hours");

        return convertView;




    }
}
