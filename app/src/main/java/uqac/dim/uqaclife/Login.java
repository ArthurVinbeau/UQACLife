package uqac.dim.uqaclife;

import android.app.DownloadManager;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Login {

    private RequestQueue queue;
    private ImageLoader iLoader;
    private CookieManager cookieManager;
    private MainActivity mainActivity;

    public void setQueue(RequestQueue q, MainActivity m) {
        queue = q;
        mainActivity = m;
        cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);
        Log.i("request", cookieManager.getCookieStore().getCookies().toString());
        iLoader = new ImageLoader(queue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(10);

            public void putBitmap(String url, Bitmap bitmap) {
                mCache.put(url, bitmap);
            }

            public Bitmap getBitmap(String url) {
                return mCache.get(url);
            }
        });
    }

    public void getCaptcha(final NetworkImageView captchaContainer) {

        String url = "https://wprodl.uqac.ca/dossier_etudiant/";

        Map<String, String> headers = new LinkedHashMap<>();


        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //Log.i("request", "Response is: " + response.substring(500));
                        String captcha = "https://wprodl.uqac.ca" + response.substring(response.indexOf("/commun/"), response.indexOf("\' alt=\"CAPTCHA\"")) + "&cookie=" + "hpn201jdp2qmsi3cl89u3l00i1";
                        Log.i("request", captcha);
                        Log.i("request", "Cookie: " + cookieManager.getCookieStore().getCookies().toString());
                        captchaContainer.setImageUrl(captcha, iLoader);
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

    public void Login(final String login, final String password, final String captcha, final TextView txt) {
        String url = "https://wprodl.uqac.ca/dossier_etudiant/validation.html";
        String url2 = "https://wprodl.uqac.ca/dossier_etudiant/grille_horaire.html?type=gl&session=TRIMESTRE";


        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("request", response);
                        txt.setText(response);
                        mainActivity.truc(response);
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
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.i("request", response);
                        queue.add(stringRequest);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.i("request", "That didn't work!");
                        Log.e("request", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
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
