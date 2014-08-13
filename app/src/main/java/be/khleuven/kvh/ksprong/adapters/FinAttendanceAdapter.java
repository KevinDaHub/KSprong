package be.khleuven.kvh.ksprong.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import be.khleuven.kvh.ksprong.R;
import be.khleuven.kvh.ksprong.db.AttendanceDataSource;
import be.khleuven.kvh.ksprong.model.Attendance;

/**
 * Created by KEVIN on 13/08/2014.
 */
public class FinAttendanceAdapter extends ArrayAdapter<Attendance> implements Filterable {

    Context mContext;
    int layoutResourceId;
    Attendance data[] = null;
    boolean notifyChanged=false;
    private AttendanceDataSource attendanceDb;

    public FinAttendanceAdapter(Context mContext, int layoutResourceId, Attendance[] data){
        super(mContext,layoutResourceId,data);
        this.layoutResourceId=layoutResourceId;
        this.mContext = mContext;
        this.data =data;
        attendanceDb = new AttendanceDataSource(mContext);


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
                ArrayList<Attendance> attendances = (ArrayList)results.values;
                data = attendances.toArray(new Attendance[attendances.size()]);

                FinAttendanceAdapter.this.notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();

                if (constraint == null || constraint.length() == 0) {
                    // No filter implemented we return all the list
                    try {
                        attendanceDb.open();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    ArrayList<Attendance> attendances = attendanceDb.getAllAttendances();
                    results.values =attendances;
                    results.count = attendances.size();
                }
                else {
                    // We perform filtering operation
                    List<Attendance> nAttendanceList = new ArrayList<Attendance>();

                    for (Attendance attendance : data) {
                        String fullName = attendance.getUser().getName() + " " + attendance.getUser().getSurname();
                        if (fullName.toUpperCase().startsWith(constraint.toString().toUpperCase()))
                            nAttendanceList.add(attendance);
                    }

                    results.values = nAttendanceList;
                    results.count = nAttendanceList.size();

                }
                return results;



            }
        };
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

        TextView txtDate = (TextView) convertView.findViewById(R.id.txtFinDate);
        String date = attendance.getDate().substring(0,10);
        if(date.equals(today)){
            txtDate.setText("Today: ");
        }else {
            txtDate.setText(date + ": ");
        }
        TextView txtHour = (TextView) convertView.findViewById(R.id.txtFinHour);
        int hours = (int)attendance.getHour();
        String hour = String.valueOf(hours);
        txtHour.setText(hour+" Hours");

        TextView txtUser = (TextView) convertView.findViewById(R.id.txtFinUser);
        txtUser.setText(attendance.getUser().getName()+" "+attendance.getUser().getSurname());

        return convertView;




    }
}
