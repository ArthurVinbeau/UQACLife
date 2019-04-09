package uqac.dim.uqaclife;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class settingsActivity extends MainActivity {

    int toCollapseNumber = 3;
    View buttonsToCollapse[] = new View[toCollapseNumber];
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setttings);
        textView = (TextView)findViewById(R.id.Debug);
        sharedPref = getSharedPreferences(getResources().getString(R.string.preferences_file), MODE_PRIVATE);
        findViewById(R.id.Logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPref.edit().putString("login",null).apply();
                sharedPref.edit().putString("password",null).apply();
            }
        });

        buttonsToCollapse[0] = findViewById(R.id.styles_list);
        buttonsToCollapse[1] = findViewById(R.id.language_list);
        buttonsToCollapse[2] =  findViewById(R.id.notifications_list);

        View.OnClickListener o = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectOption(v);
            }
        };
        for(int j = 0 ; j < toCollapseNumber -1 ; j++)
        {
            ViewGroup viewGroup = (ViewGroup) buttonsToCollapse[j];
            for (int i = 0 ; i < viewGroup.getChildCount() ; i++) {
                viewGroup.getChildAt(i).setOnClickListener(o);
            }
        }

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collapse(v);
            }
        };
        findViewById(R.id.Notifications).setOnClickListener(onClickListener);
        findViewById(R.id.Styles).setOnClickListener(onClickListener);
        findViewById(R.id.Language).setOnClickListener(onClickListener);

    }

    void collapse(View v) {
        View selected = null;
        boolean rotateV = false;
              switch (v.getId()) {
            case R.id.Styles:
                selected = findViewById(R.id.styles_list);
                break;
            case R.id.Language:
                selected = findViewById(R.id.language_list);
                break;
            case R.id.Notifications:
                rotateV = true;
                selected = findViewById(R.id.notifications_list);
                break;
        }
        if(selected != null) {
            if (selected.isShown())
            {
                ((TextView)findViewById(R.id.notification_arrow)).setRotation(0);
                selected.setVisibility(View.GONE);
            }
            else {
                ((TextView)findViewById(R.id.notification_arrow)).setRotation(rotateV?180:0);
                for (int i = 0; i < toCollapseNumber; i++)
                    buttonsToCollapse[i].setVisibility(View.GONE);
                selected.setVisibility(View.VISIBLE);
            }
        }
    }

    void selectOption(View v) {
        View parent = (View) v.getParent();
        TextView changeText = null;
        switch (parent.getId()) {
            case R.id.styles_list:
                changeText = (TextView) findViewById(R.id.selected_style);
                break;
            case R.id.language_list:
                changeText = (TextView) findViewById(R.id.selected_language);
                break;
        }
        changeText.setText(((TextView) v).getText());
        parent.setVisibility(View.GONE);
    }
}
