package net.nrp.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.rest.RestRequest;
import com.salesforce.androidsdk.rest.RestResponse;

import net.nrp.datamodels.Person;
import net.nrp.interfaces.iAsyncTaskAction;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mchin on 12/5/13.
 */
public class CallMobileQuizWS extends AsyncTask<Void,Void,String> {
    public RestClient rc;
    public iAsyncTaskAction<ArrayList<Person>> handle;

    public CallMobileQuizWS(RestClient res, iAsyncTaskAction<ArrayList<Person>> action)
    {
        rc = res;
        handle = action;
    }


    @Override


    protected String doInBackground(Void... params) {
        String text = null;

        try {
            RestResponse resp = rc.sendSync(RestRequest.RestMethod.GET, "/services/apexrest/mobilequiz/getQuestion", null);
            text = resp.asString();


        }
        catch (Exception e) {
            return e.getLocalizedMessage();
        }


        return text;
    }


    protected void onPostExecute(String response) {
        try{
            if (response!=null) {
                handle.execute(LoadPerson(response));
            }
        }
        catch (Exception e)
        {
            Log.d("CMQWSException", e.getLocalizedMessage());
        }
    }

    private ArrayList<Person> LoadPerson(String response)
    {
        ArrayList<Person> returnList = new ArrayList<Person>();
        try
        {
            JSONArray results = new JSONArray(response);
            for(int j = 0; j< results.length(); j++)
            {
                JSONObject p = results.getJSONObject(j);
                Person per = new Person();
                if(p.has("FirstName"))
                    per.firstName = p.getString("FirstName");
                else
                    per.firstName = "";
                if(p.has("LastName"))
                    per.lastName = p.getString("LastName");
                else
                    per.lastName = "";
                if(p.has("FullPhotoUrl"))
                    per.fullPhotoUrl = p.getString("FullPhotoUrl");
                else
                    per.fullPhotoUrl = "";
                if(p.has("Id"))
                    per.iD = p.getString("Id");
                else
                    per.iD = "";

                returnList.add(per);

            }
        }
        catch (Exception e) {
            Log.d("CMQWSJSONException", e.getLocalizedMessage());
        }
        return returnList;

    }
}
