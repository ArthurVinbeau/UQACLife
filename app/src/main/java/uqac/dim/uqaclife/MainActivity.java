package uqac.dim.uqaclife;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final login login = new login();
        login.setQueue(Volley.newRequestQueue(this));
        login.getCaptcha(findViewById(R.id.container_captcha));

        findViewById(R.id.validation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login.getCaptcha(findViewById(R.id.container_captcha));
            }
        });

        // Example of a call to a native method
        //TextView tv = (TextView) findViewById(R.id.sample_text);
        //tv.setText(stringFromJNI());
    }

}
