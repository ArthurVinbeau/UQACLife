package uqac.dim.uqaclife;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class Login {

    private MainActivity mainActivity;
    private RequestQueue queue;
    private CookieManager cookieManager;
    private int retrys;
    private int maxRetrys = 2;
    private DefaultRetryPolicy def;
    private SharedPreferences sharedPref;
    private TextView progressText;

    public Login(MainActivity context) {

        queue = Volley.newRequestQueue(context);
        mainActivity = context;
        retrys = 0;
        def = new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);
        sharedPref = mainActivity.getSharedPreferences(mainActivity.getResources().getString(R.string.preferences_file), MODE_PRIVATE);
        Log.i("request", "cookies : " + cookieManager.getCookieStore().getCookies().toString());
    }

    public void login(final String id, final String pwd, final boolean progress) {
        if (progress) {
            progressText = mainActivity.findViewById(R.id.progressText);

            progressText.setText("[1/5] " + mainActivity.getString(R.string.login_log_1));
        }

        Log.i("request", "Let's try something else!");
        String url = "https://etudiant.uqac.ca/EtudiantApp/SignIn";
        cookieManager.getCookieStore().removeAll();

        // Request to the loginActivity page to get the cookies and the next URL
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.contains("/adfs")) {
                            // Extract URL
                            String s = response.substring(response.indexOf("<form method="));
                            final String url = "https://fs.uqac.ca" + s.substring(s.indexOf("/adfs"), s.indexOf("\" >"));

                            Log.i("request", "url : " + url);
                            Log.i("request", cookieManager.getCookieStore().getCookies().toString());

                            postRaw(url, id, pwd, progress);
                        } else {
                            if (progress)
                                mainActivity.loginActivity.failLogin(0);
                            else
                                mainActivity.failHtml(0);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("request", "That didn't work!");
                Log.e("request", error.toString());
                if (progress)
                    mainActivity.loginActivity.failLogin(1);
                else
                    mainActivity.failHtml(1);
            }
        });

        stringRequest.setRetryPolicy(def);
        queue.add(stringRequest);
    }

    public void login(String id, String pwd) {
        login(id, pwd, false);
    }

    private void postRaw(final String url, final String id, final String pwd, final boolean progress) {
        if (progress) {
            progressText.setText("[2/5] " + mainActivity.getString(R.string.login_log_2));
        }

        // Post to the extracted url with loginActivity & password to get the last encoded Data
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (progress) {
                            progressText.setText("[3/5] " + mainActivity.getString(R.string.login_log_3));
                        }

                        if (response.contains("code")) {
                            Log.i("request", response);

                            // Extract Data
                            String s = response.substring(response.indexOf("value=\"") + 7);
                            final String code = s.substring(0, s.indexOf("\" />"));
                            s = s.substring(s.indexOf("value=\"") + 7);
                            final String idToken = s.substring(0, s.indexOf("\" />"));
                            s = s.substring(s.indexOf("value=\"") + 7);
                            final String state = s.substring(0, s.indexOf("\" />"));


                            Log.i("request", "code : " + code);
                            Log.i("request", "idToken : " + idToken);
                            Log.i("request", "state : " + state);
                            Log.i("request", "cookies : " + cookieManager.getCookieStore().getCookies().toString());


                            s = response.substring(response.indexOf("http"));
                            final String url2 = s.substring(0, s.indexOf("\""));
                            Log.i("request", "url2 : " + url2);

                            postParsed(url2, url, code, idToken, state, progress);
                        } else {
                            if (progress)
                                mainActivity.loginActivity.failLogin(2);
                            else
                                mainActivity.failHtml(2);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("request", "That didn't work!");
                Log.e("request", error.toString());
                if (progress)
                    mainActivity.loginActivity.failLogin(1);
                else
                    mainActivity.failHtml(1);            }
        }) {
            @Override
            public byte[] getBody() {
                String httpPostBody = String.format("UserName=%s&Password=%s&AuthMethod=%s", id, pwd, "FormsAuthentication");

                return httpPostBody.getBytes();
            }

            @Override
            public Map getHeaders() {
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("Origin", "https://fs.uqac.ca");
                headers.put("Referer", url);

                return headers;
            }
        };
        postRequest.setRetryPolicy(def);
        queue.add(postRequest);
    }

    private void postParsed(String url, final String referer, final String code, final String idToken, final String state, final boolean progress) {
        if (progress) {
            progressText.setText("[4/5] " + mainActivity.getString(R.string.login_log_4));
        }
        // Post to the loginActivity url to get the cookies and finally be logged in
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("request", "Post successful");
                        Log.i("request", "cookies : " + cookieManager.getCookieStore().getCookies().toString());

                        fetchSchedule(progress);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("request", "That didn't work!");
                Log.e("request", error.toString());
                if (progress)
                    mainActivity.loginActivity.failLogin(1);
                else
                    mainActivity.failHtml(1);            }
        }) {
            @Override
            public byte[] getBody() {
                String httpPostBody = String.format("code=%s&id_token=%s&state=%s", code, idToken, state);
                Log.i("request", "body : " + httpPostBody);

                return httpPostBody.getBytes();
            }

            @Override
            public Map<String, String> getHeaders() {
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("Origin", "https://fs.uqac.ca");
                headers.put("Referer", referer);

                return headers;
            }
        };

        postRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);
    }

    private void fetchSchedule(final boolean progress) {

        if (progress) {
            progressText.setText("[5/5] " + mainActivity.getString(R.string.login_log_5));
        }
        String url = "https://etudiant.uqac.ca/Dashboard";

        Log.i("request", "cookies : " + cookieManager.getCookieStore().getCookies().toString());
        Log.i("request", "URIs    : " + cookieManager.getCookieStore().getURIs().toString());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (!response.contains("aria-labelledby=\"Trimestre-dropdown\"")) {
                            Log.i("request", "not logged in! retrying... retry count : " + retrys);
                            if (retrys < maxRetrys) {
                                retrys++;
                                if (sharedPref.getString("password", null) == null)
                                    return;
                                else
                                    login(sharedPref.getString("login", ""), sharedPref.getString("password", ""));
                            } else
                            if (progress)
                                mainActivity.loginActivity.failLogin(2);
                            else
                                mainActivity.failHtml(2);
                            return;
                        }

                        Log.i("request", "dashboard");

                        String s = response.substring(response.indexOf("aria-labelledby=\"Trimestre-dropdown\""));
                        s = s.substring(s.indexOf("href=\""));
                        s = s.substring(s.indexOf("href=\"") + 6);
                        String url = s.substring(0, s.indexOf("\">"));
                        final String id =  url.split("/")[url.split("/").length - 1];
                        url = "https://etudiant.uqac.ca/EtudiantApp/HoraireListePartial/" + id + "?_=" + new Date().getTime();
                        Log.i("request", "url : " + url);

                        StringRequest getSchedule = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                Log.i("request", "transmitting schedule...");
                                Log.i("request", response);
                                if (progress)
                                    mainActivity.loginActivity.transfer(response);
                                else
                                    mainActivity.refreshHtml(response);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i("request", "That didn't work!");
                                Log.e("request", error.toString());
                                if (progress)
                                    mainActivity.loginActivity.failLogin(1);
                                else
                                    mainActivity.failHtml(1);
                            }
                        }) {
                            @Override
                            public Map<String, String> getHeaders() {
                                HashMap headers = new HashMap();
                                headers.put("Accept-Language", "fr-FR");


                                return headers;
                            }
                        };

                        getSchedule.setRetryPolicy(def);
                        queue.add(getSchedule);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("request", "That didn't work!");
                Log.e("request", error.toString());
                if (progress)
                    mainActivity.loginActivity.failLogin(1);
                else
                    mainActivity.failHtml(1);
            }
        });

        // Add the request to the RequestQueue.
        stringRequest.setRetryPolicy(def);
        queue.add(stringRequest);

    }

    public void getSchedule() {
        if(sharedPref.getString("password", null) == null)
            mainActivity.startLoginActivity();
        else if (cookieManager.getCookieStore().getCookies().isEmpty())
            login(sharedPref.getString("login", ""), sharedPref.getString("password", ""));
        else
            fetchSchedule(false);

    }

    public void getGrades(final gradesActivity gradesAct) {
        Log.i("request", "Let's get some grades!");
        Log.i("request", "cookies : " + cookieManager.getCookieStore().getCookies());

        String url = "https://etudiant.uqac.ca/Dashboard";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (!response.contains("aria-labelledby=\"Trimestre-dropdown\"")) {
                            Log.i("request", "not logged in! retrying... retry count : " + retrys);
                            if (retrys < maxRetrys) {
                                retrys++;
                                if (sharedPref.getString("password", null) == null)
                                    return;
                                else
                                    login(sharedPref.getString("login", ""), sharedPref.getString("password", ""));
                            } else
                            gradesAct.failHtml(2);
                            return;
                        }

                        Log.i("request", "dashboard");

                        String s = response.substring(response.indexOf("aria-labelledby=\"Trimestre-dropdown\""));
                        s = s.substring(s.indexOf("href=\""));
                        s = s.substring(s.indexOf("href=\"") + 6);
                        String url = s.substring(0, s.indexOf("\">"));
                        final String id =  url.split("/")[url.split("/").length - 1];
                        url = "https://etudiant.uqac.ca/Cours/" + id;
                        Log.i("request", "url : " + url);

                        StringRequest getCourses = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                Log.i("request", "Retrieving course list...");
                                List<String> urls = new ArrayList<String>();
                                String s = response.substring(response.indexOf("<li class=\"nav-item\">"));
                                while (s.contains("href=\"#")) {
                                    s = s.substring(s.indexOf("href=\"#") + 7);
                                    String e = s.substring(0, s.indexOf("\""));
                                    if (e.length() != 7)
                                        gradesAct.failHtml(2);
                                    urls.add("https://etudiant.uqac.ca/EtudiantApp/CoursDetailPartial/" + id + "/" + e);
                                }
                                fetchGrades((String[]) urls.toArray(), gradesAct);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i("request", "That didn't work!");
                                Log.e("request", error.toString());
                                gradesAct.failHtml(1);
                            }
                        });

                        getCourses.setRetryPolicy(def);
                        queue.add(getCourses);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("request", "That didn't work!");
                Log.e("request", error.toString());
                gradesAct.failHtml(1);
            }
        });

        // Add the request to the RequestQueue.
        stringRequest.setRetryPolicy(def);
        queue.add(stringRequest);
    }

    private void fetchGrades(String[] urls, final gradesActivity gradesAct) {

        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                gradesAct.showGrades(response);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("request", "That didn't work!");
                Log.e("request", error.toString());
                gradesAct.failHtml(1);
            }
        };

        for (String url : urls) {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url + "?_=" + new Date().getTime(), listener, errorListener);
            stringRequest.setRetryPolicy(def);
            queue.add(stringRequest);
        }
    }

}
