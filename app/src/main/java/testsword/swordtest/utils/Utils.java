package testsword.swordtest.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import testsword.swordtest.R;
import testsword.swordtest.models.Earthquake;
import testsword.swordtest.models.Patient;

import static testsword.swordtest.activities.MainActivity.LOG_TAG;

/**
 * Created by André Manteigas on 25/09/2017.
 */

public final class Utils {

    /** Shared Preferences Files **/
    private static final String SP_USER_INFO = "UserSharedPreferences";
    private static final String SP_PATIENT_INFO = "PatientSharedPreferences";

    /** Save the Patients List in Shared Preferences **/
    public static void savePatientsInSharedPref(Context context, List<Patient> list) {
        SharedPreferences sharedPref = context.getSharedPreferences(SP_PATIENT_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(context.getString(R.string.utils_sppatiens), json);
        editor.apply();
    }

    /** Get the Patients List from Shared Preferences **/
    public static List<Patient> getPatientsFromSharedPref(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(SP_PATIENT_INFO, Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sharedPref.getString(context.getString(R.string.utils_sppatiens), "");

        Type type = new TypeToken<ArrayList<Patient>>(){}.getType();

        return gson.fromJson(json, type);
    }

    /** Save the URL introduced by the user **/
    public static void setURLInSharedPref(Context context, String url) {
        SharedPreferences sharedPref = context.getSharedPreferences(SP_PATIENT_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(context.getString(R.string.sp_url), url);
        editor.apply();
    }

    /** Get the URL introduced by the user **/
    public static String getURLfromSharedPref(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(SP_PATIENT_INFO, Context.MODE_PRIVATE);

        return sharedPref.getString(context.getString(R.string.sp_url), "");
    }

    /** Verify if the user credentials are correct
     *
     * 0 - User email not found.
     * 1 - Incorrect password.
     * 2 - Login accepted.
     */
    public static int loginAttempt(Context context, String username, String password) {
        SharedPreferences sharedPref = context.getSharedPreferences(SP_USER_INFO, Context.MODE_PRIVATE);

        if (!TextUtils.equals(username, sharedPref.getString(context.getString(R.string.sp_username), ""))) {
            return 0;
        } else if (!TextUtils.equals(password, sharedPref.getString(context.getString(R.string.sp_password), ""))) {
            return 1;
        } else {
            return 2;
        }
    }

    /** Make a HTTP Request to the server **/
    public static ArrayList<Patient> makeServerRequest(String urls, Context context) {
        if (urls == null) {
            return null;
        }

        URL url = createURL(urls);

        String jsonResponse = null;

        try {
            jsonResponse = makeHTTPRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return extractDataFromJSON(jsonResponse, context);

    }

    /** Save the user Email and Password in Shared Preferences - Only fot Tests **/
    public static void saveUserInfo(Context context, String username, String password) {
        SharedPreferences sharedPref = context.getSharedPreferences(SP_USER_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        if (!sharedPref.getBoolean(context.getString(R.string.sp_exists), false)) {
            editor.putString(context.getString(R.string.sp_username), username);
            editor.putString(context.getString(R.string.sp_password), password);
            editor.putBoolean(context.getString(R.string.sp_exists), true);
            editor.apply();
        }
    }

    /** Create the URL used on HTTP request **/
    private static URL createURL(String urls) {
        URL url = null;

        try {
            url = new URL(urls);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error creating URL", e);
        }

        return url;
    }

    /** Make the HTTP Connection **/
    private static String makeHTTPRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return null;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "HTTP Error Code " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }

            if (inputStream != null) {
                inputStream.close();
            }
        }

        return jsonResponse;
    }

    /** Prepare the Server Response to JSON format **/
    private static String readFromStream(InputStream stream) throws IOException {
        StringBuilder output = new StringBuilder();

        if (stream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(stream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();

            while (line != null) {
                output.append(line);
                line = bufferedReader.readLine();
            }
        }

        return output.toString();
    }

    /** Parse the JSON response to Object Type
     * Discard the JSON response and load the patients info from Shared Preferences
     **/
    private static ArrayList<Patient> extractDataFromJSON(String response, Context context) {
        if (response == null) {
            return null;
        }

        ArrayList<Earthquake> earthquakes = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(response);
            JSONArray features = root.getJSONArray("features");

            for (int i = 0; i < features.length(); i++) {
                JSONObject properties = features.getJSONObject(i);
                JSONObject eartquake = properties.getJSONObject("properties");

                earthquakes.add(new Earthquake(eartquake.getDouble("mag"), eartquake.getString("place"),
                        eartquake.getLong("time"), eartquake.getString("url")));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
        }

        return (ArrayList<Patient>) Utils.getPatientsFromSharedPref(context);
    }
}
