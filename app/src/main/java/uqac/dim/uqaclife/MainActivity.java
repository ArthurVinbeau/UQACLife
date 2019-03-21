package uqac.dim.uqaclife;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {

    Login login;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login = new Login();
        login.setQueue(Volley.newRequestQueue(this));

        // Example of a call to a native method
        //TextView tv = (TextView) findViewById(R.id.sample_text);
        //tv.setText(stringFromJNI());
    }

    public void versLInfini(View v) {
        setContentView(R.layout.activity_horaire);
    }

    public void etPasLAuDela(View v) {
        setContentView(R.layout.activity_main);
    }

    public void fleur(View v) {
        login.getCaptcha((ImageView) findViewById(R.id.container_captcha), (TextView) findViewById(R.id.debug));
    }

}
