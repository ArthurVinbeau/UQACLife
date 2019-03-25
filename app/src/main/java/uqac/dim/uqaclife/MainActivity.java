package uqac.dim.uqaclife;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Boolean hideEmptyDay = true;

    int[][] colors = new int[][]{
            new int[]{0xFFFFC107,0xFFFF9B00},           //mondayColors
            new int[]{ 0xFFF75A4E,0xFFF81D0D},          // tuesdayColors
            new int[]{ 0xFFD77EF5,0xFFBF0EDD},          //wednesdayColors
            new int[]{ 0xFF5970E7,0xFF2040EC},          //thurdsdayColors
            new int[]{ 0xFF4CE751,0xFF11A214},          //fridayColors
            new int[]{ 0xFF47E1F5,0xFF0A8EA3},          //saturdayColors
            new int[]{ 0xFFFFEB3B,0xFFEBD827}};        //sundayColors
    String[] days = new String[]{"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};

    SharedPreferences sharedPref;

    String html;
    {
        html ="\t      <FONT SIZE=\"-1\"><B>Toute la journée</B></FONT>\n" +
                "\t    </TD>\n" +
                "<TD ALIGN=\"CENTER\"  HEIGHT=\"17\">&nbsp;</TD><TD ALIGN=\"CENTER\"  HEIGHT=\"17\">&nbsp;</TD><TD ALIGN=\"CENTER\"  HEIGHT=\"17\">&nbsp;</TD><TD ALIGN=\"CENTER\"  HEIGHT=\"17\">&nbsp;</TD><TD ALIGN=\"CENTER\"  HEIGHT=\"17\">&nbsp;</TD><TD ALIGN=\"CENTER\"  HEIGHT=\"17\">&nbsp;</TD><TD ALIGN=\"CENTER\"  HEIGHT=\"17\">&nbsp;</TD><TD ALIGN=\"CENTER\"  HEIGHT=\"17\">&nbsp;</TD>\t</TABLE><br><br>\n" +
                "<hr><b>4ETH236-01 - Éthique et informatique</b>\n" +
                "<UL style=\"list-style-type: none;\"><LI><b>Durée:</b> Du Lundi 07-01-2019 au Lundi 22-04-2019<li><b>Heure:</b> de 08:00 à 10:45</li><li><b> Local:</b> H2-1090&nbsp;</li></UL><hr><b>6GEN720-02 - Réseaux d'ordinateurs</b>\n" +
                "<UL style=\"list-style-type: none;\"><LI><b>Durée:</b> Du Mardi 08-01-2019 au Mardi 12-02-2019<li><b>Heure:</b> de 08:00 à 10:45</li><li><b> Local:</b> P2-1020&nbsp;</li></UL><UL style=\"list-style-type: none;\"><LI><b>Durée:</b> Du Mardi 26-02-2019 au Mardi 23-04-2019<li><b>Heure:</b> de 08:00 à 10:45</li><li><b> Local:</b> P2-1020&nbsp;</li></UL><UL style=\"list-style-type: none;\"><LI><b>Durée:</b> Du Lundi 07-01-2019 au Lundi 22-04-2019<li><b>Heure:</b> de 16:00 à 18:45</li><li><b> Local:</b> P2-1030 (LAB)</li></UL><UL style=\"list-style-type: none;\"><LI><b>Durée:</b> Le Mardi 19-02-2019<li><b>Heure:</b> de 08:00 à 10:45</li><li><b> Local:</b> P1-6140&nbsp;</li></UL><UL style=\"list-style-type: none;\"><LI><b>Durée:</b> Du Vendredi 11-01-2019 au Vendredi 26-04-2019<li><b>Heure:</b> de 13:00 à 15:45</li><li><b> Local:</b> P2-4020 (LAB)</li></UL><hr><b>8INF257-12 - Informatique mobile</b>\n" +
                "<UL style=\"list-style-type: none;\"><LI><b>Durée:</b> Du Mercredi 09-01-2019 au Mercredi 24-04-2019<li><b>Heure:</b> de 19:00 à 21:45</li><li><b> Local:</b> P1-4280&nbsp;</li></UL><hr><b>8INF433-01 - Algorithmique</b>\n" +
                "<UL style=\"list-style-type: none;\"><LI><b>Durée:</b> Du Jeudi 10-01-2019 au Jeudi 25-04-2019<li><b>Heure:</b> de 13:00 à 14:15</li><li><b> Local:</b> P1-4270&nbsp;</li></UL><UL style=\"list-style-type: none;\"><LI><b>Durée:</b> Du Mardi 08-01-2019 au Mardi 23-04-2019<li><b>Heure:</b> de 11:00 à 12:15</li><li><b> Local:</b> P1-4270&nbsp;</li></UL><UL style=\"list-style-type: none;\"><LI><b>Durée:</b> Du Jeudi 10-01-2019 au Jeudi 25-04-2019<li><b>Heure:</b> de 14:30 à 15:45</li><li><b> Local:</b> P1-4270 (T.D)</li></UL><hr><b>8MAT122-01 - Structures discrètes</b>\n" +
                "<UL style=\"list-style-type: none;\"><LI><b>Durée:</b> Du Lundi 07-01-2019 au Lundi 22-04-2019<li><b>Heure:</b> de 13:00 à 14:15</li><li><b> Local:</b> P1-4250&nbsp;</li></UL><UL style=\"list-style-type: none;\"><LI><b>Durée:</b> Du Mercredi 09-01-2019 au Mercredi 24-04-2019<li><b>Heure:</b> de 13:00 à 14:15</li><li><b> Local:</b> P1-4250&nbsp;</li></UL><UL style=\"list-style-type: none;\"><LI><b>Durée:</b> Du Lundi 07-01-2019 au Lundi 22-04-2019<li><b>Heure:</b> de 14:30 à 15:45</li><li><b> Local:</b> P1-4250 (T.D)</li></UL><UL style=\"list-style-type: none;\"><LI><b>Durée:</b> Du Mercredi 09-01-2019 au Mercredi 24-04-2019<li><b>Heure:</b> de 14:30 à 15:45</li><li><b> Local:</b> P1-4250 (T.D)</li></UL>\n" +
                "\t</div>\n" +
                "\t<!-- fin contenu centrale -->\t\n" +
                "\n" ;}

    Login login;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        show_week(null);
        login = new Login();
        login.setQueue(Volley.newRequestQueue(this), this);


        // Example of a call to a native method
        //TextView tv = (TextView) findViewById(R.id.sample_text);
        //tv.setText(stringFromJNI());
    }

    public void versLInfini(View v) {
        setContentView(R.layout.activity_test);
        show_week(v);
    }

    public void etPasLAuDela(View v) {
        sharedPref.edit().putString("json", null).commit();
        setContentView(R.layout.activity_main);
        //Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        //startActivity(intent);
        login.getCaptcha((NetworkImageView) findViewById(R.id.container_captcha));
    }


    public void refresh_week(View v){
        //html = htmlRequest();
        show_week(v);
    }

    public void show_week(View v) {
        sharedPref = getSharedPreferences(getResources().getString(R.string.preferences_file), MODE_PRIVATE);

        DateFormat dateFormat= new SimpleDateFormat("dd-MM-yyyy-u");
        String currentDate  = dateFormat.format(new Date());
        int[] movingDate = new int[4];
        int i = 0;
        for (String inte: currentDate.split("-")) {
            movingDate[i++] = Integer.parseInt(inte);
        }

        while(movingDate[3]-- > 1){
            movingDate[0]--;
            if(movingDate[0] == 0){
                movingDate[1]--;
                if(movingDate[1]== 0)
                {
                    movingDate[2]--;
                    movingDate[1] = 12;
                }
                if(movingDate[1] == 2)
                    movingDate[0] = (movingDate[0]%4 == 0 && movingDate[0]%100 != 0?29:28);
                else if(movingDate[1]>7 ? movingDate[1]%2 == 0 : movingDate[1]==1)
                    movingDate[0] = 30;
                else
                    movingDate[0] = 31;
            }
        }
        String currentweek =  String.format("%02d",movingDate[0]) + "-" + String.format("%02d", movingDate[1]) +"-" + String.format("%02d",movingDate[2]);
        for ( i = 1 ; i < 7; i++) {
            movingDate[0]++;
            if(movingDate[0]==32) {
                movingDate[0] = 1;
                movingDate[1]++;
            } else if(movingDate[0] == 31 && (movingDate[1]>7 ? movingDate[1]%2 == 0 : movingDate[1]==1)) {
                movingDate[0] = 1;
                movingDate[1]++;
            }
            else  if(movingDate[1] ==2 && movingDate[0] > 28) {
                if (movingDate[0] == 30) {
                    movingDate[0] = 1;
                    movingDate[1]++;
                } else if (!(movingDate[2]%4 == 0 && movingDate[2]%100 != 0)){
                    movingDate[0] = 1;
                    movingDate[1]++;
                }
            }

            currentweek += " " + movingDate[0] + "-" + String.format("%02d", movingDate[1]) +"-" + String.format("%02d",movingDate[2]);
        }

       Parser parser = new Parser();
        JSONObject json = null;
        try {
            String savedJson = sharedPref.getString("json", null);
            if(savedJson==null)
            {
                Log.i("TEST","ToParser");
                json = new JSONObject(parser.toJson(html));
            }
            else
            {
                Log.i("TEST","NOT");
                json = new JSONObject(savedJson);
            }
            sharedPref.edit().putString("json", json.toString()).commit();
        } catch (JSONException e){
            Log.i("JSON error",e.toString(),e);
        }


        LinearLayout dynamicContent = (LinearLayout) findViewById(R.id.weekList);
        dynamicContent.removeAllViews();
        int px = (int)(2* getApplicationContext().getResources().getDisplayMetrics().density+ 0.5f);
        int px2 = (int)(15* getApplicationContext().getResources().getDisplayMetrics().density+ 0.5f);

        for( i = 0; i < 7 ; i++) {

            GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors[i]);
            GradientDrawable gd2 = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors[i]);


            try {
                JSONArray day = json.getJSONArray(days[i]);
                if(!hideEmptyDay ||day.length()>0) {
                    TextView t = new TextView(this);
                    LinearLayout.LayoutParams l = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    t.setPadding(0, px, 0, px);
                    t.setLayoutParams(l);
                    t.setText(days[i]);
                    t.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    t.setTextColor(0xffffffff);
                    t.setBackground(gd);
                    t.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                    t.setTypeface(Typeface.DEFAULT_BOLD);
                    dynamicContent.addView(t);
                }
                gd2.setCornerRadii(new float[]{px2,px2,0,0,0,0,px2,px2});
                for(int j = 0 ; j < day.length();j++) {
                    try {

                        JSONObject lesson = day.getJSONObject(j);
                         if(dateCompare(currentweek,lesson.getString("dates"))) {
                            View cours = getLayoutInflater().inflate(R.layout.cours, dynamicContent, false);
                            cours.findViewById(R.id.courstimes).setBackground(gd2);
                            String room = lesson.getString("room");
                            if(room.contains("T.D"))
                            {
                                cours.findViewById(R.id.TD).setVisibility(View.VISIBLE);
                                room = room.replace(" (T.D)","");
                            }
                            String id = lesson.getString("id");
                            ((TextView) cours.findViewById(R.id.lessonid)).setText(id);
                            ((TextView) cours.findViewById(R.id.lessonname)).setText(lesson.getString("name"));
                            ((TextView) cours.findViewById(R.id.lessonroom)).setText(room);
                            ((TextView) cours.findViewById(R.id.timestart)).setText(lesson.getString("start"));
                            ((TextView) cours.findViewById(R.id.timeend)).setText(lesson.getString("end"));
                            ((TextView) cours.findViewById(R.id.group)).setText("Groupe : " + lesson.getString("grp"));        //groupe
                             final View more_infos = cours.findViewById(R.id.more_infos);
                             (cours.findViewById(R.id.matiere)).setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View v) {
                                     more_infos.setVisibility(more_infos.getVisibility()==View.VISIBLE?View.GONE:View.VISIBLE);
                                 }
                             });
                             (cours.findViewById(R.id.blacklistButton)).setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View v) {
                                     /*SharedPreferences sharedPref = MainActivity.this().getPreferences(getApplicationContext().MODE_PRIVATE);
                                     sharedPref.getString("blacklisted")
                                     SharedPreferences.Editor editor = sharedPref.edit();

                                     editor.putString("blacklisted", " " +  id);
                                     editor.commit();*/
                                     Log.i("Blacklisted","clicked");
                                 }
                             });
                            dynamicContent.addView(cours);
                        }

                    }catch (JSONException e){
                        Log.i("JSON error",e.toString(),e);
                    }
                }
            }catch (JSONException e){
                Log.i("JSON error",e.toString(),e);
            }
        }
    }

    public void fleur(View v) {
        login.Login(((EditText) findViewById(R.id.login)).getText().toString(), ((EditText) findViewById(R.id.password)).getText().toString(), ((EditText) findViewById(R.id.captcha)).getText().toString(), (TextView) findViewById(R.id.debug));
    }

    public void truc(String html) {
        this.html = html;
        setContentView(R.layout.activity_test);
        show_week(null);
    }


    private Boolean dateCompare(String actualWeek, String startenddates){
        String[] startandend = startenddates.split(" ");
        String start = startandend[0] , end = startandend[1];

        if(start.contains(end))
            return actualWeek.contains(start);
        if(actualWeek.contains(start )|| actualWeek.contains(end))
            return true;

        String startToCompare[][] = new String[][]{start.split("-"),actualWeek.split(" ")[0].split("-")};
        if(Integer.parseInt(startToCompare[0][2]+startToCompare[0][1]+startToCompare[0][0])
                > Integer.parseInt(startToCompare[1][2]+startToCompare[1][1]+startToCompare[1][0]))
            return false;
        String endToCompare[][] = new String[][]{end.split("-"),actualWeek.split(" ")[6].split("-")};
        if(Integer.parseInt(endToCompare[0][2]+endToCompare[0][1]+endToCompare[0][0])
                < Integer.parseInt(endToCompare[1][2]+endToCompare[1][1]+endToCompare[1][0]))
            return false;
        return true;
    }


}
