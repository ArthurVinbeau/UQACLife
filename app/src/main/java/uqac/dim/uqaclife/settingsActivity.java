package uqac.dim.uqaclife;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class settingsActivity extends MainActivity {

    int toCollapseNumber = 3;
    View buttonsToCollapse[] = new View[toCollapseNumber];
    TextView textView;
    List<TextView> languages = new ArrayList<>();

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
                sharedPref.edit().putString("loginActivity",null).apply();
                sharedPref.edit().putString("password",null).apply();
            }
        });
        ((TextView)findViewById(R.id.selected_language)).setText(sharedPref.getString("Langue"," Français"));

        buttonsToCollapse[0] = findViewById(R.id.styles_list);
        buttonsToCollapse[1] = findViewById(R.id.language_list);
        buttonsToCollapse[2] =  findViewById(R.id.notifications_list);

        for(int index=1; index<((ViewGroup)buttonsToCollapse[1]).getChildCount(); index++) {
            languages.add((TextView)(((ViewGroup)buttonsToCollapse[1]).getChildAt(index)));
        }

        View.OnClickListener o = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectOption(v);
            }
        };
        for(int j = 0 ; j < toCollapseNumber -1 ; j++)
        {
            ViewGroup viewGroup = (ViewGroup) buttonsToCollapse[j];
            for (int i = 1 ; i < viewGroup.getChildCount() ; i++) {
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

        ((EditText)findViewById(R.id.searchEditText)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                for (TextView t: languages) {
                    if(t.getText().toString().toLowerCase().contains(s.toString().toLowerCase()))
                        t.setVisibility(View.VISIBLE);
                    else
                        t.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

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
        String newText = (String)((TextView) v).getText();
        switch (parent.getId()) {
            case R.id.styles_list:
                changeText = (TextView) findViewById(R.id.selected_style);
                break;
            case R.id.language_list:
                changeText = (TextView) findViewById(R.id.selected_language);
                changeLanguage(newText);
                String newLanguage;
                switch (newText){
                    default: newLanguage = ""; break;
                    case "Français": newLanguage = "fr"; break;
                    case "Bosnian": newLanguage = "bs"; break;
                    case "Chinese": newLanguage = "zh"; break;
                    case "Deutsch": newLanguage = "de"; break;
                    case "Korean": newLanguage= "ko" ; break;
                    case "Spanish": newLanguage = "es"; break;
                    case "Finnish": newLanguage = "fi"; break;
                    case "Greek": newLanguage = "el"; break;
                    case "Icelandic": newLanguage = "is"; break;
                    case "Japanese": newLanguage = "ja"; break;
                    case "Javanese": newLanguage = "jv"; break;
                    case "Latin": newLanguage = "la"; break;
                    case "Pashto": newLanguage = "ps"; break;
                    case "Polish": newLanguage = "pl"; break;
                    case "Russian": newLanguage = "ru"; break;
                    case "Yoruba": newLanguage = "yo"; break;
                    case "Zulu": newLanguage = "zu"; break;
                }
                sharedPref.edit().putString("Language",newLanguage).putString("Langue",newText).apply();
                invalidateOptionsMenu();
                Intent refresh = new Intent(this, settingsActivity.class);
                refresh.putExtra("requestCode", 42);
                startActivity(refresh);
                finish();
                overridePendingTransition (0,0);
                break;
        }
        changeText.setText(newText);
        parent.setVisibility(View.GONE);
    }

    private void changeLanguage(String language){
        Locale myLocale = new Locale(language);
        Resources res = getResources();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf,res.getDisplayMetrics());
    }
}
