package uqac.dim.uqaclife;

import android.os.Bundle;
import android.view.View;

public class settingsActivity extends MainActivity {

    int toCollapseNumber = 3;
    View buttonsToCollapse[] = new View[toCollapseNumber];

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
 //       buttonsToCollapse = new View[toCollapseNumber];
        buttonsToCollapse[0] = findViewById(R.id.styles_list);
        buttonsToCollapse[1] = findViewById(R.id.language_list);
        buttonsToCollapse[2] =  findViewById(R.id.notifications_list);

    }

    void collapse(View v){
        View selected = null;
        switch (v.getId()){
            case R.id.Styles:
                selected = findViewById(R.id.styles_list);
                break;
            case R.id.Language:
                selected = findViewById(R.id.language_list);
                break;
            case R.id.Notifications:
                selected = findViewById(R.id.notifications_list);
                break;
        }
        if(selected.getVisibility() == View.VISIBLE)
            selected.setVisibility(View.GONE);
        else
        {
            for(int i = 0 ; i < toCollapseNumber ; i++)
                buttonsToCollapse[i].setVisibility(View.GONE);
            selected.setVisibility(View.VISIBLE);
        }

    }

//    void selectOption(View v){
//        v.getParent();
//    }

}
