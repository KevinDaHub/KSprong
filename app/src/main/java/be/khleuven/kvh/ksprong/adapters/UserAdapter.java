package be.khleuven.kvh.ksprong.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import be.khleuven.kvh.kikkersprong.R;
import be.khleuven.kvh.kikkersprong.model.User;

/**
 * Created by KEVIN on 11/08/2014.
 */
public class UserAdapter   extends ArrayAdapter<User> {

    Context mContext;
    int layoutResourceId;
    User data[] = null;

    public UserAdapter(Context mContext, int layoutResourceId, User[] data) {
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

        User user = data[position];

        TextView txtUser = (TextView) convertView.findViewById(R.id.txtUser);
        txtUser.setText(user.getName() +" "+ user.getSurname());


        return convertView;


    }
}
