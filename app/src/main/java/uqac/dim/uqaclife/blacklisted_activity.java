package uqac.dim.uqaclife;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.content.SharedPreferences;
import android.widget.TextView;

import com.android.volley.toolbox.Volley;

import java.io.Console;


public class blacklisted_activity extends MainActivity {

    SharedPreferences sharedPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blacklisted);
        sharedPref = getSharedPreferences(getResources().getString(R.string.preferences_file), MODE_PRIVATE);
        loadBlacklisted();


    }

    private void loadBlacklisted(){
        LinearLayout blacklistedList = (LinearLayout)findViewById(R.id.blacklistedList);

        String blacklisted = sharedPref.getString("blacklisted", null);
        blacklistedList.removeAllViews();
        if(blacklisted!=null){
            Log.i("Unbli",blacklisted);
            String[] lessons = blacklisted.split(" #'");
            for(int i = 1; i < lessons.length ; i++){
                Log.i("Unbli",lessons[i]);
                final String lesson = lessons[i].split("'#")[0];
                LinearLayout linearLayout = new LinearLayout(this);

                TextView t1 = new TextView(this);
                LinearLayout.LayoutParams l = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                l.setMargins(20,20,20,20);
                t1.setLayoutParams(l);
                t1.setText(lesson);
                t1.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                t1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
                TextView t2 = new TextView(this);
                t2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        unblacklist(lesson);
                        loadBlacklisted();
                    }
                });
                LinearLayout.LayoutParams l2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                l.setMargins(20,20,40,20);

                t2.setLayoutParams(l2);
                t2.setText("X");
                t2.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                t2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
                linearLayout.addView(t1);
                linearLayout.addView(t2);

                blacklistedList.addView(linearLayout);

            }
        }


    }

    private void blacklist(String lesson){
        sharedPref.edit().putString("blacklisted", sharedPref.getString("blacklisted", "") + " #'" + lesson +"'#").commit();
    }

    private void unblacklist(String lesson){
        sharedPref.edit().putString("blacklisted", sharedPref.getString("blacklisted", "") .replace(" #'"+lesson+"'#","")).commit();
    }
}
