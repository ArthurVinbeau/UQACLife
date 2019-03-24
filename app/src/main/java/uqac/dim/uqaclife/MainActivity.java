package uqac.dim.uqaclife;


import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    Boolean hideEmptyDay = true;

    int[][] colors = new int[][]{
            new int[]{0xFFFFC107,0xFFFF9B00},       //mondayColors
            new int[]{ 0xFFFFEB3B,0xFFEBD827},      //tuesdayColors
            new int[]{ 0xFFF75A4E,0xFFF81D0D},      //wednesdayColors
            new int[]{ 0xFFD77EF5,0xFFBF0EDD},      //thurdsdayColors
            new int[]{ 0xFF5970E7,0xFF2040EC},      //fridayColors
            new int[]{ 0xFF4CE751,0xFF11A214},      //saturdayColors
            new int[]{ 0xFF47E1F5,0xFF0A8EA3}};    //sundayColors
    String[] days = new String[]{"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};


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
        setContentView(R.layout.activity_main);
        login = new Login();
        login.setQueue(Volley.newRequestQueue(this));

        // Example of a call to a native method
        //TextView tv = (TextView) findViewById(R.id.sample_text);
        //tv.setText(stringFromJNI());
    }

    public void versLInfini(View v) {
        setContentView(R.layout.activity_test);
    }

    public void etPasLAuDela(View v) {
        setContentView(R.layout.activity_main);
    }


    public void refreshweek(View v) {

       Parser parser = new Parser();
        JSONObject json = null;
        try {
            json = new JSONObject(parser.toJson(html));
        } catch (JSONException e){
            Log.i("JSON error",e.toString(),e);
        }

        LinearLayout dynamicContent = (LinearLayout) findViewById(R.id.weekList);
        int px = (int)(20* getApplicationContext().getResources().getDisplayMetrics().density+ 0.5f);

        for(int i = 0; i < 7 ; i++) {

            GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors[i]);




            try {
                JSONArray day = json.getJSONArray(days[i]);
                if(hideEmptyDay && day.length()>0) {
                    TextView t = new TextView(this);
                    LinearLayout.LayoutParams l = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    l.setMargins(0, px, 0, 0);
                    t.setLayoutParams(l);
                    t.setText(days[i]);
                    t.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    t.setTextColor(0xffffffff);
                    t.setBackground(gd);
                    t.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                    t.setTypeface(Typeface.DEFAULT_BOLD);
                    dynamicContent.addView(t);
                }
                for(int j = 0 ; j < day.length();j++) {
                    try {
                        View cours = getLayoutInflater().inflate(R.layout.cours, dynamicContent, false);
                        cours.findViewById(R.id.courstimes).setBackground(gd);
                        JSONObject lesson = day.getJSONObject(j);
                        ((TextView)cours.findViewById(R.id.lessonid)).setText(lesson.getString("id"));
                        ((TextView)cours.findViewById(R.id.lessonname)).setText(lesson.getString("name"));
                        ((TextView)cours.findViewById(R.id.lessonroom)).setText(lesson.getString("room"));
                        ((TextView)cours.findViewById(R.id.timestart)).setText(lesson.getString("start"));
                        ((TextView)cours.findViewById(R.id.timeend)).setText(lesson.getString("end"));
                        ((TextView)cours.findViewById(R.id.group)).setText(lesson.getString("grp"));        //groupe

                        dynamicContent.addView(cours);
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
        login.getCaptcha((ImageView) findViewById(R.id.container_captcha), (TextView) findViewById(R.id.debug));
    }

}
