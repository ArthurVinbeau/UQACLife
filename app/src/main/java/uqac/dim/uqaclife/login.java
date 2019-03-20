package uqac.dim.uqaclife;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.InputStream;

public class login {

    private RequestQueue queue;

    public void setQueue(RequestQueue q) {
        queue = q;
    }

    public void getCaptcha(final View view) {

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
                    new DownloadImageTask((ImageView) view).execute(captcha);
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

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}
