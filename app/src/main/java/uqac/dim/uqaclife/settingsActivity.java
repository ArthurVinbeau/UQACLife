package uqac.dim.uqaclife;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.NumberPicker;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class settingsActivity extends MainActivity {

    int toCollapseNumber = 2;
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
        final Activity curentActivity = this;
        findViewById(R.id.Logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPref.edit().putString("login",null).apply();
                sharedPref.edit().putString("password",null).apply();
                curentActivity.finish();
            }
        });
        ((TextView)findViewById(R.id.selected_language)).setText(sharedPref.getString("Langue"," Français"));

        buttonsToCollapse[0] = findViewById(R.id.language_list);
        buttonsToCollapse[1] =  findViewById(R.id.notifications_list);

        for(int index=1; index<((ViewGroup)buttonsToCollapse[1]).getChildCount()-1; index++) {
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
            for (int i = 0 ; i < viewGroup.getChildCount() ; i++) {
                viewGroup.getChildAt(i).setOnClickListener(o);
            }
        }

        final View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collapse(v);
            }
        };
        //SEB
        findViewById(R.id.Notifications).setOnClickListener(onClickListener);
        Switch s = findViewById(R.id.switch1);
        Switch s2 = findViewById(R.id.switch2);
        //EditText e1 = findViewById(R.id.minute_delay);

        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activenotif(v);
            }
        });
        s2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActiveService(v);
            }
        });

        findViewById(R.id.Language).setOnClickListener(onClickListener);
        findViewById(R.id.selected_language).setOnClickListener(onClickListener);
        findViewById(R.id.notification_arrow).setOnClickListener(onClickListener);

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

        boolean statenotifswitch = sharedPref.getBoolean("switchnotif",true);
        boolean stateservswitch = sharedPref.getBoolean("switchservice",false);

        Switch swich_notif = (Switch)findViewById(R.id.switch1);
        Switch swich_sevice = (Switch)findViewById(R.id.switch2);
        swich_notif.setChecked(statenotifswitch);
        swich_sevice.setChecked(stateservswitch);
        TextView t1 = findViewById(R.id.textView5);
        TextView t2 = findViewById(R.id.textView6);
        View.OnClickListener o2 =
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LayoutInflater inflater = (LayoutInflater)
                                getSystemService(LAYOUT_INFLATER_SERVICE);
                        View popupView = inflater.inflate(R.layout.popup_timeactivity, null);

                        // create the popup window
                        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                        boolean focusable = true; // lets taps outside the popup also dismiss it
                        final NumberPicker np = popupView.findViewById(R.id.NumberPicker1);
                        Button b = popupView.findViewById(R.id.button2);

                        np.setMinValue(0);
                        np.setMaxValue(59);
                        np.setValue(sharedPref.getInt("minutetoadd", 0));
                        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                        // show the popup window
                        // which view you pass in doesn't matter, it is only used for the window tolken
                        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
                        b.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                TextView T1 = findViewById(R.id.textView6);
                                sharedPref.edit().putInt("minutetoadd", np.getValue()).apply();
                                popupWindow.dismiss();
                                T1.setText(Integer.toString(np.getValue()));
                                show_week(v);
                            }
                        });
                    }
                };
        LinearLayout l1 = findViewById(R.id.delay_layout);
        l1.setOnClickListener(o2);
        t2.setText(Integer.toString(sharedPref.getInt("minutetoadd",0)));

    }

    void collapse(View v) {
        View selected = null;
        boolean rotateV = false;
              switch (v.getId()) {
            case R.id.Language:
            case R.id.selected_language:
                selected = findViewById(R.id.language_list);
                break;
            case R.id.Notifications:
            case R.id.notification_arrow:
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

    public void Activenotif(View v)
    {
        Switch s = (Switch)findViewById(R.id.switch1);
        Switch s2 = (Switch)findViewById(R.id.switch2);
        if(!s.isChecked()) {
            s2.setChecked(false);
            stopService(v);
            sharedPref.edit().putBoolean("switchservice",s.isChecked()).apply();
        }
        sharedPref.edit().putBoolean("switchnotif",s.isChecked()).apply();
        show_week(v);

    }
    public void ActiveService(View v)
    {
        Switch s = (Switch)findViewById(R.id.switch2);
        Switch s2 = (Switch)findViewById(R.id.switch1);
        if(s.isChecked()) {
            s2.setChecked(true);
            startService(v);
            sharedPref.edit().putBoolean("switchnotif",s.isChecked()).apply();
        }
        else
        {
            stopService(v);
        }
        sharedPref.edit().putBoolean("switchservice",s.isChecked()).apply();
        show_week(v);

    }




}
