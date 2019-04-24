package uqac.dim.uqaclife;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.TextUtils;
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
    private boolean grades;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        sharedPref = getSharedPreferences(getResources().getString(R.string.preferences_file), MODE_PRIVATE);

        ((EditText)findViewById(R.id.login)).setText(sharedPref.getString("login", ""));

        MainActivity.singleton.loginActivity = this;
        login = super.login;
        grades = getIntent().getBooleanExtra("grades", false);
        Log.i("request", "grades create : " + grades);
    }

    public void send(View v) {
        Button b = (Button) v;
        b.setEnabled(false);
        b.setVisibility(View.GONE);

        String id = ((EditText)findViewById(R.id.login)).getText().toString();
        String pwd = ((EditText)findViewById(R.id.password)).getText().toString();

        findViewById(R.id.login).setEnabled(false);
        //findViewById(R.id.login).setFocusable(false);
        findViewById(R.id.password).setEnabled(false);
        //findViewById(R.id.password).setFocusable(false);


        sharedPref.edit().putString("login", id).commit();
        if (((CheckBox)findViewById(R.id.save_password)).isChecked())
            sharedPref.edit().putString("password", pwd).commit();

        Log.i("request", "grades : " + grades);
        login.login(id, pwd, grades ? 3 : 1);
    }

    public void transfer(String html) {
        sharedPref.edit().putString("json", null).commit();
        Intent i = new Intent(Intent.EXTRA_HTML_TEXT);
        i.putExtra("html", html);
        setResult(Activity.RESULT_OK, i);
        finish();
    }

    public void failLogin(int code) {
        Button b = findViewById(R.id.button);
        b.setEnabled(true);
        b.setVisibility(View.VISIBLE);

        findViewById(R.id.login).setEnabled(true);
        //findViewById(R.id.login).setFocusable(true);
        findViewById(R.id.password).setEnabled(true);
        //findViewById(R.id.password).setFocusable(true);
        Log.i("request", "enabled : " + findViewById(R.id.login).isEnabled() + findViewById(R.id.password).isFocusable());
        String[] message;
        if (code == 0)
            message = new String[]{getString(R.string.error_login_credentials_1), getString(R.string.error_login_credentials_2)};
        else if (code == 1)
            message = new String[]{getString(R.string.error_login_network_1), getString(R.string.error_login_network_2), getString(R.string.error_login_network_3)};
        else
            message = new String[]{getString(R.string.error_login_unknown_1), getString(R.string.error_login_unknown_2)};
        ((TextView)findViewById(R.id.progressText)).setText(TextUtils.join("\n", message));
        b.requestFocus();
    }

}
