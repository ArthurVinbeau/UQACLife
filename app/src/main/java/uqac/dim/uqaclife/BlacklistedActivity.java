package uqac.dim.uqaclife;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


public class BlacklistedActivity extends MainActivity {

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
        final LinearLayout blacklistedList = (LinearLayout)findViewById(R.id.blacklistedList);

        final String blacklisted = sharedPref.getString("blacklisted", null);
        blacklistedList.removeAllViews();
        if(blacklisted!=null){
            String[] lessons = blacklisted.split(" #'");
            for(int i = 1; i < lessons.length ; i++){
                final String lesson = lessons[i].split("'#")[0];

                View cours = getLayoutInflater().inflate(R.layout.blacklisted_course, blacklistedList, false);
                ((TextView)cours.findViewById(R.id.blacklistedName)).setText(lesson);
                View t2 = cours.findViewById((R.id.unblacklist_button));
                t2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        unblacklist(lesson);
                        loadBlacklisted();
                        Snackbar snackbar =  Snackbar.make(blacklistedList, getString(R.string.course_unblacklisted), Snackbar.LENGTH_LONG);
                        snackbar.setActionTextColor(0xffffffff);
                        snackbar.setAction(getString(R.string.undo), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                blacklist(lesson);
                                loadBlacklisted();
                            }
                        });
                        snackbar.show();
                    }
                });

                blacklistedList.addView(cours);

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
