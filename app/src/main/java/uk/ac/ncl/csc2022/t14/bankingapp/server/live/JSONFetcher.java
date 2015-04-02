package uk.ac.ncl.csc2022.t14.bankingapp.server.live;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
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

    public JSONFetcher(String baserlL) {

        super();
        this.baseUrl = baseUrl;
        this.isTesting = baseUrl.equals("test");
    }


    public void performFetch(String path, List<NameValuePair> postData, JSONTaskDelegate delegate) {

        // Create Task w/ delegate return
        if (isTesting) {

            // Return local JSON File
        }
        else {

            // Use JSONAsyncTask
        }
    }
}
