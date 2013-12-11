package net.nrp.tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.salesforce.androidsdk.rest.RestClient;

import net.nrp.interfaces.iAsyncTaskAction;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by mchin on 12/5/13.
 */

/*
This task is for individual image loading.
 */

public class LoadImageTask extends AsyncTask<String,Void,Integer> {

    public RestClient rc;
    public iAsyncTaskAction<Bitmap> handle;
    public Bitmap passBack;

    public LoadImageTask(RestClient client, iAsyncTaskAction<Bitmap> action)
    {
        rc = client;
        handle = action;
    }

    @Override
    public Integer doInBackground(String ... param)
    {
        int status = 0;

        try
        {
            if(param !=  null && param.length > 0 && param[0] != "")
            {
                    String fullLink = param[0] +  "?oauth_token=" + rc.getAuthToken();
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
        if(result == 0 && passBack != null && handle != null)
        {

            handle.execute(passBack);
        }
    }
}
