package uqac.dim.uqaclife;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

public class LoginActivity extends  MainActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }
}
