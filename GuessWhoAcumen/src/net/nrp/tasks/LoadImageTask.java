package net.nrp.tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.salesforce.androidsdk.rest.RestClient;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by mchin on 12/5/13.
 */
public class LoadImageTask extends AsyncTask<Integer,Void,Integer> {

    public RestClient rc;
    public Bitmap passBack;
    public ImageView iview;
    public String from;

    public LoadImageTask(RestClient client, String link, ImageView iv)
    {
        rc = client;
        from = link;
        iview = iv;
    }

    @Override
    public Integer doInBackground(Integer ... param)
    {
        int status = 0;

        try
        {
            if(from != null && from != "")
            {
                    String fullLink = from +  "?oauth_token=" + rc.getAuthToken();
                    passBack = BitmapFactory.decodeStream((InputStream) new URL(fullLink).getContent());

            }
        }
        catch (Exception e)
        {
            Log.d("LoadImageException", e.getLocalizedMessage());
            status = 1;
        }
        return status;
    }

    @Override
    protected void onPostExecute(Integer result)
    {
        super.onPostExecute(result);
        if(result == 0 && passBack != null)
        {

            iview.setImageBitmap(passBack);
        }
    }
}
