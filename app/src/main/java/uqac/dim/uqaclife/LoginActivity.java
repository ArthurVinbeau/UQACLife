package uqac.dim.uqaclife;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
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

        super.loginActivity = this;
        login = super.login;
    }

    public void send(View v) {
        Button b = (Button) v;
        b.setEnabled(false);
        b.setVisibility(View.GONE);

        String id = ((EditText)findViewById(R.id.login)).getText().toString();
        String pwd = ((EditText)findViewById(R.id.password)).getText().toString();

        sharedPref.edit().putString("login", id).commit();
        if (((CheckBox)findViewById(R.id.save_password)).isChecked())
            sharedPref.edit().putString("password", pwd).commit();

        login.login(id, pwd, true);
    }

    public void transfer(String html) {
        sharedPref.edit().putString("json", null).commit();
        Intent i = new Intent(Intent.EXTRA_HTML_TEXT);
        i.putExtra("html", html);
        setResult(Activity.RESULT_OK, i);
        finish();
    }

}
