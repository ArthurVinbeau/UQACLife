package uqac.dim.uqaclife;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;


import android.content.res.Resources;

public class MainActivity extends AppCompatActivity {

    Boolean hideEmptyDay = true;
    int id_notif = 1;


    int[][] colors = new int[][]{
            new int[]{0xFFFFC107, 0xFFFF9B00},           //mondayColors
            new int[]{0xFFF75A4E, 0xFFF81D0D},          // tuesdayColors
            new int[]{0xFFD77EF5, 0xFFBF0EDD},          //wednesdayColors
            new int[]{0xFF5970E7, 0xFF2040EC},          //thurdsdayColors
            new int[]{0xFF4CE751, 0xFF11A214},          //fridayColors
            new int[]{0xFF47E1F5, 0xFF0A8EA3},          //saturdayColors
            new int[]{0xFFFFEB3B, 0xFFEBD827}};        //sundayColors
    String[] strdays = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    int[] days = new int[]{
            R.string.monday,
            R.string.tuesday,
            R.string.wednesday,
            R.string.thursday,
            R.string.friday,
            R.string.saturday,
            R.string.sunday};

    SharedPreferences sharedPref;
    LinearLayout weeklist_layout;

    String html = "";

    LoginActivity loginActivity;
    Login login;
    RequestQueue queue;
    public static MainActivity singleton;
    public int test = 0;

    SwipeRefreshLayout swipe;

    private int jaja = 0;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        if (b != null && b.getInt("requestCode") == 42)
            return false;
        getMenuInflater().inflate(R.menu.main_drop_menu, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.preferences:
                intent = new Intent(getApplicationContext(), SettingsActivity.class);
                intent.putExtra("requestCode", 42);
                startActivityForResult(intent, 42);
                return true;
            case R.id.blacklisted:
                intent = new Intent(getApplicationContext(), BlacklistedActivity.class);
                intent.putExtra("requestCode", 42);
                startActivityForResult(intent, 42);
                return true;
            case R.id.grades_button:
                intent = new Intent(getApplicationContext(), GradesActivity.class);
                intent.putExtra("requestCode", 42);
                startActivityForResult(intent, 42);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Design_Light);
        html = "coucou";
        singleton = this;
        sharedPref = getSharedPreferences(getResources().getString(R.string.preferences_file), MODE_PRIVATE);
        changeLanguage(sharedPref.getString("Language", "fr"));
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        weeklist_layout = findViewById(R.id.weekList);
        swipe = findViewById(R.id.pullToRefresh);
        //show_week(null);
        queue = Volley.newRequestQueue(this);
        login = new Login(this);


        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                TextView t = findViewById(R.id.weeklist_error);
                if (t != null)
                    weeklist_layout.removeView(t);
                login.getSchedule();
            }
        });

        // Example of a call to a native method
        //TextView tv = (TextView) findViewById(R.id.sample_text);
        //tv.setText(stringFromJNI());
    }


    public void startService(View v)
    {
        Intent serviceIntent = new Intent(this,NotifService.class);
        //serviceIntent.putExtra("bool",b);
        ContextCompat.startForegroundService(this, serviceIntent);
    }
    public void stopService(View v)
    {
        Intent serviceIntent = new Intent(this,NotifService.class);
        stopService(serviceIntent);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //setContentView(R.layout.activity_test);
        weeklist_layout = findViewById(R.id.weekList);
        swipe = findViewById(R.id.pullToRefresh);
        Log.i("request", "onActivityResult : weeklist_layout : " + (weeklist_layout != null) + " | pull : " + (swipe != null));
        if (requestCode == 42) {
            invalidateOptionsMenu();
            show_week(null);
        } else if (requestCode == 155 && data != null) {
            html = data.getExtras().getString("html").replace("\r", "");
            Log.i("request", "result : " + html);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        weeklist_layout = findViewById(R.id.weekList);
        swipe = findViewById(R.id.pullToRefresh);
        Log.i("request", "onResume : weeklist_layout : " + (weeklist_layout != null) + " | pull : " + (swipe != null));
        if(swipe != null && weeklist_layout != null)
            show_week(null, true);
    }

    public void versLInfini(View v) {
        //setContentView(R.layout.activity_test);
        loginActivity.finish();
        sharedPref.edit().putString("loginActivity", ((TextView) findViewById(R.id.login)).getText().toString()).commit();
        sharedPref.edit().putString("password", ((CheckBox) findViewById(R.id.save_password)).isChecked() ? ((TextView) findViewById(R.id.login)).getText().toString() : "").commit();
        //show_week(v);
    }

    public void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("requestCode", 155);
        startActivityForResult(intent, 155);
    }


    public void onButtonShowPopupWindowClick(View view) {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.activity_main, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }


    public void refresh_week(View v) {
        //html = htmlRequest();
        show_week(v);
    }


    public void show_week(View v, boolean endPull) {
        Log.i("request", "show week");
        Log.i("request", "json exists : " + (sharedPref.getString("json", null) != null));
        try {

            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-u");
            String currentDate = dateFormat.format(new Date());
            int[] movingDate = new int[4];
            int i = 0;
            for (String inte : currentDate.split("-")) {
                movingDate[i++] = Integer.parseInt(inte);
            }

            while (movingDate[3]-- > 1) {
                movingDate[0]--;
                if (movingDate[0] == 0) {
                    movingDate[1]--;
                    if (movingDate[1] == 0) {
                        movingDate[2]--;
                        movingDate[1] = 12;
                    }
                    if (movingDate[1] == 2)
                        movingDate[0] = (movingDate[0] % 4 == 0 && movingDate[0] % 100 != 0 ? 29 : 28);
                    else if (movingDate[1] > 7 ? movingDate[1] % 2 == 0 : movingDate[1] == 1)
                        movingDate[0] = 30;
                    else
                        movingDate[0] = 31;
                }

            }
            String currentweek = String.format("%02d", movingDate[0]) + "/" + String.format("%02d", movingDate[1]) + "/" + String.format("%02d", movingDate[2]);
            for (i = 1; i < 7; i++) {
                movingDate[0]++;
                if (movingDate[0] == 32) {
                    movingDate[0] = 1;
                    movingDate[1]++;
                } else if (movingDate[0] == 31 && (movingDate[1] > 7 ? movingDate[1] % 2 == 0 : movingDate[1] == 1)) {
                    movingDate[0] = 1;
                    movingDate[1]++;
                } else if (movingDate[1] == 2 && movingDate[0] > 28) {
                    if (movingDate[0] == 30) {
                        movingDate[0] = 1;
                        movingDate[1]++;
                    } else if (!(movingDate[2] % 4 == 0 && movingDate[2] % 100 != 0)) {
                        movingDate[0] = 1;
                        movingDate[1]++;
                    }
                }

                currentweek += " " + String.format("%02d", movingDate[0]) + "/" + String.format("%02d", movingDate[1]) + "/" + String.format("%02d", movingDate[2]);

            }


            Parser2 parser = new Parser2();


            JSONObject json = null;
            try {
                String savedJson = sharedPref.getString("json", null);
                if (savedJson == null) {
                    json = new JSONObject(parser.toJson(html));
                } else {
                    json = new JSONObject(savedJson);
                }
                sharedPref.edit().putString("json", json.toString()).commit();
            } catch (JSONException e) {
                Log.i("JSON error", e.toString(), e);
            }

            weeklist_layout.removeAllViews();
            int px = (int) (2 * getApplicationContext().getResources().getDisplayMetrics().density + 0.5f);
            int px2 = (int) (15 * getApplicationContext().getResources().getDisplayMetrics().density + 0.5f);
            // reset notif
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent remintent = new Intent(getBaseContext(), NotifReceiver.class);
            for (int k = 1; k <= id_notif; k++) {
                PendingIntent broadcast = PendingIntent.getBroadcast(getApplicationContext(), k, remintent, 0);
                alarmManager.cancel(broadcast);
            }
            id_notif = 1;

            //
            for (i = 0; i < 7; i++) {

                GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors[i]);
                GradientDrawable gd2 = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors[i]);


                try {
                    JSONArray day = json.getJSONArray(strdays[i]);
                    if (!hideEmptyDay || day.length() > 0) {
                        TextView t = new TextView(this);
                        LinearLayout.LayoutParams l = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        t.setPadding(0, px, 0, px);
                        t.setLayoutParams(l);
                        t.setText(getString(days[i]));
                        t.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        t.setTextColor(getColor(R.color.colorWhite));
                        t.setBackground(gd);
                        t.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                        t.setTypeface(Typeface.DEFAULT_BOLD);
                        weeklist_layout.addView(t);
                    }
                    gd2.setCornerRadii(new float[]{px2, px2, 0, 0, 0, 0, px2, px2});
                    for (int j = 0; j < day.length(); j++) {
                        try {

                            JSONObject lesson = day.getJSONObject(j);
                            final String name = lesson.getString("name");
                            if (!isBlacklisted(name) && dateCompare(currentweek, lesson.getString("dates"))) {
                                View cours = getLayoutInflater().inflate(R.layout.cours, weeklist_layout, false);
                                cours.findViewById(R.id.courstimes).setBackground(gd2);
                                String room = lesson.getString("room");
                                if (room.contains("T.D")) {
                                    cours.findViewById(R.id.TD).setVisibility(View.VISIBLE);
                                    room = room.replace("T.D", "");
                                } else if (room.contains("LAB")) {
                                    cours.findViewById(R.id.TD).setVisibility(View.VISIBLE);
                                    ((TextView) cours.findViewById(R.id.TD)).setText("LAB   ");
                                    room = room.replace("LAB", "");
                                }
                                final String room2 = room;
                                final String start = lesson.getString("start");
                                final String end = lesson.getString("end");
                                String id = lesson.getString("id");
                                ((TextView) cours.findViewById(R.id.lessonid)).setText(id);
                                ((TextView) cours.findViewById(R.id.lessonname)).setText(name);
                                ((TextView) cours.findViewById(R.id.lessonroom)).setText(room);
                                ((TextView) cours.findViewById(R.id.timestart)).setText(start);
                                ((TextView) cours.findViewById(R.id.timeend)).setText(end);
                                ((TextView) cours.findViewById(R.id.group)).setText(getString(R.string.groupe)+ " : " + lesson.getString("grp"));        //groupe
                                final View more_infos = cours.findViewById(R.id.more_infos);
                                (cours.findViewById(R.id.matiere)).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        more_infos.setVisibility(more_infos.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                                    }
                                });


                                (cours.findViewById(R.id.blacklistButton)).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                     /*if(name.contains("Informatique mobile"))
                                     {
                                         Snackbar snackbar =  Snackbar.make(findViewById(R.id.weekList), "This course can't be blacklisted", Snackbar.LENGTH_SHORT);
                                         snackbar.show();


                                     } else {*/
                                        for (int k = 0; k <= 100; k++) {
                                            Intent rem = new Intent(getBaseContext(), NotifReceiver.class);
                                            PendingIntent broadcast = PendingIntent.getBroadcast(getApplicationContext(), k, rem, 0);
                                            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                                            alarmManager.cancel(broadcast);
                                        }
                                        blacklist(name);
                                        show_week(null);
                                        Snackbar snackbar = Snackbar.make(findViewById(R.id.weekList), getString(R.string.course_blacklisted), Snackbar.LENGTH_SHORT);
                                        snackbar.setActionTextColor(0xffffffff);
                                        snackbar.setAction(getString(R.string.undo), new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                unblacklist(name);
                                                show_week(null);
                                            }
                                        });
                                        snackbar.show();
                                        //}
                                    }
                                });

                                final String deb = lesson.getString("start");
                                final int lessonDay = ((i + 1) % 7) + 1;
                                final boolean notify = sharedPref.getBoolean("switchnotif",true);
                                final boolean service = sharedPref.getBoolean("switchservice",false);
                                if(notify) {
                                    int toadd  = sharedPref.getInt("minutetoadd",0) * 60000;

                                    Calendar cal = Calendar.getInstance();
                                    Calendar toret = Calendar.getInstance();
                                    int hour = cal.get(Calendar.HOUR_OF_DAY);
                                    int min = cal.get(Calendar.MINUTE);
                                    int CurrentDay = cal.get(Calendar.DAY_OF_WEEK);
                                    String debh = deb.substring(0, 2);
                                    String debm = deb.substring(3, 5);
                                    int h = 3600000 * (Integer.parseInt(debh) - hour);
                                    int m = 60000 * (Integer.parseInt(debm) - min);
                                    int d = 86400000 * (lessonDay - CurrentDay);
                                    int totwait = h + d + m- toadd < 0 ? h + d + m-toadd + 604800000 : h + d + m-toadd;
                                    toret.add(Calendar.MILLISECOND, totwait);
                                    Intent notificationIntent = new Intent(getBaseContext(), NotifReceiver.class);
                                    notificationIntent.putExtra("nom", name);
                                    notificationIntent.putExtra("room", room2);
                                    notificationIntent.putExtra("service",service);
                                    notificationIntent.putExtra("start",start);
                                    notificationIntent.putExtra("end",end);
                                    PendingIntent broadcast = PendingIntent.getBroadcast(getApplicationContext(), id_notif, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, toret.getTimeInMillis(), broadcast);
                                    id_notif++;
                                }


                                weeklist_layout.addView(cours);
                                (cours.findViewById(R.id.notif)).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //SEB
                                        if(notify) {
                                            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                                            Calendar cal = Calendar.getInstance();
                                            cal.add(Calendar.SECOND,2);
                                            Intent notificationIntent = new Intent(getBaseContext(), NotifReceiver.class);
                                            notificationIntent.putExtra("nom", name);
                                            notificationIntent.putExtra("room", room2);
                                            notificationIntent.putExtra("start",start);
                                            notificationIntent.putExtra("end",end);
                                            PendingIntent broadcast = PendingIntent.getBroadcast(getApplicationContext(), id_notif, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                                            alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);
                                        }
                                    }
                                });
                            }


                        } catch (JSONException e) {
                            Log.i("JSON error", e.toString(), e);
                        }
                    }
                } catch (JSONException e) {
                    Log.i("JSON error", e.toString(), e);
                }
            }
        } catch (Exception e) {
            Log.i("request", e.toString(), e);
            Log.i("Error", e.toString(), e);
            TextView t = findViewById(R.id.weeklist_error);
            if (t != null)
                weeklist_layout.removeView(t);
            t = new TextView(this);
            t.setText(getString(R.string.error_calendar_message));
            t.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            t.setTextColor(0xffffffff);
            t.setBackground(new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors[1]));
            t.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            t.setTypeface(Typeface.DEFAULT_BOLD);
            t.setId(R.id.weeklist_error);
            weeklist_layout.addView(t, 0);
        }
        if (endPull) {
            Log.i("request", "pull off");
            //((SwipeRefreshLayout) findViewById(R.id.pullToRefresh)).setRefreshing(false);
            swipe.setRefreshing(false);
        }

        Log.i("request", "done");
    }

    public void show_week(View v) {
        show_week(v, false);
    }

    public void refreshHtml(String html) {
        this.html = html.replace("\r", "");
        sharedPref.edit().putString("json", null).commit();
        Log.i("request", "refreshHtml : " + html.length());

        weeklist_layout = findViewById(R.id.weekList);
        swipe = findViewById(R.id.pullToRefresh);
        Log.i("request", "truc : weeklist_layout : " + (weeklist_layout != null) + " | pull : " + (swipe != null));
        show_week(null, true);
    }

    public void failHtml(int code) {
        Log.i("request", "fail html : " + code);
        TextView t = findViewById(R.id.weeklist_error);
        if (t != null)
            weeklist_layout.removeView(t);
        t = new TextView(this);
        String[] message;
        if (code == 0)
            message = new String[]{getString(R.string.error_login_credentials_1), getString(R.string.error_login_credentials_2)};
        else if (code == 1)
            message = new String[]{getString(R.string.error_login_network_1), getString(R.string.error_login_network_2), getString(R.string.error_login_network_3)};
        else
            message = new String[]{getString(R.string.error_login_unknown_2), getString(R.string.error_login_unknown_2)};
        t.setText(TextUtils.join("\n", message));
        t.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        t.setTextColor(0xffffffff);
        t.setBackground(new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors[1]));
        t.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        t.setTypeface(Typeface.DEFAULT_BOLD);
        t.setId(R.id.weeklist_error);
        weeklist_layout.addView(t, 0);
        swipe.setRefreshing(false);
    }


    private Boolean dateCompare(String actualWeek, String startenddates) {
        String[] startandend = startenddates.split(" ");
        String start = startandend[0].split("\n")[0], end = startandend[1].split("\n")[0];

        if (start.contains(end))
            return actualWeek.contains(start);
        if (actualWeek.contains(start) || actualWeek.contains(end))
            return true;

        String startToCompare[][] = new String[][]{start.split("/"), actualWeek.split(" ")[0].split("/")};
        if (Integer.parseInt(startToCompare[0][2] + startToCompare[0][1] + startToCompare[0][0])
                > Integer.parseInt(startToCompare[1][2] + startToCompare[1][1] + startToCompare[1][0]))
            return false;
        String endToCompare[][] = new String[][]{end.split("/"), actualWeek.split(" ")[6].split("/")};
        return Integer.parseInt(endToCompare[0][2] + endToCompare[0][1] + endToCompare[0][0]) >= Integer.parseInt(endToCompare[1][2] + endToCompare[1][1] + endToCompare[1][0]);
    }

    //region blacklist
    private void blacklist(String lesson) {
        sharedPref.edit().putString("blacklisted", sharedPref.getString("blacklisted", "") + " #'" + lesson + "'#").commit();
    }

    private void unblacklist(String lesson) {
        sharedPref.edit().putString("blacklisted", sharedPref.getString("blacklisted", "").replace(" #'" + lesson + "'#", "")).commit();
    }

    private void resetBlacklist() {
        sharedPref.edit().putString("blacklisted", "").commit();
    }

    private boolean isBlacklisted(String lesson) {
        return sharedPref.getString("blacklisted", "").contains("#'" + lesson + "'#");
    }
    //endregion

    private void changeLanguage(String language) {
        Locale myLocale = new Locale(language);
        Resources res = getResources();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, res.getDisplayMetrics());
    }
}
