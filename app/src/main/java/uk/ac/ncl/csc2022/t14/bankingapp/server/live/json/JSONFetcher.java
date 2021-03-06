package uk.ac.ncl.csc2022.t14.bankingapp.server.live.json;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Random;

/**
 * An Intermediate object that creates JSONAsyncTasks to get JSON from a live server
 * Also allows for Unit Testing by passing 'test' as the basUrl where it will load
 * JSON files locally for unit testing with.
 * Created by Rob A on 02/04/15.
 */
public class JSONFetcher {

    private String baseUrl;
    private boolean isTesting;
    private Random numberGenerator;

    public static final String TEST_MODE_BASEURL = "TestMode";



    public JSONFetcher(String baseUrl) {

        super();
        this.baseUrl = baseUrl;
        this.isTesting = baseUrl.equals(TEST_MODE_BASEURL);
        this.numberGenerator = new Random();
    }

    public boolean isTesting() {

        return isTesting;
    }


    public void performFetch(String path, List<NameValuePair> postData, JSONTaskDelegate delegate) {

        // Create Task w/ delegate return
        if (isTesting) {

            // Return with the delegate
            delegate.taskCompleted(JSONTaskStatus.PASSED, "passed", getLocalJSON(path));
        }
        else {

            // Concat the url, add a random number to evade caching
            int random = numberGenerator.nextInt();
            String url = baseUrl + path + "?n=" + random;

            // Create & execute a JSONAsyncTask
            JSONAsyncTask task = new JSONAsyncTask( url, postData, delegate );
            task.execute();
        }
    }

    private JSONObject getLocalJSON(String file) {

        // Work out the url
        String dir = System.getProperty("user.dir");
        String path = dir + "/robolectric-tests/src/test/assets/" + file + ".json";

        try {

            // Open the file
            InputStream is = new FileInputStream(path);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);


            // Read a string in
            String json = new String(buffer, "UTF-8");
            is.close();

            // Return a JSON object
            return new JSONObject(json);
        }
        catch (IOException e) {}
        catch (JSONException e) {}

        return null;
    }
}
