package uqac.dim.uqaclife;

import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class login {

    private RequestQueue queue;

    public void setQueue(RequestQueue q) {
        queue = q;
    }

    public void getCaptcha(View view) {

        String url ="https://wprodl.uqac.ca/dossier_etudiant/";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    // Display the first 500 characters of the response string.
                    //Log.i("request", "Response is: " + response.substring(500));
                    String captcha = "https://wprodl.uqac.ca" + response.substring(response.indexOf("/commun/"), response.indexOf("\' alt=\"CAPTCHA\""));
                    Log.i("request", captcha);
                    Log.i("request", "Cookie: ");
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("request","That didn't work!");
                    Log.e("request", error.toString());
                }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public String login(String id, String password, String captcha) {


        return "";
    }

    public String getHTML(String cookie) {


        return "";
    }

}
