package uqac.dim.uqaclife;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;


import android.content.res.Resources;

public class MainActivity extends AppCompatActivity {

    Boolean hideEmptyDay = true;
    Notification notification;

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

    String html;

    //region sethtml
    {
        html = "\n" +
                "\n" +
                "<h4>Hiver 2019</h4>\n" +
                "\n" +
                "<div class=\"card-columns\">\n" +
                "        <div class=\"card\">\n" +
                "            <div class=\"card-header p-1 rounded-0  font-weight-bold bg-horaire-1 text-white\">\n" +
                "                <span>4ETH236-01 </span>\n" +
                "                <span> - </span>\n" +
                "                <span> Éthique et informatique </span>\n" +
                "            </div>\n" +
                "            <div class=\"card-body p-1 text-small\">\n" +
                "                        <ul class=\"list-unstyled\">\n" +
                "                            <li>\n" +
                "                                    <span class=\"font-weight-bold\">Durée:</span>\n" +
                "                                    <span> Du </span>\n" +
                "                                    <span>lundi 07/01/2019</span>\n" +
                "                                    <span> au </span>\n" +
                "                                    <span>lundi 22/04/2019</span>\n" +
                "\n" +
                "                                \n" +
                "                            </li>\n" +
                "                            <li>\n" +
                "                                    <span class=\"font-weight-bold\">Heure:</span>\n" +
                "                                    <span> de </span>\n" +
                "                                    <span>08:00</span>\n" +
                "                                    <span> à </span>\n" +
                "                                    <span>10:45</span>\n" +
                "                            <li>\n" +
                "                                    <span class=\"font-weight-bold\">Local: </span>\n" +
                "                                    <span>H2-1090</span>\n" +
                "                            </li>\n" +
                "                            <li>\n" +
                "                            </li>\n" +
                "                        </ul>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "        <div class=\"card\">\n" +
                "            <div class=\"card-header p-1 rounded-0  font-weight-bold bg-horaire-2 text-white\">\n" +
                "                <span>6GEN720-02 </span>\n" +
                "                <span> - </span>\n" +
                "                <span> Réseaux d'ordinateurs</span>\n" +
                "            </div>\n" +
                "            <div class=\"card-body p-1 text-small\">\n" +
                "                        <ul class=\"list-unstyled\">\n" +
                "                            <li>\n" +
                "                                    <span class=\"font-weight-bold\">Durée:</span>\n" +
                "                                    <span> Du </span>\n" +
                "                                    <span>lundi 07/01/2019</span>\n" +
                "                                    <span> au </span>\n" +
                "                                    <span>lundi 22/04/2019</span>\n" +
                "\n" +
                "                                \n" +
                "                            </li>\n" +
                "                            <li>\n" +
                "                                    <span class=\"font-weight-bold\">Heure:</span>\n" +
                "                                    <span> de </span>\n" +
                "                                    <span>16:00</span>\n" +
                "                                    <span> à </span>\n" +
                "                                    <span>18:45</span>\n" +
                "                            <li>\n" +
                "                                    <span class=\"font-weight-bold\">Local: </span>\n" +
                "                                    <span>P2-1030</span>\n" +
                "                                        <span class=\"badge badge-info\" title=\"\">LAB</span>\n" +
                "                            </li>\n" +
                "                            <li>\n" +
                "                            </li>\n" +
                "                        </ul>\n" +
                "                        <ul class=\"list-unstyled\">\n" +
                "                            <li>\n" +
                "                                    <span class=\"font-weight-bold\">Durée:</span>\n" +
                "                                    <span> Du </span>\n" +
                "                                    <span>vendredi 11/01/2019</span>\n" +
                "                                    <span> au </span>\n" +
                "                                    <span>vendredi 26/04/2019</span>\n" +
                "\n" +
                "                                \n" +
                "                            </li>\n" +
                "                            <li>\n" +
                "                                    <span class=\"font-weight-bold\">Heure:</span>\n" +
                "                                    <span> de </span>\n" +
                "                                    <span>13:00</span>\n" +
                "                                    <span> à </span>\n" +
                "                                    <span>15:45</span>\n" +
                "                            <li>\n" +
                "                                    <span class=\"font-weight-bold\">Local: </span>\n" +
                "                                    <span>P2-4020</span>\n" +
                "                                        <span class=\"badge badge-info\" title=\"\">LAB</span>\n" +
                "                            </li>\n" +
                "                            <li>\n" +
                "                            </li>\n" +
                "                        </ul>\n" +
                "                        <ul class=\"list-unstyled\">\n" +
                "                            <li>\n" +
                "                                    <span class=\"font-weight-bold\">Durée:</span>\n" +
                "                                    <span> Du </span>\n" +
                "                                    <span>mardi 08/01/2019</span>\n" +
                "                                    <span> au </span>\n" +
                "                                    <span>mardi 12/02/2019</span>\n" +
                "\n" +
                "                                \n" +
                "                            </li>\n" +
                "                            <li>\n" +
                "                                    <span class=\"font-weight-bold\">Heure:</span>\n" +
                "                                    <span> de </span>\n" +
                "                                    <span>08:00</span>\n" +
                "                                    <span> à </span>\n" +
                "                                    <span>10:45</span>\n" +
                "                            <li>\n" +
                "                                    <span class=\"font-weight-bold\">Local: </span>\n" +
                "                                    <span>P2-1020</span>\n" +
                "                            </li>\n" +
                "                            <li>\n" +
                "                            </li>\n" +
                "                        </ul>\n" +
                "                        <ul class=\"list-unstyled\">\n" +
                "                            <li>\n" +
                "                                    <span class=\"font-weight-bold\">Durée:</span>\n" +
                "                                    <span> Du </span>\n" +
                "                                    <span>mardi 19/02/2019</span>\n" +
                "                                    <span> au </span>\n" +
                "                                    <span>mardi 19/02/2019</span>\n" +
                "\n" +
                "                                \n" +
                "                            </li>\n" +
                "                            <li>\n" +
                "                                    <span class=\"font-weight-bold\">Heure:</span>\n" +
                "                                    <span> de </span>\n" +
                "                                    <span>08:00</span>\n" +
                "                                    <span> à </span>\n" +
                "                                    <span>10:45</span>\n" +
                "                            <li>\n" +
                "                                    <span class=\"font-weight-bold\">Local: </span>\n" +
                "                                    <span>P1-6140</span>\n" +
                "                            </li>\n" +
                "                            <li>\n" +
                "                            </li>\n" +
                "                        </ul>\n" +
                "                        <ul class=\"list-unstyled\">\n" +
                "                            <li>\n" +
                "                                    <span class=\"font-weight-bold\">Durée:</span>\n" +
                "                                    <span> Du </span>\n" +
                "                                    <span>mardi 26/02/2019</span>\n" +
                "                                    <span> au </span>\n" +
                "                                    <span>mardi 23/04/2019</span>\n" +
                "\n" +
                "                                \n" +
                "                            </li>\n" +
                "                            <li>\n" +
                "                                    <span class=\"font-weight-bold\">Heure:</span>\n" +
                "                                    <span> de </span>\n" +
                "                                    <span>08:00</span>\n" +
                "                                    <span> à </span>\n" +
                "                                    <span>10:45</span>\n" +
                "                            <li>\n" +
                "                                    <span class=\"font-weight-bold\">Local: </span>\n" +
                "                                    <span>P2-1020</span>\n" +
                "                            </li>\n" +
                "                            <li>\n" +
                "                            </li>\n" +
                "                        </ul>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "        <div class=\"card\">\n" +
                "            <div class=\"card-header p-1 rounded-0  font-weight-bold bg-horaire-3 text-white\">\n" +
                "                <span>8INF257-12 </span>\n" +
                "                <span> - </span>\n" +
                "                <span> Informatique mobile</span>\n" +
                "            </div>\n" +
                "            <div class=\"card-body p-1 text-small\">\n" +
                "                        <ul class=\"list-unstyled\">\n" +
                "                            <li>\n" +
                "                                    <span class=\"font-weight-bold\">Durée:</span>\n" +
                "                                    <span> Du </span>\n" +
                "                                    <span>mercredi 09/01/2019</span>\n" +
                "                                    <span> au </span>\n" +
                "                                    <span>mercredi 24/04/2019</span>\n" +
                "\n" +
                "                                \n" +
                "                            </li>\n" +
                "                            <li>\n" +
                "                                    <span class=\"font-weight-bold\">Heure:</span>\n" +
                "                                    <span> de </span>\n" +
                "                                    <span>19:00</span>\n" +
                "                                    <span> à </span>\n" +
                "                                    <span>21:45</span>\n" +
                "                            <li>\n" +
                "                                    <span class=\"font-weight-bold\">Local: </span>\n" +
                "                                    <span>P1-4280</span>\n" +
                "                            </li>\n" +
                "                            <li>\n" +
                "                            </li>\n" +
                "                        </ul>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "        <div class=\"card\">\n" +
                "            <div class=\"card-header p-1 rounded-0  font-weight-bold bg-horaire-4 text-white\">\n" +
                "                <span>8INF433-01 </span>\n" +
                "                <span> - </span>\n" +
                "                <span> Algorithmique</span>\n" +
                "            </div>\n" +
                "            <div class=\"card-body p-1 text-small\">\n" +
                "                        <ul class=\"list-unstyled\">\n" +
                "                            <li>\n" +
                "                                    <span class=\"font-weight-bold\">Durée:</span>\n" +
                "                                    <span> Du </span>\n" +
                "                                    <span>mardi 08/01/2019</span>\n" +
                "                                    <span> au </span>\n" +
                "                                    <span>mardi 23/04/2019</span>\n" +
                "\n" +
                "                                \n" +
                "                            </li>\n" +
                "                            <li>\n" +
                "                                    <span class=\"font-weight-bold\">Heure:</span>\n" +
                "                                    <span> de </span>\n" +
                "                                    <span>11:00</span>\n" +
                "                                    <span> à </span>\n" +
                "                                    <span>12:15</span>\n" +
                "                            <li>\n" +
                "                                    <span class=\"font-weight-bold\">Local: </span>\n" +
                "                                    <span>P1-4270</span>\n" +
                "                            </li>\n" +
                "                            <li>\n" +
                "                            </li>\n" +
                "                        </ul>\n" +
                "                        <ul class=\"list-unstyled\">\n" +
                "                            <li>\n" +
                "                                    <span class=\"font-weight-bold\">Durée:</span>\n" +
                "                                    <span> Du </span>\n" +
                "                                    <span>jeudi 10/01/2019</span>\n" +
                "                                    <span> au </span>\n" +
                "                                    <span>jeudi 25/04/2019</span>\n" +
                "\n" +
                "                                \n" +
                "                            </li>\n" +
                "                            <li>\n" +
                "                                    <span class=\"font-weight-bold\">Heure:</span>\n" +
                "                                    <span> de </span>\n" +
                "                                    <span>13:00</span>\n" +
                "                                    <span> à </span>\n" +
                "                                    <span>14:15</span>\n" +
                "                            <li>\n" +
                "                                    <span class=\"font-weight-bold\">Local: </span>\n" +
                "                                    <span>P1-4270</span>\n" +
                "                            </li>\n" +
                "                            <li>\n" +
                "                            </li>\n" +
                "                        </ul>\n" +
                "                        <ul class=\"list-unstyled\">\n" +
                "                            <li>\n" +
                "                                    <span class=\"font-weight-bold\">Durée:</span>\n" +
                "                                    <span> Du </span>\n" +
                "                                    <span>jeudi 10/01/2019</span>\n" +
                "                                    <span> au </span>\n" +
                "                                    <span>jeudi 25/04/2019</span>\n" +
                "\n" +
                "                                \n" +
                "                            </li>\n" +
                "                            <li>\n" +
                "                                    <span class=\"font-weight-bold\">Heure:</span>\n" +
                "                                    <span> de </span>\n" +
                "                                    <span>14:30</span>\n" +
                "                                    <span> à </span>\n" +
                "                                    <span>15:45</span>\n" +
                "                            <li>\n" +
                "                                    <span class=\"font-weight-bold\">Local: </span>\n" +
                "                                    <span>P1-4270</span>\n" +
                "                                        <span class=\"badge badge-info\" title=\"\">T.D</span>\n" +
                "                            </li>\n" +
                "                            <li>\n" +
                "                            </li>\n" +
                "                        </ul>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "        <div class=\"card\">\n" +
                "            <div class=\"card-header p-1 rounded-0  font-weight-bold bg-horaire-5 text-white\">\n" +
                "                <span>8MAT122-01 </span>\n" +
                "                <span> - </span>\n" +
                "                <span> Structures discrètes</span>\n" +
                "            </div>\n" +
                "            <div class=\"card-body p-1 text-small\">\n" +
                "                        <ul class=\"list-unstyled\">\n" +
                "                            <li>\n" +
                "                                    <span class=\"font-weight-bold\">Durée:</span>\n" +
                "                                    <span> Du </span>\n" +
                "                                    <span>lundi 07/01/2019</span>\n" +
                "                                    <span> au </span>\n" +
                "                                    <span>lundi 22/04/2019</span>\n" +
                "\n" +
                "                                \n" +
                "                            </li>\n" +
                "                            <li>\n" +
                "                                    <span class=\"font-weight-bold\">Heure:</span>\n" +
                "                                    <span> de </span>\n" +
                "                                    <span>13:00</span>\n" +
                "                                    <span> à </span>\n" +
                "                                    <span>14:15</span>\n" +
                "                            <li>\n" +
                "                                    <span class=\"font-weight-bold\">Local: </span>\n" +
                "                                    <span>P1-4250</span>\n" +
                "                            </li>\n" +
                "                            <li>\n" +
                "                            </li>\n" +
                "                        </ul>\n" +
                "                        <ul class=\"list-unstyled\">\n" +
                "                            <li>\n" +
                "                                    <span class=\"font-weight-bold\">Durée:</span>\n" +
                "                                    <span> Du </span>\n" +
                "                                    <span>lundi 07/01/2019</span>\n" +
                "                                    <span> au </span>\n" +
                "                                    <span>lundi 22/04/2019</span>\n" +
                "\n" +
                "                                \n" +
                "                            </li>\n" +
                "                            <li>\n" +
                "                                    <span class=\"font-weight-bold\">Heure:</span>\n" +
                "                                    <span> de </span>\n" +
                "                                    <span>14:30</span>\n" +
                "                                    <span> à </span>\n" +
                "                                    <span>15:45</span>\n" +
                "                            <li>\n" +
                "                                    <span class=\"font-weight-bold\">Local: </span>\n" +
                "                                    <span>P1-4250</span>\n" +
                "                                        <span class=\"badge badge-info\" title=\"\">T.D</span>\n" +
                "                            </li>\n" +
                "                            <li>\n" +
                "                            </li>\n" +
                "                        </ul>\n" +
                "                        <ul class=\"list-unstyled\">\n" +
                "                            <li>\n" +
                "                                    <span class=\"font-weight-bold\">Durée:</span>\n" +
                "                                    <span> Du </span>\n" +
                "                                    <span>mercredi 09/01/2019</span>\n" +
                "                                    <span> au </span>\n" +
                "                                    <span>mercredi 24/04/2019</span>\n" +
                "\n" +
                "                                \n" +
                "                            </li>\n" +
                "                            <li>\n" +
                "                                    <span class=\"font-weight-bold\">Heure:</span>\n" +
                "                                    <span> de </span>\n" +
                "                                    <span>13:00</span>\n" +
                "                                    <span> à </span>\n" +
                "                                    <span>14:15</span>\n" +
                "                            <li>\n" +
                "                                    <span class=\"font-weight-bold\">Local: </span>\n" +
                "                                    <span>P1-4250</span>\n" +
                "                            </li>\n" +
                "                            <li>\n" +
                "                            </li>\n" +
                "                        </ul>\n" +
                "                        <ul class=\"list-unstyled\">\n" +
                "                            <li>\n" +
                "                                    <span class=\"font-weight-bold\">Durée:</span>\n" +
                "                                    <span> Du </span>\n" +
                "                                    <span>mercredi 09/01/2019</span>\n" +
                "                                    <span> au </span>\n" +
                "                                    <span>mercredi 24/04/2019</span>\n" +
                "\n" +
                "                                \n" +
                "                            </li>\n" +
                "                            <li>\n" +
                "                                    <span class=\"font-weight-bold\">Heure:</span>\n" +
                "                                    <span> de </span>\n" +
                "                                    <span>14:30</span>\n" +
                "                                    <span> à </span>\n" +
                "                                    <span>15:45</span>\n" +
                "                            <li>\n" +
                "                                    <span class=\"font-weight-bold\">Local: </span>\n" +
                "                                    <span>P1-4250</span>\n" +
                "                                        <span class=\"badge badge-info\" title=\"\">T.D</span>\n" +
                "                            </li>\n" +
                "                            <li>\n" +
                "                            </li>\n" +
                "                        </ul>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "</div>";
    }
    //endregion

    LoginActivity login;
    RequestQueue queue;

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
                intent = new Intent(getApplicationContext(), settingsActivity.class);
                intent.putExtra("requestCode", 42);
                startActivityForResult(intent, 42);
                return true;
            case R.id.blacklisted:
                intent = new Intent(getApplicationContext(), blacklisted_activity.class);
                intent.putExtra("requestCode", 42);
                startActivityForResult(intent, 42);
                return true;
            case R.id.grades_button:
                intent = new Intent(getApplicationContext(), gradesActivity.class);
                intent.putExtra("requestCode", 42);
                startActivityForResult(intent, 42);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref = getSharedPreferences(getResources().getString(R.string.preferences_file), MODE_PRIVATE);
        changeLanguage(sharedPref.getString("Language",""));
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        notification = new Notification(this);
        setContentView(R.layout.activity_test);
        weeklist_layout = findViewById(R.id.weekList);
        show_week(null);
        queue = Volley.newRequestQueue(this);


        ((SwipeRefreshLayout) findViewById(R.id.pullToRefresh)).setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                etPasLAuDela(null);
            }
        });

        // Example of a call to a native method
        //TextView tv = (TextView) findViewById(R.id.sample_text);
        //tv.setText(stringFromJNI());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 42) show_week(null);
    }

    public void versLInfini(View v) {
        //setContentView(R.layout.activity_test);
        login.finish();
        sharedPref.edit().putString("login", ((TextView) findViewById(R.id.login)).getText().toString()).commit();
        sharedPref.edit().putString("password", ((CheckBox) findViewById(R.id.save_password)).isChecked() ? ((TextView) findViewById(R.id.login)).getText().toString() : "").commit();
        //show_week(v);
    }

    public void etPasLAuDela(View v) {
        sharedPref.edit().putString("json", null).commit();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.putExtra("requestCode", 42);
        startActivityForResult(intent, 42);
    }


    public void refresh_week(View v) {
        //html = htmlRequest();
        show_week(v);
    }

    public void show_week(View v) {

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
                        t.setTextColor(0xffffffff);
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
                                String id = lesson.getString("id");
                                ((TextView) cours.findViewById(R.id.lessonid)).setText(id);
                                ((TextView) cours.findViewById(R.id.lessonname)).setText(name);
                                ((TextView) cours.findViewById(R.id.lessonroom)).setText(room);
                                ((TextView) cours.findViewById(R.id.timestart)).setText(lesson.getString("start"));
                                ((TextView) cours.findViewById(R.id.timeend)).setText(lesson.getString("end"));
                                ((TextView) cours.findViewById(R.id.group)).setText(getString(R.string.groupe)+ " : " + lesson.getString("grp"));        //groupe
                                final View more_infos = cours.findViewById(R.id.more_infos);
                                (cours.findViewById(R.id.matiere)).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        more_infos.setVisibility(more_infos.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                                    }
                                });
                                (cours.findViewById(R.id.notif)).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        NotificationCompat.Builder b = notification.getnotif(name, room2);
                                        notification.getManager().notify(new Random().nextInt(), b.build());
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
                                weeklist_layout.addView(cours);
                            }

                        } catch (JSONException e) {
                            Log.i("JSON error", e.toString(), e);
                        }
                    }
                } catch (JSONException e) {
                    Log.i("JSON error", e.toString(), e);
                }
            }
            ((SwipeRefreshLayout) findViewById(R.id.pullToRefresh)).setRefreshing(false);
        } catch (Exception e){
            Log.i("Error",e.toString(),e);
            TextView t = new TextView(this);
            t.setText(getString(R.string.error_calendar_message));
            t.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            t.setTextColor(0xffffffff);
            t.setBackground(new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors[1]));
            t.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            t.setTypeface(Typeface.DEFAULT_BOLD);
            weeklist_layout.addView(t);
        }

    }

    public void fleur(View v) {
        sharedPref.edit().putString("login", ((TextView)findViewById(R.id.login)).getText().toString()).commit();
        sharedPref.edit().putString("password", ((CheckBox)findViewById(R.id.save_password)).isChecked()? ((TextView)findViewById(R.id.login)).getText().toString() :"").commit();
        login.Login(((EditText) findViewById(R.id.login)).getText().toString(), ((EditText) findViewById(R.id.password)).getText().toString(), ((EditText) findViewById(R.id.captcha)).getText().toString());
    }

    public void truc(String html) {
        this.html = html;
        login.finish();
    }




    private Boolean dateCompare(String actualWeek, String startenddates){
        String[] startandend = startenddates.split(" ");
        String start = startandend[0].split("\n")[0] , end = startandend[1].split("\n")[0];

        if(start.contains(end))
            return actualWeek.contains(start);
        if(actualWeek.contains(start )|| actualWeek.contains(end))
            return true;

        String startToCompare[][] = new String[][]{start.split("/"),actualWeek.split(" ")[0].split("/")};
        if(Integer.parseInt(startToCompare[0][2]+startToCompare[0][1]+startToCompare[0][0])
                > Integer.parseInt(startToCompare[1][2]+startToCompare[1][1]+startToCompare[1][0]))
            return false;
        String endToCompare[][] = new String[][]{end.split("/"),actualWeek.split(" ")[6].split("/")};
        return Integer.parseInt(endToCompare[0][2] + endToCompare[0][1] + endToCompare[0][0]) >= Integer.parseInt(endToCompare[1][2] + endToCompare[1][1] + endToCompare[1][0]);
    }

    //region blacklist
    private void blacklist(String lesson){
        sharedPref.edit().putString("blacklisted", sharedPref.getString("blacklisted", "") + " #'" + lesson +"'#").commit();
    }

    private void unblacklist(String lesson){
        sharedPref.edit().putString("blacklisted", sharedPref.getString("blacklisted", "") .replace(" #'"+lesson+"'#","")).commit();
    }

    private void resetBlacklist(){
        sharedPref.edit().putString("blacklisted", "").commit();
    }

    private boolean isBlacklisted(String lesson){
        return sharedPref.getString("blacklisted", "").contains("#'"+ lesson+ "'#");
    }
    //endregion

    private void changeLanguage(String language){
        Locale myLocale = new Locale(language);
        Resources res = getResources();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf,res.getDisplayMetrics());
    }
}
