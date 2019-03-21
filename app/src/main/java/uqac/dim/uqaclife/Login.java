package uqac.dim.uqaclife;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;

public class Login {

    private RequestQueue queue;
    private Gson gson = new Gson();

    public void setQueue(RequestQueue q) {
        queue = q;
    }

    public void getCaptcha(final ImageView captchaContainer, final TextView txtField) {

        String url = "https://wprodl.uqac.ca/dossier_etudiant/";

        /*// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    // Display the first 500 characters of the response string.
                    //Log.i("request", "Response is: " + response.substring(500));
                    String captcha = "https://wprodl.uqac.ca" + response.substring(response.indexOf("/commun/"), response.indexOf("\' alt=\"CAPTCHA\""));
                    Log.i("request", captcha);
                    Log.i("request", "Cookie: ");
                    String fleur = "https://cdn.pixabay.com/photo/2018/02/09/21/46/rose-3142529_960_720.jpg";
                    new DownloadImageTask(captchaContainer).execute(fleur, "cookie");
                    txtField.setText(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("request","That didn't work!");
                    Log.e("request", error.toString());
                }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);*/


        try {
            HttpsURLConnection urlConnection = (HttpsURLConnection) new URL(url).openConnection();
            urlConnection.connect();
            Log.i("request", urlConnection.getContentType());
            /*InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String s = convert(in);
            Log.i("request", s);*/
            //Log.i("request", (urlConnection.getHeaderField("cookie")));
            //Log.i("request", (urlConnection.toString()));
        } catch (Exception e) {
            Log.e("request", e.toString());
            e.printStackTrace();
        }
    }

    public String convert(InputStream inputStream) throws IOException {

        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            return br.lines().collect(Collectors.joining(System.lineSeparator()));
        }
    }

    public String login(String id, String password, String captcha) {


        return "";
    }

    public String getHTML(String cookie) {


        return "";
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            String cookie = urls[1];
            Bitmap mIcon11 = null;
            try {
                //InputStream in = new java.net.URL(urldisplay).openStream();
                URLConnection con = new URL(urldisplay).openConnection();
                con.setRequestProperty("Cookie", cookie);
                con.connect();
                InputStream in = con.getInputStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("request", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            Log.i("request", "Setting image...");
            bmImage.setImageBitmap(result);
        }
    }

}
