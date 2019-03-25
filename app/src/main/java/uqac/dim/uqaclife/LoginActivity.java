package uqac.dim.uqaclife;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.android.volley.toolbox.NetworkImageView;

public class LoginActivity extends  MainActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        login.getCaptcha((NetworkImageView) findViewById(R.id.container_captcha));

    }
}
