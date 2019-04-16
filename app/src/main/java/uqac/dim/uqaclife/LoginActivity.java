package uqac.dim.uqaclife;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends MainActivity {

    SharedPreferences sharedPref;
    private Login login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        sharedPref = getSharedPreferences(getResources().getString(R.string.preferences_file), MODE_PRIVATE);

        ((EditText)findViewById(R.id.login)).setText(sharedPref.getString("login", ""));
        ((EditText)findViewById(R.id.password)).setText(sharedPref.getString("password", ""));

        super.loginActivity = this;
        login = super.login;
    }

    public void send(View v) {
        String id = ((EditText)findViewById(R.id.login)).getText().toString();
        String pwd = ((EditText)findViewById(R.id.password)).getText().toString();

        sharedPref.edit().putString("login", id).commit();
        sharedPref.edit().putString("password", pwd).commit();

        login.login(id, pwd);

        finish();
    }

}
