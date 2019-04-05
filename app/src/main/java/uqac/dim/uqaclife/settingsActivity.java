package uqac.dim.uqaclife;

import android.os.Bundle;
import android.view.View;

public class settingsActivity extends MainActivity {
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setttings);
        sharedPref = getSharedPreferences(getResources().getString(R.string.preferences_file), MODE_PRIVATE);
        findViewById(R.id.Logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPref.edit().putString("login",null).apply();
                sharedPref.edit().putString("password",null).apply();
            }
        });

    }


}
