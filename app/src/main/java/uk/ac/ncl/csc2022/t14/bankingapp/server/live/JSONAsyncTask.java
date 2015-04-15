package uk.ac.ncl.csc2022.t14.bankingapp.server.live;

import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
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
 * An AsyncTask that connects to a live server & attempts to read a JSON response from a POST request
 * Created by Rob A on 02/04/15.
 */
public class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {

    private String url;
    private List<NameValuePair> data;
    private int responseStatus;
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
            responseStatus = response.getStatusLine().getStatusCode();


            // If it was successful or deliberate error, attempt to get JSON from it
            if (responseStatus == 200 || responseStatus == 400) {

                HttpEntity entity = response.getEntity();
                String rawResponse = EntityUtils.toString(entity);
                jsonResponse = new JSONObject(rawResponse);
            }

            // If it was a success, return true
            if (responseStatus == 200) {

                return true;
            }
        }
        catch (JSONException e) {}
        catch (IOException e) {}


        // Anything other than a success was a failure
        return false;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);


        if (responseStatus == 200 && result) {

            // If it passed pass that onto the delegate
            delegate.taskCompleted(result, "passed", jsonResponse);
        }
        else if (responseStatus == 400 && jsonResponse != null) {

            // Try to get an error from the json response
            String error = "Server Error";

            try {

                // Try to find keys 'Reason' & 'Error'
                if (jsonResponse.has("Reason")) {

                    error = jsonResponse.getString("Reason");
                }
                else if (jsonResponse.has("Error")) {

                    error = jsonResponse.getString("Error");
                }
            }
            catch (JSONException e) {}

            // Inform the delegate
            delegate.taskCompleted(false, error, null);
        }
        else {

            // Otherwise, something failed
            delegate.taskCompleted(false, "Server Error", null);
        }
    }
}
