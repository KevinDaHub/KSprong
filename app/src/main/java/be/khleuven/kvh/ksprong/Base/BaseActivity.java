package be.khleuven.kvh.ksprong.Base;

import android.app.Activity;
import android.content.Intent;

import be.khleuven.kvh.ksprong.activities.LoginActivity;

/**
 * Created by KEVIN on 9/08/14.
 */
public class BaseActivity extends Activity {


    @Override
    public void onBackPressed(){
        Intent loginIntent = new Intent(this, LoginActivity.class);
         startActivity(loginIntent);

    }
}
