package uk.ac.ncl.csc2022.t14.bankingapp.server.live.json;

import org.apache.http.HttpResponse;
import org.json.JSONObject;

/**
 * An object that waits for an JSONAsyncTask
 * Created by Rob A on 02/04/15.
 */
public interface JSONTaskDelegate {

    void taskCompleted(JSONTaskStatus status, String message, JSONObject json);
}



