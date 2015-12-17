package app.ecosense.web;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;
    Activity activity;

    public DownloadImageTask(ImageView bmImage, Activity activity) {

        this.bmImage = bmImage;
        this.activity = activity;
    }


    protected Bitmap doInBackground(String... urls) {

        URL url = null;
        try {
            url = new URL(urls[0]);

            InputStream in = url.openConnection().getInputStream();

            String token = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext())
                    .getString("TOKEN", "token not found");

            BufferedInputStream bis = new BufferedInputStream(in,1024*8);
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            int len=0;
            byte[] buffer = new byte[1024];
            while((len = bis.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.close();
            bis.close();

            byte[] data = out.toByteArray();
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            return bitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    protected void onPostExecute(Bitmap result) {

        bmImage.setImageBitmap(result);
    }
}