package uk.ac.ncl.csc2022.t14.bankingapp.server.live.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import uk.ac.ncl.csc2022.t14.bankingapp.models.ModelObject;

/**
 * Created by rob on 14/04/15.
 */
public class JSONParser {

    private JSONObject json;

    public JSONParser(JSONObject json) {

        this.json = json;
    }


    /*
        Parsing with defaults
    */
    public int getInt(String key, int defaultValue) {

        try {
            return json.getInt(key);
        }
        catch (JSONException e) {

            return defaultValue;
        }
    }

    public String getString(String key, String defaultValue) {

        try {
            return json.getString(key);
        }
        catch (JSONException e) {

            return defaultValue;
        }
    }

    public double getDouble(String key, double defaultValue) {

        try {
            return json.getDouble(key);
        }
        catch (JSONException e) {

            return defaultValue;
        }
    }




    /*
        Parsing without defaults
     */

    public int getInt(String key) {

        return getInt(key, 0);
    }

    public double getDouble(String key) {

        return getDouble(key, 0.0);
    }

    public JSONObject getJSONObject(String key) {

        try {
            return json.getJSONObject(key);
        }
        catch (JSONException e) {

            return new JSONObject();
        }
    }

    public JSONArray getJSONArray(String key) {

        try {
            return json.getJSONArray(key);
        }
        catch (JSONException e) {

            return new JSONArray();
        }
    }

    public Date getDate(String key) {

        try {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
            return format.parse(json.getString(key));
        }
        catch (ParseException e) {}
        catch (JSONException e) {}

        return null;
    }


    /*
        Get Lists of values
     */
    public List<JSONObject> getJSONObjectList(String key) {

        JSONArray rawArray = getJSONArray(key);
        List<JSONObject> objectArray = new ArrayList<>();

        for (int i = 0; i < rawArray.length(); i++) {

            try {

                objectArray.add(rawArray.getJSONObject(i));

            } catch (JSONException e) { }
        }

        return objectArray;
    }

    public List<Integer> getIntegerList(String key) {

        JSONArray rawArray = getJSONArray(key);
        List<Integer> objectArray = new ArrayList<>();

        for (int i = 0; i < rawArray.length(); i++) {

            try {

                objectArray.add(rawArray.getInt(i));

            } catch (JSONException e) { }
        }

        return objectArray;
    }


    /*
        Relations
     */
    public <T extends ModelObject> T fillRelation(String key, Map<Integer, T> relation) {

        int id = getInt(key);

        if (id > 0) {

            return relation.get(id);
        }
        return null;
    }

    public <T extends ModelObject> T fillRelation(String key, List<T> relation) {

        int id = getInt(key);

        if (id > 0) {

            for (T model : relation) {

                if (model.getId() == id) {

                    return model;
                }
            }
        }
        return null;
    }

    public <T extends ModelObject> List<T> fillRelationToMany(String key, Map<Integer, T> relation) {

        JSONArray keys = getJSONArray(key);

        List<T> mapped = new ArrayList<>();

        for (int i = 0; i < keys.length(); i++) {

            try {

                int index = keys.getInt(i);

                if (relation.containsKey(index)) {

                    mapped.add(relation.get(index));
                }
            }
            catch (JSONException e) {}
        }

        return mapped;
    }
}
