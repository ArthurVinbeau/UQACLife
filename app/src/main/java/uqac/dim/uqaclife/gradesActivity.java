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
    private Login login;

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
                "        Patate \n" +
                "\n" +
                "                    <span class=\"badge badge-secondary bg-warning\">&#xC0; venir</span>\n" +
                "    </h1>    \n" +
                "        <tbody>\n" +
                "                <tr class=\"\">\n" +
                "                    <td>Travail 1</td>\n" +
                "                    <td>5.00/5.00</td>\n" +
                "                    <td>5.00/5.00</td>\n" +
                "                </tr>\n" +
                "                <tr class=\"\">\n" +
                "                    <td>Travail 2</td>\n" +
                "                    <td>5.00/5.00</td>\n" +
                "                    <td>5.00/5.00</td>\n" +
                "                </tr>\n" +
                "                <tr class=\"\">\n" +
                "                    <td>Travail Maison 1</td>\n" +
                "                    <td>10.00/10.00</td>\n" +
                "                    <td>10.00/10.00</td>\n" +
                "                </tr>\n" +
                "                <tr class=\"\">\n" +
                "                    <td>Travail Maison 2</td>\n" +
                "                    <td>20.00/20.00</td>\n" +
                "                    <td>20.00/20.00</td>\n" +
                "                </tr>\n" +
                "                <tr class=\"\">\n" +
                "                    <td>Travail 3</td>\n" +
                "                    <td>5.00/5.00</td>\n" +
                "                    <td>5.00/5.00</td>\n" +
                "                </tr>\n" +
                "                <tr class=\"\">\n" +
                "                    <td>Travail 4 </td>\n" +
                "                    <td>10.00/10.00</td>\n" +
                "                    <td>10.00/10.00</td>\n" +
                "                </tr>\n" +
                "                <tr class=\"\">\n" +
                "                    <td>Travail 5</td>\n" +
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
                "        Elephant\n" +
                "\n" +
                "                    <span class=\"badge badge-secondary bg-warning\">&#xC0; venir</span>\n" +
                "    </h1>    \n" +
                "        <tbody>\n" +
                "                <tr class=\"\">\n" +
                "                    <td>Examen partiel</td>\n" +
                "                    <td>100.00/100.00</td>\n" +
                "                    <td>25.00/25.00</td>\n" +
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
                "                    <td>20.00/20.00</td>\n" +
                "                    <td>6.00/6.00</td>\n" +
                "                </tr>\n" +
                "        </tbody>\n" +
                "    </table>\n" +
                "</div>\n" +
                "<div class=\"card float-right\">\n" +
                "    <div class=\"card-header p-1 font-weight-bold\">Total</div>\n" +
                "\n" +
                "        <div class=\"card-body p-1 \">42.00/38.00</div>\n" +
                "\n" +
                "</div>\n" +
                "<div class=\"clearfix\"></div>\n" +
                "\n" +
                "</div>\n" +
                "\n");

                //endregion
        displayed = 0;
        for (String lesson:lessonshtml) {
            //showGrades(lesson);
        }
        login = super.login;
        login.getGrades(this);
    }

    public Boolean showGrades(String html) {
        try {
            TextView lessonName = new TextView(this);
            lessonName.setText(html.split("<h1 class=\"h2\">")[1].split("</h1>")[0].split("\n")[1]);
            lessonName.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            lessonName.setTextColor(0xffffffff);
            lessonName.setBackground(new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors[displayed++ % 7]));
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

    public void failHtml(int code) {
        Log.i("request", "fail html : " + code);
        TextView t = findViewById(R.id.grades_error);
        t = t == null ? new TextView(this) : t;
        t.setText(code == 0 ? R.string.error_login_credentials : code == 1 ? R.string.error_login_network : R.string.error_login_unknown);
        t.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        t.setTextColor(0xffffffff);
        t.setBackground(new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors[1]));
        t.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        t.setTypeface(Typeface.DEFAULT_BOLD);
        t.setId(R.id.grades_error);
        ((LinearLayout)findViewById(R.id.grades_scroll)).addView(t, 0);
    }
}
