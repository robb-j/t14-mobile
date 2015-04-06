package uk.ac.ncl.csc2022.t14.bankingapp.server.live;

import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

/**
 * An AsyncTask that connects to a live server & attempts to read a JSON file from it
 * Created by Rob A on 02/04/15.
 */
public class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {

    private String url;
    private List<NameValuePair> data;
    private JSONTaskDelegate delegate;
    private JSONObject jsonResponse;


    /**
     * Creates a new Task
     * @param url The url to the file requested
     * @param data The post data to accompany the request
     * @param delegate The delegate to get notified when it is done
    */
    public JSONAsyncTask(String url, List<NameValuePair> data, JSONTaskDelegate delegate) {

        this.url = url;
        this.data = data;
        this.delegate = delegate;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(String... params) {

        // Get the client and create a post request
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        try {

            // Set the post data and execute the request
            post.setEntity(new UrlEncodedFormEntity(data));
            HttpResponse response = client.execute(post);


            // Get the status code of the request, to see what happened
            int status = response.getStatusLine().getStatusCode();


            // If it was successful, attempt to get JSON from it
            if (status == 200) {

                HttpEntity entity = response.getEntity();
                String data = EntityUtils.toString(entity);
                jsonResponse = new JSONObject(data);
                return true;
            }
        }
        catch (JSONException e) {

        }
        catch (IOException e) {

        }

        return false;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);

        // When we're finished, notify our delegate
        delegate.taskCompleted(result, "passed", jsonResponse);

    }
}
