package uqac.dim.uqaclife;

import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.jsoup.Jsoup;

public class GradesActivity extends MainActivity {
    int[][] colors = new int[][]{ //Colors for lesson names background gradients
            new int[]{0xFFFFC107, 0xFFFF9B00},
            new int[]{0xFFF75A4E, 0xFFF81D0D},
            new int[]{0xFFD77EF5, 0xFFBF0EDD},
            new int[]{0xFF5970E7, 0xFF2040EC},
            new int[]{0xFF4CE751, 0xFF11A214},
            new int[]{0xFF47E1F5, 0xFF0A8EA3},
            new int[]{0xFFFFEB3B, 0xFFEBD827}};

    LinearLayout grades_scroll;
    int displayed;
    private Login login;
    public int count;
    private int course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grades);
        grades_scroll = findViewById(R.id.grades_scroll);
        init();

        login = super.login;
        final GradesActivity context = this;
        ((SwipeRefreshLayout)findViewById(R.id.pullToRefresh)).setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                init();
                login.getGrades(context);
            }
        });
        ((SwipeRefreshLayout)findViewById(R.id.pullToRefresh)).setRefreshing(true);
        login.getGrades(this);
    }

    // Reset the view & counters
    private View init() {
        grades_scroll.removeAllViews();
        TextView debug = new TextView(this);
        debug.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        debug.setId(R.id.progressText);
        debug.setPadding(0, 200, 0, 0);
        grades_scroll.addView(debug);
        course = 0;
        displayed = 0;
        return debug;
    }

    //Show the grades from the string HTML
    public Boolean showGrades(String html) {
        try {
            //Creates lessonName TextView to separate
            TextView lessonName = new TextView(this);
            String name = html.split("<h1 class=\"h2\">")[1].split("</h1>")[0].split("\n")[1]; //get lesson name in  html
            name = Jsoup.parse(name).text(); //clean lesson name
            lessonName.setText(name);
            lessonName.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            lessonName.setTextColor(0xffffffff);
            lessonName.setBackground(new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors[displayed++ % 7]));
            lessonName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            lessonName.setTypeface(Typeface.DEFAULT_BOLD);


            grades_scroll.addView(lessonName); //add lessonname separator


            String evaluation[] = html.split("<tbody>")[1].split("</tbody>")[0].replace("<tr class=\"\">", "").split("</tr>"); //get evalutations part and split foreach evalutation
            TableLayout table = new TableLayout(this); //creates table layout
            table.setPadding(5, 0, 5, 0);
            TableRow description = new TableRow(this);
            description.setGravity(Gravity.END);

            //Add element column name
            TextView element = new TextView(this);
            element.setText(getString(R.string.element));
            element.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,1));
            element.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);

            //Add Not weighted column name
            TextView nonPonder = new TextView(this);
            nonPonder.setText(getString(R.string.non_pondere));
            nonPonder.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,0));
            nonPonder.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);

            //Add Weighted column name
            TextView ponder= new TextView(this);
            ponder.setText(getString(R.string.pondere));
            ponder.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,0));
            ponder.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);

            description.addView(element);
            description.addView(nonPonder);
            description.addView(ponder);
            table.addView(description);
            grades_scroll.addView(table);
            for (int i = 0; i < evaluation.length - 1; i++) { //Foreach grade, create table row with name, non ponder and ponder and adds it
                String vals[] = evaluation[i].split("<td>");
                TableRow note = new TableRow(this);
                note.setGravity(Gravity.END);
                TextView evalName = new TextView(this);
                evalName.setText(Jsoup.parse(vals[1].split("</td>")[0]).text());
                evalName.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,1));
                evalName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);

                TextView evalNonPonder = new TextView(this);
                evalNonPonder.setText(Jsoup.parse(vals[2].split("</td>")[0]).text());
                evalNonPonder.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,0));
                evalNonPonder.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);

                TextView evalPonder= new TextView(this);
                evalPonder.setText(Jsoup.parse(vals[3].split("</td>")[0]).text());
                evalPonder.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,0));
                evalPonder.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);

                note.addView(evalName);
                note.addView(evalNonPonder);
                note.addView(evalPonder);
                table.addView(note);
            }

            //Add total row
            TableRow note = new TableRow(this);
            note.setGravity(Gravity.END);
            TextView evalName = new TextView(this);
            evalName.setText(getString(R.string.total));
            evalName.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,1));
            evalName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

            TextView total = new TextView(this);
            total.setText(Jsoup.parse(html.split("<div class=\"card-body p-1 \">")[1].split("</div>")[0]).text());
            total.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,0));
            total.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

            note.addView(evalName);
            note.addView(total);
            table.addView(note);
            if (++course == count)
                ((SwipeRefreshLayout)findViewById(R.id.pullToRefresh)).setRefreshing(false);
            return true;
        } catch (Exception e) {
            //Display error if so
            Log.i("showGrades Error", e.toString(), e);
            TextView t = findViewById(R.id.grades_error);
            if (t != null)
                grades_scroll.removeView(t);
            t = new TextView(this);
            t.setText(getString(R.string.error_grades_message));
            t.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            t.setTextColor(0xffffffff);
            t.setBackground(new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{0xFFF75A4E, 0xFFF81D0D}));
            t.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            t.setTypeface(Typeface.DEFAULT_BOLD);
            grades_scroll.addView(t);
            ((SwipeRefreshLayout)findViewById(R.id.pullToRefresh)).setRefreshing(false);
            return false;
        }
    }

    // Display error message
    public void failHtml(int code) {
        TextView t = findViewById(R.id.grades_error);
        if (t != null)
            grades_scroll.removeView(t);
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
        t.setId(R.id.grades_error);
        ((LinearLayout)findViewById(R.id.grades_scroll)).addView(t, 0);
        ((SwipeRefreshLayout)findViewById(R.id.pullToRefresh)).setRefreshing(false);
    }
}
