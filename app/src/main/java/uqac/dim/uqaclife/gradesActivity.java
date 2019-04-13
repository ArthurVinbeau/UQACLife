package uqac.dim.uqaclife;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class gradesActivity extends MainActivity {
    int[][] colors = new int[][]{
            new int[]{0xFFFFC107, 0xFFFF9B00},
            new int[]{0xFFF75A4E, 0xFFF81D0D},
            new int[]{0xFFD77EF5, 0xFFBF0EDD},
            new int[]{0xFF5970E7, 0xFF2040EC},
            new int[]{0xFF4CE751, 0xFF11A214},
            new int[]{0xFF47E1F5, 0xFF0A8EA3},
            new int[]{0xFFFFEB3B, 0xFFEBD827}};
    SharedPreferences sharedPreferences;
    List<String> lessonshtml = new ArrayList<>();
    LinearLayout grades_scroll;
    int displayed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grades);
        grades_scroll = (LinearLayout)findViewById(R.id.grades_scroll);
        grades_scroll.removeAllViews();
        sharedPref = getSharedPreferences(getResources().getString(R.string.preferences_file), MODE_PRIVATE);
        //region sethtml
        lessonshtml.add("\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "    <div class=\"mt-2\">\n" +
                "    </div>\n" +
                "\n" +
                "<div class=\"d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3\">\n" +
                "    <h1 class=\"h2\">\n" +
                "        Éthique et informatique \n" +
                "\n" +
                "                    <span class=\"badge badge-secondary bg-warning\">&#xC0; venir</span>\n" +
                "    </h1>    \n" +
                "        <div class=\"mr-1\">\n" +
                "                <a class=\"btn btn-sm btn-outline-secondary bg-success text-white\" target=\"_blank\" href=\"/EtudiantApp/Trimestre/20191/Moodle/4ETH236\">Moodle</a>                \n" +
                "    </div>    \n" +
                "</div>\n" +
                "\n" +
                "<div class=\"row\">\n" +
                "    <div class=\"col\">\n" +
                "        <div class=\"row\">\n" +
                "            <div class=\"col-md-6 mb-3\">\n" +
                "                    <label class=\"font-weight-bold\" for=\"enseignant\">Enseignant(s)</label>\n" +
                "                    <p id=\"enseignant\">Jean-pierre Beland</p>\n" +
                "                            </div>\n" +
                "            <div class=\"col-md-6 mb-3\">\n" +
                "                    <label class=\"font-weight-bold\" for=\"programme\">Programme d'études</label>\n" +
                "                    <p id=\"programme\">Programme court de premier cycle en informatique pour &#xE9;tudiants en s&#xE9;jour d&#x27;&#xE9;tudes (0711)</p>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "            <div class=\"row\">\n" +
                "\n" +
                "                <div class=\"col-md-6 mb-3\">\n" +
                "\n" +
                "                    <label class=\"font-weight-bold\" for=\"horaire\">Horaire</label>\n" +
                "                        <p> du Monday 1/7/2019 au Monday 4/22/2019  de 8:00 AM &#xE0; 10:45 AM <span class=\"d-md-none\"> local H2-1090</span></p>\n" +
                "                </div>\n" +
                "                <div class=\"col-md-6 mb-3 d-none d-md-block\">\n" +
                "                    <label class=\"font-weight-bold\" for=\"local\">Local</label>\n" +
                "                        <p>H2-1090</p>\n" +
                "                </div>\n" +
                "            </div>\n" +
                "    </div>\n" +
                "</div>\n" +
                "<div>\n" +
                "\n" +
                "            \n" +
                "<div class=\"d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3\">\n" +
                "    <h4>Résultats académiques</h4>\n" +
                "</div>\n" +
                "<div class=\"table-responsive\">\n" +
                "    <table class=\"table table-sm\">\n" +
                "        <thead>\n" +
                "            <tr>\n" +
                "                <th>Éléments</th>\n" +
                "                <th>Non pondérés</th>\n" +
                "                <th>Pondérés</th>\n" +
                "            </tr>\n" +
                "        </thead>\n" +
                "        <tbody>\n" +
                "                <tr class=\"\">\n" +
                "                    <td>Travail 1 en classe</td>\n" +
                "                    <td>5.00/5.00</td>\n" +
                "                    <td>5.00/5.00</td>\n" +
                "                </tr>\n" +
                "                <tr class=\"\">\n" +
                "                    <td>Travail 2 en classe</td>\n" +
                "                    <td>5.00/5.00</td>\n" +
                "                    <td>5.00/5.00</td>\n" +
                "                </tr>\n" +
                "                <tr class=\"\">\n" +
                "                    <td>Travail Maison 1</td>\n" +
                "                    <td>6.50/10.00</td>\n" +
                "                    <td>6.50/10.00</td>\n" +
                "                </tr>\n" +
                "                <tr class=\"\">\n" +
                "                    <td>Travail Maison 2</td>\n" +
                "                    <td>18.00/20.00</td>\n" +
                "                    <td>18.00/20.00</td>\n" +
                "                </tr>\n" +
                "                <tr class=\"\">\n" +
                "                    <td>Travail 3 en classe</td>\n" +
                "                    <td>5.00/5.00</td>\n" +
                "                    <td>5.00/5.00</td>\n" +
                "                </tr>\n" +
                "                <tr class=\"\">\n" +
                "                    <td>Travail 4 en classe </td>\n" +
                "                    <td>7.50/10.00</td>\n" +
                "                    <td>7.50/10.00</td>\n" +
                "                </tr>\n" +
                "                <tr class=\"\">\n" +
                "                    <td>Travail 5 en clase</td>\n" +
                "                    <td>5.00/5.00</td>\n" +
                "                    <td>5.00/5.00</td>\n" +
                "                </tr>\n" +
                "        </tbody>\n" +
                "    </table>\n" +
                "</div>\n" +
                "<div class=\"card float-right\">\n" +
                "    <div class=\"card-header p-1 font-weight-bold\">Total</div>\n" +
                "\n" +
                "        <div class=\"card-body p-1 \">52.00/60.00</div>\n" +
                "\n" +
                "</div>\n" +
                "<div class=\"clearfix\"></div>\n" +
                "\n" +
                "</div>\n" +
                "\n");
        lessonshtml.add("\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "    <div class=\"mt-2\">\n" +
                "    </div>\n" +
                "\n" +
                "<div class=\"d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3\">\n" +
                "    <h1 class=\"h2\">\n" +
                "        Réseaux d'ordinateurs\n" +
                "\n" +
                "                    <span class=\"badge badge-secondary bg-warning\">&#xC0; venir</span>\n" +
                "    </h1>    \n" +
                "        <div class=\"mr-1\">\n" +
                "                <a class=\"btn btn-sm btn-outline-secondary bg-success text-white\" target=\"_blank\" href=\"/EtudiantApp/Trimestre/20191/Moodle/6GEN720\">Moodle</a>                \n" +
                "    </div>    \n" +
                "</div>\n" +
                "\n" +
                "<div class=\"row\">\n" +
                "    <div class=\"col\">\n" +
                "        <div class=\"row\">\n" +
                "            <div class=\"col-md-6 mb-3\">\n" +
                "                    <label class=\"font-weight-bold\" for=\"enseignant\">Enseignant(s)</label>\n" +
                "                    <p id=\"enseignant\">Daniel Audet</p>\n" +
                "                            </div>\n" +
                "            <div class=\"col-md-6 mb-3\">\n" +
                "                    <label class=\"font-weight-bold\" for=\"programme\">Programme d'études</label>\n" +
                "                    <p id=\"programme\">Programme court de premier cycle en informatique pour &#xE9;tudiants en s&#xE9;jour d&#x27;&#xE9;tudes (0711)</p>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "            <div class=\"row\">\n" +
                "\n" +
                "                <div class=\"col-md-6 mb-3\">\n" +
                "\n" +
                "                    <label class=\"font-weight-bold\" for=\"horaire\">Horaire</label>\n" +
                "                        <p> du Monday 1/7/2019 au Monday 4/22/2019  de 4:00 PM &#xE0; 6:45 PM <span class=\"d-md-none\"> local P2-1030</span></p>\n" +
                "                        <p> du Friday 1/11/2019 au Friday 4/26/2019  de 1:00 PM &#xE0; 3:45 PM <span class=\"d-md-none\"> local P2-4020</span></p>\n" +
                "                        <p> du Tuesday 1/8/2019 au Tuesday 2/12/2019  de 8:00 AM &#xE0; 10:45 AM <span class=\"d-md-none\"> local P2-1020</span></p>\n" +
                "                        <p> du Tuesday 2/19/2019 au Tuesday 2/19/2019  de 8:00 AM &#xE0; 10:45 AM <span class=\"d-md-none\"> local P1-6140</span></p>\n" +
                "                        <p> du Tuesday 2/26/2019 au Tuesday 4/23/2019  de 8:00 AM &#xE0; 10:45 AM <span class=\"d-md-none\"> local P2-1020</span></p>\n" +
                "                </div>\n" +
                "                <div class=\"col-md-6 mb-3 d-none d-md-block\">\n" +
                "                    <label class=\"font-weight-bold\" for=\"local\">Local</label>\n" +
                "                        <p>P2-1030</p>\n" +
                "                        <p>P2-4020</p>\n" +
                "                        <p>P2-1020</p>\n" +
                "                        <p>P1-6140</p>\n" +
                "                        <p>P2-1020</p>\n" +
                "                </div>\n" +
                "            </div>\n" +
                "    </div>\n" +
                "</div>\n" +
                "<div>\n" +
                "\n" +
                "            \n" +
                "<div class=\"d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3\">\n" +
                "    <h4>Résultats académiques</h4>\n" +
                "</div>\n" +
                "<div class=\"table-responsive\">\n" +
                "    <table class=\"table table-sm\">\n" +
                "        <thead>\n" +
                "            <tr>\n" +
                "                <th>Éléments</th>\n" +
                "                <th>Non pondérés</th>\n" +
                "                <th>Pondérés</th>\n" +
                "            </tr>\n" +
                "        </thead>\n" +
                "        <tbody>\n" +
                "                <tr class=\"\">\n" +
                "                    <td>Examen partiel</td>\n" +
                "                    <td>74.50/100.00</td>\n" +
                "                    <td>18.62/25.00</td>\n" +
                "                </tr>\n" +
                "                <tr class=\"\">\n" +
                "                    <td>Travail 1</td>\n" +
                "                    <td>20.00/20.00</td>\n" +
                "                    <td>5.00/5.00</td>\n" +
                "                </tr>\n" +
                "                <tr class=\"\">\n" +
                "                    <td>Travail 2</td>\n" +
                "                    <td>10.00/10.00</td>\n" +
                "                    <td>2.00/2.00</td>\n" +
                "                </tr>\n" +
                "                <tr class=\"\">\n" +
                "                    <td>Travail 3</td>\n" +
                "                    <td>15.00/20.00</td>\n" +
                "                    <td>4.50/6.00</td>\n" +
                "                </tr>\n" +
                "        </tbody>\n" +
                "    </table>\n" +
                "</div>\n" +
                "<div class=\"card float-right\">\n" +
                "    <div class=\"card-header p-1 font-weight-bold\">Total</div>\n" +
                "\n" +
                "        <div class=\"card-body p-1 \">30.12/38.00</div>\n" +
                "\n" +
                "</div>\n" +
                "<div class=\"clearfix\"></div>\n" +
                "\n" +
                "</div>\n" +
                "\n");

                //endregion
        displayed = 0;
        for (String lesson:lessonshtml) {
            showGrades(lesson);
        }
    }

    /*private void showGrades(String json){
        grades_scroll.removeAllViews();
        try {
            JSONObject jsonObject = new JSONObject(json);

        }
        catch (JSONException e)
        {
            Log.i("Json Error",e.toString(),e);
            TextView t = new TextView(this);
            t.setText(getString(R.string.error_calendar_message));
            t.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            t.setTextColor(0xffffffff);
            t.setBackground(new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{0xFFF75A4E, 0xFFF81D0D}));
            t.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            t.setTypeface(Typeface.DEFAULT_BOLD);
            grades_scroll.addView(t);
        }
    }*/

    private Boolean showGrades(String html) {
        try {
            TextView lessonName = new TextView(this);
            lessonName.setText(html.split("<h1 class=\"h2\">")[1].split("</h1>")[0].split("\n")[1]);
            lessonName.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            lessonName.setTextColor(0xffffffff);
            lessonName.setBackground(new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors[displayed++]));
            lessonName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            lessonName.setTypeface(Typeface.DEFAULT_BOLD);
            grades_scroll.addView(lessonName);
            String evaluation[] = html.split("<tbody>")[1].split("</tbody>")[0].replace("<tr class=\"\">", "").split("</tr>");
            TableLayout table = new TableLayout(this);
            TableRow description = new TableRow(this);

            TextView element = new TextView(this);
            element.setText(getString(R.string.element));
            element.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,.5f));
            element.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);

            TextView nonPonder = new TextView(this);
            nonPonder.setText(getString(R.string.non_pondere));
            nonPonder.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,.25f));
            nonPonder.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);

            TextView ponder= new TextView(this);
            ponder.setText(getString(R.string.pondere));
            ponder.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,.25f));
            ponder.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);

            description.addView(element);
            description.addView(nonPonder);
            description.addView(ponder);
            table.addView(description);
            grades_scroll.addView(table);
            for (int i = 0; i < evaluation.length - 1; i++) {
                String vals[] = evaluation[i].split("<td>");
                TableRow note = new TableRow(this);
                TextView evalName = new TextView(this);
                evalName.setText(vals[1].split("</td>")[0]);
                evalName.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,.5f));
                evalName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);

                TextView evalNonPonder = new TextView(this);
                evalNonPonder.setText(vals[2].split("</td>")[0]);
                evalNonPonder.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,.25f));
                evalNonPonder.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);

                TextView evalPonder= new TextView(this);
                evalPonder.setText(vals[3].split("</td>")[0]);
                evalPonder.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,.25f));
                evalPonder.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);

                note.addView(evalName);
                note.addView(evalNonPonder);
                note.addView(evalPonder);
                table.addView(note);
            }
            TableRow note = new TableRow(this);
            TextView evalName = new TextView(this);
            evalName.setText(getString(R.string.total));
            evalName.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,.5f));
            evalName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

            TextView total = new TextView(this);
            total.setText(html.split("<div class=\"card-body p-1 \">")[1].split("</div>")[0]);
            total.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,.5f));
            total.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

            note.addView(evalName);
            note.addView(total);
            table.addView(note);
            return true;
        } catch (Exception e) {
            Log.i("showGrades Error", e.toString(), e);
            TextView t = new TextView(this);
            t.setText(getString(R.string.error_grades_message));
            t.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            t.setTextColor(0xffffffff);
            t.setBackground(new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{0xFFF75A4E, 0xFFF81D0D}));
            t.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            t.setTypeface(Typeface.DEFAULT_BOLD);
            grades_scroll.addView(t);
            return false;
        }
    }
}
