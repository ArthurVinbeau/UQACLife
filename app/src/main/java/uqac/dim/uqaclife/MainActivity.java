package uqac.dim.uqaclife;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

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

public class MainActivity extends AppCompatActivity {

    Boolean hideEmptyDay = true;    //supposed to be a parameter. Hides the empty days on calendar
    int id_notif = 1;


    //Defines the 2 colors used for the gradient for each day of the week
    int[][] colors = new int[][]{
            new int[]{0xFFFFC107, 0xFFFF9B00},           //mondayColors
            new int[]{0xFFF75A4E, 0xFFF81D0D},          // tuesdayColors
            new int[]{0xFFD77EF5, 0xFFBF0EDD},          //wednesdayColors
            new int[]{0xFF5970E7, 0xFF2040EC},          //thurdsdayColors
            new int[]{0xFF4CE751, 0xFF11A214},          //fridayColors
            new int[]{0xFF47E1F5, 0xFF0A8EA3},          //saturdayColors
            new int[]{0xFFFFEB3B, 0xFFEBD827}};        //sundayColors

    //defines the names of the days used in json
    String[] strdays = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

    //defines the id of the days in strings.xml to translate them easily
    int[] days = new int[]{
            R.string.monday,
            R.string.tuesday,
            R.string.wednesday,
            R.string.thursday,
            R.string.friday,
            R.string.saturday,
            R.string.sunday};

    //SharedPreferences to save Json and parameters values
    SharedPreferences sharedPref;

    //The LinearLayout in the scrollView of the activity
    LinearLayout weeklist_layout;

    //default html used for Debug
    String html = "";


    LoginActivity loginActivity;
    Login login;
    RequestQueue queue;
    public static MainActivity singleton;
    public int test = 0;

    SwipeRefreshLayout swipe;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }


    //Creates optionMenu on mainActivity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        if (b != null ) // RequestCode is set to 42 to each activity called except MainActivity
        {
            int requestCode = b.getInt("requestCode");
            if (requestCode == 42 || requestCode == 155 || requestCode == 156)
                return false;
        }
        getMenuInflater().inflate(R.menu.main_drop_menu, menu);
        return true;
    }


    // Open a new activity concording to the selected option
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.preferences:
                intent = new Intent(getApplicationContext(), SettingsActivity.class);
                intent.putExtra("requestCode", 42); //Prevent optionMenu creation on new Activities
                startActivityForResult(intent, 42);
                return true;
            case R.id.blacklisted:
                intent = new Intent(getApplicationContext(), BlacklistedActivity.class);
                intent.putExtra("requestCode", 42); //Prevent optionMenu creation on new Activities
                startActivityForResult(intent, 42);
                return true;
            case R.id.grades_button:
                intent = new Intent(getApplicationContext(), GradesActivity.class);
                intent.putExtra("requestCode", 42); //Prevent optionMenu creation on new Activities
                startActivityForResult(intent, 42);
                return true;
            case R.id.about:
                intent = new Intent(getApplicationContext(), AboutActivity.class);
                intent.putExtra("requestCode", 42); //Prevent optionMenu creation on new Activities
                startActivityForResult(intent, 42);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Design_Light);
        singleton = this;
        sharedPref = getSharedPreferences(getResources().getString(R.string.preferences_file), MODE_PRIVATE);        //Initialise sharePref according to a string in strings.xml
        changeLanguage(sharedPref.getString("Language", "fr"));     //Set current language. Default is French
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        weeklist_layout = findViewById(R.id.weekList); //Set weeklist as the LinearLayout in the scrollView
        swipe = findViewById(R.id.pullToRefresh);
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

        if (sharedPref.getString("json", null) == null)
            login.getSchedule();
    }

    public void stopService(View v)
    {
        Intent serviceIntent = new Intent(this,NotifService.class);
        stopService(serviceIntent);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        weeklist_layout = findViewById(R.id.weekList);
        swipe = findViewById(R.id.pullToRefresh);
        if (requestCode == 42) {
            invalidateOptionsMenu(); //Reload optionMenu to translate its strings
            show_week(null);
        } else if (requestCode == 155 && data != null) {
            html = data.getExtras().getString("html").replace("\r", "");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(findViewById(R.id.pullToRefresh) != null && findViewById(R.id.weekList) != null) {
            weeklist_layout = findViewById(R.id.weekList);
            swipe = findViewById(R.id.pullToRefresh);
            show_week(null, true);
        }

    }

    public void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("requestCode", 155);
        startActivityForResult(intent, 155);
    }

    //If schedule json is already saved, displays it else gets html and displays it
    public void show_week(View v, boolean endPull) {
        try {

            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-u"); //defined for dates comparaison
            String currentDate = dateFormat.format(new Date()); //current date
            int[] movingDate = new int[4]; //[dd;MM;yyyy;u] list of the current day to generate a string with every day of the week
            int i = 0;
            for (String inte : currentDate.split("-")) {
                movingDate[i++] = Integer.parseInt(inte);
            }

            while (movingDate[3]-- > 1) {      //while we did not reach monday
                movingDate[0]--;                            //  decrement day
                if (movingDate[0] == 0) {           //  if reached start of the month
                    movingDate[1]--;                        //      decrease month
                    if (movingDate[1] == 0) {       //      if reached start of the year
                        movingDate[2]--;                    //          decrease year
                        movingDate[1] = 12;             //          set month to 12 (decemnber)
                    }
                    if (movingDate[1] == 2)         //      Set the day according to the month of the year and bisexstile year
                        movingDate[0] = (movingDate[0] % 4 == 0 && movingDate[0] % 100 != 0 ? 29 : 28);
                    else if (movingDate[1] > 7 ? movingDate[1] % 2 == 0 : movingDate[1] == 1)
                        movingDate[0] = 30;
                    else
                        movingDate[0] = 31;
                }

            }

            //String of every day date in the week. Each date as dd-MM-yyyy and separed by a ' '
            // Recreate sthe string for monday date
            String currentweek = String.format("%02d", movingDate[0]) + "/" + String.format("%02d", movingDate[1]) + "/" + String.format("%02d", movingDate[2]);
            for (i = 1; i < 7; i++) { //increment the day for each day of the week and adds the date to current week
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


            Parser2 parser = new Parser2(); //Initialise a parser


            JSONObject json = null;                 //creates jsonObject that will be used to display schedule
            try {
                String savedJson = sharedPref.getString("json", null);  //Get saved json
                if (savedJson == null) {
                    json = new JSONObject(parser.toJson(html));             //Parse current html (usually changed by request)
                    sharedPref.edit().putString("json", json.toString()).commit(); //Save new Json
                } else {
                    json = new JSONObject(savedJson);                                       //Uses already saved json
                }
            } catch (JSONException e) {
                Log.i("JSON error", e.toString(), e);
            }

            weeklist_layout.removeAllViews(); //clear scrollView content
            int px = (int) (2 * getApplicationContext().getResources().getDisplayMetrics().density + 0.5f);      //calculate default padding values in pixel
            int px2 = (int) (15 * getApplicationContext().getResources().getDisplayMetrics().density + 0.5f);
            // reset notif
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent remintent = new Intent(getBaseContext(), NotifReceiver.class);
            for (int k = 1; k <= id_notif; k++) {
                PendingIntent broadcast = PendingIntent.getBroadcast(getApplicationContext(), k, remintent, 0);
                alarmManager.cancel(broadcast);
            }
            id_notif = 1;



            long nextTime = Long.MAX_VALUE;
            PendingIntent nextIntent = null;

            //i represents the day of the week
            for (i = 0; i < 7; i++) {

                //Creates 2 gradientDrawable with same colors
                //gd2 will have rounded corners so we can't use the same gradientDrawable
                GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors[i]);
                GradientDrawable gd2 = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors[i]);
                gd2.setCornerRadii(new float[]{px2, px2, 0, 0, 0, 0, px2, px2}); //set corners for gd2. Used in lessons creation


                try {
                    JSONArray day = json.getJSONArray(strdays[i]); //get the array corresponding to the proccessed day in json
                    if (!hideEmptyDay || day.length() > 0) {            //if the day has some lessons or we want to display empty days
                        TextView t = new TextView(this);       //Cretes textView
                        LinearLayout.LayoutParams l = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT); //Creates LayoutParams for the textView
                        t.setPadding(0, px, 0, px);         //Top and bottom padding
                        t.setLayoutParams(l);
                        t.setText(getString(days[i]));                      //get translated string in strings.xml (days[i] is the id of the string in strings.xml)
                        t.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        t.setTextColor(getColor(R.color.colorWhite));
                        t.setBackground(gd);                                            //Set gradient as background
                        t.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                        t.setTypeface(Typeface.DEFAULT_BOLD);
                        weeklist_layout.addView(t); //Add the TextView to week_list ScrollView
                    }

                    for (int j = 0; j < day.length(); j++) {   //Foreach lesson in the current day
                        try {

                            JSONObject lesson = day.getJSONObject(j);  //get lesson
                            final String name = lesson.getString("name");
                            if (!isBlacklisted(name) && dateCompare(currentweek, lesson.getString("dates"))) {          //if notBlacklisted and in the current week
                                View cours = getLayoutInflater().inflate(R.layout.cours, weeklist_layout, false);  //Creates cours layout
                                cours.findViewById(R.id.courstimes).setBackground(gd2);         //Set rounded corners background
                                String room = lesson.getString("room");                                 //Set room string
                                if (room.contains("T.D")) {                                                                            //Verify if the lesson is a T.D or a LAB and displays it
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
                                //Sets every information of the lesson
                                ((TextView) cours.findViewById(R.id.lessonid)).setText(id);
                                ((TextView) cours.findViewById(R.id.lessonname)).setText(name);
                                ((TextView) cours.findViewById(R.id.lessonroom)).setText(room);
                                ((TextView) cours.findViewById(R.id.timestart)).setText(start);
                                ((TextView) cours.findViewById(R.id.timeend)).setText(end);
                                ((TextView) cours.findViewById(R.id.group)).setText(getString(R.string.groupe)+ " : " + lesson.getString("grp"));        //groupe
                                final View more_infos = cours.findViewById(R.id.more_infos);
                                (cours.findViewById(R.id.matiere)).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) { //makes cours expandable with more info (OnClick)
                                        more_infos.setVisibility(more_infos.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                                    }
                                });


                                (cours.findViewById(R.id.blacklistButton)).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) { //Onclick on blacklist

                                        //Cancel notifications
                                        for (int k = 0; k <= 100; k++) {
                                            Intent rem = new Intent(getBaseContext(), NotifReceiver.class);
                                            PendingIntent broadcast = PendingIntent.getBroadcast(getApplicationContext(), k, rem, 0);
                                            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                                            alarmManager.cancel(broadcast);
                                        }

                                        blacklist(name);             //blacklist the lesson
                                        show_week(null);    //display week again
                                        Snackbar snackbar = Snackbar.make(findViewById(R.id.weekList), getString(R.string.course_blacklisted), Snackbar.LENGTH_SHORT); //Snackbar to undo blacklist
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
                                    int toadd = sharedPref.getInt("minutetoadd", 15) * 60000;

                                    Calendar cal = (Calendar) Calendar.getInstance().clone();
                                    Calendar toret = (Calendar) Calendar.getInstance().clone();
                                    int hour = cal.get(Calendar.HOUR_OF_DAY);
                                    int min = cal.get(Calendar.MINUTE);
                                    int CurrentDay = cal.get(Calendar.DAY_OF_WEEK);
                                    String debh = deb.substring(0, 2);
                                    String debm = deb.substring(3, 5);
                                    int h = 3600000 * (Integer.parseInt(debh) - hour);
                                    int m = 60000 * (Integer.parseInt(debm) - min);
                                    int d = 86400000 * (lessonDay - CurrentDay);
                                    int totwait = h + d + m - toadd < 0 ? h + d + m - toadd + 604800000 : h + d + m - toadd;
                                    toret.add(Calendar.MILLISECOND, totwait);
                                    if(totwait >6000) {
                                        Intent notificationIntent = new Intent(getBaseContext(), NotifReceiver.class);
                                        notificationIntent.putExtra("nom", name);
                                        notificationIntent.putExtra("room", room2);
                                        notificationIntent.putExtra("service", service);
                                        notificationIntent.putExtra("start", start);
                                        notificationIntent.putExtra("end", end);
                                        PendingIntent broadcast = PendingIntent.getBroadcast(getApplicationContext(), id_notif, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, toret.getTimeInMillis(), broadcast);
                                        id_notif++;
                                        if (nextTime > toret.getTimeInMillis()) {
                                            nextIntent = broadcast;
                                            nextTime = toret.getTimeInMillis();
                                        }
                                    }

                                }


                                //Adds lesson to the scrollView
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

            if (sharedPref.getBoolean("switchservice",false) && sharedPref.getBoolean("serviceonce",true)) {
                Calendar c = Calendar.getInstance();
                c.add(Calendar.SECOND, 0);
                sharedPref.edit().putBoolean("serviceonce",false).apply();
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), nextIntent);
            }
        } catch (Exception e) {
            //displays error if an error occured
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
            swipe.setRefreshing(false);
        }
    }

    public void show_week(View v) {
        show_week(v, false);
    }

    public void refreshHtml(String html) {
        this.html = html.replace("\r", "");
        sharedPref.edit().putString("json", null).commit();  //Reset saved json so it is not used and new HTML is parsed
        weeklist_layout = findViewById(R.id.weekList);
        swipe = findViewById(R.id.pullToRefresh);
        show_week(null, true);
    }

    public void failHtml(int code) {
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
            message = new String[]{getString(R.string.error_login_unknown_1), getString(R.string.error_login_unknown_2)};
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


    //return if actualweek (string of every day date in the week) is inside the startenddates period ("07-01-2019 22-04-2019" for example)
    private Boolean dateCompare(String actualWeek, String startenddates) {
        String[] startandend = startenddates.split(" ");        //separe startDate and endDate
        String start = startandend[0].split("\n")[0], end = startandend[1].split("\n")[0];

        if (start.contains(end))    //if period is only one day, compare if it is in currentweek
            return actualWeek.contains(start);
        if (actualWeek.contains(start) || actualWeek.contains(end))  //if the period starts or end during the week
            return true;

        String startToCompare[][] = new String[][]{start.split("/"), actualWeek.split(" ")[0].split("/")};
        if (Integer.parseInt(startToCompare[0][2] + startToCompare[0][1] + startToCompare[0][0])
                > Integer.parseInt(startToCompare[1][2] + startToCompare[1][1] + startToCompare[1][0]))   ///If start day is after monday
            return false;
        String endToCompare[][] = new String[][]{end.split("/"), actualWeek.split(" ")[6].split("/")}; //if endday is before sunday
        return Integer.parseInt(endToCompare[0][2] + endToCompare[0][1] + endToCompare[0][0]) >= Integer.parseInt(endToCompare[1][2] + endToCompare[1][1] + endToCompare[1][0]);
    }

    //region blacklist

    //Blacklist the lesson with name lesson by adding it to SharedPref
    private void blacklist(String lesson) {
        sharedPref.edit().putString("blacklisted", sharedPref.getString("blacklisted", "") + " #'" + lesson + "'#").commit();
    }

    //Unblacklist the lesson with name lesson by adding it to SharedPref
    private void unblacklist(String lesson) {
        sharedPref.edit().putString("blacklisted", sharedPref.getString("blacklisted", "").replace(" #'" + lesson + "'#", "")).commit();
    }

    //Return if lesson is in blacklist SharedPref
    private boolean isBlacklisted(String lesson) {
        return sharedPref.getString("blacklisted", "").contains("#'" + lesson + "'#");
    }
    //endregion

    //Set the language to languageAbreviation file
    private void changeLanguage(String languageAbreviation) {
        Locale myLocale = new Locale(languageAbreviation);
        Resources res = getResources();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, res.getDisplayMetrics());
    }
}