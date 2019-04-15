package uqac.dim.uqaclife;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;

import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends MainActivity {

    private RequestQueue queue;
    private CookieManager cookieManager;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        sharedPref = getSharedPreferences(getResources().getString(R.string.preferences_file), MODE_PRIVATE);

        ((TextView) findViewById(R.id.login)).setText(sharedPref.getString("login", ""));
        ((TextView) findViewById(R.id.password)).setText(sharedPref.getString("password", ""));

        queue = super.queue;
        super.login = this;
        cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);
        Log.i("request", "cookies : " + cookieManager.getCookieStore().getCookies().toString());
    }

    public void letsTrySomethingElse(final TextView txt) {
        Log.i("request", "Let's try something else!");
        String url = "https://etudiant.uqac.ca/EtudiantApp/SignIn";
        cookieManager.getCookieStore().removeAll();

        final Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("request", "That didn't work!");
                Log.e("request", error.toString());
            }
        };

        // Request to the login page to get the cookies and the next URL
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        // Extract URL
                        String s = response.substring(response.indexOf("<form method="));
                        final String url = "https://fs.uqac.ca" + s.substring(s.indexOf("/adfs"), s.indexOf("\" >"));

                        Log.i("request", "url : " + url);
                        Log.i("request", cookieManager.getCookieStore().getCookies().toString());

                        postRaw(url);
                    }
                }, errorListener);

        queue.add(stringRequest);
    }

    private void postRaw(final String url) {
        // Post to the extracted url with login & password to get the last encoded Data
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
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

                        postParsed(url2, url, code, idToken, state);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("request", "That didn't work!");
                Log.e("request", error.toString());
            }
        }) {
            @Override
            public byte[] getBody() {
                String httpPostBody = String.format("UserName=%s&Password=%s&AuthMethod=%s", "tgolgevit@etu.uqac.ca", "Golgum502!", "FormsAuthentication");

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
        queue.add(postRequest);
    }

    private void postParsed(String url, final String referer, final String code, final String idToken, final String state) {
        // Post to the login url to get the cookies and finally be logged in
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("request", "Post successful");
                        Log.i("request", "cookies : " + cookieManager.getCookieStore().getCookies().toString());

                        getSchedule();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("request", "That didn't work!");
                Log.e("request", error.toString());
            }
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

    public void getSchedule() {
        String url = "https://etudiant.uqac.ca/Dashboard";

        Log.i("request", "cookies : " + cookieManager.getCookieStore().getCookies().toString());
        Log.i("request", "URIs    : " + cookieManager.getCookieStore().getURIs().toString());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("request", "dashboard");
                        //txt.setText(response);
                        if (!response.contains("aria-labelledby=\"Trimestre-dropdown\"")) return;
                        String s = response.substring(response.indexOf("aria-labelledby=\"Trimestre-dropdown\""));
                        s = s.substring(s.indexOf("href=\""));
                        s = s.substring(s.indexOf("href=\"") + 6);
                        String url = s.substring(0, s.indexOf("\">"));
                        url = "https://etudiant.uqac.ca/EtudiantApp/HoraireListePartial/" + url.split("/")[url.split("/").length - 1] + "?_=" + new Date().getTime();
                        Log.i("request", "url : " + url);

                        StringRequest getSchedule = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                Log.i("request", response);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i("request", "That didn't work!");
                                Log.e("request", error.toString());
                            }
                        });

                        queue.add(getSchedule);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("request", "That didn't work!");
                Log.e("request", error.toString());
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    public void Login(final String login, final String password, final String captcha) {
        String url = "https://wprodl.uqac.ca/dossier_etudiant/validation.html";
        String url2 = "https://wprodl.uqac.ca/dossier_etudiant/grille_horaire.html?type=gl&session=TRIMESTRE";


        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("request", response);
                        LoginActivity.super.truc(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("request", "That didn't work!");
                        Log.e("request", error.toString());
                    }
                });

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.i("request", response);
                        queue.add(stringRequest);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.i("request", "That didn't work!");
                        Log.e("request", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("codeSecret", password);
                params.put("user", login);
                params.put("codeSecret1", "12345678");
                params.put("noCaptcha", captcha);
                params.put("valider", "valider");

                return params;
            }

            @Override
            public Map getHeaders() {
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("Referer", "application/x-www-form-urlencoded");

                return headers;
            }
        };


        queue.add(postRequest);
    }
}
