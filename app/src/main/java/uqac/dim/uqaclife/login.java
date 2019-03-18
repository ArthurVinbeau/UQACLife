package uqac.dim.uqaclife;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class login {

    private RequestQueue queue;

    public String getCaptcha(android.content.Context context) {

        // Instantiate the RequestQueue.
        queue = Volley.newRequestQueue(context);
        String url ="http://www.google.com";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.i("request", "Response is: " + response.substring(0, 500));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("request","That didn't work!");
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

        return "";
    }

    public String login(String id, String password, String captcha) {


        return "";
    }

    public String getHTML(String cookie) {


        return "";
    }

}
