package com.datapp.buzz_movieselector.model;

import android.os.AsyncTask;

import org.json.JSONObject;
import android.util.Log;
import org.json.JSONException;

class RequestHandler extends AsyncTask<String, Long, JSONObject> {

    public JSONObject doInBackground(String... url) {
        try {

            // kevinsawicki's HttpRequest from github
            HttpRequest request = HttpRequest.get(url[0])
                    .trustAllCerts() // for HTTPS request
                    .trustAllHosts() // to trust all hosts
                    .acceptJson();   // to accept JSON objects
            JSONObject jsonObject;
            if (request.ok()) {
                try {
                    String s = request.body();
                    Log.d("MyApp",
                            "Downloaded json data: "+ s);
                    jsonObject = new JSONObject(s);
                    return jsonObject;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.print("error");
                return null;
            }
        } catch (HttpRequest.HttpRequestException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    protected void onProgressUpdate(Long... progress) {
        // progress bar here
    }

    @Override
    protected void onPostExecute(JSONObject result) {
        if (result != null) {
            Log.d("MyApp", "Request succeeded");
        } else {
            Log.d("MyApp", "Request failed");
        }
    }
}

