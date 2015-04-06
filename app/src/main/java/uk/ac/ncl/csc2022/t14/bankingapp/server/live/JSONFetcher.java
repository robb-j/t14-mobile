package uk.ac.ncl.csc2022.t14.bankingapp.server.live;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * An Intermediate object that creates JSONAsyncTasks to get JSON from a live server
 * Also allows for Unit Testing by passing 'test' as the basUrl where it will load
 * JSON files locally for unit testing with.
 * Created by Rob A on 02/04/15.
 */
public class JSONFetcher {

    private String baseUrl;
    private boolean isTesting;

    public static final String TEST_MODE_BASEURL = "TestMode";

    public JSONFetcher(String baseUrl) {

        super();
        this.baseUrl = baseUrl;
        this.isTesting = baseUrl.equals(TEST_MODE_BASEURL);
    }

    public boolean isTesting() {

        return isTesting;
    }


    public void performFetch(String path, List<NameValuePair> postData, JSONTaskDelegate delegate) {

        // Create Task w/ delegate return
        if (isTesting) {

            // Return with the delegate
            delegate.taskCompleted(true, "passed", getLocalJSON(path));
        }
        else {

            // Use JSONAsyncTask
            JSONAsyncTask task = new JSONAsyncTask( path, postData, delegate );

            // Execute the task
            // ...
        }
    }


    private JSONObject getLocalJSON(String file) {

        // Work out the url
        String dir = System.getProperty("user.dir");
        String path = dir + "/roboelectric-tests/src/test/assets/test.json";
        String json = "";

        try {
            InputStream is = new FileInputStream(path);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);

            json = new String(buffer, "UTF-8");
            is.close();

            return new JSONObject(json);
        }
        catch (IOException e) {}
        catch (JSONException e) {}

        return null;
    }
}
