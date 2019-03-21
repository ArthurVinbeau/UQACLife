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
import java.net.URL;
import java.net.URLConnection;

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
                    String fleur = "https://cdn.pixabay.com/photo/2018/02/09/21/46/rose-3142529_960_720.jpg";
                    new DownloadImageTask((ImageView) view).execute(fleur);
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
                //InputStream in = new java.net.URL(urldisplay).openStream();
                URLConnection con = new URL(urldisplay).openConnection();
                con.setRequestProperty("Cookie", "PHPSESSID=hpn201jdp2qmsi3cl89u3l00i1");
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
